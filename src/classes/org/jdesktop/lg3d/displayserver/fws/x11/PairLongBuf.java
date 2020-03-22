
package org.jdesktop.lg3d.displayserver.fws.x11;

public class PairLongBuf {

    public static final int PAIRLONGBUF_DEFAULT_SIZE = 1000;

    private int size;
    private int next;
    private long[] buffer;

    public PairLongBuf () {
	this(PAIRLONGBUF_DEFAULT_SIZE);
    }

    public PairLongBuf (int size) {
        this.size = size * 2;
        this.next = 0;
        this.buffer = new long[size*2];
    }

    public void add (long a, long b) {
	if (next >= size) {
	    dump();
	    next = 0;
	}

	buffer[next] = a;
        buffer[next+1] = b;
	next += 2;
    }

    public void dumpAndClear () {
	dump();
	next = 0;
    }

    public void dump () {
	System.err.println("Number of measurements = " + (size / 2));

	for (int i = 0; i < next; i += 2) {
	    long a = buffer[i];
	    long b = buffer[i+1];
	    System.err.println(a + ", " + b);
	}
    }
}







