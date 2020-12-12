package Arbiter;

public class Philosopher extends Thread {
    private final Table table;
    private final int id;
    private final int meals;
    private long totalWaitingTime;

    public Philosopher(Table table, int id, int meals) {
        this.table = table;
        this.id = id;
        this.meals = meals;
    }

    private void acquireForks() throws InterruptedException {
        Fork left = table.forks[id];
        Fork right = table.forks[(id + 1) % table.philosophers];

        table.startEat(id);

        left.acquire();
        System.out.println("Philosopher " + id + " took right fork");
        right.acquire();
        System.out.println("Philosopher " + id + " took left fork");
    }

    private void releaseForks() throws InterruptedException {
        Fork left = table.forks[id];
        Fork right = table.forks[(id + 1) % table.philosophers];
        left.release();
        right.release();

        table.endEat(id);
    }

    @Override
    public void run() {
        try{
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

    public double averageWaitingTime() {
        return totalWaitingTime / (double) meals;
    }
}
