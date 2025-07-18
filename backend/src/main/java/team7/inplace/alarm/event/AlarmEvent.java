package team7.inplace.alarm.event;

public class AlarmEvent {
    
    public record MentionAlarmEvent(Long postId, Long commentId,  String receiver) {}
    
    public record PostReportAlarmEvent(Long postId, String receiver) {}
    
    public record CommentReportAlarmEvent(Long commentId, String receiver) {}
}
