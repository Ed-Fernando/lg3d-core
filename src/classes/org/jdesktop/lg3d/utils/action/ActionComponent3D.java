/**
 * Project Looking Glass
 *
 * $RCSfile: ActionComponent3D.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2006-02-27 19:59:57 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.action;

import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.Component3D;

/**
 * An Action interface for an action with a boolean argument.
 */
public interface ActionComponent3D extends Action {
    public void performAction(LgEventSource source, Component3D comp);
}


