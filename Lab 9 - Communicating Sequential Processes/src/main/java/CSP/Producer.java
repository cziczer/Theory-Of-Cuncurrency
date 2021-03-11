package CSP;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;

public class Producer implements CSProcess {
    ArrayList<One2OneChannelInt> out;
    ArrayList<One2OneChannelInt> req;
    int count;

    public Producer(ArrayList<One2OneChannelInt> out, ArrayList<One2OneChannelInt> req,
                    int count) {
        this.out = out;
        this.req = req;
        this.count = count;
    }

    public void run() {
        var startTime = System.currentTimeMillis();

        Guard[] guards = new Guard[req.size()];
        for (int i = 0; i < req.size(); i++) {
            guards[i] = req.get(i).in();
        }

        Alternative alternative = new Alternative(guards);

        for (int i = 0; i < count; i++) {
            int index = alternative.select();
            req.get(index).in().read();
            out.get(index).out().write(1);
        }

        System.out.println("Producer time: " + (System.currentTimeMillis() - startTime));
    }
}