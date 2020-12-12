package com.company;

public class Main {

    public static void main(String[] args){
        Mandelbrot mandelbrotBasic = new Mandelbrot(new int[]{1, 2, 5, 10, 20, 30, 40, 50, 75, 100, 250, 500, 1000});
        mandelbrotBasic.run();
        System.out.println();

        Mandelbrot mandelbrotConstTasks = new Mandelbrot(new int[]{100, 100, 100, 100, 100, 100, 100, 100, 100,
                100, 100, 100, 100});
        mandelbrotConstTasks.run();
        System.out.println();

        Mandelbrot mandelbrotTasksTimes10 = new Mandelbrot(new int[]{10, 20, 50, 100, 200, 300, 400, 500, 750,
                1000, 2500, 5000, 10000});
        mandelbrotTasksTimes10.run();
    }
}
