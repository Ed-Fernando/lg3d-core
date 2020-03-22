/**
 * Project Looking Glass
 *
 * $RCSfile: MouseDragDistanceAdapter.java,v $
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
import org.jdesktop.lg3d.wg.event.MouseDraggedEvent3D;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;
import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;
import org.jdesktop.lg3d.utils.action.ActionFloat2;
import org.jdesktop.lg3d.utils.action.ActionFloat3;
import javax.vecmath.Point3f;
import org.jdesktop.lg3d.wg.event.MouseEvent3D;

/**
 * Event adapter class deal with distance of mouse drag motion.
 */
public class MouseDragDistanceAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        MouseButtonEvent3D.class,
        MouseDraggedEvent3D.class
    };
    
    private ActionFloat2 actionFloat2 = null;
    private ActionFloat3 actionFloat3 = null;
    private Point3f initLoc = new Point3f();
    private float initIPLocX;
    private float initIPLocY;
    private ButtonId button;
    private ModifierId modifier = null;
    private ModifierId[] modifierSet = new ModifierId[1];
    private Point3f tmpP3f = new Point3f();
    
    /**
     * Create a MouseDragDistanceAdapter.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseDragDistanceAdapter(ActionFloat2 action) {
        this(ButtonId.BUTTON1, null, action);
    }
    
    /**
     * Create a MouseDragDistanceAdapter.
     * 
     * @param button       the button to listen to.
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseDragDistanceAdapter(ButtonId button, ModifierId modifier,
            ActionFloat2 action) 
    {
        EventAdapterUtil.throughIAEIfNull("button", button);
        EventAdapterUtil.throughIAEIfNull("action", action);
        
        this.button = button;
        actionFloat2 = action;
    }
    
    /**
     * Create a MouseDragDistanceAdapter.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseDragDistanceAdapter(ActionFloat3 action) {
        this(ButtonId.BUTTON1, null, action);
    }
    
    /**
     * Create a MouseDragDistanceAdapter.
     * 
     * @param button       the button to listen to.
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseDragDistanceAdapter(ButtonId button, ModifierId modifier,
            ActionFloat3 action) 
    {
        EventAdapterUtil.throughIAEIfNull("button", button);
        EventAdapterUtil.throughIAEIfNull("action", action);
        
        this.button = button;
        this.modifier = modifier;
        actionFloat3 = action;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof MouseEvent3D);
        MouseEvent3D me3d = (MouseEvent3D)event;
        
        if (!me3d.getButton().equals(button)) {
            return;
        }
        
        modifierSet = me3d.getModifiersEx(modifierSet);
        if (!EventAdapterUtil.modifierMatches(modifierSet, modifier, true)) {
            return;
        }
        
        if (event instanceof MouseButtonEvent3D) {
            MouseButtonEvent3D mbe3d = (MouseButtonEvent3D)event;
            if (mbe3d.isPressed()) {
                mbe3d.getIntersection(initLoc);
                initIPLocX = mbe3d.getImagePlateX();
                initIPLocY = mbe3d.getImagePlateY();
            }
            return;
        }
        
        assert(event instanceof MouseDraggedEvent3D);
        MouseDraggedEvent3D mme3d = (MouseDraggedEvent3D)event;
        
        if (actionFloat2 != null) {
            actionFloat2.performAction(mme3d.getSource(),
                mme3d.getImagePlateX() - initIPLocX, 
                mme3d.getImagePlateY() - initIPLocY);
        } else if (actionFloat3 != null) {
            mme3d.getDragPoint(tmpP3f);
            actionFloat3.performAction(mme3d.getSource(), 
                tmpP3f.x - initLoc.x, 
                tmpP3f.y - initLoc.y, 
                tmpP3f.z - initLoc.z);
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
