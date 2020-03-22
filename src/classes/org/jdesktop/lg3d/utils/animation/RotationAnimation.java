/**
 * Project Looking Glass
 *
 * $RCSfile: RotationAnimation.java,v $
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
 * $Date: 2006-08-14 23:47:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.animation;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.utils.action.ActionFloat;
import org.jdesktop.lg3d.utils.smoother.FloatTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.NaturalFloatSmoother;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 *
 * An animation that changes the rotation of it's target between a start and finish
 * angle.
 *
 */public class RotationAnimation extends TimedAnimation {
    private AxisAngle4f rotation;
    private FloatTransitionSmoother smoother;
    
    /**
     * Creates a new instance of RotationAnimation
     *
     * Defaults to a NaturalFloatSmoother.
     */
    public RotationAnimation(Vector3f rotationAxis, float startAngle, float finishAngle,
            int duration, int loopCount, LoopType loopType ) {
        
        this(rotationAxis, startAngle, finishAngle, duration, loopCount, loopType, new NaturalFloatSmoother());
    }
    
    /**
     * Creates a new instance of RotationAnimationFloat 
     */
    public RotationAnimation(Vector3f rotationAxis, float startAngle, float finishAngle,
            int duration, int loopCount, LoopType loopType, FloatTransitionSmoother smoother) {
        
        super(duration, loopCount, loopType);
        
        if (rotationAxis == null) {
            throw new IllegalArgumentException("rotationAxis cannot be null");
        }
        if (smoother == null) {
            throw new IllegalArgumentException("smoother cannot be null");
        }
        rotation = new AxisAngle4f(rotationAxis, startAngle);
        this.smoother = smoother;
        smoother.setInternalValue(startAngle);
        smoother.setTargetValue(finishAngle);
    }
    
    public void initialize() {
        getTarget().setRotation(rotation);
    }
    
    public void destroy() {
        // nothing to do
    }
    
    public void doTimedAnimation(float elapsedDuration) {
        rotation.angle = smoother.getValue(elapsedDuration);
        getTarget().setRotation(rotation);
    }    
    
  
}
