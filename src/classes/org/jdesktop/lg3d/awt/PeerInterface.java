/**
 * Project Looking Glass
 *
 * $RCSfile: PeerInterface.java,v $
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
 * $Date: 2006-06-30 20:37:58 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.awt.Component;

/**
 *
 * @author paulby
 */
public interface PeerInterface extends Lg3dBackBuffer.BufferFlipListener, Lg3dBackBuffer.BufferResizeListener{
    public void setEnabled(boolean enabled);
    
    /**
     * Set the visibility of the Peer
     */
    public void setVisible(boolean visible);
    
    public void setComponent(Component component);

    /** 
     * Set location of peer on screen in swing coordinates
     */
    public void setLocation(int x, int y);
    
    public void setTitle(String title);
    
    public void addSceneManagerListener(SceneManagerListener listener);
}
