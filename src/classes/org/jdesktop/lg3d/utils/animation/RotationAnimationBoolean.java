/**
 * Project Looking Glass
 *
 * $RCSfile: RotationAnimationBoolean.java,v $
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
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.smoother.FloatTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.NaturalFloatSmoother;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 *
 * An animation that changes the rotation of it's target depending on a boolean
 * value.
 *
 * When true the target is rotated to 'onAngle', when false target is rotated to 'baseAngle'.
 *
 * The Animation state is changed by ActionBoolean callback which is typically 
 * called from a boolean event adapater.
 *
 */
public class RotationAnimationBoolean extends TimedAnimation implements ActionBoolean {
    
    private AxisAngle4f rotation;
    private FloatTransitionSmoother smoother;
    private float baseAngle;
    private float onAngle;
    
    /**
     * Creates a new instance of RotationAnimationBoolean.
     *
     * Uses a NaturalFloatSmoother.
     */
    public RotationAnimationBoolean(Vector3f rotationAxis, float baseAngle, 
            float onAngle, int duration) {
        
        this(rotationAxis, baseAngle, onAngle, duration, new NaturalFloatSmoother());
    }
    
    /**
     * Creates a new instance of RotationAnimationBoolean 
     */
    public RotationAnimationBoolean(Vector3f rotationAxis, float baseAngle, 
            float onAngle, int duration, FloatTransitionSmoother smoother) {
        
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
        this.onAngle = onAngle;
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
    
    public void performAction(LgEventSource source, boolean value) {
        float v = (value)?(onAngle):(baseAngle);
        smoother.setTargetValue(v);
        setRunning(true);
    }
}
