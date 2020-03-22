/**
 * Project Looking Glass
 *
 * $RCSfile: AddFrame3DMessage.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2005-04-14 23:04:02 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.socketconnector;

import org.jdesktop.lg3d.displayserver.LgNodeManager;
import org.jdesktop.lg3d.displayserver.NodeID;
import org.jdesktop.lg3d.wg.Frame3D;

/**
 * Add a Frame3D to the DisplayServer
 *
 * @author  paulby
 */
public class AddFrame3DMessage extends Message {
    
    private NodeID nodeID;
    
    /** Creates a new instance of AddFrame3DMessage */
    public AddFrame3DMessage(Frame3D frame3d) {
        // nodeID = frame3d.getNodeID();
        nodeID = LgNodeManager.c3dAccessHelper.getNodeID(frame3d);
    }
    
    /**
     * Returns the NodeID of the Frame3D
     */
    public NodeID getNodeID() {
        return nodeID;
    }
    
    public String toString() {
        return "Adding Frame "+nodeID;
    }
    
}
