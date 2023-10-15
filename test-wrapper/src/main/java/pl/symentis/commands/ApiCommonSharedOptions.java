package pl.symentis.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import pl.symentis.services.CommonSharedOptions;

@Command
public class ApiCommonSharedOptions {

    @Option(names = "--commit-sha", defaultValue = "${GITHUB_SHA}", description = "Commit SHA - you could provide it as a option value or put in GITHUB_SHA env variable")
    String commitSha;

    @Option(names = "--run-attempt", defaultValue = "${GITHUB_RUN_ATTEMPT}", description = "Run attempt no - you could provide it as a option value or put in GITHUB_RUN_ATTEMPT env variable")
    int runAttempt;

    @Option(names = "--jvm-args", description = "JVM args to pass to benchmark")
    String jvmArgs;

    @Parameters(index = "0", description = "Test name regex", arity = "0..1")
    String testNameRegex;

    public CommonSharedOptions getValues(){
        return new CommonSharedOptions(commitSha, runAttempt, jvmArgs, testNameRegex);
    }

}
