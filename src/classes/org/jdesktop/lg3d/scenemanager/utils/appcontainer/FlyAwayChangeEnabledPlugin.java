/*
 * Project Looking Glass
 *
 * $RCSfile: FlyAwayChangeEnabledPlugin.java,v $
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
 * $Date: 2005-04-14 23:04:08 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.appcontainer;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.utils.animation.ConcurrentTimedSubAnimationHelper;
import org.jdesktop.lg3d.utils.smoother.AcceleratingVector3fSmoother;
import org.jdesktop.lg3d.utils.smoother.AcceleratingFloatSmoother;
import org.jdesktop.lg3d.utils.smoother.LinearFloatSmoother;
import org.jdesktop.lg3d.wg.Frame3DAnimationTarget;


/**
 *
 */
public class FlyAwayChangeEnabledPlugin implements ChangeEnabledAnimationPlugin {
    private PluggableF3DAnimation anim;
    private int defaultDuration;
    private ConcurrentTimedSubAnimationHelper.SubAnimation animItem;
    private float screenWidth;
    private float screenHeight;
    
    public FlyAwayChangeEnabledPlugin(float width, float height, int duration) {
        screenWidth = width;
        screenHeight = height;
        defaultDuration = duration;
    }
    
    public void initialize(final PluggableF3DAnimation anim) {
        this.anim = anim;
        
        animItem 
            = new ConcurrentTimedSubAnimationHelper.SubAnimation() {
                public void doTimedAnimation(float elapsedDuration) {
                    if (elapsedDuration == 1.0f && (!anim.isFinalEnabled())) {
                        // make the object disabled after finishing the animation
                        Frame3DAnimationTarget animTarget = (Frame3DAnimationTarget)anim.getTarget();
                        animTarget.setEnabled(false);
                    }
                }
            };
    }
    
    public void changeEnabled(boolean enabled, int duration) {
        if (enabled) {
            Frame3DAnimationTarget animTarget = (Frame3DAnimationTarget)anim.getTarget();
            // make the object enabled before starting the animation
            animTarget.setEnabled(true);
        } else {
            float scale = anim.getScale();
            anim.changeVisible(false, duration);
            
            // do the following after calling changeVisible()
            anim.setTranslationSmoother(new AcceleratingVector3fSmoother());
            anim.setRotationAngleSmoother(new AcceleratingFloatSmoother());
            anim.setScaleSmoother(new AcceleratingFloatSmoother());
            anim.setTransparencySmoother(new LinearFloatSmoother());
            
            Vector3f v3f = anim.getTranslation(new Vector3f());
            v3f.x += screenWidth * 0.05f;
            v3f.y += screenHeight * 0.05f;
            v3f.z += screenWidth * 0.05f;
            anim.changeTranslation(v3f.x, v3f.y, v3f.z, 0);
            v3f.x += screenWidth * 0.75f;
            v3f.y += screenHeight * 0.75f;
            v3f.z += screenWidth * 0.75f;
            anim.changeTranslation(v3f.x, v3f.y, v3f.z, duration);
            anim.changeRotationAxis(1.0f, -1.0f, 0.0f, 0);
            anim.changeRotationAngle((float)Math.toRadians(315), duration);
            anim.changeScale(scale, 0); // cancel any scaling animation
            anim.changeTransparency(0.25f, 0);
            anim.changeTransparency(0.85f, duration); // fading out
        }
        anim.startSubAnimation(animItem, duration);
    }
    
    public int getDefaultEnabledDuration() {
        return defaultDuration;
    }
    
    public void destroy() {
        // do nothing
    }
}
