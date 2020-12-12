package com.company;

public class Consumer extends Thread{
    ThreadBuffor buffor;
    int id;
    int maxPow = 13;
    int successConsumption = 0;
    int portionSize;
    Consumer(ThreadBuffor buffor, int id, int portionSize){
        this.buffor = buffor;
        this.id = id;
        this.portionSize = portionSize;
    }

    @Override
    public void run() {
        while(true){
            try {
                buffor.consume(portionSize);
                successConsumption++;
            } catch (InterruptedException e) {
                successConsumption--;
                System.out.println("Consumer error");
            }
        }
    }

    public void summary(){
        System.out.println("Producer " + id + " consumed " + successConsumption + " times " + portionSize);
    }
}