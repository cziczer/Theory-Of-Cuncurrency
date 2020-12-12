package com.company;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer implements IBuffer {
    int currentSize = 0;
    int maxSize;

    private Lock lock = new ReentrantLock();
    private Condition readCondition = lock.newCondition();
    private Condition writeCondition = lock.newCondition();

    private Lock consumer = new ReentrantLock(true);
    private Lock producer = new ReentrantLock(true);


    public Buffer(int maxSize) {
        this.maxSize = maxSize;
    }

    public void Produce(int size) {
        producer.lock();
        lock.lock();
        while (currentSize + size > maxSize) {
            try {
                writeCondition.await();
            } catch (InterruptedException e) {
                lock.unlock();
                producer.unlock();
                Thread.currentThread().interrupt(); // need to break thread action
                return;
            }
        }
        this.currentSize += size;

        readCondition.signal();
        lock.unlock();
        producer.unlock();
    }

    public void Consume(int size) {
        consumer.lock();
        lock.lock();
        while (this.currentSize - size < 0) {
            try {
                readCondition.await();
            } catch (InterruptedException e) {
                lock.unlock();
                consumer.unlock();
                Thread.currentThread().interrupt(); // need to break thread action
                return;
            }
        }
        this.currentSize -= size;

        writeCondition.signal();
        lock.unlock();
        consumer.unlock();
    }
}
