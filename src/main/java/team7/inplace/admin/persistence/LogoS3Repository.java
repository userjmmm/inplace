package team7.inplace.admin.persistence;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import team7.inplace.infra.s3.AwsProperties;

@Repository
@RequiredArgsConstructor
public class LogoS3Repository {
    private final AmazonS3Client amazonS3Client;
    private final AwsProperties awsProperties;

    public String uploadLogo(String logoName, MultipartFile logo) {
        var bucketName = awsProperties.bucketName();
        var key = "logo/" + logoName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(logo.getSize());
        metadata.setContentType(logo.getContentType());

        try {
            amazonS3Client.putObject(bucketName, key, logo.getInputStream(), metadata);
            return amazonS3Client.getUrl(bucketName, key).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload logo", e);
        }
    }
}
