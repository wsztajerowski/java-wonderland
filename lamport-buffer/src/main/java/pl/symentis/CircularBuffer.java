package pl.symentis;

import java.lang.reflect.Array;
import java.util.Optional;

public class CircularBuffer<T> {
    private volatile int readPosition;
    private volatile int writePosition;
    private final T[] buffer;

    CircularBuffer(T[] buffer) {
        this.readPosition = 0;
        this.writePosition = 0;
        this.buffer = buffer;
    }

    static <T> CircularBuffer<T> createBuffer(Class<T> clazz, int bufferSize){
        //FIXME: Is there a better way of creating below array?
        T[] buffer = (T[]) Array.newInstance(clazz, bufferSize);
        return new CircularBuffer<>(buffer);
    }

    public Optional<T> pop(){
        if (buffer[readPosition] == null){
            return Optional.empty();
        }
        T elem = buffer[readPosition];
        buffer[readPosition] = null;
        int nextReadPosition = (readPosition + 1 >= buffer.length) ? 0 : readPosition + 1;
        readPosition = nextReadPosition;
        return Optional.of(elem);
    }

    public boolean push(T element){
        if(buffer[writePosition] != null){
            return false;
        }
        buffer[writePosition] = element;
        int nextWritePosition = (writePosition + 1 >= buffer.length) ? 0 : writePosition + 1;
        writePosition = nextWritePosition;
        return true;
    }
}