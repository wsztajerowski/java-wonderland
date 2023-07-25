
package pl.symentis.entities;

import dev.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class JmhResult {

    public String jmhVersion;
    public String benchmark;
    public String mode;
    public Long threads;
    public Long forks;
    public String jvm;
    public List<String> jvmArgs = new ArrayList<>();
    public String jdkVersion;
    public String vmName;
    public String vmVersion;
    public Long warmupIterations;
    public String warmupTime;
    public Long warmupBatchSize;
    public Long measurementIterations;
    public String measurementTime;
    public Long measurementBatchSize;
    public Metric primaryMetric;
    public Map<String, Metric> secondaryMetrics = new HashMap<>();

    public JmhResult withJmhVersion(String jmhVersion) {
        this.jmhVersion = jmhVersion;
        return this;
    }

    public JmhResult withBenchmark(String benchmark) {
        this.benchmark = benchmark;
        return this;
    }

    public JmhResult withMode(String mode) {
        this.mode = mode;
        return this;
    }

    public JmhResult withThreads(Long threads) {
        this.threads = threads;
        return this;
    }

    public JmhResult withForks(Long forks) {
        this.forks = forks;
        return this;
    }

    public JmhResult withJvm(String jvm) {
        this.jvm = jvm;
        return this;
    }

    public JmhResult withJvmArgs(List<String> jvmArgs) {
        this.jvmArgs = jvmArgs;
        return this;
    }

    public JmhResult withJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
        return this;
    }

    public JmhResult withVmName(String vmName) {
        this.vmName = vmName;
        return this;
    }

    public JmhResult withVmVersion(String vmVersion) {
        this.vmVersion = vmVersion;
        return this;
    }

    public JmhResult withWarmupIterations(Long warmupIterations) {
        this.warmupIterations = warmupIterations;
        return this;
    }

    public JmhResult withWarmupTime(String warmupTime) {
        this.warmupTime = warmupTime;
        return this;
    }

    public JmhResult withWarmupBatchSize(Long warmupBatchSize) {
        this.warmupBatchSize = warmupBatchSize;
        return this;
    }

    public JmhResult withMeasurementIterations(Long measurementIterations) {
        this.measurementIterations = measurementIterations;
        return this;
    }

    public JmhResult withMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
        return this;
    }

    public JmhResult withMeasurementBatchSize(Long measurementBatchSize) {
        this.measurementBatchSize = measurementBatchSize;
        return this;
    }
    
    public JmhResult withPrimaryMetric(Metric primaryMetric) {
        this.primaryMetric = primaryMetric;
        return this;
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
