package pl.symentis.entities.jcstress;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.PrePersist;

import java.time.LocalDateTime;

@Entity
public class JCStressTestMetadata {
    public LocalDateTime createdAt;

    @PrePersist
    public void addCreatedAt() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

}
