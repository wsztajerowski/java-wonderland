package pl.symentis;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

public class S3Service {
    public static final String BUCKET_NAME = "java-wonderland";
    private final S3Client s3Client;

    private S3Service() {
        s3Client = S3Client.builder()
            .build();
    }

    public static S3Service getS3Service() {
        return new S3Service();
    }

    public void saveFileOnS3(String objectKey, Path pathToFile) {
        PutObjectRequest putOb = PutObjectRequest.builder()
            .bucket(BUCKET_NAME)
            .key(objectKey)
//                .metadata(metadata)
            .build();

        s3Client.putObject(putOb, RequestBody.fromFile(pathToFile));
    }
}
