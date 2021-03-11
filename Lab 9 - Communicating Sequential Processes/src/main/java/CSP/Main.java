package CSP;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int count = 1000;// 10 000 000
        int N = 50;

        ArrayList<One2OneChannelInt> consumer = new ArrayList<>();
        ArrayList<One2OneChannelInt> producer = new ArrayList<>();
        ArrayList<One2OneChannelInt> req = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            consumer.add(Channel.one2oneInt());
            producer.add(Channel.one2oneInt());
            req.add(Channel.one2oneInt());
        }

        CSProcess[] procList = new CSProcess[N + 2];

        procList[0] = new Producer(producer, req, count);
        procList[1] = new Consumer(consumer, count);

        for (int i = 0; i < N; i++) {
            procList[2 + i] = new Buffer(producer.get(i), consumer.get(i), req.get(i));
        }
        //przyjmuje CSProcess
        Parallel par= new Parallel(procList);
        par.run();
    }
}