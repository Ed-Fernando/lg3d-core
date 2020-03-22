/**
 * Project Looking Glass
 *
 * $RCSfile: MousePressedEventAdapter.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2006-03-08 02:27:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import javax.vecmath.Point3f;

import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;
import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.action.ActionBooleanFloat2;
import org.jdesktop.lg3d.utils.action.ActionBooleanFloat3;


/**
 * Event adapter class for mouse pressed and released events.
 */
public class MousePressedEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        MouseButtonEvent3D.class
    };
    
    private ActionBoolean actionBoolean = null;
    private ActionBooleanFloat2 actionBooleanFloat2 = null;
    private ActionBooleanFloat3 actionBooleanFloat3 = null;
    private ButtonId button; // the button for which to register an event
    private ModifierId modifier = null;
    private ModifierId[] modifierSet = new ModifierId[1];
    private Point3f tmpP3f = new Point3f();
    
    /**
     * Create a MousePressedEventAdapter.
     * Register press/release of mouse BUTTON1.
     *
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MousePressedEventAdapter(ActionBoolean action) {
        this(ButtonId.BUTTON1, action);
    }
    
    /**
     * Create a MousePressedEventAdapter.
     * Register press/release of the specified button.
     *
     * @param button       the button to listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the button or the action is null.
     */
    public MousePressedEventAdapter(ButtonId button, ActionBoolean action) {
        this(button, null, action);
    }
    /**
     * Create a MousePressedEventAdapter.
     * Register press/release of the specified button.
     *
     * @param button       the button to listens to.
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the button or the action is null.
     */
    public MousePressedEventAdapter(ButtonId button, ModifierId modifier,
            ActionBoolean action) 
    {
        EventAdapterUtil.throughIAEIfNull("button", button);
        EventAdapterUtil.throughIAEIfNull("action", action);
        
        this.button = button;
        this.modifier = modifier;
        this.actionBoolean = action;
    }
    
    /**
     * Create a MousePressedEventAdapter.
     * Register press/release of mouse BUTTON1.
     * Event sources will be added later. 
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MousePressedEventAdapter(ActionBooleanFloat2 action) {
        this(ButtonId.BUTTON1, action);
    }
    
    /**
     * Create a MousePressedEventAdapter.
     * Register press/release of the specified button.
     * 
     * @param button       the button to listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the button or the action is null.
     */
    public MousePressedEventAdapter(ButtonId button, ActionBooleanFloat2 action) {
        this(button, null, action);
    }
    
    /**
     * Create a MousePressedEventAdapter.
     * Register press/release of the specified button.
     * 
     * @param button       the button to listens to.
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the button or the action is null.
     */
    public MousePressedEventAdapter(ButtonId button, ModifierId modifier, 
            ActionBooleanFloat2 action) 
    {
        EventAdapterUtil.throughIAEIfNull("button", button);
        EventAdapterUtil.throughIAEIfNull("action", action);
        
        this.button = button;
        this.modifier = modifier;
        this.actionBooleanFloat2 = action;
    }
    
    /**
     * Create a MousePressedEventAdapter.
     * Register press/release of mouse BUTTON1.
     * Event sources will be added later. 
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MousePressedEventAdapter(ActionBooleanFloat3 action) {
        this(ButtonId.BUTTON1, action);
    }
    
    /**
     * Create a MousePressedEventAdapter.
     * Register press/release of the specified button.
     * 
     * @param button       the button to listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the button or the action is null.
     */
    public MousePressedEventAdapter(ButtonId button, ActionBooleanFloat3 action) {
        this(button, null, action);
    }
    
    /**
     * Create a MousePressedEventAdapter.
     * Register press/release of the specified button.
     * 
     * @param button       the button to listens to.
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the button or the action is null.
     */
    public MousePressedEventAdapter(ButtonId button, ModifierId modifier,
            ActionBooleanFloat3 action) 
    {
        EventAdapterUtil.throughIAEIfNull("button", button);
        EventAdapterUtil.throughIAEIfNull("action", action);
        
        this.button = button;
        this.modifier = modifier;
        this.actionBooleanFloat3 = action;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof MouseButtonEvent3D);
        MouseButtonEvent3D me3d = (MouseButtonEvent3D)event;
        
        if (me3d.getButton() != button) {
            return;
        }
        
        modifierSet = me3d.getModifiersEx(modifierSet);
        if (!EventAdapterUtil.modifierMatches(modifierSet, modifier, true)) {
            return;
        }
        
        if (me3d.isPressed()) {
            processLgEventSub(me3d, true);
        } else if (me3d.isReleased()) {
            processLgEventSub(me3d, false);
        }
    }
    
    private void processLgEventSub(MouseButtonEvent3D me3d, boolean arg) {
        LgEventSource source = me3d.getSource();
        if (actionBoolean != null) {
            actionBoolean.performAction(source, arg);
        } else if (actionBooleanFloat3 != null) {
            me3d.getLocalIntersection(tmpP3f);
            actionBooleanFloat3.performAction(source, arg, 
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
