/**
 * Project Looking Glass
 *
 * $RCSfile: ScaleActionBoolean.java,v $
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
 * $Date: 2006-05-24 22:44:38 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.action;

import java.lang.ref.WeakReference;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 * An action that changes component's scaling factor.
 */
public class ScaleActionBoolean implements ActionBoolean {
    private static final int USE_DEFAULT_DURATION = -1;
    private WeakReference<Component3D> target;
    private float baseScale;
    private float onScale;
    private int duration = USE_DEFAULT_DURATION;
    
    public ScaleActionBoolean(Component3D target, float onScale) {
        if (target == null) {
            new IllegalArgumentException("target cannot be null");
        }
        this.target = new WeakReference(target);
        this.onScale = onScale;
        this.baseScale = Float.NaN;
    }
    
    public ScaleActionBoolean(Component3D target, float onScale, int duration) {
        this(target, onScale);
        if (duration < 0) {
            new IllegalArgumentException("duration cannot be negative");
        }
        this.duration = duration;
    }
    
    public void performAction(LgEventSource source, boolean state) {
        float newScale = Float.NaN;
        if (state) {
            newScale = onScale;
            baseScale = target.get().getFinalScale();
        } else {
            if (Float.isNaN(baseScale)) {
                return;
            }
            newScale = baseScale;
        }
        if (duration == USE_DEFAULT_DURATION) {
            target.get().changeScale(newScale);
        } else {
            target.get().changeScale(newScale, duration);
        }
    }
}
