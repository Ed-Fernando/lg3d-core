/**
 * Project Looking Glass
 *
 * $RCSfile: MouseDraggedEventAdapter.java,v $
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
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;
import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;
import org.jdesktop.lg3d.wg.event.MouseDraggedEvent3D;
import org.jdesktop.lg3d.utils.action.ActionFloat2;
import org.jdesktop.lg3d.utils.action.ActionFloat3;


/**
 * Event adapter class for mouse dragged events.
 */
public class MouseDraggedEventAdapter extends MouseMotionEventAdapter {    
    private static final Class[] targetEventClasses = {
        MouseDraggedEvent3D.class
    };
    
    private ButtonId button;
    
    /**
     * Create a MouseDraggedEventAdapter.
     *
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseDraggedEventAdapter(ActionFloat2 action) {
        this(ButtonId.BUTTON1, action);
    }
    
    /**
     * Create a MouseDraggedEventAdapter.
     *
     * @param button       the button to listen to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseDraggedEventAdapter(ButtonId button, ActionFloat2 action) {
        this(button, null, action);
    }
    
    /**
     * Create a MouseDraggedEventAdapter.
     *
     * @param button       the button to listen to.
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseDraggedEventAdapter(ButtonId button, ModifierId modifier,
            ActionFloat2 action) 
    {
        super(modifier, action);
        EventAdapterUtil.throughIAEIfNull("button", button);
       
        this.button = button;
    }
    
    /**
     * Create a MouseDraggedEventAdapter.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseDraggedEventAdapter(ActionFloat3 action) {
        this(ButtonId.BUTTON1, action);
    }
    
    /**
     * Create a MouseDraggedEventAdapter.
     *
     * @param button       the button to listen to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseDraggedEventAdapter(ButtonId button, ActionFloat3 action) {
        this(button, null, action);
    }
    
    /**
     * Create a MouseDraggedEventAdapter.
     *
     * @param button       the button to listen to.
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseDraggedEventAdapter(ButtonId button, ModifierId modifier,
            ActionFloat3 action) 
    {
        super(modifier, action);
        EventAdapterUtil.throughIAEIfNull("button", button);
        
        this.button = button;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof MouseDraggedEvent3D);
        MouseDraggedEvent3D mme3d = (MouseDraggedEvent3D)event;
        
        if (!mme3d.getButton().equals(button)) {
            return;
        }
        
        modifierSet = mme3d.getModifiersEx(modifierSet);
        if (!EventAdapterUtil.modifierMatches(modifierSet, modifier, true)) {
            return;
        }
        
        if (actionFloat2 != null) {
            actionFloat2.performAction(mme3d.getSource(),
                mme3d.getImagePlateX(), mme3d.getImagePlateY());
        } else if (actionFloat3 != null) {
            mme3d.getLocalDragPoint(tmpP3f);
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
