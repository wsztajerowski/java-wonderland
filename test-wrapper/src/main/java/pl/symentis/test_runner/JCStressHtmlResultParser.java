package pl.symentis.test_runner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.lang.Integer.*;

public class JCStressHtmlResultParser {


    private final HtmlParser htmlParser;

    public JCStressHtmlResultParser(HtmlParser htmlParser) {
        this.htmlParser = htmlParser;
    }

    public static JCStressHtmlResultParser getJCStressHtmlResultParser(Path resultFilepath) throws IOException {
        HtmlParser htmlParser = HtmlParser.getHtmlParser(resultFilepath);
        return new JCStressHtmlResultParser(htmlParser);
    }

    public List<String> getTestsWithFailedResults(){
        return getTestsWithResults("FAILED");
    }

    public List<String> getTestsWithErrorResults(){
        return getTestsWithResults("ERROR");
    }

    public List<String> getTestsWithInterestingResults(){
        return getTestsWithResults("INTERESTING");
    }

    public int getPassedTests() {
        return getOverallRate()[0];
    }

    public int getTotalTests() {
        return getOverallRate()[1];
    }

    public String getJavaVendor(){
        return getEnvironmentProperty("java.vendor");
    }

    public String getJavaVmName(){
        return getEnvironmentProperty("java.vm.name");
    }

    public String getJavaVmVendor(){
        return getEnvironmentProperty("java.vm.vendor");
    }

    public String getJavaVmVersion(){
        return getEnvironmentProperty("java.vm.version");
    }

    public String getOsArch(){
        return getEnvironmentProperty("os.arch");
    }

    public String getOsName(){
        return getEnvironmentProperty("os.name");
    }

    public String getOsVersion(){
        return getEnvironmentProperty("os.version");
    }

    private String getEnvironmentProperty(String propertyName) {
        String selector = "td:containsOwn(" + propertyName + ")";
        return htmlParser
            .getTextOfNextSibling(selector);
    }

    private List<String> getTestsWithResults(String resultType){
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
}
