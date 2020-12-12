package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final int consumersNumber = 8;
        final int producersNumber = 8;
        int maxPow = 13;

        Producer[] producers = new Producer[producersNumber];
        Consumer[] consumers = new Consumer[consumersNumber];
        ThreadBuffor threadSafeStorage = new ThreadBuffor();

        for (int i = 0; i < producersNumber; i++) {
            Producer producer = new Producer(threadSafeStorage, i, (int)(Math.random() * ((maxPow) + 1)));
            producers[i] = producer;
            producer.start();
        }

        for (int i = 0; i < consumersNumber; i++) {
            Consumer consumer = new Consumer(threadSafeStorage, i, (int)(Math.random() * ((maxPow) + 1)));
            consumers[i] = consumer;
            consumer.start();
        }

        Thread.sleep(5000);

        System.out.println("\nProducers: ");
        for(Producer producer : producers){
            producer.summary();
        }

        System.out.println("\nConsumers: ");
        for(Consumer consumer : consumers){
            consumer.summary();
        }
    }
}
