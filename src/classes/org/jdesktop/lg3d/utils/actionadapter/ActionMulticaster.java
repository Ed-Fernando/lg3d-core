/**
 * Project Looking Glass
 *
 * $RCSfile: ActionMulticaster.java,v $
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
 * An action adapter that replicates a single action to multiple
 * target actions that implement the same action interface as
 * the original action.
 */
public class ActionMulticaster implements ActionNoArg, ActionBoolean, 
    ActionBooleanFloat, ActionBooleanFloat2, ActionBooleanFloat3, 
    ActionFloat, ActionFloat2, ActionFloat3
{
    private Action[] actions;
    
    public ActionMulticaster(Action[] actions) {
        if (actions == null) {
            throw new IllegalArgumentException("argement cannot be null");
        }
        for (Action a : actions) {
            if (a == null) {
                throw new IllegalArgumentException("action cannot be null");
            }
        }
        this.actions = actions;
    }
    
    public ActionMulticaster(Action action) {
        this(new Action[] {action});
    }
    
    public ActionMulticaster(Action a1, Action a2) {
        this(new Action[] {a1, a2});
    }
    
    public ActionMulticaster(Action a1, Action a2, Action a3) {
        this(new Action[] {a1, a2, a3});
    }
    
    public ActionMulticaster(Action a1, Action a2, Action a3, Action a4) {
        this(new Action[] {a1, a2, a3, a4});
    }

    public void performAction(LgEventSource source) {
        for (Action action : actions) {
            if (action instanceof ActionNoArg) {
                ((ActionNoArg)action).performAction(source);
            }
        }
    }
    
    public void performAction(LgEventSource source, boolean state) {
        for (Action action : actions) {
            if (action instanceof ActionBoolean) {
                ((ActionBoolean)action).performAction(source, state);
            }
        }
    }
    
    public void performAction(LgEventSource source, float value) {
        for (Action action : actions) {
            if (action instanceof ActionFloat) {
                ((ActionFloat)action).performAction(source, value);
            }
        }
    }
    
    public void performAction(LgEventSource source, float x, float y) {
        for (Action action : actions) {
            if (action instanceof ActionFloat2) {
                ((ActionFloat2)action).performAction(source, x, y);
            }
        }
    }
    
    public void performAction(LgEventSource source, float x, float y, float z) {
        for (Action action : actions) {
            if (action instanceof ActionFloat3) {
                ((ActionFloat3)action).performAction(source, x, y, z);
            }
        }
    }
    
    public void performAction(LgEventSource source, boolean state, float x) {
        for (Action action : actions) {
            if (action instanceof ActionFloat) {
                ((ActionBooleanFloat)action).performAction(source, state, x);
            }
        }
    }
    
    public void performAction(LgEventSource source, boolean state, float x, float y) {
        for (Action action : actions) {
            if (action instanceof ActionFloat2) {
                ((ActionBooleanFloat2)action).performAction(source, state, x, y);
            }
        }
    }
    
    public void performAction(LgEventSource source, boolean state, float x, float y, float z) {
        for (Action action : actions) {
            if (action instanceof ActionFloat3) {
                ((ActionBooleanFloat3)action).performAction(source, state, x, y, z);
            }
        }
    }
}
