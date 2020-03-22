/**
 * Project Looking Glass
 *
 * $RCSfile: ByteSwappedInputEvent.java,v $
 *
 * Copyright (c) 2004, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.2 $
 * $Date: 2005-01-20 22:05:20 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import gnu.x11.Display;
import gnu.x11.event.Input;

// A subclass of the Escher event.Input class which 
// recognizes that the raw data byte array returned
// by Display.next_event is byte swapped.

public class ByteSwappedInputEvent extends Input {

    private boolean swapped = true;

    public ByteSwappedInputEvent (Display dpy, byte[] data) {
	super(dpy, data);
    }

    public int read2 (int j) {
	if (!swapped) {
	    return super.read2(j);
	} else {
	    int b0 = data [j] & 0xff;
	    int b1 = data [j+1] & 0xff;
	    return (b1 << 8) | b0;
	}
    }

    public int read4 (int j) {
	if (!swapped) {
	    return super.read4(j);
	} else {
	    int b0 = data [j] & 0xff;
	    int b1 = data [j+1] & 0xff;
	    int b2 = data [j+2] & 0xff;
	    int b3 = data [j+3] & 0xff;
	    return b3 << 24 | b2 << 16 | b1 << 8 | b0;
	}
    }

    public void write2 (int j, int s) {
	if (!swapped) {
	    super.write2(j, s);
	} else {
	    data [j+offset] = (byte) (s & 0xff);
	    data [j+offset+1] = (byte) ((s >> 8) & 0xff);
	}
    }

    public void write4 (int j, int i) {
	if (!swapped) {
	    super.write4(j, i);
	} else {
	    data [j+offset] = (byte) (i & 0xff);
	    data [j+offset+1] = (byte) ((i >> 8) & 0xff);
	    data [j+offset+2] = (byte) ((i >> 16) & 0xff);
	    data [j+offset+3] = (byte) ((i >> 24) & 0xff);
	}
    }

    public boolean getSwapped () { return swapped; }
    public void setSwapped (boolean s) { swapped = s; }

    public int window_id () { return read4 (8); }
    public void set_window (int i) { write4 (8, i); }
}

