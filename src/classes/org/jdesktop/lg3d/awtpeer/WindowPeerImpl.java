/**
 * Project Looking Glass
 *
 * $RCSfile: WindowPeerImpl.java,v $
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
 * $Date: 2006-06-30 20:38:49 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.awtpeer;

import java.awt.Component;
import org.jdesktop.lg3d.awt.PeerInterface;
import javax.swing.JWindow;


public class WindowPeerImpl extends BasicPeer implements PeerInterface {
    
    public void setComponent(Component component) {
        super.setComponent(component);
        logger.fine("Window created "+component);
        if (component instanceof JWindow)
            logger.fine("Owner "+((JWindow)component).getOwner());
    }
    
    public float getDecorationWidth() {
        return 0f;
    }
    
    public void createControlButtons() {
        // No buttons
    }

    void setSize(float width, float height) {
        super.setSize(width,height);
    }

    boolean hasThumbnail() {
        return false;
    }

}


