/*
 * Project Looking Glass
 *
 * $RCSfile: ScaleAndRotateChangeVisiblePlugin.java,v $
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
 * $Date: 2005-04-14 23:04:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.c3danimation;

import org.jdesktop.lg3d.utils.animation.ConcurrentTimedSubAnimationHelper;
import org.jdesktop.lg3d.wg.Component3DAnimation;
import org.jdesktop.lg3d.wg.Component3DAnimationTarget;
import org.jdesktop.lg3d.wg.Frame3DAnimation;
import org.jdesktop.lg3d.wg.Frame3DAnimationTarget;

/**
 *
 */
public class ScaleAndRotateChangeVisiblePlugin implements ChangeVisibleAnimationPlugin {
    private PluggableC3DAnimation anim;
    private int defaultDuration;
    private ConcurrentTimedSubAnimationHelper.SubAnimation animItem;
    private boolean visible;
    
    public ScaleAndRotateChangeVisiblePlugin(int duration) {
        this.defaultDuration = duration;
    }
    
    public void initialize(final PluggableC3DAnimation anim) {
        this.anim = anim;
        
        animItem 
            = new ConcurrentTimedSubAnimationHelper.SubAnimation() {
                public void doTimedAnimation(float elapsedDuration) {
                    if (elapsedDuration == 1.0f && (!anim.isFinalVisible())) {
                        // make the object invisible after finishing the animation
                        Component3DAnimationTarget animTarget = (Component3DAnimationTarget)anim.getTarget();
                        animTarget.setVisible(false);
                    }
                }
            };
    }
    
    public void destroy() {
        // do nothing
    }
    
    public boolean isVisible() {
        return ((Component3DAnimationTarget)anim.getTarget()).isVisible();
    }
    
    public boolean isFinalVisible() {
        return visible;
    }
    
    public void changeVisible(boolean visible, int duration) {
        if (this.visible == visible && duration != 0) {
            return;
        }
        this.visible = visible;
        if (visible) {
            Component3DAnimationTarget animTarget = (Component3DAnimationTarget)anim.getTarget();
            // make the object visible before starting the animation
            animTarget.setVisible(true);
            
            anim.changeScale(1.0f, duration);
            anim.changeRotationAngle((float)Math.toRadians(180), 0);
            anim.changeRotationAngle(0.0f, duration);
        } else {
            anim.changeScale(0.0f, duration);
            anim.changeRotationAngle((float)Math.toRadians(-180), duration);
        }
        anim.startSubAnimation(animItem, duration);
    }
    
    public int getDefaultVisibleDuration() {
        return defaultDuration;
    }
}
