/**
 * Project Looking Glass
 *
 * $RCSfile: FloatShifter.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2006-08-14 23:47:41 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.actionadapter;

import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.utils.action.ActionFloat;

/**
 * An action adapter that adds a specified value to the float argument
 * of ActionFloat,
 * and propagate the result to an ActionFloat object.
 */
public class FloatShifter implements ActionFloat {
    private ActionFloat action;
    private float bias;
    
    public FloatShifter(float bias, ActionFloat action) {
        this.action = action;
        this.bias = bias;
    }
    
    public void performAction(LgEventSource source, float x) {
        float x1 = x + bias;
        
        action.performAction(source, x1);
    }
}
