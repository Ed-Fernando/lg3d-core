/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DToBackEventAdapter.java,v $
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
 * $Date: 2006-03-08 02:27:39 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.Component3DToBackEvent;
import org.jdesktop.lg3d.utils.action.ActionComponent3D;


/**
 * Event adapter for Component3DToBackEvent.
 */
public class Component3DToBackEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        Component3DToBackEvent.class
    };
    
    private ActionComponent3D action;
    
    /**
     * Create a Component3DToFrontEventAdapter.
     *
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public Component3DToBackEventAdapter(ActionComponent3D action) {
        EventAdapterUtil.throughIAEIfNull("action", action);
        this.action = action;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof Component3DToBackEvent);
        assert(action != null);
        
        LgEventSource source = event.getSource();
        Component3DToBackEvent cbe = (Component3DToBackEvent)event;
        Component3D sibling = cbe.getSibling();
        action.performAction(source, sibling);
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
