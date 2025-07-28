package team7.inplace.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fcm")
public record FcmProperties(
        String projectId,
        String type,
        String privateKeyId,
        String privateKey,
        String clientEmail,
        String clientId,
        String authUri,
        String tokenUri,
        String authProviderX509CertUrl,
        String clientX509CertUrl,
        String universeDomain
) {

}
