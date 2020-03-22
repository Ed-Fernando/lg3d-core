/**
 * Project Looking Glass
 *
 * $RCSfile: TranslationAnimationFloat.java,v $
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
 * $Date: 2005-04-14 23:04:38 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.animation;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.utils.action.ActionFloat;
import org.jdesktop.lg3d.utils.smoother.Vector3fTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.NaturalVector3fSmoother;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 *
 */
public class TranslationAnimationFloat extends TimedAnimation implements ActionFloat {
    
    private Vector3fTransitionSmoother smoother;
    private Vector3f baseTrans = new Vector3f();
    private Vector3f addTrans = new Vector3f();
    private Vector3f tmpV3f = new Vector3f();
    
    /**
     * Creates a new instance of RotationAnimationBoolean 
     */
    public TranslationAnimationFloat(Vector3f baseTrans, Vector3f addTrans, int duration) {
        this(baseTrans, addTrans, duration, new NaturalVector3fSmoother());
    }
    
    /**
     * Creates a new instance of RotationAnimationBoolean 
     */
    public TranslationAnimationFloat(Vector3f baseTrans, Vector3f addTrans, int duration, Vector3fTransitionSmoother smoother) {
        super(duration);
        
        if (baseTrans == null || addTrans == null) {
            throw new IllegalArgumentException("translation object cannot be negative");
        }
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        if (smoother == null) {
            throw new IllegalArgumentException("smoother cannot be null");
        }
        this.smoother = smoother;
        this.baseTrans.set(baseTrans);
        this.addTrans.set(addTrans);
        smoother.setInternalValue(baseTrans);
    }
    
    public void initialize() {
        getTarget().setTranslation(baseTrans);
    }
    
    public void destroy() {
        // nothing to do
    }
    
    public void doTimedAnimation(float elapsedDuration) {
        smoother.getValue(tmpV3f, elapsedDuration);
        getTarget().setTranslation(tmpV3f);
    }    
    
    public void performAction(LgEventSource source, float value) {
        tmpV3f.set(addTrans);
        tmpV3f.scale(value);
        tmpV3f.add(baseTrans);
        
        smoother.setTargetValue(tmpV3f);
        setRunning(true);
    }
    
}
