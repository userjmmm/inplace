package team7.inplace.alarm.event;

public class AlarmEvent {
    
    public record MentionAlarmEvent(Long postId, String username) {}
    
    public record PostReportAlarmEvent(Long postId, String username) {}
    
    public record CommentReportAlarmEvent(Long commentId, String username) {}
}
