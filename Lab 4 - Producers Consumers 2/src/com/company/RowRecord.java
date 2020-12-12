package com.company;

public class RowRecord  implements Comparable<RowRecord>{
    public int bufferSize;
    public int size;
    public String producerConsumer;
    public int ProdConsConfig;
    public boolean fairness;
    public boolean isRandom;
    public int counter;

    public RowRecord(int bufferSize, int size, String producerConsumer, int ProdConsConfig, boolean fairness, boolean isRandom, int counter) {
        this.bufferSize = bufferSize;
        this.size = size;
        this.producerConsumer = producerConsumer;
        this.ProdConsConfig = ProdConsConfig;
        this.fairness = fairness;
        this.isRandom = isRandom;
        this.counter = counter;
    }

    @Override
    public int compareTo(RowRecord o) {
        int b = producerConsumer.compareTo(o.producerConsumer);
        if (b == 0) {
            int compare = Integer.compare(this.size, o.size);
            return compare == 0 ? Integer.compare(this.counter, o.counter) : compare;
        } else return b;
    }
}