/**
 * Project Looking Glass
 *
 * $RCSfile: LgNodeManager.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2006-09-26 18:11:55 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.logging.Logger;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.wg.Component3D;

/**
 * Manage LG branch groups (nodes)
 * @author  paulby
 */
public class LgNodeManager {
    /** the logger instance */
    protected static final Logger logger = Logger.getLogger("lg.displayserver");
    
    /** the hash of the stored nodes */
    private HashMap<NodeID, ComponentRef> store;
    /** the LgNodeManager singleton */
    private static LgNodeManager nodeManager = null;
    
    private ReferenceQueue refQueue = new ReferenceQueue();
    
    /** Creates a new instance of LgNodeManager */
    LgNodeManager() {
        store = new HashMap();
        
        // Cleanup the store as the WeakReferences to the nodes
        // are reapped
        Thread refCleanup = new Thread(AppConnectorPrivate.getThreadGroup(), "RefCleanup") {
            public void run() {
                while(true) {
                    ComponentRef c;
                    try {
                        c = (ComponentRef) refQueue.remove();
                        store.remove(c.getNodeID());
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        refCleanup.start();
    }
    /**
     * Get the LgNodeManager singleton, creating a new one if needed
     * @return the LgNodeManager singleton
     */
    public static LgNodeManager getLgNodeManager() {
        if (nodeManager==null)
            nodeManager = new LgNodeManager();
        return nodeManager;
    }
    
    /**
     * Add a node to the manager
     * @param node the node to add
     * @see #getNode
     */
    public void addNode(Component3D node) {
        // NodeID nodeID = node.getNodeID();
        NodeID nodeID = c3dAccessHelper.getNodeID(node);
        //logger.finer("NodeManager adding " + nodeID + " " + node.getName() + "  " + node);
        Object exists = store.put(nodeID, new ComponentRef(nodeID, node));
        if (exists != null) {
            throw new RuntimeException("ERROR - Illegal change of NodeID");
        }
    }
    
    /**
     * Get a node from the node manager by its unique node id
     * @param nodeID the unique node id for the node
     * @return the node
     */
    public Component3D getNode( NodeID nodeID ) {
        return store.get(nodeID).get();
    }
    
    
    /*
     * The following is a kludge to let the LgNodeManager invoke 
     * Component3D's private methods.
     */
    public interface Component3DAccessHelper {
//        public void createWrapped(Component3D comp);
        public NodeID getNodeID(Component3D comp);
        public void setNodeID(Component3D comp, NodeID nodeID);
        public void setCapabilities(Component3D comp);
        public void setTransform(Component3D comp, Transform3D transform);
    }
    
    public static Component3DAccessHelper c3dAccessHelper;
    
    public static void setComponent3DAccessHelper(Component3DAccessHelper helper) {
        c3dAccessHelper = helper;
    }
    
    class ComponentRef extends WeakReference<Component3D> {
        private NodeID nodeID;
        
        public ComponentRef(NodeID id, Component3D comp) {
            super(comp, refQueue);
            nodeID = id;
        }
        
        public Component3D get() {
            return super.get();
        }
        
        public NodeID getNodeID() {
            return nodeID;
        }
    }
}
