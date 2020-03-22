/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dWindowPeer.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2006-06-30 20:36:37 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.awt.event.FocusEvent;
import java.awt.Component;
import java.awt.Dialog;

/**
 *
 * @author paulby
 */
public class Lg3dWindowPeer extends Lg3dContainerPeer implements java.awt.peer.WindowPeer {
    
    /** Creates a new instance of Lg3dWindowPeer */
    public Lg3dWindowPeer(java.awt.peer.ComponentPeer realPeer, java.awt.Component frame, java.awt.Toolkit osToolkit) {
        super(realPeer, frame, osToolkit);
    }
    
    public void toBack() {
        logger.info("toBack");
        //realPeer.toBack();
    }

    public void toFront() {
        logger.info("toFront");
        //realPeer.toFront();
    }

    public void updateAlwaysOnTop() {
        logger.info("updateAlwaysOnTop");
        //realPeer.updateAlwaysOnTop();
    }

    public boolean requestWindowFocus() {
        logger.info("requestWindowFocus");
        //return realPeer.requestWindowFocus();
        return false;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        logger.info("setAlwaysOnTop");
    }

    public void updateFocusableWindowState() {
        logger.info("updateFocusableWindowState");
    }

    public void setModalBlocked(Dialog blocker, boolean blocked) {
        logger.info("setModalBlocked");
    }

    public void updateMinimumSize() {
        logger.info("updateMinimumSize");
    }

}
