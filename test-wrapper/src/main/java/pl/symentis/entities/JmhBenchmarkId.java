package pl.symentis.entities;

import dev.morphia.annotations.Entity;

@Entity
public class JmhBenchmarkId {
    public String commitSha;

    public String benchmarkName;
    public String benchmarkType;
    public Integer runAttempt;

    public JmhBenchmarkId() {
    }

    public JmhBenchmarkId withCommitSha(String commitSha) {
        this.commitSha = commitSha;
        return this;
    }

    public JmhBenchmarkId withBenchmarkName(String benchmarkName) {
        this.benchmarkName = benchmarkName;
        return this;
    }

    public JmhBenchmarkId withBenchmarkType(String benchmarkType) {
        this.benchmarkType = benchmarkType;
        return this;
    }

    public JmhBenchmarkId withRunAttempt(Integer runAttempt) {
        this.runAttempt = runAttempt;
        return this;
    }

    @Override
    public String toString() {
        return "JmhBenchmarkId{" +
            "commitSha='" + commitSha + '\'' +
            ", benchmarkName='" + benchmarkName + '\'' +
            ", benchmarkType='" + benchmarkType + '\'' +
            ", runAttempt=" + runAttempt +
            '}';
    }
}
