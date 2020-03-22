/**
 * Project Looking Glass
 *
 * $RCSfile: KeyTypedEventAdapter.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2006-03-08 02:27:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import org.jdesktop.lg3d.utils.action.ActionChar;
import org.jdesktop.lg3d.wg.event.KeyEvent3D;
import org.jdesktop.lg3d.wg.event.LgEvent;



/**
 * Event adapter class for mouse clicked events.
 */
public class KeyTypedEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        KeyEvent3D.class
    };
    
    private ActionChar action = null;
    
    /**
     * Create a KeyTypedEventAdapter.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public KeyTypedEventAdapter(ActionChar action) {
        EventAdapterUtil.throughIAEIfNull("action", action);
        this.action = action;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof KeyEvent3D);
        KeyEvent3D ke3d = (KeyEvent3D)event;
        
        if (!ke3d.isTyped()) {
            return;
        }
        action.performAction(event.getSource(), ke3d.getKeyChar());
    }
    
    /**
     * Called by Component3D when attaching this listener to the component
     * in order to obtain the list of LgEvent classes which this listens to.
     * 
     * @return  the list of LgEvent classes which this listener listens to.
     */
    public Class[] getTargetEventClasses() {
        return targetEventClasses;
    }
}
