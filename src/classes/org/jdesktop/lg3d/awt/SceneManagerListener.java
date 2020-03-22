/**
 * Project Looking Glass
 *
 * $RCSfile: SceneManagerListener.java,v $
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

/**
 *
 * @author paulby
 */
public interface SceneManagerListener {
    
    /**
     * Called when the scene manager moves the peer
     */
    public void windowMoved(int x, int y);
}
