package team7.inplace.report.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import team7.inplace.global.annotation.Facade;
import team7.inplace.report.event.ReportEvent.CommentReportEvent;
import team7.inplace.report.event.ReportEvent.PostReportEvent;

@Facade
@RequiredArgsConstructor
public class ReportFacade {
    private final ApplicationEventPublisher eventPublisher;

    public void processPostReport(Long postId) {
        eventPublisher.publishEvent(new PostReportEvent(postId));
    }

    public void processCommentReport(Long commentId) {
        eventPublisher.publishEvent(new CommentReportEvent(commentId));
    }

}
