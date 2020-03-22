/**
 * Project Looking Glass
 *
 * $RCSfile: SceneGraphPathWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:49 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

import  javax.vecmath.Point3d;
import  javax.vecmath.Point4d;

/**
 * A SceneGraphPath object represents the path from a Locale to a
 * terminal node in the scene graph.  This path consists of a Locale, a
 * terminal node, and an array of internal nodes that are in the path
 * from the Locale to the terminal node.  The terminal node may be
 * either a Leaf node or a Group node.  A valid SceneGraphPath must
 * uniquely identify a specific instance of the terminal node.  For
 * nodes that are not under a SharedGroup, the minimal SceneGraphPath
 * consists of the Locale and the terminal node itself.  For nodes that
 * are under a SharedGroup, the minimal SceneGraphPath consists of the
 * Locale, the terminal node, and a list of all Link nodes in the path
 * from the Locale to the terminal node.  A SceneGraphPath may optionally
 * contain other interior nodes that are in the path.
 * A SceneGraphPath is verified for correctness and uniqueness when
 * it is sent as an argument to other methods of Java 3D.
 * <p>
 * In the array of internal nodes, the node at index 0 is the node
 * closest to the Locale.  The indices increase along the path to the
 * terminal node, with the node at index length-1 being the node closest
 * to the terminal node.  The array of nodes does not contain either the
 * Locale (which is not a node) or the terminal node.
 * <p>
 * When a SceneGraphPath is returned from the picking or collision
 * methods of Java 3D, it will also contain the value of the
 * LocalToVworld transform of the terminal node that was in effect at
 * the time the pick or collision occurred.
 * Note that ENABLE_PICK_REPORTING and ENABLE_COLLISION_REPORTING are
 * disabled by default.  This means that the picking and collision
 * methods will return the minimal SceneGraphPath by default.
 *
 * @see Node#ENABLE_PICK_REPORTING
 * @see Node#ENABLE_COLLISION_REPORTING
 * @see BranchGroup#pickAll
 * @see BranchGroup#pickAllSorted
 * @see BranchGroup#pickClosest
 * @see BranchGroup#pickAny
 */

public interface SceneGraphPathWrapper {

    /**
     * Sets this path's values to that of the specified path.
     * @param newPath the SceneGraphPath to copy
     */
    public void set(SceneGraphPathWrapper newPath) ;

    /**
     * Sets this path's Locale to the specified Locale.
     * @param newLocale The new Locale
     */
    //public final void setLocale(Locale newLocale) ;

    /**
     * Sets this path's terminal node to the specified node object.
     * @param object the new terminal node
     */
    public void setObject(NodeWrapper  object) ;

    /**
     * Sets this path's node objects to the specified node objects.
     * @param nodes an array of node objects in the path from
     * the Locale to the terminal node
     */
    public void setNodes(NodeWrapper nodes[]) ;

    /**
     * Replaces the node at the specified index with newNode.
     * @param index the index of the node to replace
     * @param newNode the new node
     * @exception NullPointerException if the node array pointer is null.
     *
     */
    public void setNode(int index, NodeWrapper newNode) ;

    /**
     * Sets the transform component of this SceneGraphPath to the value of
     * the passed transform.
     * @param trans the transform to be copied. trans should be the 
     * localToVworld matrix of this SceneGraphPath object.
     */
     public void setTransform(Transform3DWrapper trans) ;

    /**
      *  Returns a copy of the transform associated with this SceneGraphPath;
      *  returns null if there is no transform associated.
      *  If this SceneGraphPath was returned by a Java 3D picking or
      *  collision method, the local coordinate to virtual world
      *  coordinate transform for this scene graph object at the
      *  time of the pick or collision is recorded.
      *  @return the local to VWorld transform
      */ 
    public Transform3DWrapper getTransform() ;

    /**
     * Retrieves the path's Locale
     * @return this path's Locale
     */
    //public final Locale getLocale() ;

    /**
     * Retrieves the path's terminal node object.
     * @return the terminal node
     */
    public NodeWrapper getObject() ;

    /**
     * Retrieves the number of nodes in this path.  The number of nodes
     * does not include the Locale or the terminal node object itself.
     * @return a count of the number of nodes in this path
     */
    public int nodeCount() ;

    /**
     * Retrieves the node at the specified index.
     * @param index the index specifying which node to retrieve
     * @return the specified node
     */
    public NodeWrapper getNode(int index) ;

   /**
     * Determines whether two SceneGraphPath objects represent the same
     * path in the scene graph; either object might include a different 
     * subset of internal nodes; only the internal link nodes, the Locale, 
     * and the Node itself are compared.  The paths are not validated for
     * correctness or uniqueness.
     * @param path  the SceneGraphPath to be compared to this SceneGraphPath
     * @return true or false
     */
    public boolean isSamePath(SceneGraphPathWrapper testPath) ;


}
