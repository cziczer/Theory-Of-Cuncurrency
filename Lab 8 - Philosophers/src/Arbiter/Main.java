package Arbiter;

public class Main {
    public static void main(String args[]) throws InterruptedException {
        var philosophersNumber = 5;
        var meals = 100; // Every thread eat same amount of food

        Table table = new Table(philosophersNumber);
        Philosopher[] philosophers = new Philosopher[philosophersNumber];

        for (int i = 0 ; i < philosophersNumber ; i++){
            philosophers[i] = new Philosopher(table, i, meals);
        }

        for(Philosopher p : philosophers)
            p.start();

        for(Philosopher p : philosophers)
            p.join();

        System.out.println("\nArbiter timers:");

        for (int i = 0; i < philosophersNumber; i++)
            System.out.println ("Philosopher " + i + " wait:  " + philosophers[i].averageWaitingTime() + "ms");
    }
}
