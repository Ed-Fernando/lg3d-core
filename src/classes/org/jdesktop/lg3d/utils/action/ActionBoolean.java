/**
 * Project Looking Glass
 *
 * $RCSfile: ActionBoolean.java,v $
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
 * $Date: 2005-04-14 23:04:24 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.action;

import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 * An Action interface for an action with a boolean argument.
 */
public interface ActionBoolean extends Action {
    public void performAction(LgEventSource source, boolean state);
}


