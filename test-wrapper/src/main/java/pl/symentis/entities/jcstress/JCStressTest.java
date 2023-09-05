package pl.symentis.entities.jcstress;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("jcstress-tests")
public record JCStressTest(
    @Id
    JCStressTestId testId,
    JCStressTestMetadata metadata,
    JCStressResult result) {
}
