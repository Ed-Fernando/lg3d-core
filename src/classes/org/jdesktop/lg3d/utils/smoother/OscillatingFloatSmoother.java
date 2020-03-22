/**
 * Project Looking Glass
 *
 * $RCSfile: OscillatingFloatSmoother.java,v $
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
 * A smoother that doesn't reaches the target value, but generates
 * alternating sequence of float value around the specified base value.
 */
public class OscillatingFloatSmoother implements FloatTransitionSmoother {
    private SpringFloatSmoother resilientSmoother;
    private float baseValue;
    private int period;
    private float current;
    
    public OscillatingFloatSmoother(float baseValue, int period) {
        resilientSmoother = new SpringFloatSmoother();
        this.baseValue = baseValue;
	this.period = period;
    }
    
    public void setInternalValue(float value) {
        resilientSmoother.setInternalValue(value);
    }
    
    public float getValue(float n) {
	float val = resilientSmoother.getValue(n);

	double t 
	    = (double)(System.currentTimeMillis() % period) 
	        / (double)period * (2.0 * Math.PI);
	float w = (float)Math.sin(t);

	current = baseValue + val * w;

        return current;
    }
    
    public float getLatestValue() {
        return current;
    }
    
    public float getFinalValue() {
        return baseValue;
    }
    
    public void setTargetValue(float target) {
	resilientSmoother.setTargetValue(target);
    }
}

