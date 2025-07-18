package team7.inplace.alarm.application;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import team7.inplace.global.annotation.Client;

import java.io.IOException;

@Slf4j
@Client
@RequiredArgsConstructor
public class FcmClient {
    @Value("${fcm.service-account-file}")
    private String serviceAccountFilePath;
    
    // 프로젝트 아이디 환경 변수
    @Value("${fcm.project-id}")
    private String projectId;
    
    @PostConstruct
    public void initialize() throws IOException {
        //Firebase 프로젝트 정보를 FireBaseOptions에 입력해준다.
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(serviceAccountFilePath).getInputStream()))
            .setProjectId(projectId)
            .build();
        
        //입력한 정보를 이용하여 initialze 해준다.
        FirebaseApp.initializeApp(options);
    }
    
    // 받은 token을 이용하여 fcm를 보내는 메서드
    public void sendMessageByToken(String title, String body, String token) throws FirebaseMessagingException{
        FirebaseMessaging.getInstance().send(Message.builder()
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .setToken(token)
            .build());
        
        log.info("메세지 전송 성공");
    }
    
}
