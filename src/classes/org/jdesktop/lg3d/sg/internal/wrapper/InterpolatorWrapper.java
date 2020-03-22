/**
 * Project Looking Glass
 *
 * $RCSfile: InterpolatorWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:45 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;


import java.util.Vector;

/**
 * Interpolator is an abstract class that extends Behavior to provide
 * common methods used by various interpolation subclasses.  These
 * include methods to convert a time value into an alpha value (A
 * value in the range 0 to 1) and a method to initialize the behavior.
 * Subclasses provide the methods that convert alpha values into
 * values within that subclass' output range.
 */

public interface InterpolatorWrapper extends BehaviorWrapper {

    /**
      * Retrieves this interpolator's alpha object.
      * @return this interpolator's alpha object
      */
    //public AlphaWrapper getAlpha() ;


    /**
     * Set this interpolator's alpha to the specified alpha object.
     * @param alpha the new alpha object.  If set to null,
     * then this interpolator will stop running.
     */
    //public void setAlpha(AlphaWrapper alpha) ;
    
}
