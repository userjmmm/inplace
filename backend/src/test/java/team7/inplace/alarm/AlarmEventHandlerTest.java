package team7.inplace.alarm;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import team7.inplace.alarm.application.AlarmService;
import team7.inplace.alarm.application.FcmClient;
import team7.inplace.alarm.domain.AlarmType;
import team7.inplace.alarm.event.AlarmEvent.MentionAlarmEvent;
import team7.inplace.user.application.UserService;
import team7.inplace.user.application.dto.UserCommand;
import team7.inplace.user.domain.Role;
import team7.inplace.user.domain.User;
import team7.inplace.user.domain.UserType;

import static org.mockito.BDDMockito.*;

@SpringBootTest
@EnableAsync
public class AlarmEventHandlerTest {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @MockBean
    private UserService userService;
    @MockBean
    private FcmClient fcmClient;
    @MockBean
    private AlarmService alarmService;
    
    @Test
    @DisplayName("멘션 이벤트 테스트")
    void test1() throws FirebaseMessagingException {
        // given
        String receiver = "유저2";
        String fcmToken = "tokenValue";
        Long postId = 1L;
        Long commentId = 1L;
        Long userId = 2L;
        User user = new User("유저2", "", "", "", UserType.KAKAO, Role.USER);
        ReflectionTestUtils.setField(user, "id", userId);
        UserCommand.Info info = UserCommand.Info.of(user);
        
        MentionAlarmEvent alarmEvent = new MentionAlarmEvent(postId, commentId, receiver);
        
        given(userService.getUserByUsername(receiver)).willReturn(info);
        given(userService.getFcmTokenByUser(userId)).willReturn(fcmToken);
        willDoNothing().given(fcmClient).sendMessageByToken(anyString(), anyString(), eq(fcmToken));
        willDoNothing().given(alarmService).saveAlarm(
            userId, postId, commentId, postId + "번 게시물에서 회원님을 언급했습니다!", AlarmType.MENTION
        );
        
        // when
        eventPublisher.publishEvent(alarmEvent);
        
        // then
        verify(userService).getFcmTokenByUser(userId);
        verify(userService).getUserByUsername(receiver);
        verify(fcmClient).sendMessageByToken("새로운 언급 알림", "1번 게시물에서 회원님을 언급했습니다!", fcmToken);
        verify(alarmService).saveAlarm(
            userId, postId, commentId, postId + "번 게시물에서 회원님을 언급했습니다!", AlarmType.MENTION
        );
    }
}
