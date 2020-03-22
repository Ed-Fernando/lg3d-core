/**
 * Project Looking Glass
 *
 * $RCSfile: AppearanceChangeAction.java,v $
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
 * $Date: 2005-04-14 23:04:26 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.action;

import org.jdesktop.lg3d.wg.*;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.sg.*;

/**
 * An ActionBoolean class that changes Appearance of 
 * specified Shape3D back and forth based on the boolean argument.
 */
public class AppearanceChangeAction implements ActionBoolean {
    private Shape3D target;
    private Appearance appOn;
    private Appearance appOff;

    public AppearanceChangeAction(Shape3D target, Appearance appOn) {
	this.target = target;
	this.appOn = appOn;
	target.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
	target.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    }

    public void performAction(LgEventSource source, boolean state) {
	if (state) {
	    // Initialize appOff lazily so that this action works fine
	    // even if the appearance is changed after initialization
	    // of this action.
	    appOff = target.getAppearance();
	    target.setAppearance(appOn);
	} else {
	    // appOff == null can happen when this action is activated
	    // while mouse is already entered the source object.
	    if (appOff != null) {
		target.setAppearance(appOff);
	    }
	}
    }
}

