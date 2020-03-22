/**
 * Project Looking Glass
 *
 * $RCSfile: SpringFloatSmoother.java,v $
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
 * $Date: 2005-04-14 23:04:56 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.smoother;


/**
 * A smoother that start generating a vlaue toward the given target value
 * but doesn't reaches it and goes back to the given base value.
 */
public class SpringFloatSmoother implements FloatTransitionSmoother {
    // this value seems to give a natural motion (10.0 felt slighly smaller)
    private static final float p = 12.0f;

    private float target;
    private float acc;
    private float val;
    private float prevAcc;
    private float prevVal;
    private float baseValue;
    
    public SpringFloatSmoother() {
	this(0.0f);
    }
    
    public SpringFloatSmoother(float baseValue) {
	this.baseValue = baseValue;
        setInternalValue(0.0f);
    }
    
    public void setInternalValue(float value) {
        target = acc = val = prevAcc = prevVal = value;
    }
    
    public float getValue(float n) {
	float x = (float)Math.pow(0.5, n * p);
        float y = x * x;
        float t = target * y;
	acc = x * prevAcc + (1 - x) * t;
	val = x * prevVal + (1 - x) * acc;
	return baseValue + val;
    }
    
    public float getLatestValue() {
        return baseValue + val;
    }
    
    public float getFinalValue() {
        return baseValue;
    }
    
    public void setTargetValue(float target) {
	this.target = target;
	prevVal = val;
	prevAcc = acc;
    }
}

