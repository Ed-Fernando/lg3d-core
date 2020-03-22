/**
 * Project Looking Glass
 *
 * $RCSfile: AnimationTarget.java,v $
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
 * $Date: 2005-04-14 23:04:59 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;

/**
 * Interface describing all operations an Animation can perform
 * on a target
 *
 * @author paulby
 */
public interface AnimationTarget {
        
    /**
     * Set the position 
     */
    public void setTranslation(Vector3f position);
    
    /**
     * Returns the position, both as the parameter and
     * as the return value.
     *
     * @param position return the position data in this parameter, it must not
     *       be null
     * @return returns the position object
     */
    public Vector3f getTranslation(Vector3f position);
    
    /**
     * Set the axis and angle of rotation
     */
    public void setRotation(AxisAngle4f rotation);
    
    /**
     * Returns the rotation, both as the parameter and
     * as the return value.
     *
     * @param rotation return the rotation data in this parameter, it must not
     *       be null
     * @return returns the rotation object
     */
    public AxisAngle4f getRotation(AxisAngle4f rotation);
    
    /**
     * Set a non-uniform scale. Note uniform scales are cheaper.
     */
    public void setScale(Vector3f scale);
    
    /**
     * Set a uniform scale
     */
    public void setScale(float scale);
    
    /**
     * Return the scale
     */
    public Vector3f getScale(Vector3f scale);
    
    /**
     * Return the uniform scale, returns float.NAN if the scale is
     * non-uniform
     */
    public float getScale();
    
    /**
     * Set rotation, translation and non-uniform scale
     */
    public void set(AxisAngle4f rotation, Vector3f translation, Vector3f scale);
    
    /**
     * Set rotation, translation and uniform scale
     */
    public void set(AxisAngle4f rotation, Vector3f translation, float scale);

    /**
     * Set the alpha/transparency of the target. The value should
     * be in the range [0.0, 1.0] with 0.0 being fully opaque and 1.0
     * fully transparent.
     */
    public void setTransparency( float alpha );
    
    /**
     * Returns the alpha level of the target. The value will
     * be in the range [0.0, 1.0] with 0.0 being fully opaque and 1.0
     * fully transparent.
     */
    public float getTransparency();
    
    
}
