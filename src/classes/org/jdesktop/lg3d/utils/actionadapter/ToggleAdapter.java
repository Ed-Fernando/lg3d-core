/**
 * Project Looking Glass
 *
 * $RCSfile: ToggleAdapter.java,v $
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
 * $Date: 2005-04-14 23:04:34 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.actionadapter;

import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.action.ActionBoolean;

/**
 * An action adapter that converts ActionNoArg input to
 * ActionBoolean by keeping an internal boolean value
 * and toggling it when ActionNoArg action happened.
 */
public class ToggleAdapter implements ActionNoArg {
    private ActionBoolean action;
    private boolean state;
    
    public ToggleAdapter(ActionBoolean action) {
        this(false, action);
    }
    
    public ToggleAdapter(boolean initialState, ActionBoolean action) {
        this.action = action;
        this.state = initialState;
    }
    
    public void resetState(boolean state) {
        this.state = state;
    }
    
    public void performAction(LgEventSource source) {
        state = !state;
        action.performAction(source, state);
    }
}
