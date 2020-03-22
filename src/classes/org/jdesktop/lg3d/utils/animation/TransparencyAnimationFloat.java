/**
 * Project Looking Glass
 *
 * $RCSfile: TransparencyAnimationFloat.java,v $
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
 * $Date: 2006-07-01 20:47:46 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.animation;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.utils.action.ActionFloat;
import org.jdesktop.lg3d.utils.smoother.FloatTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.NaturalFloatSmoother;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 *
 */
public class TransparencyAnimationFloat extends TimedAnimation implements ActionFloat {
    
    private FloatTransitionSmoother smoother;
    private float baseTrans;
    
    /**
     * Creates a new instance of TransparencyAnimationFloat 
     */
    public TransparencyAnimationFloat(float baseTrans, int duration) {
        this(baseTrans, duration, new NaturalFloatSmoother());
    }
    
    /**
     * Creates a new instance of TransparencyAnimationFloat 
     */
    public TransparencyAnimationFloat(float baseTrans, int duration, FloatTransitionSmoother smoother) {
        super(duration);
        
        if (baseTrans < 0.0f || baseTrans > 1.0f) {
            throw new IllegalArgumentException("transparency out of range");
        }
        if (smoother == null) {
            throw new IllegalArgumentException("smoother cannot be null");
        }
        this.smoother = smoother;
        this.baseTrans = baseTrans;
        smoother.setInternalValue(baseTrans);
    }
    
    public void initialize() {
        getTarget().setTransparency(baseTrans);
    }
    
    public void destroy() {
        // nothing to do
    }
    
    public void doTimedAnimation(float elapsedDuration) {
        float trans = smoother.getValue(elapsedDuration);
        getTarget().setTransparency(trans);
    }    
    
    public void performAction(LgEventSource source, float value) {
        float trans = baseTrans + value;
        if (trans < 0.0f) {
            trans = 0.0f;
        } else if (trans > 1.0) {
            trans = 1.0f;
        }
        smoother.setTargetValue(trans);
        setRunning(true);
    }
    
}
