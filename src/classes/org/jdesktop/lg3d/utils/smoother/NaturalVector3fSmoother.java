/**
 * Project Looking Glass
 *
 * $RCSfile: NaturalVector3fSmoother.java,v $
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

import javax.vecmath.Vector3f;


/**
 * A smoother that changes a Vector3f value in a natural looking fashion.
 */
public class NaturalVector3fSmoother implements Vector3fTransitionSmoother {
    // this value seems to give a natural motion (10.0 felt slighly smaller)
    private static final float p = 12.0f;

    private Vector3f target = new Vector3f();
    private Vector3f acc1 = new Vector3f();
    private Vector3f acc2 = new Vector3f();
    private Vector3f val = new Vector3f();
    private Vector3f prevAcc1 = new Vector3f(); // (0, 0, 0)
    private Vector3f prevAcc2 = new Vector3f(); // (0, 0, 0)
    private Vector3f prevVal = new Vector3f(); // (0, 0, 0)
    
    public NaturalVector3fSmoother() {
	
    }
    
    public NaturalVector3fSmoother(Vector3f initValue) {
        setInternalValue(initValue);
    }
    
    public void setInternalValue(Vector3f value) {
	target.set(value);
        acc1.set(value);
        acc2.set(value);
        val.set(value);
        prevAcc1.set(value);
        prevAcc2.set(value);
        prevVal.set(value);
    }
    
    public Vector3f getValue(Vector3f value, float n) {
        // The calculation is a bit heavy, but priority has been put on better
        // visual experience than computation power consumption.
	float x = (float)Math.pow(0.5, n * p);

	acc1.x = x * prevAcc1.x + (1 - x) * target.x;
	acc1.y = x * prevAcc1.y + (1 - x) * target.y;
	acc1.z = x * prevAcc1.z + (1 - x) * target.z;
        
        acc2.x = x * prevAcc2.x + (1 - x) * acc1.x;
        acc2.y = x * prevAcc2.y + (1 - x) * acc1.y;
        acc2.z = x * prevAcc2.z + (1 - x) * acc1.z;
        
	val.x = x * prevVal.x + (1 - x) * acc1.x;
	val.y = x * prevVal.y + (1 - x) * acc1.y;
	val.z = x * prevVal.z + (1 - x) * acc1.z;
        
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
	prevVal.set(val);
	prevAcc1.set(acc1);
        prevAcc2.set(acc2);
    }
}

