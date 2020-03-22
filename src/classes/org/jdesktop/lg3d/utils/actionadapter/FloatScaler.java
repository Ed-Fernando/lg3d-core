/**
 * Project Looking Glass
 *
 * $RCSfile: FloatScaler.java,v $
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
import org.jdesktop.lg3d.utils.action.ActionFloat;

/**
 * An action adapter that scales the float arguments
 * of ActionFloat,
 * and propagate the result to an ActionFloat object.
 */
public class FloatScaler implements ActionFloat {
    private ActionFloat action;
    private float scale;
    private float range;

    public FloatScaler(float scale, float range, ActionFloat action) {
        this.action = action;
        this.scale = scale;
        this.range = range;
    }
    
    public FloatScaler(float scale, ActionFloat action) {
        this(scale, Float.NaN, action);
    }

    public void performAction(LgEventSource source, float x) {
        float x1 = x * scale;

        if (x1 > range) {
            x1 = range;
        } else if (x1 < -range) {
            x1 = -range;
        }
        action.performAction(source, x1);
    }
}
