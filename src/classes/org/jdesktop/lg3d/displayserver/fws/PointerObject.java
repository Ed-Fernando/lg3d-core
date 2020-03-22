/**
 * Project Looking Glass
 *
 * $RCSfile: PointerObject.java,v $
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
 * $Date: 2006-09-14 22:27:57 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws;

import java.util.Iterator;
import java.util.LinkedList;
import javax.media.j3d.Node;
import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;

/**
 * Contains information about the scene graph node that the pointer 
 * is to be sent to. Also contains information derived from the
 * node and the scene graph. Also, for drag events, the results
 * of the most recent pick are stored in intersectedNodeInfo.
 */

public class PointerObject {

    // The scene graph node that events should be sent to

    public Node destNode;

    // The destination node information (e.g. pick results, etc.)

    public MouseEventNodeInfo destNodeInfo;

    // The node information from the most recent pick operation. Only
    // valid for MOUSE_DRAGGED events.

    public MouseEventNodeInfo intersectedNodeInfo;

    // Derived: A linked list which contains all nodes starting 
    // from the root of the screen graph down to, and including, the node.

    public LinkedList<Node> destPathFromRoot = new LinkedList();

    // Calculate information which is derived from the node and the scenegraph.

    public void calcDerivedInfo () {
	resetDerivedInfo();
	if (destNodeInfo == null) return;

	destNode = destNodeInfo.getNode();
	if (destNode == null) return;

	calcPathFromRoot();
    }

    // Copy members (by reference) from another PointerObject

    public void copyFrom (PointerObject po) {
	destNode = po.destNode;
	destNodeInfo = po.destNodeInfo;
	intersectedNodeInfo = po.intersectedNodeInfo;

	destPathFromRoot.clear();
	for (Node n : po.destPathFromRoot) {
	    destPathFromRoot.add(n);
	}
    }

    // Does this pointer object equal a given pointer object?

    public boolean equals (PointerObject po) {
	return destNode == po.destNode;
    }

    // Scan the two paths-from-root of this pointer object and another and
    // return of same objects that appear both of them.  

    protected int countSameObjectsFromRoot (PointerObject po) {

	if (destNode == null || po.destNode == null) return 0;

	Iterator it1 = destPathFromRoot.iterator();
        Iterator it2 = po.destPathFromRoot.iterator();

        int i = 0;
        while (it1.hasNext() && it2.hasNext()) {
    	    Node n1 = (Node) it1.next();
    	    Node n2 = (Node) it2.next();
            if (!n1.equals(n2)) {
                break;
            }
	    i++;
        }
        return i;
    }
    
    public void reset () {
	resetDerivedInfo();
        destNodeInfo = null;
        intersectedNodeInfo = null;
    }

    private void resetDerivedInfo () {
	destNode = null;
	destPathFromRoot.clear();
    }

    private void calcPathFromRoot () {
	if (destNode.getCapability(Node.ENABLE_PICK_REPORTING)) {
	    destPathFromRoot.addFirst(destNode);
	}

	Node n = destNode.getParent();
	while (n != null) {
	    if (n.getCapability(Node.ENABLE_PICK_REPORTING)) {
		destPathFromRoot.addFirst(n);
	    }
	    n = n.getParent();
	}

        //System.err.println("============================================");
        //System.err.println("destPathFromRoot = ");
        //if (destPathFromRoot == null) {
	//    System.err.println("null");
        //} else {
        //    for(int i=0; i< destPathFromRoot.size(); i++) {
        //        System.out.println(destPathFromRoot.get(i));
        //    }
        //System.err.println("============================================");
    }

    private String destPathFromRootToString () {
	String str = "";
	boolean first = true;

	for (Node n : destPathFromRoot) {
	    if (!first) str += ",";
	    str += n;
	    first = false;
	}

	return str;
    }

    public String toString () {
	String str;

	str = "destNode=" + destNode;
	str += ",destNodeInfo=" + destNodeInfo;
	str += ",intersectedNodeInfo=" + intersectedNodeInfo;
	str += ",destPathFromRoot=" + destPathFromRootToString();

	return str;
    }
}
