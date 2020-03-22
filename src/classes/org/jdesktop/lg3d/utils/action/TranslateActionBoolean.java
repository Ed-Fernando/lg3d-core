/**
 * Project Looking Glass
 *
 * $RCSfile: TranslateActionBoolean.java,v $
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
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 * An action that changes component's translation.
 */
public class TranslateActionBoolean implements ActionBoolean {
    private static final int USE_DEFAULT_DURATION = -1;
    private WeakReference<Component3D> target;
    private Vector3f baseTranslation;
    private Vector3f onTranslation;
    private int duration = USE_DEFAULT_DURATION;
    
    
    public TranslateActionBoolean(Component3D target, Vector3f onTranslation) {
        if (target == null) {
            new IllegalArgumentException("target cannot be null");
        }
        this.target = new WeakReference(target);
        if (onTranslation == null) {
            new IllegalArgumentException("argument cannot be null");
        }
        this.target = new WeakReference(target);
        this.onTranslation = new Vector3f(onTranslation);
        this.baseTranslation = null;
    }
    
    public TranslateActionBoolean(Component3D target, Vector3f onTranslation, int duration) {
        this(target, onTranslation);
        if (duration < 0) {
            new IllegalArgumentException("duration cannot be negative");
        }
        this.duration = duration;
    }
    
    public void performAction(LgEventSource source, boolean state) {
        Vector3f newTrans = null;
        if (state) {
            newTrans = onTranslation;
            if (baseTranslation == null) {
                baseTranslation = new Vector3f();
            }
            baseTranslation = target.get().getFinalTranslation(baseTranslation);
        } else {
            if (baseTranslation == null) {
                return;
            }
            newTrans = baseTranslation;
        }
        if (duration == USE_DEFAULT_DURATION) {
            target.get().changeTranslation(newTrans);
        } else {
            target.get().changeTranslation(newTrans, duration);
	}
    }
}
