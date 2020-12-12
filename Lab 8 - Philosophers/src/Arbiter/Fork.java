package Arbiter;

import java.util.concurrent.Semaphore;

public class Fork {
    private boolean isFree;
    private final Semaphore semaphore;

    public Fork() {
        semaphore = new Semaphore(1); // now we add mutex for each fork
        isFree = true;
    }

    public void acquire() throws InterruptedException {
        int waitingCounter = 1;
        boolean ableToEat = false;

        do {
            Thread.sleep(waitingCounter);
            semaphore.acquire();
            if (!isFree)
                waitingCounter *= 2;
            else {
                isFree = false;
                ableToEat = true;
            }
            semaphore.release();
        }
        while (!ableToEat);
    }

    public void release() throws InterruptedException {
        semaphore.acquire();
        isFree = true;
        semaphore.release();
    }
}