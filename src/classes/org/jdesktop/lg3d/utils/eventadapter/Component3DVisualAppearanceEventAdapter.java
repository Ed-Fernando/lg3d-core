/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DVisualAppearanceEventAdapter.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2006-03-08 02:27:39 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.Component3DVisualAppearanceEvent;
import org.jdesktop.lg3d.wg.event.Component3DVisualAppearanceEvent.VisualAppearanceType;
import org.jdesktop.lg3d.utils.action.ActionNoArg;


/**
 * Event adapter for Component3DVisualAppearanceEvent.
 */
public class Component3DVisualAppearanceEventAdapter implements EventAdapter {
    private static final Class[] targetEventClasses = {
        Component3DVisualAppearanceEvent.class
    };
    
    private ActionNoArg actionNormal;
    private ActionNoArg actionHighlight;
    private ActionNoArg actionLowlight;
    
    /**
     * Create a Component3DVisualAppearanceEvent.
     *
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public Component3DVisualAppearanceEventAdapter(
            ActionNoArg actionNormal, ActionNoArg actionHighlight, ActionNoArg actionLowlight) 
    {
        EventAdapterUtil.throughIAEIfNull("actionNormal", actionNormal);
        EventAdapterUtil.throughIAEIfNull("actionHighlight", actionHighlight);
        EventAdapterUtil.throughIAEIfNull("actionLowlight", actionLowlight);
        
        this.actionNormal = actionNormal;
        this.actionHighlight = actionHighlight;
        this.actionLowlight = actionLowlight;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(event instanceof Component3DVisualAppearanceEvent);
        
        LgEventSource source = event.getSource();
        Component3DVisualAppearanceEvent vae = (Component3DVisualAppearanceEvent)event;
        VisualAppearanceType vat = vae.getVisualAppearance();
        
        if (vat.equals(VisualAppearanceType.NORMAL)) {
            actionNormal.performAction(source);
        } else if (vat.equals(VisualAppearanceType.HIGHLIGHT)) {
            actionHighlight.performAction(source);
        } else if (vat.equals(VisualAppearanceType.LOWLIGHT)) {
            actionLowlight.performAction(source);
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
