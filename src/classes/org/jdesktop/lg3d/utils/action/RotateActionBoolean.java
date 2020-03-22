/**
 * Project Looking Glass
 *
 * $RCSfile: RotateActionBoolean.java,v $
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
 * $Date: 2006-05-24 22:44:37 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.action;

import java.lang.ref.WeakReference;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 * An action that changes component's rotation.
 */
public class RotateActionBoolean implements ActionBoolean {
    private static final int USE_DEFAULT_DURATION = -1;
    private WeakReference<Component3D> target;
    private float baseAngle;
    private float onAngle;
    private int duration = USE_DEFAULT_DURATION;
    
    public RotateActionBoolean(Component3D target, float onAngle) {
        if (target == null) {
            new IllegalArgumentException("target cannot be null");
        }
        this.target = new WeakReference(target);
        this.onAngle = onAngle;
        this.baseAngle = Float.NaN;
    }
    
    public RotateActionBoolean(Component3D target, float addAngle, int duration) {
        this(target, addAngle);
        if (duration < 0) {
            new IllegalArgumentException("duration cannot be negative");
        }
        this.duration = duration;
    }
    
    public void performAction(LgEventSource source, boolean state) {
        float newAngle = Float.NaN;
        if (state) {
            newAngle = onAngle;
            baseAngle = target.get().getFinalRotationAngle();
        } else {
            if (Float.isNaN(baseAngle)) {
                return;
            }
            newAngle = baseAngle;
        }
        if (duration == USE_DEFAULT_DURATION) {
            target.get().changeRotationAngle(newAngle);
        } else {
            target.get().changeRotationAngle(newAngle, duration);
        }
    }
}
