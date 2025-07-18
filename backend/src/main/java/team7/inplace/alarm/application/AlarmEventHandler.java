package team7.inplace.alarm.application;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import team7.inplace.alarm.domain.AlarmType;
import team7.inplace.alarm.event.AlarmEvent.PostReportAlarmEvent;
import team7.inplace.alarm.event.AlarmEvent.CommentReportAlarmEvent;
import team7.inplace.alarm.event.AlarmEvent.MentionAlarmEvent;
import team7.inplace.user.application.UserService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmEventHandler {
    private static final String MENTION_CONTENT_TEMPLATE = "번 게시물에서 회원님을 언급했습니다!";
    
    private final FcmClient fcmClient;
    private final AlarmService alarmService;
    private final UserService userService;
    
    @Async
    @EventListener
    public void processMentionAlarm(MentionAlarmEvent mentionAlarmEvent) {
        String fcmToken = userService.getFcmTokenByUsername(mentionAlarmEvent.receiver());
        String title = "새로운 언급 알림";
        String content = mentionAlarmEvent.postId() + MENTION_CONTENT_TEMPLATE;
        
        Long userId = userService.getUserByUsername(mentionAlarmEvent.receiver()).id();
        try {
            fcmClient.sendMessageByToken(title, content, fcmToken);
        } catch (FirebaseMessagingException e) {
            // todo 에러 로직 어떻게?
            log.error(e.getMessage());
        }
        
        alarmService.saveAlarm(
            userId,
            mentionAlarmEvent.postId(),
            mentionAlarmEvent.commentId(),
            content,
            AlarmType.MENTION
        );
    }
    
    @Async
    @EventListener
    public void processPostReportAlarm(PostReportAlarmEvent postReportAlarmEvent) {
        // todo 통신 로직 정하고 구현하기
    }
    
    @Async
    @EventListener
    public void processPostReportAlarm(CommentReportAlarmEvent commentReportAlarmEvent) {
        // todo 통신 로직 정하고 구현하기
    }
}
