/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DManualMoveEventAdapter.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-03-21 02:14:06 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import javax.vecmath.Point3f;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.Component3DManualMoveEvent;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.action.ActionBooleanFloat3;

/**
 * Event adapter for Component3DManualMoveEvent.
 */
public class Component3DManualMoveEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        Component3DManualMoveEvent.class
    };
    
    private ActionBoolean actionBoolean;
    private ActionBooleanFloat3 actionBooleanFloat3;
    private Point3f tmpP3f = new Point3f();
    
    /**
     * Create a Component3DManualMoveEventAdapter.
     *
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public Component3DManualMoveEventAdapter(ActionBoolean action) {
        EventAdapterUtil.throughIAEIfNull("action", action);
        this.actionBoolean = action;
    }
    
    /**
     * Create a Component3DManualMoveEventAdapter.
     *
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public Component3DManualMoveEventAdapter(ActionBooleanFloat3 action) {
        EventAdapterUtil.throughIAEIfNull("action", action);
        this.actionBooleanFloat3 = action;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof Component3DManualMoveEvent);
        assert(actionBoolean != null || actionBooleanFloat3 != null);
        
        LgEventSource source = event.getSource();
        Component3DManualMoveEvent cmme = (Component3DManualMoveEvent)event;
        if (actionBoolean != null) {
            actionBoolean.performAction(source, cmme.isStarted());
        } else {
            assert(actionBooleanFloat3 != null);
            cmme.getIntersection(tmpP3f);
            actionBooleanFloat3.performAction(source, 
                    cmme.isStarted(), tmpP3f.x, tmpP3f.y, tmpP3f.z);
        }
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
