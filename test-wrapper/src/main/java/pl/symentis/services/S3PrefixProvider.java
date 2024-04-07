package pl.symentis.services;

import java.nio.file.Path;

public class S3PrefixProvider {
    public static Path jmhS3Prefix(String commitSha, int runAttempt) {
        return Path.of(compileS3Prefix("jmh", commitSha, runAttempt));
    }

    public static Path jmhWithAsyncS3Prefix(String commitSha, int runAttempt) {
        return Path.of(compileS3Prefix("jmh-with-async", commitSha, runAttempt));
    }

    public static Path jcstressS3Prefix(String commitSha, int runAttempt) {
        return Path.of(compileS3Prefix("jcstress", commitSha, runAttempt));
    }

    private static String compileS3Prefix(String testType, String commitSha, int runAttempt){
        return "gha-outputs/commit-%s/attempt-%d/%s".formatted(commitSha, runAttempt, testType);
    }
}
