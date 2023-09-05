package pl.symentis.entities.jcstress;

import dev.morphia.annotations.Entity;

import java.time.OffsetDateTime;

@Entity
public record JCStressTestMetadata(OffsetDateTime createdAt) {
    public JCStressTestMetadata() {
        this(OffsetDateTime.now());
    }
}
