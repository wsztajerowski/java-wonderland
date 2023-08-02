package pl.symentis.entities.jcstress;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.PrePersist;
import pl.symentis.entities.jmh.BenchmarkMetadata;
import pl.symentis.entities.jmh.JmhBenchmarkId;
import pl.symentis.entities.jmh.JmhResult;

@Entity("jcstress-tests")
public class JCStressTest {
    public JCStressTest(){
        this.metadata = new JCStressTestMetadata();
    }
    @Id
    public JCStressTestId testId;

    public JCStressTestMetadata metadata;

    public JCStressResult result;

    public JCStressTest withTestId(JCStressTestId testId) {
        this.testId = testId;
        return this;
    }

    public JCStressTest withMetadata(JCStressTestMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public JCStressTest withResult(JCStressResult result) {
        this.result = result;
        return this;
    }
}
