/**
 * Project Looking Glass
 *
 * $RCSfile: RotationAnimationFloat.java,v $
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
 * $Date: 2005-06-24 19:48:36 $
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
 * An animation that changes the rotation of it's target depending on a float
 * angle.
 *
 * The angle of the target is changed over time to match the angle provided.
 *
 * The angle is changed by ActionFloat callback which is typically 
 * called from a float event adapater.
 *
 */public class RotationAnimationFloat extends TimedAnimation implements ActionFloat {
    private AxisAngle4f rotation;
    private FloatTransitionSmoother smoother;
    private float baseAngle;
    
    /**
     * Creates a new instance of RotationAnimationFloat 
     *
     * Defaults to a NaturalFloatSmoother.
     */
    public RotationAnimationFloat(Vector3f rotationAxis, float baseAngle, 
            int duration) {
        
        this(rotationAxis, baseAngle, duration, new NaturalFloatSmoother());
    }
    
    /**
     * Creates a new instance of RotationAnimationFloat 
     */
    public RotationAnimationFloat(Vector3f rotationAxis, float baseAngle, 
            int duration, FloatTransitionSmoother smoother) {
        
        super(duration);
        
        if (rotationAxis == null) {
            throw new IllegalArgumentException("rotationAxis cannot be null");
        }
        if (smoother == null) {
            throw new IllegalArgumentException("smoother cannot be null");
        }
        rotation = new AxisAngle4f(rotationAxis, baseAngle);
        this.smoother = smoother;
        this.baseAngle = baseAngle;
        smoother.setInternalValue(baseAngle);
    }
    
    public void initialize() {
        assert(rotation.angle == baseAngle);
        getTarget().setRotation(rotation);
    }
    
    public void destroy() {
        // nothing to do
    }
    
    public void doTimedAnimation(float elapsedDuration) {
        rotation.angle = smoother.getValue(elapsedDuration);
        getTarget().setRotation(rotation);
    }    
    
    public void performAction(LgEventSource source, float value) {
        smoother.setTargetValue(value + baseAngle);
        setRunning(true);
    }
    
}
