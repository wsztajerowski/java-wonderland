package pl.symentis.services;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

public class S3Service {
    public static final String BUCKET_NAME = "java-wonderland";
    private final S3Client s3Client;

    private S3Service(S3Client client) {
        s3Client = client;
    }

    public static S3Service getS3Service() {
        S3Client client = S3Client
            .builder()
            .build();
        return new S3Service(client);
    }

    public void saveFileOnS3(String objectKey, Path pathToFile) {
        PutObjectRequest putOb = PutObjectRequest.builder()
            .bucket(BUCKET_NAME)
            .key(objectKey)
            .build();

        s3Client.putObject(putOb, RequestBody.fromFile(pathToFile));
    }
}
