/*
 * Project Looking Glass
 *
 * $RCSfile: ChangeEnabledAnimationPlugin.java,v $
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
 * $Date: 2005-04-14 23:04:07 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.appcontainer;


/**
 *
 */
public interface ChangeEnabledAnimationPlugin {
    public void initialize(PluggableF3DAnimation anim);
    public void changeEnabled(boolean enabled, int duration);
    public int getDefaultEnabledDuration();
    public void destroy();
}
