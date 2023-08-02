package pl.symentis.entities.jcstress;

import dev.morphia.annotations.Entity;

@Entity
public class JCStressTestId {
    public String commitSha;
    public Integer runAttempt;

    public JCStressTestId() {
    }

    public JCStressTestId withCommitSha(String commitSha) {
        this.commitSha = commitSha;
        return this;
    }

    public JCStressTestId withRunAttempt(Integer runAttempt) {
        this.runAttempt = runAttempt;
        return this;
    }
}
