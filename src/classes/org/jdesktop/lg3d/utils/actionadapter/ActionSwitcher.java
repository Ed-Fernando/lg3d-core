/**
 * Project Looking Glass
 *
 * $RCSfile: ActionSwitcher.java,v $
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
 * $Date: 2006-03-09 04:04:10 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.actionadapter;

import org.jdesktop.lg3d.utils.action.Action;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.action.ActionBooleanFloat;
import org.jdesktop.lg3d.utils.action.ActionBooleanFloat2;
import org.jdesktop.lg3d.utils.action.ActionBooleanFloat3;
import org.jdesktop.lg3d.utils.action.ActionFloat;
import org.jdesktop.lg3d.utils.action.ActionFloat2;
import org.jdesktop.lg3d.utils.action.ActionFloat3;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 * An action adapter that switches propagation from a single action 
 * to one of multiple actions.
 */
public class ActionSwitcher implements ActionNoArg, ActionBoolean, 
    ActionBooleanFloat, ActionBooleanFloat2, ActionBooleanFloat3, 
    ActionFloat, ActionFloat2, ActionFloat3
{
    private Action[] actions;
    private int currentAction = 0;
    private boolean enabled = true;
    
    public ActionSwitcher(Action[] actions) {
        if (actions == null) {
            throw new IllegalArgumentException("argement cannot be null");
        }
        for (Action action : actions) {
            if (action == null) {
                throw new IllegalArgumentException("action cannot be null");
            }
        }
        this.actions = actions;
    }
    
    public ActionSwitcher(Action action) {
        this(new Action[] {action});
    }
    
    public ActionSwitcher(Action a1, Action a2) {
        this(new Action[] {a1, a2});
    }
    
    public ActionSwitcher(Action a1, Action a2, Action a3) {
        this(new Action[] {a1, a2, a3});
    }
    
    public ActionSwitcher(Action a1, Action a2, Action a3, Action a4) {
        this(new Action[] {a1, a2, a3, a4});
    }
    
    public void setWhichAction(int action) {
        if (action < 0 || action >= actions.length) {
            throw new IllegalArgumentException("argument out of range");
        }
        currentAction = action;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void performAction(LgEventSource source) {
        if (enabled) {
            Action action = actions[currentAction];
            if (action instanceof ActionNoArg) {
                ((ActionNoArg)action).performAction(source);
            }
        }
    }
    
    public void performAction(LgEventSource source, boolean state) {
        if (enabled) {
            Action action = actions[currentAction];
            if (action instanceof ActionBoolean) {
                ((ActionBoolean)action).performAction(source, state);
            }
        }
    }
    
    public void performAction(LgEventSource source, float value) {
        if (enabled) {
            Action action = actions[currentAction];
            if (action instanceof ActionFloat) {
                ((ActionFloat)action).performAction(source, value);
            }
        }
    }
    
    public void performAction(LgEventSource source, float x, float y) {
        if (enabled) {
            Action action = actions[currentAction];
            if (action instanceof ActionFloat2) {
                ((ActionFloat2)action).performAction(source, x, y);
            }
        }
    }
    
    public void performAction(LgEventSource source, float x, float y, float z) {
        if (enabled) {
            Action action = actions[currentAction];
            if (action instanceof ActionFloat3) {
                ((ActionFloat3)action).performAction(source, x, y, z);
            }
        }
    }
    
    public void performAction(LgEventSource source, boolean state, float x) {
        if (enabled) {
            Action action = actions[currentAction];
            if (action instanceof ActionFloat) {
                ((ActionBooleanFloat)action).performAction(source, state, x);
            }
        }
    }
    
    public void performAction(LgEventSource source, boolean state, float x, float y) {
        if (enabled) {
            Action action = actions[currentAction];
            if (action instanceof ActionFloat2) {
                ((ActionBooleanFloat2)action).performAction(source, state, x, y);
            }
        }
    }
    
    public void performAction(LgEventSource source, boolean state, float x, float y, float z) {
        if (enabled) {
            Action action = actions[currentAction];
            if (action instanceof ActionFloat3) {
                ((ActionBooleanFloat3)action).performAction(source, state, x, y, z);
            }
        }
    }
}
