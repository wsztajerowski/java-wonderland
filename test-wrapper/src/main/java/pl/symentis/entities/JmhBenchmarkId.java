package pl.symentis.entities;

import dev.morphia.annotations.Entity;

@Entity
public class JmhBenchmarkId {
    private String commitSha;

    private String benchmarkName;
    private String benchmarkType;
    private Integer runAttempt;

    public JmhBenchmarkId() {
    }

    public JmhBenchmarkId(String commitSha, String benchmarkName, String benchmarkType, Integer runAttempt) {
        this.commitSha = commitSha;
        this.benchmarkName = benchmarkName;
        this.benchmarkType = benchmarkType;
        this.runAttempt = runAttempt;
    }

    public String getCommitSha() {
        return commitSha;
    }

    public void setCommitSha(String commitSha) {
        this.commitSha = commitSha;
    }

    public JmhBenchmarkId withCommitSha(String commitSha) {
        this.commitSha = commitSha;
        return this;
    }

    public Integer getRunAttempt() {
        return runAttempt;
    }

    public void setRunAttempt(Integer runAttempt) {
        this.runAttempt = runAttempt;
    }

    public JmhBenchmarkId withRunAttempt(Integer runAttempt) {
        this.runAttempt = runAttempt;
        return this;
    }

    public String getBenchmarkType() {
        return benchmarkType;
    }

    public void setBenchmarkType(String benchmarkType) {
        this.benchmarkType = benchmarkType;
    }

    public String getBenchmarkName() {
        return benchmarkName;
    }

    public void setBenchmarkName(String benchmarkName) {
        this.benchmarkName = benchmarkName;
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
