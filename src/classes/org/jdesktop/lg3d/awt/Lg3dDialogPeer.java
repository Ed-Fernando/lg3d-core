/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dDialogPeer.java,v $
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
 * $Date: 2006-06-30 20:37:52 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;
import java.awt.Toolkit;



/**
 *
 * @author paulby
 */
public class Lg3dDialogPeer extends Lg3dWindowPeer implements java.awt.peer.DialogPeer {

    /** Creates a new instance of Lg3dFramePeer */
    public Lg3dDialogPeer(java.awt.peer.FramePeer realPeer, java.awt.Component frame, Toolkit osToolkit) {
        super(realPeer, frame, osToolkit);    
    }

    public void setTitle(String title) {
        logger.warning("setTitle not implemented");
    }
    
    public void setResizable(boolean resizeable) {
        logger.warning("setResizable not implemented");
    }
}
