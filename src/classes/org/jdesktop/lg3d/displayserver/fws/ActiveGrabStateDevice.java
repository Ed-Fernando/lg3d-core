/**
 * Project Looking Glass
 *
 * $RCSfile: ActiveGrabStateDevice.java,v $
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

public class ActiveGrabStateDevice {

    // Whether this device is the pointer or keyboard
    public boolean isPointer;

    // The current grab state for this device. If non-null, the device is grabbed.
    public Grab grab = null;

    // Whether the current grab is passive
    public boolean isPassive = false;

    public ActiveGrabStateDevice (boolean isPointer) {
	this.isPointer = isPointer;
    }
} 
