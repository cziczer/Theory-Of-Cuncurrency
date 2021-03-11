package CSP;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;

public class Consumer implements CSProcess {
    ArrayList<One2OneChannelInt> in;
    int count;

    public Consumer(ArrayList<One2OneChannelInt> in, int count) {
        this.in = in;
        this.count = count;
    }

    public void run() {
        var startTime = System.currentTimeMillis();
        // super-class for Alternative, channel In
        Guard[] guards = new Guard[in.size()];
        for (int i = 0; i < in.size(); i++) {
            guards[i] = in.get(i).in(); // returns input end of channel
        }

        Alternative alternative = new Alternative(guards);

        for (int i = 0; i < count; i++) {
            int index = alternative.select(); // return index of ready guard
            // Request data - blocks until data is available
            in.get(index).in().read();
        }

        System.out.println("Consumer time: " + (System.currentTimeMillis() - startTime));
    }
}
