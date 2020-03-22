/**
 * Project Looking Glass
 *
 * $RCSfile: EventInfo3D.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2006-08-14 23:13:28 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import javax.vecmath.Point3d;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import javax.media.j3d.PickInfo;

public class EventInfo3D {

    private static LinkedList<EventInfo3D> queue = new LinkedList<EventInfo3D>();

    int pickSeq;
    PickInfo[] pickInfos;

    // The time in nanoseconds when the event was sent back to the 
    // X server for further processing. Used for performance analysis
    // in order to determine the incremental cost of the Z-path
    // (i.e. the round trip to the X server and back again).
    //long nsTimeSentToXS;

    public static EventInfo3D create (int pickSeq,
				      PickInfo[] pickInfos) {
	EventInfo3D evinfo = new EventInfo3D();
	evinfo.pickSeq = pickSeq;
	evinfo.pickInfos = pickInfos;

	synchronized (queue) {
	    queue.add(evinfo);
	}

	return evinfo;
    }

    public static EventInfo3D dequeue (int pickSeq) {
	EventInfo3D evinfo = null;

	synchronized (queue) {

	    if (queue.size() == 0) {
		System.err.println("EventInfo3D.dequeue: queue is empty!");
		return null;
	    }

	    evinfo = queue.getFirst();
	    //System.err.println("EventInfo3D.dequeue: Acquire pickSeq " + pickSeq);

	    // Skip obsolete event infos. These are events that may have been grabbed
	    // by a 2D window, or they  may have been motion events compressed out in 
	    // PickEngine.add().

	    while (evinfo != null && evinfo.pickSeq < pickSeq) {
		//System.err.println("EventInfo3D.dequeue: skipping pickSeq " + evinfo.pickSeq);
		try {
		    queue.removeFirst();
		    evinfo = queue.getFirst();
		} catch (NoSuchElementException e) {
		    evinfo = null;
		} 
	    }
		
	    if (evinfo == null) {
		System.err.println("EventInfo3D.dequeue: queue is empty!");
		return null;
	    }

	    if (evinfo.pickSeq != pickSeq) {
		System.err.print("EventInfo3D.dequeue: mismatch");
		System.err.print(", Expected pickSeq = " + pickSeq);
		System.err.println(", Actual pickSeq = " + evinfo.pickSeq);
		return null;
	    }

	    // Remove the one found from the queue
            //System.err.println("EventInfo3D.dequeue: Acquired pickSeq = " + evinfo.pickSeq);
            queue.removeFirst();
	}

	return evinfo;
    }

    // Return the evinfo, but don't remove from queue
    public static EventInfo3D peek (int pickSeq) {
	EventInfo3D evinfo = null;
	int i = 0;

	try {
	    synchronized (queue) {

		if (queue.size() == 0) {
		    return null;
		}

		evinfo = queue.get(i);

		// Skip obsolete event infos
		while (evinfo != null && evinfo.pickSeq < pickSeq) {
		    i++;
		    evinfo = queue.get(i);
		}
		
		if (evinfo != null && evinfo.pickSeq != pickSeq) {
		    //System.err.print("EventInfo3D.dequeue: mismatch");
		    //System.err.print(", Expected pickSeq = " + pickSeq);
		    //System.err.println(", Actual pickSeq = " + evinfo.pickSeq);
		    return null;
		}
	    }
	} catch (NoSuchElementException e) {}

	return evinfo;
    }

    public String toString () {
	String str;

	str = "pickSeq = " + pickSeq;
	str += ", pickInfos = " + pickInfos;

	return str;
    }

}

