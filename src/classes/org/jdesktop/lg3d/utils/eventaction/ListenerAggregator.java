/**
 * Project Looking Glass
 *
 * $RCSfile: ListenerAggregator.java,v $
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
 * $Date: 2006-06-29 23:13:08 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventaction;

import java.util.concurrent.CopyOnWriteArraySet;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventListener;


/**
 * Utility class for aggregating event listeners like event adapters.
 */
public class ListenerAggregator implements LgEventListener {
    private LgEventListener[] listeners = null;
    private Class[] targetEventClasses = null;
    
    public ListenerAggregator() {
    
    }
    
    public ListenerAggregator(LgEventListener listener) {
        this(new LgEventListener[] {listener});
    }
    
    public ListenerAggregator(LgEventListener listener1, 
            LgEventListener listener2) {
        
        this(new LgEventListener[] {listener1, listener2});
    }
    
    public ListenerAggregator(LgEventListener listener1, 
            LgEventListener listener2, LgEventListener listener3) {
        
        this(new LgEventListener[] {listener1, listener2, listener3});
    }
    
    public ListenerAggregator(LgEventListener[] listeners) {
        setListeners(listeners);
    }
    
    public void setListeners(LgEventListener[] listeners) {
        if (listeners == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        this.listeners = listeners;
        targetEventClasses = null;
    }
    
    public void addListener(LgEventListener listener) {
        if (listeners == null) {
            listeners = new LgEventListener[] {listener};
        } else {
            LgEventListener[] prevListeners = listeners;
            listeners = new LgEventListener[prevListeners.length + 1];
            for (int i = 0; i < prevListeners.length; i++) {
                listeners[i] = prevListeners[i];
            }
            listeners[listeners.length - 1] = listener;
        }
        targetEventClasses = null;
    }
    
    /**
     * Process an incoming event. Invoked when a registered event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent event) {
        if (listeners == null) {
            throw new IllegalStateException("listener hasn't been set");
        }
        for (LgEventListener listener : listeners) {
            if (doesEventTypeMatch(event, listener)) {
                listener.processEvent(event);
            }
        }
    }
    
    private boolean doesEventTypeMatch(LgEvent event, LgEventListener listener) {
        // REMINDER -- implementation is a bit inefficient
        Class eventCls = event.getClass();
        Class[] events = listener.getTargetEventClasses();
        for (Class cls : events) {
            if (eventCls.equals(cls)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Called by Component3D when attaching this listener to the component
     * in order to obtain the list of LgEvent classes which this listens to.
     * 
     * @return  the list of LgEvent classes which this listener listens to.
     */
    public Class[] getTargetEventClasses() {
        if (listeners == null) {
            throw new IllegalStateException("listener hasn't been set");
        }
        if (targetEventClasses == null) {
            targetEventClasses = collectTargetEventClasses();
        }
        return targetEventClasses;
    }
    
    private Class[] collectTargetEventClasses() {
        CopyOnWriteArraySet<Class> evtClsSet = new CopyOnWriteArraySet();
        for (LgEventListener listener : listeners) {
            if (listener == null) {
                throw new IllegalArgumentException("listener cannot be null");
            }
            Class[] events = listener.getTargetEventClasses();
            for (Class evtCls : events) {
                evtClsSet.add(evtCls);
            }
        }
        return evtClsSet.toArray(new Class[evtClsSet.size()]);
    }
}

