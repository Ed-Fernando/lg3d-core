/**
 * Project Looking Glass
 *
 * $RCSfile: DamageEventPoller.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-04-18 20:31:48 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import gnu.x11.Display;
import gnu.x11.Window;
import gnu.x11.Input;
import gnu.x11.event.Event;
import gnu.x11.event.ConfigureNotify;

import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.displayserver.fws.*;


/**
 * DamageEventPoller is a thread which polls for damage events
 * and enqueues them with the DamageEventBroker.
 */

class DamageEventPoller 
    implements Runnable
{
    private static final Logger logger = Logger.getLogger("lg.x11");

    private Display depDpy;
    private ExtensionSet exts;

    private DamageEventBroker ebDamage;     

    private int damageNotifyEventCode;

//int numEvents = 0;

    public DamageEventPoller (Display depDpy, DamageEventBroker ebDamage, 
			      ExtensionSet exts, long[] prws)
        throws InstantiationException
    {
	this.depDpy = depDpy;
	this.ebDamage = ebDamage;
	this.exts = exts;

	damageNotifyEventCode = exts.damageExtForDam.first_event + DamageEvent.CODE_OFFSET;
	//System.err.println("damageNotifyEventCode = " + damageNotifyEventCode);

        // Express interest in ConfigureNotify events for top-level children of PRW
	for (int i=0; i<prws.length; i++) {
	    Window prwin = new Window((int)prws[i]);
	    prwin.display = depDpy;
	    prwin.select_input(Event.SUBSTRUCTURE_NOTIFY_MASK);
	}

	Thread eventThread = new Thread( AppConnectorPrivate.getThreadGroup(), this, "DamageEventPoller" );
	eventThread.start();
    }

    public int getDamageNotifyEventCode () {
	return damageNotifyEventCode;
    }

    public void run () {
	
	for (;;) {
	    Event xevent = null;

	    // Blocking wait for next event
	    try {
		xevent = depDpy.next_event();
	    } catch (Throwable t) {
		// Ignore Damage Extension errors. These can sometimes occur 
		// because a window has been destroyed prematurely. This is okay;
		if (t instanceof gnu.x11.Error) {
		    gnu.x11.Error err = (gnu.x11.Error) t;
		    if (err.code == exts.damageExtForDam.first_error) {
			// Ignore damage extension errors
			continue;
		    } 
		}
		throw new RuntimeException("CookedEventPoller.next_event: received Escher error", t);
	    }

	    // Distribute event to appropriate broker
            int eventCode = xevent.code();
             
            if (eventCode == damageNotifyEventCode) {

		Event event = new DamageEvent(depDpy, xevent.data);
    	        ebDamage.enqueue(event);
//		++numEvents;
//		System.err.print("DEP.Received damage event: " + event);
//		System.err.print("DEP.Received damage event " + numEvents + ": " + event);

            } else if (eventCode == ConfigureNotify.CODE) {

    	        ebDamage.enqueue(xevent);
		//System.err.println("DEP.Received configure event" + xevent);

	    } else {
		// Ignore events such as UnmapNotify and DestroyNotify
	    } 
	}
    }

    private static void prn (String str) { System.err.println(str); }
}

