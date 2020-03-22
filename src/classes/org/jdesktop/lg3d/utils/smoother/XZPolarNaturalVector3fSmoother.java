/**
 * Project Looking Glass
 *
 * $RCSfile: XZPolarNaturalVector3fSmoother.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2006-02-15 02:59:16 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.smoother;

import javax.vecmath.Vector3f;


/**
 * A smoother that changes a Vector3f value in a natural looking fashion.
 * As for (x, y), the values change so that the coordinate moves around
 * the center (0, 0) making an arc (instead of a straight line).
 * As for the z value, it moves along a straight line.
 */
public class XZPolarNaturalVector3fSmoother implements Vector3fTransitionSmoother {
    private NaturalFloatSmoother smootherR;
    private NaturalFloatSmoother smootherA;
    private NaturalFloatSmoother smootherY;
    
    private Vector3f target = new Vector3f();
    private Vector3f val = new Vector3f();
    
    public XZPolarNaturalVector3fSmoother() {
	smootherR = new NaturalFloatSmoother();
        smootherA = new NaturalFloatSmoother();
        smootherY = new NaturalFloatSmoother();
    }
    
    public void setInternalValue(Vector3f value) {
	setTargetValue(value); // NOTE: this updates target value
        
        smootherR.setInternalValue(smootherR.getFinalValue());
        smootherA.setInternalValue(smootherA.getFinalValue());
        smootherY.setInternalValue(smootherY.getFinalValue());
        
        val.set(value);
    }
    
    public Vector3f getValue(Vector3f value, float n) {
        float radius = smootherR.getValue(n);
        float angle = smootherA.getValue(n);
        float y = smootherY.getValue(n);
        
        float x = radius * (float)Math.cos(angle);
        float z = radius * (float)Math.sin(angle);
        
        val.set(x, y, z);
        value.set(val);
        
	return value;
    }
    
    public Vector3f getLatestValue(Vector3f value) {
        value.set(val);
        
        return value;
    }
    
    public Vector3f getFinalValue(Vector3f value) {
        value.set(target);
        
        return value;
    }
    
    public void setTargetValue(Vector3f value) {
	target.set(value);
        
        float radius = (float)Math.hypot(target.z, target.x);
        float angle = (float)Math.atan2(target.z, target.x);
        
        // avoid warping motion
        float a = smootherA.getLatestValue();
        if (angle - Math.PI > a) {
            a += (float)Math.PI * 2; 
            smootherA.setInternalValue(a);
        } else if (angle + Math.PI < a) {
            a -= (float)Math.PI * 2;
            smootherA.setInternalValue(a);
        }
        
        smootherR.setTargetValue(radius);
        smootherA.setTargetValue(angle);
        smootherY.setTargetValue(target.y);
    }
}

