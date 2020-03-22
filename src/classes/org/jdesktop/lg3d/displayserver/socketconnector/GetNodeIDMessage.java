/**
 * Project Looking Glass
 *
 * $RCSfile: GetNodeIDMessage.java,v $
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
 * $Date: 2004-06-23 18:50:13 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.socketconnector;

import org.jdesktop.lg3d.displayserver.NodeID;

/**
 *
 * @author  paulby
 */
public class GetNodeIDMessage extends Message {
    
    /** Creates a new instance of GetNodeIDMessage */
    public GetNodeIDMessage() {
    }
    
    public GetNodeIDMessage.GotNodeIDMessage createResult( NodeID nodeID ) {
        return new GetNodeIDMessage.GotNodeIDMessage(nodeID);
    }
    
    class GotNodeIDMessage extends Message {
        private NodeID nodeID;
        
        public GotNodeIDMessage( NodeID nodeID ) {
            this.nodeID = nodeID;
        }
        
        public NodeID getNodeID() {
            return nodeID;
        }
    }
    
}
