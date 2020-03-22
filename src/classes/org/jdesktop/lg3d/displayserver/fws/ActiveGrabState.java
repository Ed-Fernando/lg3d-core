/**
 * Project Looking Glass
 *
 * $RCSfile: ActiveGrabState.java,v $
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
 * $Date: 2005-06-24 19:47:53 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws;

import java.util.LinkedList;
import org.jdesktop.lg3d.displayserver.fws.Grab;

public class ActiveGrabState {

    // Grab state for the pointer device
    public ActiveGrabStateDevice pointer;

    // Grab state for the keyboard device
    public ActiveGrabStateDevice keyboard;

    public ActiveGrabState () {
        pointer = new ActiveGrabStateDevice(true);
        keyboard = new ActiveGrabStateDevice(false);
    }

    // Return the current pointer grab if the pointer is grabbed.
    // If the pointer is not grabbed return null

    public Grab currentGrab (boolean isPointer) {
	if (isPointer) {
	    return pointer.grab;
	} else {
	    return keyboard.grab;
	}
    }

    // Is the current pointer grab from a passive grab?
    public boolean isCurrentGrabPassive (boolean isPointer) {
	if (isPointer) {
	    return pointer.isPassive;
	} else {
	    return keyboard.isPassive;
	}
    }

    // If a grab is already enabled, return false. Otherwise, enable the grab and return true
    public boolean grabEnable (boolean isPointer, Grab grab, boolean isPassive) {

	Grab currentGrab = currentGrab(isPointer);
	if (currentGrab != null) {
    	    return false;
	}

	if (isPointer) {
	    pointer.grab = grab;
    	    pointer.isPassive = isPassive;
	} else {
	    keyboard.grab = grab;
	    keyboard.isPassive = isPassive;
	}

        return true;
    }

    public void grabDisable (boolean isPointer) {
	if (isPointer) {
	    pointer.grab = null;
    	    pointer.isPassive = false;
	} else {
	    keyboard.grab = null;
	    keyboard.isPassive = false;
	}
    }
}

