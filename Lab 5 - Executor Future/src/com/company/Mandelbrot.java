package com.company;

import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.JFrame;

public class Mandelbrot extends JFrame {
    int MAX_ITER = 5000;
    double ZOOM = 500;
    BufferedImage I;
    int[] tasksCount;
    int[] threadsCount;

    public Mandelbrot(int[] tasksCount) {
        super("Mandelbrot Set");
        setBounds(100, 100, 1000, 1000);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.tasksCount = tasksCount;
        this.threadsCount = new int[]{1, 2, 5, 10, 20, 30, 40, 50, 75, 100, 250, 500, 1000};
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    public void run() {
        for (int j = 0; j < tasksCount.length; j++){
            var currentCountThread = threadsCount[j];
            var taskCount = tasksCount[j];

            long startTime = System.currentTimeMillis();

            ThreadPoolExecutor executor  = (ThreadPoolExecutor) Executors.newFixedThreadPool(currentCountThread);
            List<Runnable> tasks = new ArrayList<>();

            for (int i = 1; i <= taskCount; i++) {
                int height = getHeight() / taskCount * i;
                tasks.add(() -> draw(height, getHeight() / taskCount));
            }

            CompletableFuture<?>[] futures = tasks.stream()
                    .map(task -> CompletableFuture.runAsync(task, executor))
                    .toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
            executor .shutdown();

            long endTime = System.currentTimeMillis();
            System.out.println(currentCountThread + " threads - " + taskCount +
                    " tasks: " + (endTime - startTime) + " milliseconds");
        }
    }

    private void draw(int height, int dy) {
        for (int y = height - dy; y < height; y++) {
            for (int x = 0; x < getWidth(); x++) {
                double zx = 0;
                double zy = 0;
                double cX = (x - 400) / ZOOM;
                double cY = (y - 300) / ZOOM;
                int iter = MAX_ITER;
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                synchronized (this) {
                    I.setRGB(x, y, iter | (iter << 8));
                }
            }
        }
    }
//
//    public void paint(Graphics g) {
//        g.drawImage(I, 0, 0, this);
//    }
}
