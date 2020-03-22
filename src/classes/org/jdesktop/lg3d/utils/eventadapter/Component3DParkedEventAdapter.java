/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DParkedEventAdapter.java,v $
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
 * $Date: 2006-03-08 02:27:39 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.Component3DParkedEvent;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.wg.Component3D;

/**
 * Event adapter for Component3DManualMoveEvent.
 */
public class Component3DParkedEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        Component3DParkedEvent.class
    };
    
    private ActionBoolean actionBoolean;
    
    /**
     * Create a Component3DManualMoveEventAdapter.
     *
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public Component3DParkedEventAdapter(ActionBoolean action) {
        EventAdapterUtil.throughIAEIfNull("action", action);
        this.actionBoolean = action;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof Component3DParkedEvent);
        assert(actionBoolean != null);
        
        LgEventSource source = event.getSource();
        Component3DParkedEvent cpe = (Component3DParkedEvent)event;
        actionBoolean.performAction(source, cpe.isParked());
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
