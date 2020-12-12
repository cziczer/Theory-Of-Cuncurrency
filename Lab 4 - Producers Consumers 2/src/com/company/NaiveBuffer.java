package com.company;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NaiveBuffer implements IBuffer {
    int buffer = 0;
    int maxSize;

    private Lock lock = new ReentrantLock();
    private Condition readCondition = lock.newCondition();
    private Condition writeCondition = lock.newCondition();

    public NaiveBuffer(int maxSize) {
        this.maxSize = maxSize;
    }

    public void Produce(int size) {
        lock.lock();
        while (buffer + size > maxSize) {
            try {
                writeCondition.await();
            } catch (InterruptedException e) {
                lock.unlock();
                Thread.currentThread().interrupt(); // need to break thread action
                return;
            }
        }
        this.buffer += size;

        readCondition.signalAll();
        lock.unlock();
    }

    public void Consume(int size) {
        lock.lock();
        while (this.buffer - size < 0) {
            try {
                readCondition.await();
            } catch (InterruptedException e) {
                lock.unlock();
                Thread.currentThread().interrupt(); // need to break thread action
                return;
            }
        }
        this.buffer -= size;

        writeCondition.signal();
        lock.unlock();
    }
}
