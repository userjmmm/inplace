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
import team7.inplace.global.annotation.Client;
import team7.inplace.global.properties.FcmProperties;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Client
@RequiredArgsConstructor
public class FcmClient {
    private final FcmProperties fcmProperties;
    
    @PostConstruct
    public void initialize() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(
            createFirebaseConfigJson().getBytes(StandardCharsets.UTF_8)));
        
        FirebaseOptions options = FirebaseOptions.builder()
                                      .setCredentials(credentials)
                                      .setProjectId(fcmProperties.projectId())
                                      .build();
        
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
    
    private String createFirebaseConfigJson() {
        return String.format(
            "{\n" +
                "  \"type\": \"%s\",\n" +
                "  \"project_id\": \"%s\",\n" +
                "  \"private_key_id\": \"%s\",\n" +
                "  \"private_key\": \"%s\",\n" +
                "  \"client_email\": \"%s\",\n" +
                "  \"client_id\": \"%s\",\n" +
                "  \"auth_uri\": \"%s\",\n" +
                "  \"token_uri\": \"%s\",\n" +
                "  \"auth_provider_x509_cert_url\": \"%s\",\n" +
                "  \"client_x509_cert_url\": \"%s\"\n" +
                "}",
            fcmProperties.type(),
            fcmProperties.projectId(),
            fcmProperties.privateKeyId(),
            fcmProperties.privateKey().replace("\\n", "\n"),
            fcmProperties.clientEmail(),
            fcmProperties.clientId(),
            fcmProperties.authUri(),
            fcmProperties.tokenUri(),
            fcmProperties.authProviderX509CertUrl(),
            fcmProperties.clientX509CertUrl()
        );
        
    }
    
    public void sendMessageByToken(String title, String body, String token) throws FirebaseMessagingException{
        FirebaseMessaging.getInstance().send(Message.builder()
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .setToken(token)
            .build());
    }
}
