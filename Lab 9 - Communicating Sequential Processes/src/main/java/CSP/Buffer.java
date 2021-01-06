package CSP;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Buffer implements CSProcess {
    private One2OneChannelInt in;
    private One2OneChannelInt out;
    private One2OneChannelInt req;
    //Out for buffor = channel In, because output (write action) we get in (read) on the opposit side (for consumer)
    public Buffer(One2OneChannelInt in, One2OneChannelInt out, One2OneChannelInt req) {
        this.in = in;
        this.out = out;
        this.req = req;
    }

    public void run() {
        while (true) {
            req.out().write(0);
            out.out().write(in.in().read());
        }
    }
}
