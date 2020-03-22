/**
 * Project Looking Glass
 *
 * $RCSfile: NaturalFloatSmoother.java,v $
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
 * $Date: 2005-04-14 23:04:56 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.smoother;

/**
 * A smoother that changes a float value in a natural looking fashion.
 */
public class NaturalFloatSmoother implements FloatTransitionSmoother {
    // this value seems to give a natural motion (10.0 felt slighly smaller)
    private static final float p = 12.0f;

    private float target;
    private float acc1;
    private float acc2;
    private float val;
    private float prevAcc1;
    private float prevAcc2;
    private float prevVal;
    
    public NaturalFloatSmoother() {
        this(0.0f);
    }
    
    public NaturalFloatSmoother(float initValue) {
        setInternalValue(initValue);
    }
    
    public void setInternalValue(float value) {
	target = acc1 = acc2 = val = prevAcc1 = prevAcc2 = prevVal = value;
    }
    
    public float getValue(float n) {
        // The calculation is a bit heavy, but priority has been put on better
        // visual experience than computation power consumption.
	float x = (float)Math.pow(0.5, n * p);

	acc1 = x * prevAcc1 + (1 - x) * target;
	acc2 = x * prevAcc2 + (1 - x) * acc1;
	val = x * prevVal + (1 - x) * acc2;

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
	prevAcc1 = acc1;
	prevAcc2 = acc2;
    }
}

