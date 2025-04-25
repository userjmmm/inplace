package team7.inplace.token.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import team7.inplace.global.annotation.Client;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.UserErrorCode;

@Client
@RequiredArgsConstructor
public class KakaoOauthClient {

    private final String UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";
    private final RestTemplate restTemplate;

    public void unlink(String accessToken) {
        var header = new HttpHeaders();
        header.set("Authorization", "Bearer " + accessToken);
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var request = restTemplate.exchange(RequestEntity
                .method(HttpMethod.POST, UNLINK_URL)
                .headers(header)
                .build(),
            String.class);

        if (request.getStatusCode().isError()) {
            throw InplaceException.of(UserErrorCode.FAIL_OAUTH_TOKEN_UNLINKE);
        }
    }
}
