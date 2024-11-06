package team7.inplace.placeMessage.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team7.inplace.global.kakao.config.KakaoApiProperties;

@Component
@RequiredArgsConstructor
public class KakaoMessageClient {

    private final KakaoApiProperties kakaoApiProperties;
    private final KakaoMessageMaker kakaoMessageMaker;

    public void sendLocationMessageToMe() {
        
    }
}
