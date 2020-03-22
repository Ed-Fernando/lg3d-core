/**
 * Project Looking Glass
 *
 * $RCSfile: KeyPressedEventAdapter.java,v $
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
 * $Date: 2006-06-29 23:12:19 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import java.awt.event.KeyEvent;
import org.jdesktop.lg3d.wg.event.KeyEvent3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;
import org.jdesktop.lg3d.utils.action.ActionBooleanInt;


/**
 * Event adapter class for key pressed and released events.
 */
public class KeyPressedEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        KeyEvent3D.class
    };
    
    private ActionBooleanInt action = null;
    private ModifierId modifier = null;
    private ModifierId[] modifierSet = new ModifierId[1];
    
    /**
     * Create a KeyPressedEventAdapter.
     * 
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public KeyPressedEventAdapter(ActionBooleanInt action) {
        this(null, action);
    }
    
    /**
     * Create a KeyPressedEventAdapter.
     * 
     * @param modifier     the modifier that this adapter listens to.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public KeyPressedEventAdapter(ModifierId modifier, ActionBooleanInt action) {
        EventAdapterUtil.throughIAEIfNull("action", action);
        
        this.modifier = modifier;
        this.action = action;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof KeyEvent3D);
        KeyEvent3D ke3d = (KeyEvent3D)event;
        int keyCode = ke3d.getKeyCode();
        
        if (modifier != null) {
            modifierSet = ke3d.getModifiersEx(modifierSet);
            if (!EventAdapterUtil.modifierMatches(modifierSet, modifier, false)) {
                return;
            }
            if (isKeyModifier(keyCode)) {
                return;
            }
        }
        if (ke3d.isPressed()) {
            action.performAction(event.getSource(), true, keyCode);
        } else if (ke3d.isReleased()) {
            action.performAction(event.getSource(), false, keyCode);
        }
    }
    
    private boolean isKeyModifier(int keyCode) {
        return (keyCode == KeyEvent.VK_ALT
                || keyCode == KeyEvent.VK_ALT_GRAPH
                || keyCode == KeyEvent.VK_CONTROL
                || keyCode == KeyEvent.VK_META
                || keyCode == KeyEvent.VK_SHIFT); 
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
