package com.company;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadBuffor {
    final Lock lock = new ReentrantLock();
    final Condition notFull  = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final int maxSize = 16384;
    private int currentSize = 0;

    public void produce(int amount) throws InterruptedException {
        lock.lock();
        try {
            while (currentSize + amount > maxSize)
                notFull.await();
            currentSize += amount;
            notEmpty.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void consume(int amount) throws InterruptedException {
        lock.lock();
        try {
            while (currentSize < amount)
                notEmpty.await();
            currentSize -= amount;
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }
}