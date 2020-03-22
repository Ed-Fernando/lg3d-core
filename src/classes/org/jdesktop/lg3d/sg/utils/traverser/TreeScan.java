/**
 * Project Looking Glass
 *
 * $RCSfile: TreeScan.java,v $
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
 * $Date: 2005-04-14 23:04:24 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.utils.traverser;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.BitSet;

import org.jdesktop.lg3d.sg.Group;
import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.sg.Switch;

public class TreeScan extends Object {
    
    private static HashSet visitedSharedGroups = null;
    
    
    /** Traverse the SceneGraph starting at node treeRoot. Every time a node of
     * class nodeClass is found call processNode method in processor.
     * @param treeRoot The root of the SceneGraph to search
     * @param nodeClass The class of the node(s) to search for
     * @param processor The class containing the processNode method which will be
     * called every time the correct nodeClass is found in the Scene Graph.
     * @param onlyEnabledSwitchChildren when true only recurse into Switch
     * children which are enabled
     * @param sharedGroupsOnce when true only process SharedGroups once,
     * regardless how many Links refer to them
     * @throws CapabilityNotSetException If the node is live or compiled and the scene graph
     * contains groups without ALLOW_CHILDREN_READ capability
     */
    public static void findNode(Node treeRoot,Class nodeClass,ProcessNodeInterface processor,boolean onlyEnabledSwitchChildren,boolean sharedGroupsOnce) throws javax.media.j3d.CapabilityNotSetException {
        
        Class[] nodeClasses = new Class[]{ nodeClass };
        
        findNode( treeRoot, nodeClasses, processor,
                onlyEnabledSwitchChildren,
                sharedGroupsOnce );
        
    }
    
    /** Traverse the SceneGraph starting at node treeRoot. Every time a node of
     * class nodeClass is found call processNode method in processor.
     * @param treeRoot The root of the SceneGraph to search
     * @param nodeClasses The list of classes of the node(s) to search for
     * @param processor The class containing the processNode method which will be
     * called every time the correct nodeClass is found in the Scene Graph.
     * @param onlyEnabledSwitchChildren when true only recurse into Switch
     * children which are enabled
     * @param sharedGroupsOnce when true only process SharedGroups once,
     * regardless how many Links refer to them
     * @throws CapabilityNotSetException If the node is live or compiled and the scene graph
     * contains groups without ALLOW_CHILDREN_READ capability
     */
    public static void findNode( Node treeRoot,
            Class[] nodeClasses,
            ProcessNodeInterface processor,
            boolean onlyEnabledSwitchChildren,
            boolean sharedGroupsOnce ) throws
            javax.media.j3d.CapabilityNotSetException {
        if (sharedGroupsOnce)
            if (visitedSharedGroups==null)
                visitedSharedGroups = new HashSet();
        
        actualFindNode( treeRoot, nodeClasses, processor,
                onlyEnabledSwitchChildren,
                sharedGroupsOnce );
        
        if (sharedGroupsOnce)
            visitedSharedGroups.clear();
    }
    
    /**
     * Conveniance method to return a Class given the full Class name
     * without throwing ClassNotFoundException
     *
     * If the class is not available an error message is displayed and a
     * runtime exception thrown
     */
    public static Class getClass( String str ) {
        try {
            return Class.forName( str );
        } catch( ClassNotFoundException e ) {
            e.printStackTrace();
            throw new RuntimeException( "BAD CLASS "+str );
        }
    }
    
    private static void actualFindNode( Node treeRoot,
            Class[] nodeClasses,
            ProcessNodeInterface processor,
            boolean onlyEnabledSwitchChildren,
            boolean sharedGroupsOnce ) throws
            javax.media.j3d.CapabilityNotSetException {
        boolean doChildren = true;
        
        if (treeRoot == null)
            return;
        
        //System.out.print( treeRoot.getClass().getName()+"  ");
        //System.out.print( nodeClasses[0].getName()+"  ");
        //System.out.println( nodeClasses[0].isAssignableFrom( treeRoot.getClass() ));
        for(int i=0; i<nodeClasses.length; i++)
            if (nodeClasses[i].isAssignableFrom( treeRoot.getClass() )) {
            doChildren = processor.processNode( treeRoot );
            i = nodeClasses.length;
        }
        
        if (!doChildren)
            return;
        
        if (onlyEnabledSwitchChildren && treeRoot instanceof Switch ) {
            int whichChild = ((Switch)treeRoot).getWhichChild();
            
            if (whichChild==Switch.CHILD_ALL) {
                Enumeration e = ((Group)treeRoot).getAllChildren();
                while( e.hasMoreElements() )
                    actualFindNode( (Node)e.nextElement(), nodeClasses, processor,
                            onlyEnabledSwitchChildren, sharedGroupsOnce  );
            } else if (whichChild==Switch.CHILD_MASK) {
                BitSet set = ((Switch)treeRoot).getChildMask();
                for(int s=0; s<set.length(); s++) {
                    if (set.get(s))
                        actualFindNode( ((Switch)treeRoot).getChild(s), nodeClasses,
                                processor, onlyEnabledSwitchChildren, sharedGroupsOnce );
                }
            } else if (whichChild==Switch.CHILD_NONE) {
                // DO nothing
            } else
                actualFindNode( ((Switch)treeRoot).currentChild(), nodeClasses,
                        processor, onlyEnabledSwitchChildren, sharedGroupsOnce );
        } else if (treeRoot instanceof Group) {
            Enumeration e = ((Group)treeRoot).getAllChildren();
            while( e!=null && e.hasMoreElements() )
                actualFindNode( (Node)e.nextElement(), nodeClasses, processor,
                        onlyEnabledSwitchChildren, sharedGroupsOnce  );
        }
    }
}
