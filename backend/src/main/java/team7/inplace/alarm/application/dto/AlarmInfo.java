package team7.inplace.alarm.application.dto;

import team7.inplace.alarm.domain.Alarm;

public record AlarmInfo(
    Long alarmId,
    Long postId,
    Long commentId,
    String content,
    Boolean isReading,
    String type
) {
    public static AlarmInfo from(Alarm alarm) {
        return new AlarmInfo(
            alarm.getId(),
            alarm.getPostId(),
            alarm.getCommentId(),
            alarm.getContent(),
            alarm.isReading(),
            alarm.getAlarmType().name()
        );
    }
}
