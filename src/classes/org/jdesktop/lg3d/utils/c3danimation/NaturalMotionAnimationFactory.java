/**
 * Project Looking Glass
 *
 * $RCSfile: NaturalMotionAnimationFactory.java,v $
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
 * $Date: 2005-04-14 23:04:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.c3danimation;

import org.jdesktop.lg3d.wg.Component3DAnimation;


/**
 */
public class NaturalMotionAnimationFactory implements Component3DAnimationFactory {
    private int defaultDuration;
    
    public NaturalMotionAnimationFactory(int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }
    
    /**
     *
     */
    public Component3DAnimation createInstance() {
        return new NaturalMotionAnimation(defaultDuration);
    }
}
