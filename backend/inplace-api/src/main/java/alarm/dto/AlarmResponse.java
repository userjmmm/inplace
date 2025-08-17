package alarm.dto;

import alarm.query.dto.AlarmResult;

public record AlarmResponse(
    Long alarmId,
    Long postId,
    Long commentId,
    String content,
    Boolean checked,
    String type,
    String createdAt
) {

    public static AlarmResponse from(AlarmResult alarmResult) {
        return new AlarmResponse(
            alarmResult.alarmId(),
            alarmResult.postId(),
            alarmResult.commentId(),
            alarmResult.content(),
            alarmResult.checked(),
            alarmResult.type(),
            alarmResult.createdAt()
        );
    }
}
