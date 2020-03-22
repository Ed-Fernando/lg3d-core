/**
 * Project Looking Glass
 *
 * $RCSfile: FloatTransitionSmoother.java,v $
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
 * $Date: 2005-04-14 23:04:55 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.smoother;

/**
 * Interface for utility classes that generates sequence of float values.
 * Typically it keeps an internal value and changes it towards a given
 * target value gradually.
 */
public interface FloatTransitionSmoother extends TransitionSmoother {
    /**
     * Set a new target value for this smoother's value generation.
     *
     * @param  target  new target value.
     */
    public void setTargetValue(float target);
    
    /**
     * Reset the internal value to the specified value.
     *
     * @param  target  new internal value.
     */
    public void setInternalValue(float value);
    
    /**
     * Return the current internal value.
     */
    public float getValue(float elapsedTime);
    
    /**
     * Return the internal value most recently calculated.
     */
    public float getLatestValue();
    
    /**
     * Return the current target value.
     */
    public float getFinalValue();
}

