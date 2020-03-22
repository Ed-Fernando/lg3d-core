
/**
 * Project Looking Glass
 *
 * $RCSfile: MouseEventNodeInfo.java,v $
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
 * $Date: 2006-09-26 23:13:39 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws;

import java.lang.ref.WeakReference;
import javax.media.j3d.Node;
import javax.vecmath.Point3f;

/**
 * Instances of MouseEventNodeInfo are objects which communicate
 * information about the destination nodes for an event (determined
 * either through picking or grabbing) and other information related 
 * to the destination nodes.
 */

public class MouseEventNodeInfo {

    private WeakReference<Node>[] nodes;
    private Point3f[] pointsLocal;
    private Point3f[] pointsVW;
    private float[]   eyeDistances;
    private boolean   fromGrab;

    /**
     * Creates a new instance of MouseEventNodeInfo. All of the array arguments must
     * have the same length.
     *
     * @param nodes the destination nodes for the event.
     * This may be determined either by picking or by a grab.
     * @param points the intersection points of the destination nodes, in local node coordinates.
     * If a destination nodes resulted from a grab, the values of these points are (0, 0, 0). If they resulted
     * from picking, the values of the point for each node is the intersection point closest to the eye 
     * (in local node coordinates) for that node.
     * @param pointVW the intersection points of the destination nodes, in virtual 
     * world coordinates. If the nodes resulted from a grab, the values of these points are (0, 0, 0). 
     * If they resulted from picking, the value of these points for each node is the intersection point 
     * closest to the eye (in virtual world coordinates) for that node.
     * @param eyeDistances the array of distances to the eye position. If the node resulted from
     * a grab, the value is always 0. If the node resulted from a pick, the value is the distance
     * from the node's intersection point to the eye position.
     * @param fromGrab true if the destination nodes resulted from a grab, false if it
     * resulted from picking.
     */
    public MouseEventNodeInfo (Node[] nodes, Point3f[] pointsLocal, Point3f[] pointsVW,
			       float[] eyeDistances, boolean fromGrab) {
	if (nodes.length != pointsLocal.length ||
	    nodes.length != pointsVW.length ||
	    nodes.length != eyeDistances.length) {
	    throw new RuntimeException("MouseEventNodeInfo(): array lengths are not the same");
	}
	this.nodes = new WeakReference[nodes.length];
        for(int i=0; i<nodes.length; i++)
            this.nodes[i] = new WeakReference(nodes[i]);
	this.pointsLocal = pointsLocal;
	this.pointsVW = pointsVW;
	this.eyeDistances = eyeDistances;
	this.fromGrab = fromGrab;
    }

    /**
     * Returns the number of nodes
     */
    public int getNumNodes () {
	return nodes.length;
    }

    /**
     * Returns the index'th destination node.
     */
    public Node getNode (int index) {
	if (index < nodes.length) {
	    return nodes[index].get();
	}
	return null;
    }

    /**
     * Returns the first destination node.
     */
    public Node getNode () { 
	return getNode(0);
    }

    /**
     * Returns the index'th destination node intersection point in local node coordinates.
     */
    public Point3f getPointLocal (int index) {
	if (index < pointsLocal.length) {
	    return pointsLocal[index];
	}
	return null;
    }

    /**
     * Returns the first destination node intersection point in local node coordinates.
     */
    public Point3f getPointLocal () {
	return getPointLocal(0);
    }

    /**
     * Returns the index'th destination node intersection point in virtual world coordinates.
     */
    public Point3f getPointVW (int index) {
	if (index < pointsVW.length) {
	    return pointsVW[index];
	}
	return null;
    }

    /**
     * Returns the first destination node intersection point in virtual world coordinates.
     */
    public Point3f getPointVW () {
	return getPointVW(0);
    }

    /**
     * Returns the index'th eye point distance.
     */
    public float getEyeDistance (int index) {
	if (index < eyeDistances.length) {
	    return eyeDistances[index];
	}
	return 0.0f;
    }

    /**
     * Returns the first eye point distance
     */
    public float getEyeDistance () {
	return getEyeDistance(0);
    }

    /**
     * Returns whether the destination nodes resulted from grabbing (true) or
     * picking (false).
     */
    public boolean fromGrab () {
        return fromGrab;
    }

    /**
     * Returns a string representation of the object.
     */
    public String toString () {
	String str;

	str = "[";

	str += "nodes=[";
	for (int i = 0; i < nodes.length; i++) {
            str += nodes[i];
	}
	str += "]";

	str += ",pointsLocal=[";
	for (int i = 0; i < pointsLocal.length; i++) {
            str += pointsLocal[i];
	}
	str += "]";

	str += ",pointsVW=[";
	for (int i = 0; i < pointsVW.length; i++) {
            str += pointsVW[i];
	}
	str += "]";

	str += ",eyeDistances=[";
	for (int i = 0; i < eyeDistances.length; i++) {
            str += eyeDistances[i];
	}
	str += "]";

	str += ",fromGrab=" + fromGrab;

	str += "]";

	return str;
    }
}
