package com.company;

import java.util.Random;

public class Consumer implements Runnable {
    int maxSize;
    IBuffer buffer;
    Histogram histogram;
    int counter = 0;
    boolean probability;

    public Consumer(IBuffer buffer, Histogram histogram, int maxSize, boolean probability) {
        this.buffer = buffer;
        this.maxSize = maxSize;
        this.histogram = histogram;
        this.probability = probability;
    }

    @Override
    public void run() {
        while (true) {
            var size = this.probability ?
                    (int)(Math.random() * ((maxSize) + 1))
                    : (int) Math.floor((maxSize + 1) * (Math.pow(new Random().nextDouble(), 2)));

            buffer.Consume(size);
            histogram.Add("CONSUMER-" + size);

            if (Thread.currentThread().isInterrupted()) return;

            this.counter++;
        }
    }
}
