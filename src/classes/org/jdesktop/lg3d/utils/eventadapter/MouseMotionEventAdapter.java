/**
 * Project Looking Glass
 *
 * $RCSfile: MouseMotionEventAdapter.java,v $
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
 * $Date: 2006-03-08 02:27:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import javax.vecmath.Point3f;

import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;
import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;
import org.jdesktop.lg3d.utils.action.ActionFloat2;
import org.jdesktop.lg3d.utils.action.ActionFloat3;


/**
 * Event adapter class for mouse motion events.
 */
public class MouseMotionEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        MouseMotionEvent3D.class
    };
    
    ActionFloat2 actionFloat2 = null;
    ActionFloat3 actionFloat3 = null;
    ModifierId modifier = null;
    ModifierId[] modifierSet = new ModifierId[1];
    Point3f tmpP3f = new Point3f();
    
    /**
     * Create a MouseMotionEventAdapter.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseMotionEventAdapter(ActionFloat2 action) {
        this(null, action);
    }
    
    /**
     * Create a MouseMotionEventAdapter.
     * 
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseMotionEventAdapter(ModifierId modifier, ActionFloat2 action) {
        EventAdapterUtil.throughIAEIfNull("action", action);
        this.actionFloat2 = action;
        this.modifier = modifier;
    }
    
    /**
     * Create a MouseMotionEventAdapter.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseMotionEventAdapter(ActionFloat3 action) {
        this(null, action);
    }
    /**
     * Create a MouseMotionEventAdapter.
     * 
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseMotionEventAdapter(ModifierId modifier, ActionFloat3 action) {
        EventAdapterUtil.throughIAEIfNull("action", action);
        this.actionFloat3 = action;
        this.modifier = modifier;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof MouseMotionEvent3D);
        MouseMotionEvent3D mme3d = (MouseMotionEvent3D)event;
        
        modifierSet = mme3d.getModifiersEx(modifierSet);
        if (!EventAdapterUtil.modifierMatches(modifierSet, modifier, true)) {
            return;
        }
        
        if (actionFloat2 != null) {
            actionFloat2.performAction(mme3d.getSource(),
                mme3d.getImagePlateX(), mme3d.getImagePlateY());
        } else if (actionFloat3 != null) {
            mme3d.getLocalIntersection(tmpP3f);
            actionFloat3.performAction(mme3d.getSource(), 
                tmpP3f.x, tmpP3f.y, tmpP3f.z);
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
