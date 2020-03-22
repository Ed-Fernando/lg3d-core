/**
 * Project Looking Glass
 *
 * $RCSfile: X11IntegrationModule.java,v $
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
 * $Revision: 1.8 $
 * $Date: 2007-04-10 23:25:10 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow.x11;

import org.jdesktop.lg3d.displayserver.nativewindow.IntegrationModule;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class X11IntegrationModule implements IntegrationModule {
    private static final Logger logger = Logger.getLogger("lg.x11");
    
    public void initialize() {
        String display = System.getProperty("lg.lgserverdisplay");
	if (display == null) {
	    display = ":0";
	}
	try {
            logger.fine("Starting X11WindowManager on display " + display);
            // X11WindowManager will start it's own event thread.
            new X11WindowManager(display);
            logger.fine("X11 integration module successfully started");
	} catch (Throwable e) {
            logger.log(Level.SEVERE, "X Window Manager creation failed: ", e);
	    throw new RuntimeException("X Window Manager creation failed: " + e);
	}
    }
    
}
