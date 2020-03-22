/**
 * Project Looking Glass
 *
 * $RCSfile: Vector3fTransitionSmoother.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2005-04-14 23:04:57 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.smoother;

import javax.vecmath.*;

/**
 * Interface for utility classes that generates sequence of Vector3f values.
 * Typically it keeps an internal value and changes it towards a given
 * target value gradually.
 */
public interface Vector3fTransitionSmoother extends TransitionSmoother {
    /**
     * Set a new target value for this smoother's value generation.
     *
     * @param  target  new target value.
     */
    public void setTargetValue(Vector3f target);
    
    /**
     * Reset the internal value to the specified value.
     *
     * @param  target  new internal value.
     */
    public void setInternalValue(Vector3f value);
    
    /**
     * Return the current internal value.
     */
    public Vector3f getValue(Vector3f value, float elapsedTime);
    
    /**
     * Return the internal value most recently calculated.
     */
    public Vector3f getLatestValue(Vector3f value);
    
    /**
     * Return the current target value.
     */
    public Vector3f getFinalValue(Vector3f value);
}

