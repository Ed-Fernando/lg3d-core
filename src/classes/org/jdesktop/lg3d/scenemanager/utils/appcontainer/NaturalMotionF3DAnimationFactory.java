/**
 * Project Looking Glass
 *
 * $RCSfile: NaturalMotionF3DAnimationFactory.java,v $
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

import org.jdesktop.lg3d.utils.c3danimation.Component3DAnimationFactory;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.wg.Component3DAnimation;


/**
 */
public class NaturalMotionF3DAnimationFactory implements Component3DAnimationFactory {
    private int defaultDuration;
    
    public NaturalMotionF3DAnimationFactory(int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }
    
    /**
     *
     */
    public Component3DAnimation createInstance() {
        return 
            new PluggableF3DAnimation(
                new NaturalMotionAnimation(defaultDuration));
    }
}
