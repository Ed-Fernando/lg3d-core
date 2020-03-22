/**
 * Project Looking Glass
 *
 * $RCSfile: TransparencyAnimationBoolean.java,v $
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
 * $Date: 2005-04-14 23:04:38 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.animation;

import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.smoother.FloatTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.NaturalFloatSmoother;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 *
 * Changes the transparency from a base Transparency to an 'on' Transparency
 * depending on the boolean.
 *
 * This is used by StandardAppContainer to 'highlight' Frame3D's when the mouse
 * moves over them.
 *
 */
public class TransparencyAnimationBoolean extends TimedAnimation implements ActionBoolean {
    
    private FloatTransitionSmoother smoother;
    private float baseTrans;
    private float onTrans;
    
    /**
     * Creates a new instance of RotationAnimationBoolean 
     */
    public TransparencyAnimationBoolean(float baseTrans, float onTrans, int duration) {
        this(baseTrans, onTrans, duration, new NaturalFloatSmoother());
    }
    
    /**
     * Creates a new instance of RotationAnimationBoolean 
     */
    public TransparencyAnimationBoolean(float baseTrans, float onTrans, int duration, FloatTransitionSmoother smoother) {
        //super(duration);
        super(0);
        
        if (baseTrans < 0.0f || baseTrans > 1.0f) {
            throw new IllegalArgumentException("transparency out of range");
        }
        if (onTrans < 0.0f || onTrans > 1.0f) {
            throw new IllegalArgumentException("transparency out of range");
        }
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        if (smoother == null) {
            throw new IllegalArgumentException("smoother cannot be null");
        }
        this.smoother = smoother;
        this.baseTrans = baseTrans;
        this.onTrans = onTrans;
        smoother.setInternalValue(baseTrans);
    }
    
    public void initialize() {
        getTarget().setTransparency(baseTrans);
    }
    
    public void destroy() {
        // nothing to do
    }
    
    public void doTimedAnimation(float elapsedDuration) {
        logger.severe("doTimedAnimation elapsedDuration="+elapsedDuration);
        float trans = smoother.getValue(elapsedDuration);
        getTarget().setTransparency(trans);
    }
    
    public void performAction(LgEventSource source, boolean value) {
        float trans = (value)?(onTrans):(baseTrans);
        smoother.setTargetValue(trans);
        setRunning(true);
    }
    
}
