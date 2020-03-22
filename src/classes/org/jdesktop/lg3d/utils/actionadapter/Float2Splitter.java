/**
 * Project Looking Glass
 *
 * $RCSfile: Float2Splitter.java,v $
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
 * $Date: 2005-04-14 23:04:33 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.actionadapter;

import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.utils.action.ActionFloat2;
import org.jdesktop.lg3d.utils.action.ActionFloat;

/**
 * An action adapter that converts ActionFloat2 into two
 * ActionFloat actions by invoking each of them with one of
 * the two original argument for ActionFloat2.
 */
public class Float2Splitter implements ActionFloat2 {
    private ActionFloat actionX;
    private ActionFloat actionY;

    public Float2Splitter(ActionFloat actionX, ActionFloat actionY) {
        this.actionX = actionX;
        this.actionY = actionY;
    }
    
    public void performAction(LgEventSource source, float x, float y) {
        if (actionX != null) {
            actionX.performAction(source, x);
        }
        if (actionY != null) {
            actionY.performAction(source, y);
        }
    }
}
