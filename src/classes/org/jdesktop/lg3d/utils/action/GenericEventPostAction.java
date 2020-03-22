/**
 * Project Looking Glass
 *
 * $RCSfile: GenericEventPostAction.java,v $
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
 * $Date: 2005-04-14 23:04:27 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.action;

import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.*;
import java.lang.reflect.*;

/**
 * An ActionNoArg class that instantiates and posts an event of
 * specified type.
 */
public class GenericEventPostAction implements ActionNoArg {
    private LgEventSource source;
    private Class eventClass;

    public GenericEventPostAction(Class eventClass) {
        if (!LgEvent.class.isAssignableFrom(eventClass)) {
            throw new IllegalArgumentException(
                "eventClass have to be a subclass of LgEvent class");
        }
        try {
            Constructor ctor = eventClass.getConstructor(new Class[] {});
        } catch (NoSuchMethodException nsme) {
            throw new IllegalArgumentException(
                "eventClass have to have a constructor with no argument");
        }
        this.eventClass = eventClass;
    }
    
    public GenericEventPostAction(Class eventClass, LgEventSource source) {
        this(eventClass);
        if (source == null) {
            throw new IllegalArgumentException("event source cannot be null");
        }
        this.source = source;
    }
    
    public void performAction(LgEventSource eventSource) {
        try {
            if (source != null) {
                eventSource = source;
            }
            eventSource.postEvent((LgEvent)eventClass.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to instantiate an object of " + eventClass);
        }
    }
}
