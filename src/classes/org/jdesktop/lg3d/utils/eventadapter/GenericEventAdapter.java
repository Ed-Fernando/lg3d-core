/**
 * Project Looking Glass
 *
 * $RCSfile: GenericEventAdapter.java,v $
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
 * $Date: 2006-03-08 02:27:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.wg.Component3D;

/**
 * Event adapter whose souce is configurable and that propagates
 * event information to an ActionNoArg.
 */
public class GenericEventAdapter implements EventAdapter {
    private ActionNoArg action;
    private Class[] eventClass;
    
    /**
     * Create a GenericEnteredEventAdapter.
     *
     * @param eventClass   the class of the event to listen for.
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public GenericEventAdapter(Class eventClass, ActionNoArg action) {
        EventAdapterUtil.throughIAEIfNull("eventClass", eventClass);
        EventAdapterUtil.throughIAEIfNull("action", action);
        
        if (!LgEvent.class.isAssignableFrom(eventClass)) {
            throw new IllegalArgumentException("eventClass has to be subclasses of LgEvent");
        }
        
        this.eventClass = new Class[] {eventClass};
        this.action = action;
    }
    
    /**
     * Process an incoming event.  Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        assert(action != null);
        action.performAction(event.getSource());
    }
    
    /**
     * Called by Component3D when attaching this listener to the component
     * in order to obtain the list of LgEvent classes which this listens to.
     * 
     * @return  the list of LgEvent classes which this listener listens to.
     */
    public Class[] getTargetEventClasses() {
        return eventClass;
    }
}
