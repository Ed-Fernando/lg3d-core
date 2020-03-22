/**
 * Project Looking Glass
 *
 * $RCSfile: NativeEnterEvent.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-04-25 21:33:54 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import java.awt.event.MouseEvent;
import javax.media.j3d.Canvas3D;

public class NativeEnterEvent extends SequencedMouseEvent {

    public NativeEnterEvent (Canvas3D canvas, long rootWid, long wid, long when, 
			     int x, int y) {
	super(canvas, rootWid, wid, MouseEvent.MOUSE_ENTERED, when, 
	      0, x, y, 0, false, MouseEvent.NOBUTTON);
    }
}

