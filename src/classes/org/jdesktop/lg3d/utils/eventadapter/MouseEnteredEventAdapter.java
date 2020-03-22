/**
 * Project Looking Glass
 *
 * $RCSfile: MouseEnteredEventAdapter.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2006-03-08 02:27:41 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseEnteredEvent3D;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.wg.Component3D;
import javax.vecmath.Tuple3f;

/**
 * Event adapter class for mouse entered and exited events.
 */
public class MouseEnteredEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        MouseEnteredEvent3D.class
    };
    
    private ActionBoolean actionBoolean;
    
    /**
     * Create a MouseEnteredEventAdapter.
     *
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseEnteredEventAdapter(ActionBoolean action) {
        EventAdapterUtil.throughIAEIfNull("action", action);
        this.actionBoolean = action;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof MouseEnteredEvent3D);
        assert(actionBoolean != null);
        
        MouseEnteredEvent3D me3d = (MouseEnteredEvent3D)event;
        LgEventSource source = me3d.getSource();
        boolean entered = me3d.isEntered();
        actionBoolean.performAction(source, entered);
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
