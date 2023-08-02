package pl.symentis;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testcontainers.shaded.com.google.common.reflect.TypeToken;
import pl.symentis.entities.jmh.JmhResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class ResultLoaderService {
    public static final String JMH_RESULT_FILENAME = "jmh-result.json";
    private final Gson gson;

    private ResultLoaderService(){
        gson = Converters.registerLocalDateTime(new GsonBuilder()
                .enableComplexMapKeySerialization()
                .setVersion(1.0))
            .create();
    }

    public static ResultLoaderService getResultLoaderService() {
        return new ResultLoaderService();
    }


    public List<JmhResult> loadJmhResults() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(
            new FileReader(JMH_RESULT_FILENAME));
        Type listType = new TypeToken<List<JmhResult>>() {
        }.getType();
        return gson.fromJson(bufferedReader, listType);
    }
}
