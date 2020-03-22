/**
 * Project Looking Glass
 *
 * $RCSfile: NodeID.java,v $
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
 * $Date: 2004-09-10 23:29:41 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

/**
 * An id object for uniquely identifying Frame3Ds. Implements Serializable so
 * that it can be sent via an Object*putStream for client/server messaging
 *
 * @author  paulby
 */
public class NodeID implements java.io.Serializable {
    
    /** constant: definition of an undefined node */
    public static final long UNDEFINED_NODE_ID = Long.MIN_VALUE;
    
    /** an index of the next available unique id */
    private static long nextID = 1L;
    /** the unique id of this node */
    private long nodeID = UNDEFINED_NODE_ID;
    /** the id of the client */
    private int clientID;

    /**
     * Create a unique NodeID for the given client
     * @param clientID the id of the client
     */
    NodeID( int clientID ) {
        assert( clientID!=Integer.MIN_VALUE );
        
        this.clientID = clientID;
        nodeID = nextID++;
    }
    
    /**
     * get the client ID of this NodeID
     * @return the clientID
     */
    int getClientID() {
        return clientID;
    }
    
    /**
     * get the unique node ID of this NodeID
     * @return the nodeID
     */
    long getNodeID() {
        return nodeID;
    }
    
    /**
     * see if this NodeId equals the given object
     * @param obj the object to compare to
     * @return true - the two are identical
     *         false - obj is not a NodeId, or not identical
     */
    public boolean equals( Object obj ) {
        if (!(obj instanceof NodeID))
            return false;
        
        if (((NodeID)obj).getClientID()==clientID &&
            ((NodeID)obj).getNodeID()==nodeID)
            return true;
        
        return false;
    }
    
    /**
     * get the hashCode for this object
     * @return the hashCode for this object
     */
    public int hashCode() {
        return ((int)nodeID)+clientID;
    }

    /**
     * get the stringified version of this object
     * @return the stringified version of this object
     */
    public String toString() {
        return new String(clientID+":"+nodeID);
    }
}
