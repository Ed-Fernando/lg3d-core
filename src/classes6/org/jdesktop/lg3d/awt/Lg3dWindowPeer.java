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
 * $Date: 2006-06-30 20:35:50 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.awt;

import java.awt.event.FocusEvent;
import sun.awt.CausedFocusEvent;
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
        logger.warning("toBack not implemented");
        //realPeer.toBack();
    }

    public void toFront() {
        logger.warning("toFront not implemented");
        //realPeer.toFront();
    }

    public void updateAlwaysOnTop() {
        logger.warning("updateAlwaysOnTop not implemented");
        //realPeer.updateAlwaysOnTop();
    }

    public boolean requestWindowFocus() {
        logger.warning("requestWindowFocus not implemented");
        //return realPeer.requestWindowFocus();
        return true;
    }

    public boolean requestFocus(Component lightweightChild, boolean temporary, boolean focusedWindowChangeAllowed, long time, sun.awt.CausedFocusEvent.Cause cause) {
        boolean ret = false;
        ret = Lg3dKeyboardFocusManager.processLg3dLightweightTransfer(awtComponent,lightweightChild,temporary,focusedWindowChangeAllowed,time);         
        logger.info("requestWindowFocus 2 returning "+ret);
        return ret;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        logger.warning("setAlwaysOnTop not implemented");
    }

    public void updateFocusableWindowState() {
        logger.warning("updateFocusableWindowState not implemented");
    }

    public void setModalBlocked(Dialog blocker, boolean blocked) {
        logger.warning("setModalBlocked not implemented");
    }

    public void updateMinimumSize() {
        logger.warning("updateMinimumSize not implemented");
    }

    public void updateIconImages() {
        logger.warning("updateIconImages not implemented");
    }

    private boolean grab = false; // Whether to do a grab during showing

    public void setGrab(boolean grab) {
        this.grab = grab;
        if (grab) {
            ;
        } else {
            ;
        }
    }

    public boolean isGrabbed() {
        return grab;
    }
}
