package com.company;

import javax.xml.crypto.Data;
import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	    final Buffor buffor = new Buffor();

	    int maxPortion = 13;
        final Lock lock = new ReentrantLock();
        final Condition notFull = lock.newCondition();
        final Condition notEmpty = lock.newCondition();

        var startDate = System.currentTimeMillis();

        Thread[] producents = new Thread[8];
        Thread[] consuments = new Thread[8];

        for (int i = 0; i< 8; i++) {
            producents[i] = new Thread(() -> {
                while (true) {
                    lock.lock();
                        try {
                            while(buffor.value > buffor.maxValue)
                                notEmpty.await();
                            double randomNum = ThreadLocalRandom.current().nextInt(0, maxPortion + 1);
                            double addValue = Math.pow(2, randomNum);
                            buffor.value += addValue;
                            System.out.println(addValue + " " + buffor.value);
                            notFull.signal();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            lock.unlock();
                        }
                    }
                //System.out.println("Koniec");
            });

            consuments[i] = new Thread(() ->{
                while (true) {
                    lock.lock();
                    try {
                        while(buffor.value < 0)
                            notFull.await();
                        double randomNum = ThreadLocalRandom.current().nextInt(0, maxPortion + 1);
                        double addValue = Math.pow(2, randomNum);
                        buffor.value -= addValue;
                        System.out.println("-" + addValue + " " + buffor.value);
                        notEmpty.signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }
        while (startDate + 100 > System.currentTimeMillis()){
            for (int i = 0; i< 8; i++) {
                producents[i].start();
                consuments[i].start();
            }
        }
    }
}
