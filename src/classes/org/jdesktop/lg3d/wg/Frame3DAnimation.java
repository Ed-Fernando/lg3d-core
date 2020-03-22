/*
 * Project Looking Glass
 *
 * $RCSfile: Frame3DAnimation.java,v $
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
 * $Date: 2005-04-14 23:05:01 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

/**
 * Animations for Frame3D
 *
 * @author paulby
 */
public abstract class Frame3DAnimation extends Component3DAnimation {
    
    /**
     * Enable/disable the app, synonymous with starting/closing an app
     */
    public abstract void changeEnabled(boolean enabled, int duration);
    
    /**
     * Get the enabled status
     */
    public abstract boolean isEnabled();
    
    /**
     * Get the enabled status
     */
    public abstract boolean isFinalEnabled();
    
    /**
     * Returnt the default duration for the enabled animation
     */
    public abstract int getDefaultEnabledDuration();
}
