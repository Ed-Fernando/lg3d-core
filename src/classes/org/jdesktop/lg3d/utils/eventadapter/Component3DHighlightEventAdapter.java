/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DHighlightEventAdapter.java,v $
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
 * $Date: 2006-03-08 02:27:38 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.Component3DVisualAppearanceEvent;
import org.jdesktop.lg3d.wg.event.Component3DVisualAppearanceEvent.VisualAppearanceType;
import org.jdesktop.lg3d.utils.action.ActionBoolean;


/**
 * Event adapter for Component3DVisualAppearanceEvent.
 */
public class Component3DHighlightEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        Component3DVisualAppearanceEvent.class
    };
    
    private ActionBoolean actionBoolean;
    
    /**
     * Create a Component3DHighlightEventAdapter.
     *
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public Component3DHighlightEventAdapter(ActionBoolean action) {
        EventAdapterUtil.throughIAEIfNull("action", action);
        this.actionBoolean = action;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof Component3DVisualAppearanceEvent);
        assert(actionBoolean != null);
        
        LgEventSource source = event.getSource();
        Component3DVisualAppearanceEvent vae = (Component3DVisualAppearanceEvent)event;
        boolean val = (vae.getVisualAppearance() == VisualAppearanceType.HIGHLIGHT);
        actionBoolean.performAction(source, val);
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
