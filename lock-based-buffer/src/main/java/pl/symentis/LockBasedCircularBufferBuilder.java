package pl.symentis;

public final class LockBasedCircularBufferBuilder<T> {
    private final Class<T> bufferElementsType;
    private int bufferSize;

    private LockBasedCircularBufferBuilder(Class<T> bufferElementsType) {
        this.bufferElementsType = bufferElementsType;
    }

    public static <T> LockBasedCircularBufferBuilder<T> circularBufferBuilder(Class<T> type) {
        return new LockBasedCircularBufferBuilder<>(type);
    }

    public static LockBasedCircularBufferBuilder<String> stringCircularBufferBuilder(){
        return circularBufferBuilder(String.class);
    }

    public static LockBasedCircularBufferBuilder<Long> longCircularBufferBuilder(){
        return circularBufferBuilder(Long.class);
    }

    public static LockBasedCircularBufferBuilder<Integer> integerCircularBufferBuilder(){
        return circularBufferBuilder(Integer.class);
    }

    public LockBasedCircularBufferBuilder<T> withBufferSize(int bufferSize){
        this.bufferSize = bufferSize;
        return this;
    }

    public LockBasedCircularBuffer<T> build() {
        return LockBasedCircularBuffer.createBuffer(bufferElementsType, bufferSize);
    }
}
