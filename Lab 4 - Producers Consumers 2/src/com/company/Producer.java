package com.company;

import java.util.Random;

public class Producer implements Runnable {
    int maxSize;
    IBuffer buffer;
    Histogram histogram;
    boolean probability;
    int counter = 0;

    public Producer(IBuffer buffer, Histogram histogram, int maxSize, boolean probability) {
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

            buffer.Produce(size);
            histogram.Add("PRODUCER-" + size);

            if (Thread.currentThread().isInterrupted()) return;

            this.counter++;
        }
    }
}

