/**
 * Project Looking Glass
 *
 * $RCSfile: ActionDebugPrinter.java,v $
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
 * $Date: 2005-04-14 23:04:32 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.actionadapter;

import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.utils.action.*;
import java.util.logging.Logger;

/**
 * An action adapter for debugging purpose that prints out
 * the source and values passed via performAction method invocation.
 */
public class ActionDebugPrinter 
    implements ActionNoArg, ActionBoolean, 
        ActionBooleanFloat, ActionBooleanFloat2, ActionBooleanFloat3,
        ActionFloat, ActionFloat2, ActionFloat3
{
    protected static final Logger logger = Logger.getLogger("lg.utils");
    private Action action;

    public ActionDebugPrinter(Action action) {
        this.action = action;
    }
    
    public ActionDebugPrinter() {
        this(null);
    }
    
    public void performAction(LgEventSource source) {
        logger.info("performAction (" + source + ")");
        if (action != null && action instanceof ActionNoArg) {
            ((ActionNoArg)action).performAction(source);
        }
    }
    
    public void performAction(LgEventSource source, boolean state) {
        logger.info("performAction (" + source + ", " + state + ")");
        if (action != null && action instanceof ActionBoolean) {
            ((ActionBoolean)action).performAction(source, state);
        }
    }
    
    public void performAction(LgEventSource source, float value) {
        logger.info("performAction (" + source + ", " + value + ")");
        if (action != null && action instanceof ActionFloat) {
            ((ActionFloat)action).performAction(source, value);
        }
    }
    
    public void performAction(LgEventSource source, float x, float y) {
        logger.info("performAction (" + source + ", " + x + ", " + y + ")");
        if (action != null && action instanceof ActionFloat2) {
            ((ActionFloat2)action).performAction(source, x, y);
        }
    }
    
    public void performAction(LgEventSource source, float x, float y, float z) {
        logger.info("performAction (" + source + ", " + x + ", " + y + ", " + z + ")");
        if (action != null && action instanceof ActionFloat3) {
            ((ActionFloat3)action).performAction(source, x, y, z);
        }
    }
    
    public void performAction(LgEventSource source, boolean state, float x) {
        logger.info("performAction (" + source + ", " + state + ", " + x + ")");
        if (action != null && action instanceof ActionFloat) {
            ((ActionBooleanFloat)action).performAction(source, state, x);
        }
    }
    
    public void performAction(LgEventSource source, boolean state, float x, float y) {
        logger.info("performAction (" + source + ", " + state + ", " + x + ", " + y + ")");
        if (action != null && action instanceof ActionFloat2) {
            ((ActionBooleanFloat2)action).performAction(source, state, x, y);
        }
    }
    
    public void performAction(LgEventSource source, boolean state, float x, float y, float z) {
        logger.info("performAction (" + source + ", " + state + ", " + x + ", " + y + ", " + z + ")");
        if (action != null && action instanceof ActionFloat3) {
            ((ActionBooleanFloat3)action).performAction(source, state, x, y, z);
        }
    }
}
