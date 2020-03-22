/**
 * Project Looking Glass
 *
 * $RCSfile: AcceleratingVector3fSmoother.java,v $
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

import javax.vecmath.Vector3f;

/**
 * A smoother that changes a Vector3f value in an accelerated fashion.
 */
public class AcceleratingVector3fSmoother implements Vector3fTransitionSmoother {
    // this value seems to give a natural motion
    private static final float p = 6.0f;

    private Vector3f target = new Vector3f();
    private Vector3f val = new Vector3f();
    private Vector3f prevVal = new Vector3f();
    
    public AcceleratingVector3fSmoother() {
	
    }
    
    public AcceleratingVector3fSmoother(Vector3f initValue) {
	setInternalValue(initValue);
    }
    
    public void setInternalValue(Vector3f value) {
        target.set(value);
        val.set(value);
        prevVal.set(value);
    }
    
    public Vector3f getValue(Vector3f value, float n) {
	float x = (float)Math.pow(0.5, (1.0f - n) * p);
        
        val.x = (1.0f - x) * prevVal.x + x * target.x;
        val.y = (1.0f - x) * prevVal.y + x * target.y;
        val.z = (1.0f - x) * prevVal.z + x * target.z;
	
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
    
    public void setTargetValue(Vector3f target) {
	this.target.set(target);
	prevVal.set(val);
    }
}

