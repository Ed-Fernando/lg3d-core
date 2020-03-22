/**
 * Project Looking Glass
 *
 * $RCSfile: Float2Differ.java,v $
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
import org.jdesktop.lg3d.utils.action.ActionFloat2;

/**
 * An action adapter that calculates diff of each two float arguments
 * of ActionFloat2 against a previous value, 
 * and propagate the result to an ActionFloat2 object.
 */
public class Float2Differ implements ActionFloat2 {
    private ActionFloat2 action;
    private float prevX = Float.NaN;
    private float prevY = Float.NaN;

    public Float2Differ(ActionFloat2 action) {
        this.action = action;
    }
    
    public void performAction(LgEventSource source, float x, float y) {
        if (Float.isNaN(prevX)) {
            assert(Float.isNaN(prevY));
            prevX = x;
            prevY = y;
            return;
        }
        float diffX = x - prevX;
        float diffY = y - prevY;
        prevX = x;
        prevY = y;
        
        action.performAction(source, diffX, diffY);
    }
}
