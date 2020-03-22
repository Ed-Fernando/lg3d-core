/**
 * Project Looking Glass
 *
 * $RCSfile: MouseMovedEventAdapter.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-03-08 02:27:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.MouseMovedEvent3D;
import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;
import org.jdesktop.lg3d.utils.action.ActionFloat2;
import org.jdesktop.lg3d.utils.action.ActionFloat3;


/**
 * Event adapter class for mouse moved events.
 */
public class MouseMovedEventAdapter extends MouseMotionEventAdapter {
    private static final Class[] targetEventClasses = {
        MouseMovedEvent3D.class
    };
    
    /**
     * Create a MouseMovedEventAdapter.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseMovedEventAdapter(ActionFloat2 action) {
        this(null, action);
    }
    
    /**
     * Create a MouseMovedEventAdapter.
     * 
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseMovedEventAdapter(ModifierId modifier, ActionFloat2 action) {
        super(modifier, action);
    }
    
    /**
     * Create a MouseMovedEventAdapter.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseMovedEventAdapter(ActionFloat3 action) {
        this(null, action);
    }
    
    /**
     * Create a MouseMovedEventAdapter.
     * 
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseMovedEventAdapter(ModifierId modifier, ActionFloat3 action) {
        super(modifier, action);
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
