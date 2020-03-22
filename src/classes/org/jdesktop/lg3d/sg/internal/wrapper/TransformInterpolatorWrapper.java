/**
 * Project Looking Glass
 *
 * $RCSfile: TransformInterpolatorWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:50 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

import java.util.Enumeration;
/**
 * TransformInterpolator is an abstract class that extends
 * Interpolator to provide common methods used by various transform
 * related interpolator subclasses.  These include methods to set/get
 * the target of TransformGroup, and set/get transform of axis.
 *
 * @since Java 3D 1.3
 */

public interface TransformInterpolatorWrapper extends InterpolatorWrapper {

    
    /**
     * This method sets the target TransformGroup node for this 
     * interpolator.
     * @param target The target TransformGroup
     */
    public void setTarget(TransformGroupWrapper target) ;

    /**
     * This method retrieves this interpolator's TransformGroup
     * node reference.
     * @return the Interpolator's target TransformGroup
     */
    public TransformGroupWrapper getTarget() ;


    /**
     * This method sets the axis of transform for this interpolator.
     * @param axisOfTransform the transform that defines the local coordinate
     * system in which this interpolator operates
     */ 
    public void setTransformAxis(Transform3DWrapper axisOfTransform) ;

       
    /**
     * This method retrieves this interpolator's axis of transform.
     * @return the interpolator's axis of transform
     */ 
    public Transform3DWrapper getTransformAxis() ;


    /**
     * Computes the new transform for this interpolator for a given
     * alpha value.
     *
     * @param alphaValue alpha value between 0.0 and 1.0
     * @param transform object that receives the computed transform for
     * the specified alpha value
     */
    //public abstract void computeTransform(float alphaValue,
//					  Transform3D transform);

    /**
     * This method is invoked by the behavior scheduler every frame.
     * First it gets the alpha value that corresponds to the current time.
     * Then it calls computeTransform() method to computes the transform based on this
     * alpha vaule,  and updates the specified TransformGroup node with this new transform.
     * @param criteria an enumeration of the criteria that caused the
     * stimulus
     */
//    public void processStimulus(Enumeration criteria) {
//	/* Handle stimulus */
//	WakeupCriterion criterion = passiveWakeupCriterion;
//
//	if (alpha != null) {
//	    float value = alpha.value();
//	    if (value != prevAlphaValue) {
//		computeTransform(value, currentTransform);
//		target.setTransform(currentTransform);
//		prevAlphaValue = value;
//	    }
//	    if (!alpha.finished() && !alpha.isPaused()) {
//		criterion = defaultWakeupCriterion;
//	    }
//	}
//	wakeupOn(criterion);
//    }
    
}
