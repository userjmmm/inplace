package team7.inplace.alarm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team7.inplace.alarm.application.FcmClient;

@SpringBootTest
public class FcmClientTest {
    @Autowired
    private FcmClient fcmClient;
    
    @Test
    public void sendMessageByToken_realRequest() throws Exception {
        String testToken = "실제 FCM 토큰";
        fcmClient.sendMessageByToken("테스트 제목", "테스트 본문", testToken);
        // 예외 없으면 성공, 실패 시 FirebaseMessagingException 발생
    }
}
