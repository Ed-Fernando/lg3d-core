/**
 * Project Looking Glass
 *
 * $RCSfile: SpringRotationAnimationFloat.java,v $
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
 * $Date: 2005-04-14 23:04:37 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.animation;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.utils.smoother.SpringFloatSmoother;


/**
 *
 */
public class SpringRotationAnimationFloat extends RotationAnimationFloat {
    
    private AxisAngle4f rotation;
    
    /**
     * Creates a new instance of ResilientRotationAnimation
     */
    public SpringRotationAnimationFloat(Vector3f rotationAxis, int duration) {
        this(rotationAxis, 0.0f, duration);
    }
    
    /**
     * Creates a new instance of ResilientRotationAnimation 
     */
    public SpringRotationAnimationFloat(Vector3f rotationAxis, float baseAngle, int duration) {
        super(rotationAxis, 0.0f, duration, new SpringFloatSmoother(baseAngle));
        rotation = new AxisAngle4f(rotationAxis, baseAngle);
    }
    
    public void initialize() {
        getTarget().setRotation(rotation);
        rotation = null; // let 'rotation' GC'ed
    }
}
