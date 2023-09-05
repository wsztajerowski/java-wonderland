package pl.symentis.commands;

import pl.symentis.entities.jcstress.JCStressResult;
import pl.symentis.entities.jcstress.JCStressResultBuilder;

import java.nio.file.Path;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.text.MessageFormat.format;
import static pl.symentis.commands.HtmlParser.getHtmlParser;
import static pl.symentis.entities.jcstress.JCStressResultBuilder.*;

public class JCStressHtmlResultParser {

    private final HtmlParser htmlParser;
    private final String commitSha;
    private final int runAttempt;

    JCStressHtmlResultParser(HtmlParser htmlParser, String commitSha, int runAttempt) {
        this.htmlParser = htmlParser;
        this.commitSha = commitSha;
        this.runAttempt = runAttempt;
    }

    public static JCStressHtmlResultParser getJCStressHtmlResultParser(Path resultFilepath, String commitSha, int runAttempt) {
        HtmlParser htmlParser = getHtmlParser(resultFilepath);
        return new JCStressHtmlResultParser(htmlParser, commitSha, runAttempt);
    }

    public List<String> getTestsWithFailedResults() {
        return getTestsWithResults("FAILED");
    }

    public List<String> getTestsWithErrorResults() {
        return getTestsWithResults("ERROR");
    }

    public List<String> getTestsWithInterestingResults() {
        return getTestsWithResults("INTERESTING");
    }

    public int getPassedTests() {
        return getOverallRate()[0];
    }

    public int getTotalTests() {
        return getOverallRate()[1];
    }

    public String getJavaVendor() {
        return getEnvironmentProperty("java.vendor");
    }

    public String getJavaVmName() {
        return getEnvironmentProperty("java.vm.name");
    }

    public String getJavaVmVendor() {
        return getEnvironmentProperty("java.vm.vendor");
    }

    public String getJavaVmVersion() {
        return getEnvironmentProperty("java.vm.version");
    }

    public String getOsArch() {
        return getEnvironmentProperty("os.arch");
    }

    public String getOsName() {
        return getEnvironmentProperty("os.name");
    }

    public String getOsVersion() {
        return getEnvironmentProperty("os.version");
    }

    private String getEnvironmentProperty(String propertyName) {
        String selector = "td:containsOwn(" + propertyName + ")";
        return htmlParser
            .getTextOfNextSibling(selector);
    }

    private List<String> getTestsWithResults(String resultType) {
        String selector = "h3:containsOwn(" + resultType + ") ~ table";
        return htmlParser
            .getParserForSelector(selector)
            .orElseGet(HtmlParser::getEmptyParser)
            .getListValuesForSelector("td > a");
    }

    private int[] getOverallRate() {
        String overallRateText = htmlParser
            .getTextOfParent("b:containsOwn(Overall pass rate)");
        String[] rate = overallRateText
            .replace("Overall pass rate: ", "")
            .split("/");
        return new int[]{
            parseInt(rate[0]),
            parseInt(rate[1])
        };
    }

    public JCStressResult parse() {
        JCStressResultBuilder jcStressResultBuilder = getJCStressResult()
            .withTotalTests(this.getTotalTests())
            .withPassedTests(this.getPassedTests())
            .withJavaVendor(this.getJavaVendor())
            .withJavaVmVendor(this.getJavaVmVendor())
            .withJavaVmName(this.getJavaVmName())
            .withJavaVmVersion(this.getJavaVmVersion())
            .withOsArch(this.getOsArch())
            .withOsName(this.getOsName())
            .withOsVersion(this.getOsVersion());
        getTestsWithErrorResults()
            .forEach(test -> jcStressResultBuilder.addTestWithErrorResults(test, createS3Path(this.commitSha, this.runAttempt, test)));
        getTestsWithFailedResults()
            .forEach(test -> jcStressResultBuilder.addTestWithFailedResults(test, createS3Path(this.commitSha, this.runAttempt, test)));
        getTestsWithInterestingResults()
            .forEach(test -> jcStressResultBuilder.addTestWithInterestingResults(test, createS3Path(this.commitSha, this.runAttempt, test)));
        return jcStressResultBuilder
            .build();
    }

    private String createS3Path(String commitSha, int runAttempt, String testName) {
        return format("gha-outputs/commit-{0}/attempt-{1}/jcstress/{2}.html",
            commitSha,
            runAttempt,
            testName);
    }

}
