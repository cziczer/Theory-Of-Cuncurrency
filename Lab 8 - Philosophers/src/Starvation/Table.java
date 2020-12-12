package Starvation;

import java.util.concurrent.Semaphore;

public class Table {
    final int philosophersCount;
    final Fork[] forks;
    final Semaphore semaphore = new Semaphore(1); // works like mutex for table

    public Table(int philosophersNumber){
        this.philosophersCount = philosophersNumber;
        forks = new Fork[philosophersNumber];

        for(int i = 0; i < philosophersCount; i++){
            forks[i] = new Fork();
        }
    }
}
