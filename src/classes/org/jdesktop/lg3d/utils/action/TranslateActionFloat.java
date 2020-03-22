/**
 * Project Looking Glass
 *
 * $RCSfile: TranslateActionFloat.java,v $
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
 * $Date: 2006-08-14 23:47:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.action;

import java.lang.ref.WeakReference;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 * An action that changes component's translation.
 */
public class TranslateActionFloat implements ActionFloat {
    private static final int USE_DEFAULT_DURATION = -1;
    private WeakReference<Component3D> target;
    private Vector3f tmpTranslation = new Vector3f();
    private Vector3f baseTranslation;
    private Vector3f addTranslation;
    private int duration = USE_DEFAULT_DURATION;
    
    public TranslateActionFloat(Component3D target, Vector3f addTranslation) {
        if (target == null) {
            throw new IllegalArgumentException("target cannot be null");
        }
        if (addTranslation == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        this.target = new WeakReference(target);
        this.addTranslation = new Vector3f(addTranslation);
        this.baseTranslation = target.getFinalTranslation(new Vector3f());
    }
    
    public TranslateActionFloat(Component3D target, Vector3f addTranslation, int duration) {
        this(target, addTranslation);
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        this.duration = duration;
    }
    
    public void performAction(LgEventSource source, float v) {
        tmpTranslation.set(addTranslation);
        tmpTranslation.scale(v);
        tmpTranslation.add(baseTranslation);
        if (duration == USE_DEFAULT_DURATION) {
            target.get().changeTranslation(tmpTranslation);
        } else {
            target.get().changeTranslation(tmpTranslation, duration);
        }
    }
}
