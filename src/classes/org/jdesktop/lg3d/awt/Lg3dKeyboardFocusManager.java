/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dKeyboardFocusManager.java,v $
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
 * $Date: 2006-08-14 23:13:26 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.awt.event.FocusEvent;
import javax.swing.SwingUtilities;
import sun.awt.SunToolkit;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.KeyboardFocusManager;

/**
 *
 * @author paulby
 */
public class Lg3dKeyboardFocusManager extends DefaultKeyboardFocusManager {
    
    public boolean dispatchEvent(AWTEvent e) {

	/*
        Object root = SwingUtilities.getWindowAncestor((Component)e.getSource());
        if (root!=null && root instanceof )
        System.err.println("lg3dkfm "+e);       
	 */

        try {
            return super.dispatchEvent(e);
        } catch(Exception ex) {
            System.out.println("Exception caught in Lg3dKeyboardFocusManager, from event "+e);
            ex.printStackTrace();
        }
        return false;
    }

    /*
    public Component getGlobalFocusOwner() {
	Component ret = super.getGlobalFocusOwner();
        System.err.println("real getGlobalFocusOwner "+ret);
        return ret;
    }
    
    protected void setGlobalFocusOwner(Component focusOwner) {
        //System.err.println("***** setGlobalFocusOwner "+focusOwner);
        super.setGlobalFocusOwner(focusOwner);
    }
     */
    
    private static Lg3dMethod getCurrentKeyboardFocusManagerMethod = 
		new Lg3dMethod(KeyboardFocusManager.class, "getCurrentKeyboardFocusManager", sun.awt.AppContext.class);

    private static Lg3dMethod getGlobalFocusOwnerMethod =
		new Lg3dMethod(KeyboardFocusManager.class, "getGlobalFocusOwner");

    private static Lg3dField isPostedField =
		new Lg3dField(AWTEvent.class, "isPosted");

    // Inspired by processSynchronizedLightweightTransfer in KeyboardFocusManager
    public static boolean processLg3dLightweightTransfer(Component heavyweight, Component descendant,
                                                  boolean temporary, boolean focusedWindowChangeAllowed,
                                                  long time) {
        KeyboardFocusManager manager = (KeyboardFocusManager)
			getCurrentKeyboardFocusManagerMethod.invoke(null,
					SunToolkit.targetToAppContext(descendant));

        Component currentFocusOwner = (Component)
			getGlobalFocusOwnerMethod.invoke(manager);

        FocusEvent currentFocusOwnerEvent = null;
        FocusEvent newFocusOwnerEvent = null;

        // Original working code from Lg3dWindowPeer
//        FocusEvent  fg = new CausedFocusEvent(lightweightChild, FocusEvent.FOCUS_GAINED, false, null, cause);
//        lightweightChild.dispatchEvent(fg);

        if (descendant==currentFocusOwner)
            return true;
        
        if (currentFocusOwner != null) {
            currentFocusOwnerEvent =
                new FocusEvent(currentFocusOwner,
                               FocusEvent.FOCUS_LOST,
                               temporary, descendant);
        }
        newFocusOwnerEvent =
            new FocusEvent(descendant, FocusEvent.FOCUS_GAINED,
                           temporary, currentFocusOwner);
        
        boolean result = false;
        synchronized (descendant.getTreeLock()) {
            if (currentFocusOwnerEvent != null && currentFocusOwner != null) {
		isPostedField.set(currentFocusOwnerEvent, true);
                currentFocusOwner.dispatchEvent(currentFocusOwnerEvent);
                /// System.out.println("Dispatching old "+currentFocusOwnerEvent);
                result = true;
            }
            isPostedField.set(newFocusOwnerEvent, true);
            descendant.dispatchEvent(newFocusOwnerEvent);
            /// System.out.println("Dispatching new "+newFocusOwnerEvent);
            /// System.out.println("Descendant "+descendant);
            /// System.out.println("Current "+currentFocusOwner);                        
            result = true;        
        }
        return result;
    }
}
