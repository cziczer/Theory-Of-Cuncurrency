package basicVersion;

public class Consumer extends Thread{
    Buffer buffer;
    int portionSize = 1;
    int successConsumption = 0;
    int count;

    Consumer(Buffer buffer, int count){
        this.buffer = buffer;
        this.count = count;
    }

    @Override
    public void run() {
        var startTime = System.currentTimeMillis();
        for(int i = 0; i < count; i++){
            try {
                buffer.consume(portionSize);
            } catch (InterruptedException e) {
                System.out.println("Consumer error");
            }
        }
        System.out.println("Consumer time " + (System.currentTimeMillis() - startTime));
    }
}