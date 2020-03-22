/**
 * Project Looking Glass
 *
 * $RCSfile: FloatDiffer.java,v $
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
 * $Date: 2005-04-14 23:04:33 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.actionadapter;

import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.utils.action.ActionFloat;

/**
 * An action adapter that calculates diff of the float argument
 * ActionFloat against a previous value, 
 * and propagate the result to an ActionFloat object.
 */
public class FloatDiffer implements ActionFloat {
    private ActionFloat action;
    private float prevX = Float.NaN;

    public FloatDiffer(ActionFloat action) {
        this.action = action;
    }
    
    public void performAction(LgEventSource source, float x) {
        if (Float.isNaN(prevX)) {
            prevX = x;
            return;
        }
        float diffX = x - prevX;
        prevX = x;
        
        action.performAction(source, diffX);
    }
}
