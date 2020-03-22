/**
 * Project Looking Glass
 *
 * $RCSfile: MouseWheelEventAdapter.java,v $
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
 * $Date: 2006-03-08 02:27:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.MouseWheelEvent3D;
import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;
import org.jdesktop.lg3d.utils.action.ActionInt;


/**
 * Event adapter class for mouse wheel events.
 */
public class MouseWheelEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        MouseWheelEvent3D.class
    };
    
    private ActionInt actionInt;
    private ModifierId modifier;
    private ModifierId[] modifierSet = new ModifierId[1];
    
    /**
     * Create a MouseWheelEventAdapter.  Listens to the mouse wheel event,
     * and propagates them if no modifier key is pressed.
     * 
     * @param actionInt       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseWheelEventAdapter(ActionInt actionInt) {
        this(null, actionInt);
    }
    
    /**
     * Create a MouseWheelEventAdapter. Listens to the mouse wheel event,
     * and propagates them if the specified modifier key is pressed.
     * 
     * @param modifier     the modifier that this adapter listens to.
     * @param actionInt    the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseWheelEventAdapter(ModifierId modifier, ActionInt actionInt) {
        EventAdapterUtil.throughIAEIfNull("actionInt", actionInt);
        
        this.actionInt = actionInt;
        this.modifier = modifier;
    }
    
    public void processEvent(LgEvent event) {
        assert(event instanceof MouseWheelEvent3D);
        assert(actionInt != null);
        
        MouseWheelEvent3D mwe = (MouseWheelEvent3D)event;
        modifierSet = mwe.getModifiersEx(modifierSet);
        if (!EventAdapterUtil.modifierMatches(modifierSet, modifier, true)) {
            return;
        }
        
        actionInt.performAction(mwe.getSource(), mwe.getWheelRotation());
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
