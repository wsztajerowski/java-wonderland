package pl.symentis.entities.jcstress;

import dev.morphia.annotations.Entity;

@Entity
public record JCStressTestId(
    String commitSha,
    Integer runAttempt) {
}
