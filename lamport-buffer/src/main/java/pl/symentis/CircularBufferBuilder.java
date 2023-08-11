package pl.symentis;

public final class CircularBufferBuilder<T> {
    private final Class<T> bufferElementsType;
    private int bufferSize;

    private CircularBufferBuilder(Class<T> bufferElementsType) {
        this.bufferElementsType = bufferElementsType;
    }

    public static <T> CircularBufferBuilder<T> circularBufferBuilder(Class<T> type) {
        return new CircularBufferBuilder<>(type);
    }

    public static CircularBufferBuilder<String> stringCircularBufferBuilder(){
        return circularBufferBuilder(String.class);
    }

    public static CircularBufferBuilder<Long> longCircularBufferBuilder(){
        return circularBufferBuilder(Long.class);
    }

    public static CircularBufferBuilder<Integer> integerCircularBufferBuilder(){
        return circularBufferBuilder(Integer.class);
    }

    public CircularBufferBuilder<T> withBufferSize(int bufferSize){
        this.bufferSize = bufferSize;
        return this;
    }

    public CircularBuffer<T> build() {
        return CircularBuffer.createBuffer(bufferElementsType, bufferSize);
    }
}
