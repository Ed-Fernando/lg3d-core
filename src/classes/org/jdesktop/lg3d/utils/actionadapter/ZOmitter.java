/**
 * Project Looking Glass
 *
 * $RCSfile: ZOmitter.java,v $
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
 * $Date: 2005-04-14 23:04:34 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.actionadapter;

import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.utils.action.ActionFloat2;
import org.jdesktop.lg3d.utils.action.ActionFloat3;

/**
 * An action adapter that converts ActionFloat3 to ActionFloat2
 * by omitting the third argument.
 */
public class ZOmitter implements ActionFloat3 {
    private ActionFloat2 action;

    public ZOmitter(ActionFloat2 action) {
        this.action = action;
    }
    
    public void performAction(LgEventSource source, float x, float y, float z) {
        action.performAction(source, x, y);
    }
}
