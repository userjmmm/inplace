package report.command;

import annotation.Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import post.command.PostCommandService;
import report.event.dto.ReportEvent.CommentReportEvent;
import report.event.dto.ReportEvent.PostReportEvent;

@Facade
@RequiredArgsConstructor
public class ReportCommandFacade {
    private final ApplicationEventPublisher eventPublisher;
    private final PostCommandService postCommandService;

    public void processPostReport(Long postId) {
        postCommandService.reportPost(postId);
        eventPublisher.publishEvent(new PostReportEvent(postId));
    }

    public void processCommentReport(Long commentId) {
        postCommandService.reportComment(commentId);
        eventPublisher.publishEvent(new CommentReportEvent(commentId));
    }

}
