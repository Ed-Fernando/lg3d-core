/**
 * Project Looking Glass
 *
 * $RCSfile: BooleanToNoArgConverter.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2005-04-14 23:04:33 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.actionadapter;

import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.action.ActionBoolean;

/**
 * An action adapter that converts ActionBoolean to ActionNoArg
 * by invoking an ActionNoArg object based on the value of
 * the boolean argument of ActionBoolean.
 */
public class BooleanToNoArgConverter implements ActionBoolean {
    private ActionNoArg action;
    private boolean state;
    private boolean always;
    
    public BooleanToNoArgConverter(ActionNoArg action) {
        this.action = action;
        this.always = true;
    }
    
    public BooleanToNoArgConverter(boolean state, ActionNoArg action) {
        this.action = action;
        this.always = false;
        this.state = state;
    }
    
    public void performAction(LgEventSource source, boolean state) {
        if (always || state == this.state) {
            action.performAction(source);
        }
    }
}
