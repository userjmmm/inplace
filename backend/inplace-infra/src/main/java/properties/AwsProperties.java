package properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws") // TODO - springboot web 의존성
public record AwsProperties(
    String accessKey,
    String secretKey,
    String region,
    String bucketName
) {
}
