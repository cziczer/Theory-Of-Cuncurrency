package basicVersion;

public class Producer extends Thread {
    Buffer buffer;
    int portionSize = 1;
    int count;

    Producer(Buffer buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    @Override
    public void run() {
        var startTime = System.currentTimeMillis();
        for(int i = 0; i < count; i++){
            try {
                buffer.produce(portionSize);
            } catch (InterruptedException e) {
                System.out.println("Producer error");
            }
        }
        System.out.println("Producer time " + (System.currentTimeMillis() - startTime));
    }
}