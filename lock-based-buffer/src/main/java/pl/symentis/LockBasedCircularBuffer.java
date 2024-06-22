package pl.symentis;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.currentTimeMillis;

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

    public T pop(long timeoutInMilliseconds) throws TimeoutException {
        long timeoutTime = currentTimeMillis() + timeoutInMilliseconds;
        lock.lock();
        try {
            while (buffer[readPosition] == null) {
                var millisecondsUntilTimeout = timeoutTime - currentTimeMillis();
                var awaitSuccessfully = canIReadCondition.await(millisecondsUntilTimeout, TimeUnit.MILLISECONDS);
                if( !awaitSuccessfully || currentTimeMillis() > timeoutTime ){
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

    public void push(T element, long timeoutInMilliseconds) throws TimeoutException {
        long timeoutTime = currentTimeMillis() + timeoutInMilliseconds;
        lock.lock();
        try {
            while (buffer[writePosition] != null) {
                var millisecondsUntilTimeout = timeoutTime - currentTimeMillis();
                boolean awaitSuccessfully = canIWriteCondition.await(millisecondsUntilTimeout, TimeUnit.MILLISECONDS);
                if( !awaitSuccessfully ||  currentTimeMillis() > timeoutTime ){
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