package report.event;

import alarm.event.dto.AlarmEvent.CommentReportAlarmEvent;
import alarm.event.dto.AlarmEvent.PostReportAlarmEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import post.command.PostCommandService;
import post.query.PostQueryService;
import report.query.ModerationService;
import report.event.dto.ReportEvent.CommentReportEvent;
import report.event.dto.ReportEvent.PostReportEvent;


@Component
@RequiredArgsConstructor
public class ReportEventHandler {

    private final ApplicationEventPublisher eventPublisher;
    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;
    private final ModerationService moderationService;

    @Async("aiExecutor")
    @EventListener
    public void processPostReport(PostReportEvent event) {
        String content = postQueryService.getPostContentById(event.postId());
        boolean flag = moderationService.isContentFlagged(content);
        if (flag) {
            postCommandService.deletePostSoftly(event.postId());
            eventPublisher.publishEvent(new PostReportAlarmEvent(event.postId()));
        }
    }

    @Async("aiExecutor")
    @EventListener
    public void processCommentReport(CommentReportEvent event) {
        String content = postQueryService.getCommentContentById(event.commentId());
        boolean flag = moderationService.isContentFlagged(content);
        if (flag) {
            postCommandService.deleteCommentSoftly(event.commentId());
            eventPublisher.publishEvent(new CommentReportAlarmEvent(event.commentId()));
        }
    }
}
