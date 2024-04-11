package pl.symentis.services;

import dev.morphia.annotations.Entity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.symentis.MongoDbTestHelpers;
import pl.symentis.TestcontainersWithS3AndMongoBaseIT;
import pl.symentis.entities.jmh.JmhBenchmark;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static pl.symentis.MongoDbTestHelpers.all;
import static pl.symentis.services.JmhSubcommandServiceBuilder.getJmhSubcommandService;

class JmhSubcommandServiceIT extends TestcontainersWithS3AndMongoBaseIT {

    private static MongoDbTestHelpers helper;

    @BeforeAll
    static void setupHelper(){
        helper = new MongoDbTestHelpers(getConnectionString());
    }

    @Test
    void successful_scenario(){
        // given
        String jhhTestBenchmark = Path.of("target", "fake-jmh-benchmarks.jar").toAbsolutePath().toString();
        JmhSubcommandService sut = getJmhSubcommandService()
            .withMongoConnectionString(getConnectionString())
            .withCommonOptions(new CommonSharedOptions("commit-sha", 1, "", "incrementUsingSynchronized"))
            .withJmhOptions(new JmhBenchmarksSharedOptions(0, 1, 1, jhhTestBenchmark))
            .build();

        // when
        sut.executeCommand();

        // then
        String collectionName = JmhBenchmark.class.getAnnotation(Entity.class).value();
        helper.assertFindResult(collectionName, all(), documents ->
            assertThat(documents.first())
                .isNotNull()
                .containsEntry("_t", "JmhBenchmark")
                .extracting("_id.benchmarkName", as(STRING))
                .endsWith("incrementUsingSynchronized")
        );
    }


}