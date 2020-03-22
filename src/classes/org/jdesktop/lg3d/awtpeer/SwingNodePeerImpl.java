/**
 * Project Looking Glass
 *
 * $RCSfile: SwingNodePeerImpl.java,v $
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
 * $Date: 2006-08-07 19:02:50 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.awtpeer;

import org.jdesktop.lg3d.awt.Lg3dBackBuffer;
import org.jdesktop.lg3d.awt.PeerInterface;
import org.jdesktop.lg3d.wg.internal.swingnode.SwingNodeJFrame;

/**
 * AWT peer for SwingNodes
 *
 * This is a special case because the peer does not actually render anything but 
 * acts as a proxy to vector the Panels image to the SwingNodeRenderer
 */
public class SwingNodePeerImpl extends PeerBase implements PeerInterface {
    
    public SwingNodePeerImpl() {
        setThumbnail(null);     
    }
    
    public void setEnabled(boolean enabled) {
        // Never enable this Frame3D
    }
    
    public void setVisible(boolean visible) {
        // Never make this Frame3D visible
    }
    
    public float getDecorationWidth() {
        return 0f;
    }
    
    public void createControlButtons() {
        // No buttons
    }

    void setSize(float width, float height) {
        //super.setSize(width,height);
    }

    public void renderBuffer(Lg3dBackBuffer buffer) {
        super.renderBuffer(buffer);
        if (swingTexture!=null)
            ((SwingNodeJFrame)component).notifyTextureChangedListeners(swingTexture);
    }
}


