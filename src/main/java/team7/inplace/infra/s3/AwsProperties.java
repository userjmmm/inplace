package team7.inplace.infra.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public record AwsProperties(
        String accessKey,
        String secretKey,
        String region,
        String bucketName
) {
}
