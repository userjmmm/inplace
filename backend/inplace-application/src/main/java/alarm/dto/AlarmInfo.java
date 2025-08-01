package alarm.dto;

public record AlarmInfo(
    Long alarmId,
    Long postId,
    Long commentId,
    String content,
    Boolean checked,
    String type,
    String createdAt
) {

    public static AlarmInfo from(Alarm alarm) { // TODO
        return new AlarmInfo(
            alarm.getId(),
            alarm.getPostId(),
            alarm.getCommentId(),
            alarm.getContent(),
            alarm.isChecked(),
            alarm.getAlarmType().name(),
            TimeUtil.betweenTime(alarm.getCreatedAt())
        );
    }
}
