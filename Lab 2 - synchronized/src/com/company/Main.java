package com.company;

public class Main {

    public static void main(String[] args) throws InterruptedException,
            IllegalMonitorStateException {

        var number = new Number();


            Thread writeOne = new Thread(() ->{
                synchronized (number){
                for(int i = 0; i < 10; i++){
                    while(number.n != 1) {
                        try {
                            number.wait();
                        } catch (InterruptedException e) {
                           System.out.println("123");
                        }
                    }

                    number.writeNumber();
                    number.n = 2;
                    number.notifyAll();
                }
            }});

            Thread writeTwo = new Thread(() ->{
                synchronized (number){
                for(int i = 0; i < 10; i++){
                    while(number.n != 2) {
                        try {
                            number.wait();
                        } catch (InterruptedException e) {
                            System.out.println("123");
                        }
                    }

                    number.writeNumber();
                    number.n = 3;
                    number.notifyAll();
                }
            }});

            Thread writeThree = new Thread(() ->{
                synchronized (number){
                for(int i = 0; i < 10; i++){
                    while(number.n != 3) {
                        try {
                            number.wait();
                        } catch (InterruptedException e) {
                            System.out.println("123");
                        }
                    }

                    number.writeNumber();
                    number.n = 1;
                    number.notifyAll();
                }
            }});

        writeOne.start();
        writeTwo.start();
        writeThree.start();
        }
    }
