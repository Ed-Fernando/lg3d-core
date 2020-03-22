/**
 * Project Looking Glass
 *
 * $RCSfile: TransparencyActionBoolean.java,v $
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
 * An action that changes component's transparency.
 */
public class TransparencyActionBoolean implements ActionBoolean {
    private static final int USE_DEFAULT_DURATION = -1;
    private WeakReference<Component3D> target;
    private float baseTrans;
    private float onTrans;
    private int duration = USE_DEFAULT_DURATION;
    
    public TransparencyActionBoolean(Component3D target, float onTrans) {
        if (target == null) {
            new IllegalArgumentException("target cannot be null");
        }
        if (onTrans < 0.0f || onTrans > 1.0f) {
            throw new IllegalArgumentException("transparency argument out of range");
        }
        this.target = new WeakReference(target);
        this.onTrans = onTrans;
        this.baseTrans = Float.NaN;
    }
    
    public TransparencyActionBoolean(Component3D target, float onTrans, int duration) {
        this(target, onTrans);
        if (duration < 0) {
            new IllegalArgumentException("duration cannot be negative");
        }
        this.duration = duration;
    }
    
    public void performAction(LgEventSource source, boolean state) {
        float newTrans = Float.NaN;
        if (state) {
            newTrans = onTrans;
            baseTrans = baseTrans = target.get().getFinalTransparency();
        } else {
            if (Float.isNaN(baseTrans)) {
                return;
            }
            newTrans = baseTrans;
        }
        if (duration == USE_DEFAULT_DURATION) {
            target.get().changeTransparency(newTrans);
        } else {
            target.get().changeTransparency(newTrans, duration);
        }
    }
}
