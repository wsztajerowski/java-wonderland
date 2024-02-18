package pl.symentis.infra;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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
}
