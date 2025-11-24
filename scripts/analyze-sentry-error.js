#!/usr/bin/env node

/**
 * Sentry Error Analyzer with Google Gemini AI
 *
 * This script:
 * 1. Fetches detailed error information from Sentry API
 * 2. Reads the source code file where the error occurred
 * 3. Uses Google Gemini AI to analyze the error and suggest fixes
 * 4. Generates a formatted GitHub issue body
 */

import { GoogleGenerativeAI } from '@google/generative-ai';
import fetch from 'node-fetch';
import fs from 'fs/promises';
import path from 'path';

// Environment variables
const GEMINI_API_KEY = process.env.GEMINI_API_KEY;
const SENTRY_AUTH_TOKEN = process.env.SENTRY_AUTH_TOKEN;
const SENTRY_ORG = process.env.SENTRY_ORG;
const SENTRY_EVENT_ID = process.env.SENTRY_EVENT_ID;
const SENTRY_ISSUE_ID = process.env.SENTRY_ISSUE_ID;
const ERROR_TITLE = process.env.ERROR_TITLE;
const ERROR_CULPRIT = process.env.ERROR_CULPRIT;
const ERROR_LEVEL = process.env.ERROR_LEVEL;
const PROJECT = process.env.PROJECT;
const PERMALINK = process.env.PERMALINK;

