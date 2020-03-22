/**
 * Project Looking Glass
 *
 * $RCSfile: AcceleratingFloatSmoother.java,v $
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
 * $Date: 2005-04-14 23:04:55 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.smoother;

/**
 * A smoother that changes a float value in an accelerated fashion.
 */
public class AcceleratingFloatSmoother implements FloatTransitionSmoother {
    // this value seems to give a natural motion
    private static final float p = 6.0f;

    private float target;
    private float val;
    private float prevVal;
    
    public AcceleratingFloatSmoother() {
	this(0.0f);
    }
    
    public AcceleratingFloatSmoother(float initValue) {
	setInternalValue(initValue);
    }
    
    public void setInternalValue(float value) {
        target = val = prevVal = value;
    }
    
    public float getValue(float n) {
	float x = (float)Math.pow(0.5, (1.0f - n) * p);
        
        val = (1.0f - x) * prevVal + x * target;
	
	return val;
    }
    
    public float getLatestValue() {
        return val;
    }
    
    public float getFinalValue() {
        return target;
    }
    
    public void setTargetValue(float target) {
	this.target = target;
	prevVal = val;
    }
}

