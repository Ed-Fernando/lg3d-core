/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dFocusManagerPeer.java,v $
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
 * $Date: 2006-06-30 20:37:53 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.awt;

import java.awt.Component;
import java.awt.Window;
import java.awt.peer.KeyboardFocusManagerPeer;
import javax.swing.SwingUtilities;

/**
 *
 * @author paulby
 */
public class Lg3dFocusManagerPeer implements KeyboardFocusManagerPeer {
    
    private KeyboardFocusManagerPeer osPeer;
    private Window currentFocusedWindow = null;
    
    // Should we always return the X peer or should we return the
    // lg3d peer when it has focus.
    // A quick test showed returning lg3d peer did not work as well as
    // returning the X peer, but I'm not convinced it's correct.
    private Window lg3dCurrentFocusedWindow = null;
    private Component lg3dCurrentFocusedOwner = null;
    
    /** Creates a new instance of Lg3dFocusManagerPeer */
    public Lg3dFocusManagerPeer(KeyboardFocusManagerPeer osPeer) {
        this.osPeer = osPeer;
    }
    
    @SuppressWarnings("deprecation")
    public void setCurrentFocusedWindow(Window win) {
        //System.out.println("peer setCurrentFocusedWindow "+win);
        if (win!=null) {
            if (win.getPeer() instanceof Lg3dWindowPeer) {
                //System.out.println("LG3D Peer");
                lg3dCurrentFocusedWindow = win;
                // If this is an lg3d peer then don't notify the osPeer of the change
                return;
            } 
        } 
        lg3dCurrentFocusedWindow = null;
        //System.out.println();
        osPeer.setCurrentFocusedWindow(win);
    }

    public Window getCurrentFocusedWindow() {
//        if (lg3dCurrentFocusedWindow!=null)
//            return lg3dCurrentFocusedWindow;
        
        Window ret = osPeer.getCurrentFocusedWindow();
        //System.out.println("************* peer getCurrentFocusedWindow "+ret);
        return ret;
    }
    
    @SuppressWarnings("deprecation") // getPeer has been deprecated
    public void setCurrentFocusOwner(Component comp) {
        //System.out.println("************* peer setCurrentFocusedOwner "+comp);
        if (comp!=null) {
            if (SwingUtilities.getRoot(comp).getPeer() instanceof Lg3dWindowPeer) {
                lg3dCurrentFocusedOwner = comp;
                // If this is an lg3d peer then don't notify the osPeer of the change
                return;
            }            
        }
        lg3dCurrentFocusedOwner = null;
        osPeer.setCurrentFocusOwner(comp);
    }

    public Component getCurrentFocusOwner() {
        if (lg3dCurrentFocusedOwner!=null)
            return lg3dCurrentFocusedOwner;
        
        Component ret = osPeer.getCurrentFocusOwner();
        //System.out.println("************* peer getCurrentFocusedOwner "+ret);
        return ret;
    }

    public void clearGlobalFocusOwner(Window activeWindow) {
        //System.out.println("************* peer clearGlobalFocusedOwner "+activeWindow+"*******************************");
        osPeer.clearGlobalFocusOwner(activeWindow);
    }
    
}
