package basicVersion;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int count = 10000000;// 10 000 000
        int N = 5;
        Buffer buffer = new Buffer(N);
        Producer producer = new Producer(buffer, count);
        Consumer consumer = new Consumer(buffer, count);

        producer.start();
        consumer.start();
    }
}
