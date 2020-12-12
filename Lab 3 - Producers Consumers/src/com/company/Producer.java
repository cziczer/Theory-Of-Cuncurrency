package com.company;

import java.util.ArrayList;

public class Producer extends Thread {
    ThreadBuffor buffor;
    int id;
    int successProduction = 0;
    int portionSize;

    Producer(ThreadBuffor buffor, int id, int portionSize) {
        this.buffor = buffor;
        this.id = id;
        this.portionSize = portionSize;
    }

    @Override
    public void run() {
        while (true) {
            try {
                buffor.produce(portionSize);
                successProduction++;
            } catch (InterruptedException e) {
                successProduction--;
                System.out.println("Producer error");
            }
        }
    }

    public void summary(){
        System.out.println("Producer " + id + " produced " + successProduction + " times " + portionSize);
    }
}

