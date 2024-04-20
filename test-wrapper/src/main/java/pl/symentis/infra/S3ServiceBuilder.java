package pl.symentis.infra;

import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

public class S3ServiceBuilder {
    public static final String DEFAULT_BUCKET_NAME = "java-wonderland";
    private S3Client s3Client;
    private String bucketName = DEFAULT_BUCKET_NAME;

    private S3ServiceBuilder(){
    }

    public static S3ServiceBuilder getS3ServiceBuilder(){
        return new S3ServiceBuilder();
    }

    public static S3ServiceBuilder getDefaultS3ServiceBuilder(){
        String customEndpoint = System.getenv("AWS_ENDPOINT");
        S3Client client = S3Client
            .builder()
//            .region(Region.EU_CENTRAL_1)
            .endpointOverride(customEndpoint != null ? URI.create(customEndpoint) : null)
            .build();
        return new S3ServiceBuilder()
            .withS3Client(client);
    }

    public S3ServiceBuilder withS3Client(S3Client client){
        this.s3Client = client;
        return this;
    }

    public S3ServiceBuilder withBucketName(String bucketName){
        this.bucketName = bucketName;
        return this;
    }

    public S3Service build(){
        return new S3Service(s3Client, bucketName);
    }
}
