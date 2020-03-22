/**
 * Project Looking Glass
 *
 * $RCSfile: LgEventConnector.java,v $
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
 * $Date: 2006-12-13 22:17:22 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;

import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.wg.Component3D;

/**
 * A utility class that provides full access to the LG3D event facility.
 */
public final class LgEventConnector implements java.io.Serializable {
    private static final Logger logger = Logger.getLogger("lg.event");
    
    private static LgEventConnector eventConnectorSingleton;
    
    private AppConnectorPrivate connector;
    
    
    
    /** Creates new EventConnector */
    private LgEventConnector() {
        connector = AppConnectorPrivate.getAppConnector();
    }
    
    public static synchronized LgEventConnector getLgEventConnector() {
        if (eventConnectorSingleton == null) {
            eventConnectorSingleton = new LgEventConnector();
        }
        return eventConnectorSingleton;
    }
    
    /**
     * Register the specified listener to this component. Note this call will 
     * only maintain a WeakRefernece to the sourceObject so the user must ensure
     * that a strong reference is kept to the sourceObject until it is no longer
     * required.
     *
     * @param listener LgEventListener the listener to add
     */
    public void addListener(LgEventSource sourceObject, LgEventListener listener) {
        addOrRemoveListener(true, listener, sourceObject, null);
    }
    
    /**
     * Remove the specified listener from this component.
     *
     * @param listener LgEventListener the listener to remove
     */
    public void removeListener(LgEventSource sourceObject, LgEventListener listener) {
        addOrRemoveListener(false, listener, sourceObject, null);
    }
    
    /**
     * Register the specified listener for events which are generated
     * from any source of the specified class.
     * 
     * @param listener LgEventListener the listener to add
     * @param  source  the class of the source to listen to
     */
    public void addListener(Class sourceClass, LgEventListener listener) {
        addOrRemoveListener(true, listener, null, sourceClass);
    }
    
    /**
     * Remove the specified listener for events which are generated
     * from any source of the specified class.
     *
     * @param listener LgEventListener the listener to remove
     * @param  source  the class of the source to listen to
     */
    public void removeListener(Class sourceClass, LgEventListener listener) {
        addOrRemoveListener(false, listener, null, sourceClass);
    }
    
    private void addOrRemoveListener(boolean addingListener, LgEventListener listener, 
            LgEventSource sourceObject, Class sourceClass) 
    {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }
        
        if (sourceObject == null) {
            if (sourceClass == null) {
                throw new IllegalArgumentException("sourceClass cannot be null");
            }
            if (!LgEventSource.class.isAssignableFrom(sourceClass)) {
                throw new IllegalArgumentException(
                    "the class of the source have to be a subclass of LgEventSource");
            }
        }
        
        Class<LgEvent>[] eventClasses = listener.getTargetEventClasses();
        
        if (eventClasses == null) {
            throw new IllegalArgumentException(
                    "target event class list returned by the listener cannot be null");
        }
        
        for (Class eventCls: eventClasses) {
            if (eventCls == null) {
                throw new IllegalArgumentException(
                        "any of target event class returned by the listener cannot be null");
            }
            if (!LgEvent.class.isAssignableFrom(eventCls)) {
                throw new IllegalArgumentException(
                        "all the target event classes returned by the listener have to be subclasses of LgEvent");
            }
            
            if (addingListener) {
                if (sourceObject != null) {
                    if (sourceObject instanceof Component3D) {
                        // automatically set this as event source of eventCls
                        // if eventCls is of mouse or key event class.
                        if (MouseEvent3D.class.isAssignableFrom(eventCls)) {
                            ((Component3D)sourceObject).setMouseEventSource(eventCls, true);
                        } else if (KeyEvent3D.class.isAssignableFrom(eventCls)) {
                            ((Component3D)sourceObject).setKeyEventSource(true);
                        } else if (InputEvent3D.class.isAssignableFrom(eventCls)
                                || LgEvent.class.isAssignableFrom(eventCls))
                        {
                            ((Component3D)sourceObject).setMouseEventSource(MouseEvent3D.class, true);
                            ((Component3D)sourceObject).setKeyEventSource(true);
                        }
                    }
                    connector.addListener(listener, eventCls, sourceObject);
                } else {
                    assert(sourceClass != null);
                    connector.addListener(listener, eventCls, sourceClass);
                }
            } else {
                if (sourceObject != null) {
                    connector.removeListener(listener, eventCls, sourceObject);
                } else {
                    assert(sourceClass != null);
                    connector.removeListener(listener, eventCls, sourceClass);
                }
            }
        }
    }
    
    /**
     * Post an event into the system.
     * 
     * @param event the event to post
     * @param source the source of the event
     */
    public void postEvent(LgEvent event, LgEventSource source) {
        connector.postEvent(event, source);
    }
}



