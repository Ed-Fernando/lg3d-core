/**
 * Project Looking Glass
 *
 * $RCSfile: Float2Scaler.java,v $
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
 * An action adapter that scales each of two float arguments
 * of ActionFloat2,
 * and propagate the result to an ActionFloat2 object.
 */
public class Float2Scaler implements ActionFloat2 {
    private ActionFloat2 action;
    private float scaleX;
    private float scaleY;
    private float rangeX;
    private float rangeY;

    public Float2Scaler(float scaleX, float scaleY, 
        float rangeX, float rangeY, ActionFloat2 action) 
    {
        this.action = action;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rangeX = rangeX;
        this.rangeY = rangeY;
    }
    
    public Float2Scaler(float scaleX, float scaleY, 
        float range, ActionFloat2 action) 
    {
        this(scaleX, scaleY, range, range, action);
    }    
    
    public Float2Scaler(float scaleX, float scaleY, ActionFloat2 action) 
    {
        this(scaleX, scaleY, Float.NaN, action);
    }
    
    public void performAction(LgEventSource source, float x, float y) {
        float x1 = x * scaleX;
        float y1 = y * scaleY;
        
        if (x1 > rangeX) {
            x1 = rangeX;
        } else if (x1 < -rangeX) {
            x1 = -rangeX;
        }
        if (y1 > rangeY) {
            y1 = rangeY;
        } else if (y1 < -rangeY) {
            y1 = -rangeY;
        }
        
        action.performAction(source, x1, y1);
    }
}
