package my.inplace.application.alarm.event.dto;

public class AlarmEvent {
    
    public record MentionAlarmEvent(Long postId, Long commentId, String sender, String receiver) {}
    
    public record PostReportAlarmEvent(Long postId) {}
    
    public record CommentReportAlarmEvent(Long commentId) {}
}
