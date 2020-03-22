/**
 * Project Looking Glass
 *
 * $RCSfile: EventDebugPrinter.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2006-03-17 22:17:26 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventaction;

import java.util.logging.Logger;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.MouseEvent3D;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;

/**
 * Event listener that prints specified or all the events for debugging.
 */
public class EventDebugPrinter implements LgEventListener {
    protected static final Logger logger = Logger.getLogger("lg.utils");
    private Class[] eventClass;
    private boolean reportMouseMotionEvent;
    
    /**
     * Create a EventDebugPrinter.
     */
    public EventDebugPrinter() {
        this(LgEvent.class, true);
    }
    
    /**
     * Create a EventDebugPrinter.
     */
    public EventDebugPrinter(Class eventClass) {
        this(eventClass, true);
    }
    
    /**
     * Create a EventDebugPrinter.
     */
    public EventDebugPrinter(boolean reportMouseMotionEvent) {
        this(LgEvent.class, reportMouseMotionEvent);
    }
    
    /**
     * Create a EventDebugPrinter.
     */
    public EventDebugPrinter(Class eventClass, boolean reportMouseMotionEvent) {
        if (eventClass == null) {
            throw new IllegalArgumentException("eventClass cannot be null");
        }
        if (!LgEvent.class.isAssignableFrom(eventClass)) {
            throw new IllegalArgumentException("eventClass has to be subclasses of LgEvent");
        }
        
        this.eventClass = new Class[] {eventClass};
        this.reportMouseMotionEvent = reportMouseMotionEvent;
    }
    
    /**
     * Print an incoming event.  Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        if ((!reportMouseMotionEvent) && (event instanceof MouseMotionEvent3D)) {
            return;
        }
        logger.info("LgEvent caught: " + event + " Source: " + event.getSource());
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
