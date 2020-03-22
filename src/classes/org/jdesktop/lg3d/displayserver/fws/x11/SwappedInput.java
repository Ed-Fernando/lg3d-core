/**
 * Project Looking Glass
 *
 * $RCSfile: SwappedInput.java,v $
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
 * $Date: 2005-01-20 22:05:26 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import gnu.x11.Display;
import gnu.x11.event.Input;

public class SwappedInput extends Input {

    public SwappedInput (Display dpy, byte[] data) {
	super(dpy, data);
	window_offset = 16;
    }

/*
  public int detail () { return read1 (1); }
  public int root_id () { return read4 (8); }
  public int child_id () { return read4 (16); }
  public int root_x () { return read2 (20); }
  public int root_y () { return read2 (22); }
  public int event_x () { return read2 (24); }
  public int event_y () { return read2 (26); }
  public int state () { return read2 (28); }
  public boolean same_screen () { return read_boolean (30); }
  */

  public int read2 (int j) {
    int b0 = data [j] & 0xff;
    int b1 = data [j+1] & 0xff;
    return (b1 << 8) | b0;
  }

  public int read4 (int j) {
    int b0 = data [j] & 0xff;
    int b1 = data [j+1] & 0xff;
    int b2 = data [j+2] & 0xff;
    int b3 = data [j+3] & 0xff;
    return b3 << 24 | b2 << 16 | b1 << 8 | b0;
  }

}
