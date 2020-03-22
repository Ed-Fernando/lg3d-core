/**
 * Project Looking Glass
 *
 * $RCSfile: LgEvent.java,v $
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
 * $Date: 2007-03-08 00:17:49 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;

import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.NodeID;
import org.jdesktop.lg3d.displayserver.LgNodeManager;
import org.jdesktop.lg3d.displayserver.EventProcessor;
import org.jdesktop.lg3d.wg.Animation;
import org.jdesktop.lg3d.wg.Component3D;

/**
 * The base class for all LG events. This provides the basic information
 * required by all LG events.
 * @author  paulby
 * @version 
 */
public class LgEvent implements java.io.Serializable {
    protected static final Logger logger = Logger.getLogger("lg.event");
    
    /** the source of the event */
    private transient LgEventSource source;
    /** the unique ID of the source object */
    private NodeID sourceID;
    /** the class of the source object */
    private Class sourceClass;
    
    
    /** Creates new LgEvent */
    public LgEvent() {
        source = null;
        sourceClass = null;
        sourceID=null;
    }

    /**
     * Set the source object for this event
     * @param source the source object of this event
     */
    private void setSource(LgEventSource source) {

        this.source = source;
        if (source == null) {
            sourceClass = null;
            sourceID = null;
        } else {
            sourceClass = source.getClass();
            if (source instanceof Component3D) {
                // sourceID = source.getNodeID();
                sourceID = LgNodeManager.c3dAccessHelper.getNodeID((Component3D)source);
            } else if (source instanceof Animation) {
                logger.fine("Animation class cannot be remote-event source: " + source);
            } else {
//                logger.warning("Event source without getNodeID()? " + source);
            }
        }
    }

    /**
     * Returns the source of the event. If you only need the class of the source
     * call getSourceClass
     * @return the source object for this event
     */
    public LgEventSource getSource() {
        if (source == null && sourceID != null) {
            source = LgNodeManager.getLgNodeManager().getNode(sourceID);
            if (source != null) {
                sourceClass = source.getClass();
            }
        }
        
        return source;
    }
    
    /**
     * Get the class of the source object for this event
     * Getting the class from this method can be much
     * cheaper than calling getSource().getClass()
     * @return the class of the source object
     */
    public Class getSourceClass() {
       if (source == null && sourceID != null) {
            source = LgNodeManager.getLgNodeManager().getNode(sourceID);
            if (source != null) {
                sourceClass = source.getClass();
            }
       }
       return sourceClass;
    }
    
    
    /**
     * A hack to allow EventProcessor, which resides out side of this package,
     * to access the setSource() method.  We needed to keep the method package
     * private since it is an implementation detail and shouldn't be exposed
     * to application developers.
     */
    static {
        EventProcessor.setLgEventAccessHelper(new EventProcessorHelper());
    }
    
    /*
     * The following is a kludge to let the EventProcessor invoke setSource()
     * while hiding the method from the public API.
     */
    private static class EventProcessorHelper 
        implements EventProcessor.LgEventAccessHelper 
    {
        public void setSource(LgEvent event, LgEventSource source) {
            event.setSource(source);
        }
    }
}



