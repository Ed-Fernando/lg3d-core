/**
 * Project Looking Glass
 *
 * $RCSfile: Frame3DAddedEvent.java,v $
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
 * $Date: 2005-08-26 18:49:37 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.event;

import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.event.LgEvent;

public class Frame3DAddedEvent extends LgEvent {    
    private Frame3D frame;
    
    public Frame3DAddedEvent(Frame3D frame) {
        this.frame = frame;
    }
    
    public Frame3D getFrame3D() {
        return frame;
    }
}
