package com.company;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        var histogram = new Histogram();
        histogram.CreateHeader();
        for (int producersConsumersAmount : List.of(100, 1000)) {
            for (int M : List.of(10000, 100000)) {
                for (boolean probabilityBiased : List.of(true, false)) {
                    for (boolean fairness : List.of(true, false)) {

                        Execute(histogram, M, producersConsumersAmount, fairness, probabilityBiased);
                    }
                }
            }
        }
    }

    private static void Execute(Histogram histogram, int M, int producersConsumersAmount,
                                boolean fairness, boolean probabilityBiased) throws InterruptedException {
        IBuffer buffer;
        if (fairness) {
            buffer = new Buffer(M);
        } else {
            buffer = new NaiveBuffer(M);
        }

        ExecutorService es = Executors.newFixedThreadPool(2 * producersConsumersAmount + producersConsumersAmount);

        for (int i = 0; i < producersConsumersAmount; i++) {
            Producer producer = new Producer(buffer, histogram, M / 2, probabilityBiased);
            Consumer consumer = new Consumer(buffer, histogram, M / 2, probabilityBiased);
            es.submit(producer);
            es.submit(consumer);
        }
        Thread.sleep(1000);

        es.shutdownNow();
        es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        histogram.SafeRecords(M, producersConsumersAmount, fairness, probabilityBiased);
    }
}
