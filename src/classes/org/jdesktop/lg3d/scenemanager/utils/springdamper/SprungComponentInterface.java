/**
 * Project Looking Glass
 *
 * $RCSfile: SprungComponentInterface.java,v $
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
 * $Date: 2005-12-23 21:44:48 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.springdamper;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.wg.Component3D;

/**
 *
 * @author paulby
 */
public interface SprungComponentInterface {
    
    /**
     * Returns the value of locked. Locked objects can not be moved
     * by the springs
     */
    public boolean isLocked();
    
    /**
     * Returns true if the component is enabled
     */
    public boolean isLive();
    
    /**
     * Set the locked state of this object. Locked objects can not be moved
     * by the springs
     */
    public void setLocked( boolean locked );
    
    /**
     * Returns the spring translation.
     *
     * @param translation to be populated and returned, must not be null.
     */
    public Vector3f getSpringTranslation(Vector3f translation);
    
    /**
     * Get the force enduced velocity
     *
     * @param velocity to be populated and returned, must not be null.
     */
    public Vector3f getVelocity(Vector3f velocity);
    
    /**
     * Add a force to this object. Forces are not actually applied until
     * the step method is called.
     */
    public void addForce(Vector3f force);
    
    /**
     * Step through the simulation, only change the rendered position of
     * the object is render is true in order to reduce overhead
     */
    public void step(float timeDelta, boolean render);
        
}
