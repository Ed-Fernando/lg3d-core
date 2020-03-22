/**
 * Project Looking Glass
 *
 * $RCSfile: TransparencyActionFloat.java,v $
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
 * $Date: 2006-08-14 23:13:18 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.action;

import java.lang.ref.WeakReference;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 * An action that changes component's transparency.
 */
public class TransparencyActionFloat implements ActionFloat {
    private static final int USE_DEFAULT_DURATION = -1;
    private WeakReference<Component3D> target;
    private float baseTrans;
    private int duration = USE_DEFAULT_DURATION;
    
    public TransparencyActionFloat(Component3D target) {
        if (target == null) {
            throw new IllegalArgumentException("target cannot be null");
        }
        this.target = new WeakReference(target);
        this.baseTrans = target.getFinalTransparency();
    }
    
    public TransparencyActionFloat(Component3D target, int duration) {
        this(target);
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        this.duration = duration;
    }
    
    public void performAction(LgEventSource source, float value) {
        float newTrans = baseTrans + value;
        if (newTrans < 0.0f) {
            newTrans = 0.0f;
        } else if (newTrans > 1.0f) {
            newTrans = 1.0f;
        }
        if (duration == USE_DEFAULT_DURATION) {
            target.get().changeTransparency(newTrans);
        } else {
            target.get().changeTransparency(newTrans, duration);
        }
    }
}
