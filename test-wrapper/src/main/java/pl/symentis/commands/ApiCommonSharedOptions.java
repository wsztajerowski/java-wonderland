package pl.symentis.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import pl.symentis.services.CommonSharedOptions;

@Command
public class ApiCommonSharedOptions {

    @Option(names = {"--mongo-connection-string", "-m"},
        defaultValue = "${MONGO_CONNECTION_STRING}",
        description = "MongoDB connection string - you could provide it as a option value or put in MONGO_CONNECTION_STRING env variable. For details see: https://www.mongodb.com/docs/manual/reference/connection-string/")
    String mongoConnectionString;

    @Option(names = "--commit-sha", defaultValue = "${COMMIT_SHA}", description = "Commit SHA - you could provide it as a option value or put in COMMIT_SHA env variable. Provided value will be truncated to first 8 chars")
    String commitSha;

    @Option(names = "--run-attempt", defaultValue = "${GITHUB_RUN_ATTEMPT}", description = "Run attempt no - you could provide it as a option value or put in GITHUB_RUN_ATTEMPT env variable")
    int runAttempt;

    @Option(names = "--jvm-args", description = "JVM args to pass to benchmark")
    String jvmArgs;

    @Parameters(index = "0", description = "Test name regex", arity = "0..1")
    String testNameRegex;

    public CommonSharedOptions getValues(){
        if (commitSha != null && commitSha.length() > 8) {
            commitSha = commitSha.substring(0,8);
        }
        return new CommonSharedOptions(commitSha, runAttempt, jvmArgs, testNameRegex);
    }

    public String getMongoConnectionString() {
        return mongoConnectionString;
    }
}
