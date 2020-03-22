/**
 * Project Looking Glass
 *
 * $RCSfile: MouseClickedEventAdapter.java,v $
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
 * $Revision: 1.9 $
 * $Date: 2006-09-05 18:33:00 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import javax.vecmath.Point3f;

import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;
import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.action.ActionFloat2;
import org.jdesktop.lg3d.utils.action.ActionFloat3;


/**
 * Event adapter class for mouse clicked events.
 */
public class MouseClickedEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        MouseButtonEvent3D.class
    };
    
    private ActionNoArg actionNoArg = null;
    private ActionFloat2 actionFloat2 = null;
    private ActionFloat3 actionFloat3 = null;
    private ButtonId button;
    private Boolean doubleClick; // handle single click by default
    private ModifierId modifier = null;
    private ModifierId[] modifierSet = new ModifierId[1];
    private Point3f tmpV3f = new Point3f();
    
    /**
     * Create a MouseClickedEventAdapter that
     * listens to BUTTON1 events.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseClickedEventAdapter(ActionNoArg action) {
        this(ButtonId.BUTTON1, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter that listens to events from an
     * arbitrary mouse button.
     * 
     * @param button       the button to listen to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null or
     *         if the button value is out of range.
     */
    public MouseClickedEventAdapter(ButtonId button, ActionNoArg action) {
        this(button, false, null, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter that listens to either single or
     * double clicks from BUTTON1.
     * 
     * @param dblClick     If set to true handle double click event instead of single click.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null or
     *         if the button value is out of range.
     */
    public MouseClickedEventAdapter(Boolean dblClick, ActionNoArg action) {
        this(ButtonId.BUTTON1, dblClick, null, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter that responds to clicks when a
     * given modifier key is held down.
     * 
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null or
     *         if the button value is out of range.
     */
    public MouseClickedEventAdapter(ModifierId modifier, ActionNoArg action) {
        this(ButtonId.BUTTON1, false, modifier, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter.
     * 
     * @param button       the button to listen to.
     * @param dblClick     If set to true handle double click event instead of single click.
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null or
     *         if the button value is out of range.
     */
    public MouseClickedEventAdapter(ButtonId button, Boolean dblClick, 
            ModifierId modifier, ActionNoArg action) 
    {
        EventAdapterUtil.throughIAEIfNull("button", button);
        EventAdapterUtil.throughIAEIfNull("action", action);
        
        this.button = button;
        this.doubleClick = dblClick;
        this.modifier = modifier;
        this.actionNoArg = action;
    }
    
    /**
     * Create a MouseClickedEventAdapter that
     * listens to BUTTON1 events.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseClickedEventAdapter(ActionFloat2 action) {
        this(ButtonId.BUTTON1, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter that listens to clicks on an
     * arbitrary mouse button.
     * 
     * @param button       the button to listen to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null or.
     */
    public MouseClickedEventAdapter(ButtonId button, ActionFloat2 action) {
        this(button, false, null, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter that listens to either single or
     * double clicks on the first mouse button.
     * 
     * @param dblClick     If set to true handle double click event instead of single click.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null or
     *         if the button value is out of range.
     */
    public MouseClickedEventAdapter(Boolean dblClick, ActionFloat2 action) {
        this(ButtonId.BUTTON1, dblClick, null, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter that listens to BUTTON1 mouse
     * events when a given modifier key is held down.
     * 
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null or
     *         if the button value is out of range.
     */
    public MouseClickedEventAdapter(ModifierId modifier, ActionFloat2 action) {
        this(ButtonId.BUTTON1, false, modifier, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter.
     * 
     * @param button       the button to listen to.
     * @param dblClick     If set to true handle double click event instead of single click.
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseClickedEventAdapter(ButtonId button, Boolean dblClick, 
            ModifierId modifier, ActionFloat2 action) 
    {
        EventAdapterUtil.throughIAEIfNull("button", button);
        EventAdapterUtil.throughIAEIfNull("action", action);
        
        this.button = button;
        this.doubleClick = dblClick;
        this.modifier = modifier;
        this.actionFloat2 = action;
    }
    
    /**
     * Create a MouseClickedEventAdapter that
     * listens to BUTTON1 events.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseClickedEventAdapter(ActionFloat3 action) {
        this(ButtonId.BUTTON1, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter that listens to events from an
     * arbitrary mouse button.
     * 
     * @param button       the button to listen to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null or.
     */
    public MouseClickedEventAdapter(ButtonId button, ActionFloat3 action) {
        this(button, false, null, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter that listens to either single or
     * double clicks on the first mouse button.
     * 
     * @param dblClick     If set to true handle double click event instead of single click.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null or
     *         if the button value is out of range.
     */
    public MouseClickedEventAdapter(Boolean dblClick, ActionFloat3 action) {
        this(ButtonId.BUTTON1, dblClick, null, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter that listens to events on BUTTON1
     * when a modifier key is held down.
     * 
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null or
     *         if the button value is out of range.
     */
    public MouseClickedEventAdapter(ModifierId modifier, ActionFloat3 action) {
        this(ButtonId.BUTTON1, false, modifier, action);
    }
    
    /**
     * Create a MouseClickedEventAdapter.
     * 
     * @param button       the button to listen to.
     * @param dblClick     If set to true handle double click event instead of single click.
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseClickedEventAdapter(ButtonId button, Boolean dblClick, 
            ModifierId modifier, ActionFloat3 action) 
    {
        EventAdapterUtil.throughIAEIfNull("button", button);
        EventAdapterUtil.throughIAEIfNull("action", action);
        
        this.button = button;
        this.doubleClick = dblClick;
        this.modifier = modifier;
        this.actionFloat3 = action;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof MouseButtonEvent3D);
        MouseButtonEvent3D me3d = (MouseButtonEvent3D)event;
        
        if (!me3d.isClicked() 
            || me3d.getButton() != button
            || (doubleClick && me3d.getClickCount() != 2)) 
        {
            return;
        }
        
        modifierSet = me3d.getModifiersEx(modifierSet);
        if (!EventAdapterUtil.modifierMatches(modifierSet, modifier, true)) {
            return;
        }
        
        if (actionNoArg != null) {
            actionNoArg.performAction(event.getSource());
        } else if (actionFloat2 != null) {
            assert(actionFloat3 == null);
            actionFloat2.performAction(event.getSource(),
                me3d.getImagePlateX(), me3d.getImagePlateY());
        } else if (actionFloat3 != null) {
            me3d.getLocalIntersection(tmpV3f);
            actionFloat3.performAction(me3d.getSource(), 
                tmpV3f.x, tmpV3f.y, tmpV3f.z);
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
