package Arbiter;

import java.util.concurrent.Semaphore;

public class Table {
    Fork[] forks;
    int philosophers;
    private final boolean[] whoIsEating;
    private final Semaphore arbiter; // arbiter pozwala dostać się do stołu, jeszcze zostają widlece

    public Table(int philosophersNum){
        this.arbiter = new Semaphore( 1, true);

        this.philosophers = philosophersNum;
        this.whoIsEating = new boolean[philosophersNum];
        this.forks = new Fork[philosophersNum];
        for(int i = 0; i < philosophersNum; i++){
            forks[i] = new Fork();
        }
    }

    public void startEat(int id) throws InterruptedException {
        arbiter.acquire();

        whoIsEating[id] = true;

        if (checkPermission(id))
            arbiter.release();
    }

    public void endEat(int id){
        whoIsEating[id] = false;

        if (checkPermission(id))
            arbiter.release();
    }

    private boolean checkPermission(int id) {
        var currentEatingPhilosophersNumber = 0;

        for (int i = 0; i < philosophers; i++) {
            if (whoIsEating[i]) {
                currentEatingPhilosophersNumber++;
            }
        }

        return currentEatingPhilosophersNumber < 2 ||
                (currentEatingPhilosophersNumber == 2 &&
                        !whoIsEating[(id-1+philosophers)% philosophers] &&
                        !whoIsEating[(id+1+philosophers)% philosophers]);
    }
}