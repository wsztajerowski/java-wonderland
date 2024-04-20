package pl.symentis.infra;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.nio.file.Path;

public class S3Service {
    private final String bucketName;
    private final S3Client s3Client;

    S3Service(S3Client client, String bucketName) {
        s3Client = client;
        this.bucketName = bucketName;
    }

    public void saveFileOnS3(String objectKey, Path pathToFile) {
        PutObjectRequest putOb = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build();

        s3Client.putObject(putOb, RequestBody.fromFile(pathToFile));
    }

    public String getEndpoint(){
        return s3Client
            .serviceClientConfiguration()
            .endpointOverride()
            .map(URI::toString)
            .orElse("https://.s3.%s.amazonaws.com".formatted(s3Client.serviceClientConfiguration().region()));
    }

    public String getBucketName() {
        return bucketName;
    }
}