async function fetchSentryEvent() {
  console.log('Fetching Sentry event details...');

  const url = `https://sentry.io/api/0/projects/${SENTRY_ORG}/${PROJECT}/events/${SENTRY_EVENT_ID}/`;

  const response = await fetch(url, {
    headers: {
      'Authorization': `Bearer ${SENTRY_AUTH_TOKEN}`,
    },
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch Sentry event: ${response.statusText}`);
  }

  return await response.json();
}

async function readSourceCode(filePath, lineNumber) {
  console.log(`Reading source code: ${filePath}:${lineNumber}`);

  try {
    // Resolve the file path relative to the project root
    const projectRoot = path.resolve(process.cwd(), '..');
    const fullPath = path.join(projectRoot, filePath);

    const content = await fs.readFile(fullPath, 'utf-8');
    const lines = content.split('\n');

    // Get context around the error line (±10 lines)
    const startLine = Math.max(0, lineNumber - 10);
    const endLine = Math.min(lines.length, lineNumber + 10);

    const contextLines = lines.slice(startLine, endLine);
    const contextWithNumbers = contextLines
      .map((line, index) => {
        const num = startLine + index + 1;
        const marker = num === lineNumber ? '→' : ' ';
        return `${marker} ${num.toString().padStart(4)} | ${line}`;
      })
      .join('\n');

    return {
      fullPath,
      context: contextWithNumbers,
      lineNumber,
      fileExists: true,
    };
  } catch (error) {
    console.warn(`Could not read source file: ${error.message}`);
    return {
      fullPath: filePath,
      context: null,
      lineNumber,
      fileExists: false,
    };
  }
}

async function analyzeWithGemini(sentryEvent, sourceCode) {
  console.log('Analyzing error with Gemini AI...');

  const genAI = new GoogleGenerativeAI(GEMINI_API_KEY);
  const model = genAI.getGenerativeModel({ model: 'gemini-1.5-pro' });

  // Prepare error context
  const stackTrace = sentryEvent.entries
    ?.find(entry => entry.type === 'exception')
    ?.data?.values?.[0]?.stacktrace?.frames || [];

  const errorMessage = sentryEvent.entries
    ?.find(entry => entry.type === 'exception')
    ?.data?.values?.[0]?.value || ERROR_TITLE;

  const errorType = sentryEvent.entries
    ?.find(entry => entry.type === 'exception')
    ?.data?.values?.[0]?.type || 'Error';

  // Build prompt for Gemini
  let prompt = `You are a senior software engineer analyzing a production error from Sentry.

## Error Information

**Error Type:** ${errorType}
**Error Message:** ${errorMessage}
**Severity:** ${ERROR_LEVEL}
**Location:** ${ERROR_CULPRIT}
**Sentry Link:** ${PERMALINK}

## Stack Trace

\`\`\`
${stackTrace.map(frame =>
  `  at ${frame.function || 'anonymous'} (${frame.filename}:${frame.lineno}:${frame.colno})`
).join('\n')}
\`\`\`
`;

  if (sourceCode.fileExists && sourceCode.context) {
    prompt += `

## Source Code Context

File: ${sourceCode.fullPath}

\`\`\`
${sourceCode.context}
\`\`\`
`;
  }

  prompt += `

## Request Context

${sentryEvent.request ? `
**URL:** ${sentryEvent.request.url || 'N/A'}
**Method:** ${sentryEvent.request.method || 'N/A'}
**Headers:** ${JSON.stringify(sentryEvent.request.headers || {}, null, 2)}
` : 'No request context available'}

## Environment

${sentryEvent.contexts?.runtime ? `
**Runtime:** ${sentryEvent.contexts.runtime.name} ${sentryEvent.contexts.runtime.version}
` : ''}
${sentryEvent.contexts?.os ? `
**OS:** ${sentryEvent.contexts.os.name} ${sentryEvent.contexts.os.version}
` : ''}
${sentryEvent.contexts?.browser ? `
**Browser:** ${sentryEvent.contexts.browser.name} ${sentryEvent.contexts.browser.version}
` : ''}

## Please provide:

1. **Root Cause Analysis**: What caused this error?
2. **Impact Assessment**: How severe is this issue and who is affected?
3. **Recommended Fix**: Step-by-step solution to fix this error
4. **Prevention**: How to prevent similar errors in the future
5. **Priority**: Critical / High / Medium / Low

Please format your response in clear markdown that can be used directly in a GitHub issue.`;

  const result = await model.generateContent(prompt);
  const response = result.response;
  return response.text();
}

async function generateIssueBody(analysis, sentryEvent, sourceCode) {
  console.log('Generating GitHub issue body...');

  const timestamp = new Date(sentryEvent.dateCreated).toLocaleString('ko-KR', {
    timeZone: 'Asia/Seoul',
  });

  let body = `# Sentry Error Report

**Error ID:** ${SENTRY_EVENT_ID}
**Issue ID:** ${SENTRY_ISSUE_ID}
**Detected:** ${timestamp}
**Severity:** ${ERROR_LEVEL}
**View in Sentry:** ${PERMALINK}

---

## Error Details

**Type:** ${sentryEvent.entries?.find(e => e.type === 'exception')?.data?.values?.[0]?.type || 'Error'}
**Message:** ${sentryEvent.entries?.find(e => e.type === 'exception')?.data?.values?.[0]?.value || ERROR_TITLE}
**Location:** \`${ERROR_CULPRIT}\`
`;

  if (sourceCode.fileExists) {
    body += `**File:** [\`${sourceCode.fullPath}\`](${sourceCode.fullPath}#L${sourceCode.lineNumber})

`;
  }

  body += `---

## AI Analysis (Gemini)

${analysis}

---

## Additional Information

### Stack Trace

\`\`\`
${sentryEvent.entries
  ?.find(entry => entry.type === 'exception')
  ?.data?.values?.[0]?.stacktrace?.frames.map(frame =>
    `  at ${frame.function || 'anonymous'} (${frame.filename}:${frame.lineno}:${frame.colno})`
  ).join('\n') || 'No stack trace available'}
\`\`\`
`;

  if (sentryEvent.request) {
    body += `
### Request Context

- **URL:** ${sentryEvent.request.url || 'N/A'}
- **Method:** ${sentryEvent.request.method || 'N/A'}
- **User Agent:** ${sentryEvent.request.headers?.['User-Agent'] || 'N/A'}
`;
  }

  body += `

---

*This issue was automatically created by Gemini AI based on a Sentry error report.*
`;

  return body;
}

function determineSeverity(errorLevel, analysis) {
  // Parse severity from AI analysis
  const lowerAnalysis = analysis.toLowerCase();

  if (lowerAnalysis.includes('priority: critical') || lowerAnalysis.includes('critical')) {
    return 'critical';
  } else if (lowerAnalysis.includes('priority: high') || errorLevel === 'error') {
    return 'high';
  } else if (lowerAnalysis.includes('priority: medium') || errorLevel === 'warning') {
    return 'medium';
  } else {
    return 'low';
  }
}

async function main() {
  try {
    console.log('=== Sentry Error Analysis Started ===');
    console.log(`Event ID: ${SENTRY_EVENT_ID}`);
    console.log(`Issue ID: ${SENTRY_ISSUE_ID}`);
    console.log(`Project: ${PROJECT}`);

    // Validate environment variables
    if (!GEMINI_API_KEY) {
      throw new Error('GEMINI_API_KEY is not set');
    }
    if (!SENTRY_AUTH_TOKEN) {
      throw new Error('SENTRY_AUTH_TOKEN is not set');
    }
    if (!SENTRY_ORG) {
      throw new Error('SENTRY_ORG is not set');
    }

    // 1. Fetch Sentry event details
    const sentryEvent = await fetchSentryEvent();
    console.log('✓ Fetched Sentry event');

    // 2. Read source code context
    const stackFrames = sentryEvent.entries
      ?.find(entry => entry.type === 'exception')
      ?.data?.values?.[0]?.stacktrace?.frames || [];

    const topFrame = stackFrames[stackFrames.length - 1]; // Most recent frame
    let sourceCode = { fileExists: false };

    if (topFrame && topFrame.filename && topFrame.lineno) {
      sourceCode = await readSourceCode(topFrame.filename, topFrame.lineno);
      console.log('✓ Read source code context');
    }

    // 3. Analyze with Gemini AI
    const analysis = await analyzeWithGemini(sentryEvent, sourceCode);
    console.log('✓ Completed AI analysis');

    // 4. Generate GitHub issue body
    const issueBody = await generateIssueBody(analysis, sentryEvent, sourceCode);
    const severity = determineSeverity(ERROR_LEVEL, analysis);

    // 5. Save result for GitHub Actions
    const result = {
      title: ERROR_TITLE,
      body: issueBody,
      severity,
      eventId: SENTRY_EVENT_ID,
      issueId: SENTRY_ISSUE_ID,
    };

    await fs.writeFile(
      'scripts/analysis-result.json',
      JSON.stringify(result, null, 2)
    );

    console.log('✓ Saved analysis result');
    console.log('=== Analysis Complete ===');

  } catch (error) {
    console.error('Error during analysis:', error);
    process.exit(1);
  }
}

main();