/**
 * Project Looking Glass
 *
 * $RCSfile: LinearFloatSmoother.java,v $
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
 * $Date: 2005-06-24 19:48:41 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.smoother;

/**
 * A smoother that does not smooth.
 *
 * This returns a linear value from start to end value over a duration of 1.0
 */
public class LinearFloatSmoother implements FloatTransitionSmoother {
    private float target;
    private float start;
    private float current;
    
    public LinearFloatSmoother() {
	this(0.0f);
    }
    
    public LinearFloatSmoother(float initValue) {
	setInternalValue(initValue);
    }
    
    public void setInternalValue(float value) {
        start = target = current = value;
    }
    
    public float getValue(float n) {
        current = start + (target-start) * n;
	return current;
    }
    
    public float getLatestValue() {
        return current;
    }
    
    public float getFinalValue() {
        return target;
    }
    
    public void setTargetValue(float target) {
        start = current;
        this.target = target;
    }
}

