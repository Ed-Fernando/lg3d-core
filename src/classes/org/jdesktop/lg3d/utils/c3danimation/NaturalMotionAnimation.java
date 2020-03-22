/*
 * Project Looking Glass
 *
 * $RCSfile: NaturalMotionAnimation.java,v $
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

import org.jdesktop.lg3d.utils.smoother.NaturalFloatSmoother;
import org.jdesktop.lg3d.utils.smoother.NaturalVector3fSmoother;


/**
 *
 */
public class NaturalMotionAnimation extends PluggableC3DAnimation {
    public NaturalMotionAnimation(int defaultDuration) {
        super(
            defaultDuration,
            new NaturalVector3fSmoother(),
            new NaturalFloatSmoother(),
            new NaturalFloatSmoother(),
            new NaturalFloatSmoother());
    }
    
    public NaturalMotionAnimation(int defaultDuration, 
        ChangeVisibleAnimationPlugin cvaPlugin) 
    {
        super(
            defaultDuration,
            new NaturalVector3fSmoother(),
            new NaturalFloatSmoother(),
            new NaturalFloatSmoother(),
            new NaturalFloatSmoother(),
            cvaPlugin);
    }
}
