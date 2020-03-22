/**
 * Project Looking Glass
 *
 * $RCSfile: HeavyWeightWindowPeerImpl.java,v $
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

import java.awt.Component;
import org.jdesktop.lg3d.awt.PeerInterface;
import javax.swing.JWindow;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.wg.Frame3D;


public class HeavyWeightWindowPeerImpl extends BasicPeerC3D implements PeerInterface {
    
    static Frame3D frame3d = new Frame3D();

    static {
	frame3d.setThumbnail(null);
	frame3d.setPreferredSize(new Vector3f(0.0f, 0.0f, 0.0f));
	frame3d.changeEnabled(true);
	frame3d.changeVisible(true);
    }

    public HeavyWeightWindowPeerImpl() {
	super();
	frame3d.addChild(this);
    }

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

    public void setEnabled(boolean enabled) {
	// frame3d.setEnabled(enabled);
    }
}
