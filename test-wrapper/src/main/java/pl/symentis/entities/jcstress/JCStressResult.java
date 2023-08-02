package pl.symentis.entities.jcstress;

import dev.morphia.annotations.Entity;

import java.util.HashMap;
import java.util.Map;

@Entity
public class JCStressResult {
    public int totalTests;
    public int passedTests;
    public String javaVendor;
    public String javaVmName;
    public String javaVmVendor;
    public String javaVmVersion;
    public String osArch;
    public String osName;
    public String osVersion;
    public Map<String, String> testsWithFailedResults = new HashMap<>();
    public Map<String, String> testsWithErrorResults = new HashMap<>();
    public Map<String, String> testsWithInterestingResults = new HashMap<>();

    public JCStressResult withTotalTests(int totalTests) {
        this.totalTests = totalTests;
        return this;
    }

    public JCStressResult withPassedTests(int passedTests) {
        this.passedTests = passedTests;
        return this;
    }

    public JCStressResult withJavaVendor(String javaVendor) {
        this.javaVendor = javaVendor;
        return this;
    }

    public JCStressResult withJavaVmName(String javaVmName) {
        this.javaVmName = javaVmName;
        return this;
    }

    public JCStressResult withJavaVmVendor(String javaVmVendor) {
        this.javaVmVendor = javaVmVendor;
        return this;
    }

    public JCStressResult withJavaVmVersion(String javaVmVersion) {
        this.javaVmVersion = javaVmVersion;
        return this;
    }

    public JCStressResult withOsArch(String osArch) {
        this.osArch = osArch;
        return this;
    }

    public JCStressResult withOsName(String osName) {
        this.osName = osName;
        return this;
    }

    public JCStressResult withOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public JCStressResult withTestsWithFailedResults(Map<String, String> testsWithFailedResults) {
        this.testsWithFailedResults = testsWithFailedResults;
        return this;
    }

    public JCStressResult withTestsWithErrorResults(Map<String, String> testsWithErrorResults) {
        this.testsWithErrorResults = testsWithErrorResults;
        return this;
    }

    public JCStressResult withTestsWithInterestingResults(Map<String, String> testsWithInterestingResults) {
        this.testsWithInterestingResults = testsWithInterestingResults;
        return this;
    }

    public JCStressResult addTestWithInterestingResults(String testName, String resultFilePath) {
        this.testsWithInterestingResults.put(testName, resultFilePath);
        return this;
    }

    public JCStressResult addTestWithFailedResults(String testName, String resultFilePath) {
        this.testsWithFailedResults.put(testName, resultFilePath);
        return this;
    }

    public JCStressResult addTestWithErrorResults(String testName, String resultFilePath) {
        this.testsWithErrorResults.put(testName, resultFilePath);
        return this;
    }

    public boolean hasUnsuccessfulTests(){
        return totalTests == passedTests;
    }

    public Map<String, String> getAllUnsuccessfulTest(){
        HashMap<String, String> tests = new HashMap<>();
        tests.putAll(testsWithErrorResults);
        tests.putAll(testsWithFailedResults);
        tests.putAll(testsWithInterestingResults);
        return tests;
    }
}
