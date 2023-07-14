
package pl.symentis.entities;

import dev.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class JmhResult {

    private String jmhVersion;
    private String benchmark;
    private String mode;
    private Long threads;
    private Long forks;
    private String jvm;
    private List<String> jvmArgs = new ArrayList<>();
    private String jdkVersion;
    private String vmName;
    private String vmVersion;
    private Long warmupIterations;
    private String warmupTime;
    private Long warmupBatchSize;
    private Long measurementIterations;
    private String measurementTime;
    private Long measurementBatchSize;
    private Metric primaryMetric;
    private Map<String, Metric> secondaryMetrics = new HashMap<>();

    public String getJmhVersion() {
        return jmhVersion;
    }

    public void setJmhVersion(String jmhVersion) {
        this.jmhVersion = jmhVersion;
    }

    public JmhResult withJmhVersion(String jmhVersion) {
        this.jmhVersion = jmhVersion;
        return this;
    }

    public String getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }

    public JmhResult withBenchmark(String benchmark) {
        this.benchmark = benchmark;
        return this;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public JmhResult withMode(String mode) {
        this.mode = mode;
        return this;
    }

    public Long getThreads() {
        return threads;
    }

    public void setThreads(Long threads) {
        this.threads = threads;
    }

    public JmhResult withThreads(Long threads) {
        this.threads = threads;
        return this;
    }

    public Long getForks() {
        return forks;
    }

    public void setForks(Long forks) {
        this.forks = forks;
    }

    public JmhResult withForks(Long forks) {
        this.forks = forks;
        return this;
    }

    public String getJvm() {
        return jvm;
    }

    public void setJvm(String jvm) {
        this.jvm = jvm;
    }

    public JmhResult withJvm(String jvm) {
        this.jvm = jvm;
        return this;
    }

    public List<String> getJvmArgs() {
        return jvmArgs;
    }

    public void setJvmArgs(List<String> jvmArgs) {
        this.jvmArgs = jvmArgs;
    }

    public JmhResult withJvmArgs(List<String> jvmArgs) {
        this.jvmArgs = jvmArgs;
        return this;
    }

    public String getJdkVersion() {
        return jdkVersion;
    }

    public void setJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
    }

    public JmhResult withJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
        return this;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public JmhResult withVmName(String vmName) {
        this.vmName = vmName;
        return this;
    }

    public String getVmVersion() {
        return vmVersion;
    }

    public void setVmVersion(String vmVersion) {
        this.vmVersion = vmVersion;
    }

    public JmhResult withVmVersion(String vmVersion) {
        this.vmVersion = vmVersion;
        return this;
    }

    public Long getWarmupIterations() {
        return warmupIterations;
    }

    public void setWarmupIterations(Long warmupIterations) {
        this.warmupIterations = warmupIterations;
    }

    public JmhResult withWarmupIterations(Long warmupIterations) {
        this.warmupIterations = warmupIterations;
        return this;
    }

    public String getWarmupTime() {
        return warmupTime;
    }

    public void setWarmupTime(String warmupTime) {
        this.warmupTime = warmupTime;
    }

    public JmhResult withWarmupTime(String warmupTime) {
        this.warmupTime = warmupTime;
        return this;
    }

    public Long getWarmupBatchSize() {
        return warmupBatchSize;
    }

    public void setWarmupBatchSize(Long warmupBatchSize) {
        this.warmupBatchSize = warmupBatchSize;
    }

    public JmhResult withWarmupBatchSize(Long warmupBatchSize) {
        this.warmupBatchSize = warmupBatchSize;
        return this;
    }

    public Long getMeasurementIterations() {
        return measurementIterations;
    }

    public void setMeasurementIterations(Long measurementIterations) {
        this.measurementIterations = measurementIterations;
    }

    public JmhResult withMeasurementIterations(Long measurementIterations) {
        this.measurementIterations = measurementIterations;
        return this;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    public JmhResult withMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
        return this;
    }

    public Long getMeasurementBatchSize() {
        return measurementBatchSize;
    }

    public void setMeasurementBatchSize(Long measurementBatchSize) {
        this.measurementBatchSize = measurementBatchSize;
    }

    public JmhResult withMeasurementBatchSize(Long measurementBatchSize) {
        this.measurementBatchSize = measurementBatchSize;
        return this;
    }

    public Metric getPrimaryMetric() {
        return primaryMetric;
    }

    public void setPrimaryMetric(Metric primaryMetric) {
        this.primaryMetric = primaryMetric;
    }

    public JmhResult withPrimaryMetric(Metric primaryMetric) {
        this.primaryMetric = primaryMetric;
        return this;
    }

    public Map<String, Metric> getSecondaryMetrics() {
        return secondaryMetrics;
    }

    public void setSecondaryMetrics(Map<String, Metric> secondaryMetrics) {
        this.secondaryMetrics = secondaryMetrics;
    }

    public JmhResult withSecondaryMetrics(Map<String, Metric> secondaryMetrics) {
        this.secondaryMetrics = secondaryMetrics;
        return this;
    }

    @Override
    public String toString() {
        return "Benchmark{" +
            "jmhVersion='" + jmhVersion + '\'' +
            ", benchmark='" + benchmark + '\'' +
            ", mode='" + mode + '\'' +
            ", threads=" + threads +
            ", forks=" + forks +
            ", jvm='" + jvm + '\'' +
            ", jvmArgs=" + jvmArgs +
            ", jdkVersion='" + jdkVersion + '\'' +
            ", vmName='" + vmName + '\'' +
            ", vmVersion='" + vmVersion + '\'' +
            ", warmupIterations=" + warmupIterations +
            ", warmupTime='" + warmupTime + '\'' +
            ", warmupBatchSize=" + warmupBatchSize +
            ", measurementIterations=" + measurementIterations +
            ", measurementTime='" + measurementTime + '\'' +
            ", measurementBatchSize=" + measurementBatchSize +
            ", primaryMetric=" + primaryMetric +
            ", secondaryMetrics=" + secondaryMetrics +
            '}';
    }
}
