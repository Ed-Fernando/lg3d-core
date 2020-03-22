/**
 * Project Looking Glass
 *
 * $RCSfile: ScaleAnimationBoolean.java,v $
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
 * $Date: 2005-04-14 23:04:36 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.animation;

import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.smoother.FloatTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.NaturalFloatSmoother;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 *
 */
public class ScaleAnimationBoolean extends TimedAnimation implements ActionBoolean {
    
    private FloatTransitionSmoother smoother;
    private float baseScale;
    private float onScale;
    
    /**
     * Creates a new instance of RotationAnimationBoolean 
     */
    public ScaleAnimationBoolean(float baseScale, float onScale, int duration) {
        this(baseScale, onScale, duration, new NaturalFloatSmoother());
    }
    
    /**
     * Creates a new instance of RotationAnimationBoolean 
     */
    public ScaleAnimationBoolean(float baseScale, float onScale, int duration, FloatTransitionSmoother smoother) {
        super(duration);
        
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        if (smoother == null) {
            throw new IllegalArgumentException("smoother cannot be null");
        }
        this.smoother = smoother;
        this.baseScale = baseScale;
        this.onScale = onScale;
        smoother.setInternalValue(baseScale);
    }
    
    public void initialize() {
        getTarget().setScale(baseScale);
    }
    
    public void destroy() {
        // nothing to do
    }
    
    public void doTimedAnimation(float elapsedDuration) {
        float s = smoother.getValue(elapsedDuration);
        getTarget().setScale(s);
    }    
    
    public void performAction(LgEventSource source, boolean value) {
        float v = (value)?(onScale):(baseScale);
        smoother.setTargetValue(v);
        setRunning(true);
    }
    
}
