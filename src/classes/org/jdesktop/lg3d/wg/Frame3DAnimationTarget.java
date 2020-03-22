/**
 * Project Looking Glass
 *
 * $RCSfile: Frame3DAnimationTarget.java,v $
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
 * $Date: 2006-02-04 00:01:38 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

/**
 * Animation Target for the SceneManager animations, only a single
 * SmAnimationTarget can be active at any one time, the system will enforce
 * this policy.
 */
public class Frame3DAnimationTarget extends Component3DAnimationTarget {
    public void setEnabled(boolean enabled) {
        checkLive();
        assert(targetComponent.get() instanceof Frame3D);
        ((Frame3D)targetComponent.get()).setEnabledInternal(enabled);
    }
    
    public boolean isEnabled() {
        checkLive();
        assert(targetComponent.get() instanceof Frame3D);
        return ((Frame3D)targetComponent.get()).isEnabledInternal();
    }
}
