package pl.symentis.services.options;

import java.nio.file.Path;

public record AsyncProfilerOptions(
    String asyncPath,
    int asyncInterval,
    String asyncOutputType,
    Path asyncOutputPath) {

    public static AsyncProfilerOptionsBuilder asyncProfilerOptionsBuilder() {
        return new AsyncProfilerOptionsBuilder();
    }

    public static final class AsyncProfilerOptionsBuilder {
        private String asyncPath;
        private int asyncInterval;
        private String asyncOutputType;
        private Path asyncOutputPath;

        private AsyncProfilerOptionsBuilder() {
        }

        public AsyncProfilerOptionsBuilder withAsyncPath(String asyncPath) {
            this.asyncPath = asyncPath;
            return this;
        }

        public AsyncProfilerOptionsBuilder withAsyncInterval(int asyncInterval) {
            this.asyncInterval = asyncInterval;
            return this;
        }

        public AsyncProfilerOptionsBuilder withAsyncOutputType(String asyncOutputType) {
            this.asyncOutputType = asyncOutputType;
            return this;
        }

        public AsyncProfilerOptionsBuilder withAsyncOutputPath(Path asyncOutputPath) {
            this.asyncOutputPath = asyncOutputPath;
            return this;
        }

        public AsyncProfilerOptions build() {
            return new AsyncProfilerOptions(asyncPath, asyncInterval, asyncOutputType, asyncOutputPath);
        }
    }
}
