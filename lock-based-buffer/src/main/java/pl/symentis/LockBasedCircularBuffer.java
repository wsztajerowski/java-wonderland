package pl.symentis;

import java.lang.reflect.Array;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockBasedCircularBuffer<T> {
    private int readPosition;
    private int writePosition;
    private final T[] buffer;
    private final Lock lock = new ReentrantLock();
    private final Condition canIReadCondition = lock.newCondition();
    private final Condition canIWriteCondition = lock.newCondition();

    LockBasedCircularBuffer(T[] buffer) {
        this.readPosition = 0;
        this.writePosition = 0;
        this.buffer = buffer;
    }

    static <T> LockBasedCircularBuffer<T> createBuffer(Class<T> clazz, int bufferSize){
        //FIXME: Is there a better way of creating below array?
        T[] buffer = (T[]) Array.newInstance(clazz, bufferSize);
        return new LockBasedCircularBuffer<>(buffer);
    }

    public T pop() {
        lock.lock();
        try {
            while (buffer[readPosition] == null) {
                canIReadCondition.await();
            }
            T elem = buffer[readPosition];
            buffer[readPosition] = null;
            int nextReadPosition = (readPosition + 1 >= buffer.length) ? 0 : readPosition + 1;
            readPosition = nextReadPosition;
            canIWriteCondition.signal();
            return elem;
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public T pop(long timeout, TimeUnit unit) throws TimeoutException {
        lock.lock();
        try {
            while (buffer[readPosition] == null) {
                boolean awaitSuccessfully = canIReadCondition.await(timeout, unit);
                if(!awaitSuccessfully){
                    throw new TimeoutException();
                }
            }
            T elem = buffer[readPosition];
            buffer[readPosition] = null;
            int nextReadPosition = (readPosition + 1 >= buffer.length) ? 0 : readPosition + 1;
            readPosition = nextReadPosition;
            canIWriteCondition.signal();
            return elem;
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void push(T element) {
        lock.lock();
        try {
            while (buffer[writePosition] != null) {
                canIWriteCondition.await();
            }
            buffer[writePosition] = element;
            int nextWritePosition = (writePosition + 1 >= buffer.length) ? 0 : writePosition + 1;
            writePosition = nextWritePosition;
            canIReadCondition.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void push(T element, long timeout, TimeUnit unit) throws TimeoutException {
        lock.lock();
        try {
            while (buffer[writePosition] != null) {
                canIWriteCondition.await();
                boolean awaitSuccessfully = canIWriteCondition.await(timeout, unit);
                if(!awaitSuccessfully){
                    throw new TimeoutException();
                }
            }
            buffer[writePosition] = element;
            int nextWritePosition = (writePosition + 1 >= buffer.length) ? 0 : writePosition + 1;
            writePosition = nextWritePosition;
            canIReadCondition.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}