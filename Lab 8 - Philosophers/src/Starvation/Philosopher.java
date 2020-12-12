package Starvation;

public class Philosopher extends Thread{
    private final Table table;
    private final int philosopherID;
    private long waitingTime = 1;
    private int meals;
    private long totalWaitingTime = 0;

    public Philosopher(Table table, int id, int meals){
        this.table = table;
        this.philosopherID = id;
        this.meals = meals;
    }

    private void acquireForks() throws InterruptedException {
        boolean ableToEat = false;
        Fork left = table.forks[philosopherID];
        Fork right = table.forks[(philosopherID + 1) % table.philosophersCount];

        do {
            Thread.sleep(waitingTime);
            table.semaphore.acquire();
            if(!left.isFree || !right.isFree)
                waitingTime *= 2; // backoff
            else {
                left.isFree = false;
                right.isFree = false;
                ableToEat = true;
                System.out.println("Philosopher" + philosopherID + " took forks");
            }
            table.semaphore.release();
        }
        while(!ableToEat);
    }

    private void releaseForks() throws InterruptedException {
        table.semaphore.acquire();
        Fork left = table.forks[philosopherID];
        Fork right = table.forks[(philosopherID + 1) % table.philosophersCount];

        left.isFree = true;
        right.isFree = true;
        System.out.println("Philosopher" + philosopherID + " released forks");

        table.semaphore.release();
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < meals; i++) {
                var startTime = System.currentTimeMillis();
                acquireForks();

                totalWaitingTime += System.currentTimeMillis() - startTime;

                releaseForks();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    public double averageWaitingTime(){
        return totalWaitingTime / (double) meals;
    }
}
