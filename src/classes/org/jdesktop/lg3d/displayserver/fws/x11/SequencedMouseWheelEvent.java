/**
 * Project Looking Glass
 *
 * $RCSfile: SequencedMouseWheelEvent.java,v $
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
 * $Date: 2006-04-25 21:33:55 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import java.awt.event.MouseWheelEvent;
import javax.media.j3d.Canvas3D;

public class SequencedMouseWheelEvent extends MouseWheelEvent {

    private static WinSysX11 wsx;

    private long rootWid;
    private long wid;
    private int pickSeq;

    public static void setWsx (WinSysX11 wsx) { SequencedMouseWheelEvent.wsx = wsx; }
    public static WinSysX11 getWsx () { return wsx; }

    public SequencedMouseWheelEvent (Canvas3D canvas, long rootWid, long wid, int id, 
				     long when, int modifiers, int x, int y, int clickCount, 
				     boolean popupTrigger, 
				     int scrollType, int scrollAmount, int wheelRotation) {

	super(canvas, id, when, modifiers, x, y, clickCount, popupTrigger, 
	      scrollType, scrollAmount, wheelRotation);
	this.rootWid = rootWid;
	this.wid = wid;
    }

    public long getRootWid () { return rootWid; }
    public long getWid () { return wid; }

    public int getPickSeq () { return pickSeq; }
    public void setPickSeq (int pickSeq) { this.pickSeq = pickSeq; }

    public String toString () {
	String s = super.toString();
	s += ", rootWid = " + rootWid;
	s += ", wid = " + wid;
	s += ", pickSeq = " + pickSeq;
	return s;
    }
}
