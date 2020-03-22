/**
 * Project Looking Glass
 *
 * $RCSfile: NodeComponent.java,v $
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
 * $Date: 2004-06-23 18:50:55 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;
import java.util.Hashtable;

import org.jdesktop.lg3d.sg.internal.wrapper.NodeComponentWrapper;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.NodeComponentRemote;

/**
 * NodeComponent is a common superclass for all scene graph node
 * component objects such as: Geometry, Appearance, Material, Texture, etc.
 */
public abstract class NodeComponent extends SceneGraphObject implements NodeComponentWrapper {

  // This is use for cloneTree only, set to false after the operation
    //boolean forceDuplicate = false; 
    /**
     * Constructs a NodeComponent object with default parameters.
     * The default values are as follows:
     * <ul>
     * duplicate on clone tree : false<br>
     * </ul>
     */
    public NodeComponent() {
    }

  /**
   * Sets this node's duplicateOnCloneTree value.  The
   * <i>duplicateOnCloneTree</i> value is used to determine if NodeComponent
   * objects are to be duplicated or referenced during a
   * <code>cloneTree</code> operation. A value of <code>true</code> means
   *  that this NodeComponent object should be duplicated, while a value
   *  of <code>false</code> indicates that this NodeComponent object's
   *  reference will be copied into the newly cloned object.  This value
   *  can be overriden via the <code>forceDuplicate</code> parameter of
   *  the <code>cloneTree</code> method.
   * @param duplicate the value to set.
   * @see Node#cloneTree
   */
//    public void setDuplicateOnCloneTree(boolean duplicate) {
//        try {
//            ((NodeComponentRemote)remote).setDuplicateOnCloneTree(duplicate);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException( rex );
//        }
//    }

  /**
   * Returns this node's duplicateOnCloneTree value. The
   * <i>duplicateOnCloneTree</i> value is used to determine if NodeComponent
   * objects are to be duplicated or referenced during a
   * <code>cloneTree</code> operation. A value of <code>true</code> means
   *  that this NodeComponent object should be duplicated, while a value
   *  of <code>false</code> indicates that this NodeComponent object's
   *  reference will be copied into the newly cloned object.  This value
   *  can be overriden via the <code>forceDuplicate</code> parameter of
   *  the <code>cloneTree</code> method.
   * @return the value of this node's duplicateOnCloneTree
   * @see Node#cloneTree
   */
//    public boolean getDuplicateOnCloneTree() {
//	return ((javax.media.j3d.NodeComponent)wrapped).getDuplicateOnCloneTree();
//    }

  /**
   * @deprecated As of Java 3D version 1.2, replaced by
   * <code>cloneNodeComponent(boolean forceDuplicate)</code>
   */
//    public NodeComponent cloneNodeComponent() {
//	throw new RuntimeException(J3dI18N.getString("NodeComponent0"));
//    }


  /**
   * Copies all node information from <code>originalNodeComponent</code>
   * into the current node.  This method is called from the
   * <code>cloneNodeComponent</code> method which is, in turn, called
   * by the <code>cloneNode</code> method.
   * <br>
   * NOTE: Applications should <i>not</i> call this method directly.
   * It should only be called by the cloneNode method.
   *
   * @param originalNodeComponent the node to duplicate.
   * @param forceDuplicate when set to <code>true</code>, causes the
   *  <code>duplicateOnCloneTree</code> flag to be ignored.  When
   *  <code>false</code>, the value of each node's
   *  <code>duplicateOnCloneTree</code> variable determines whether
   *  NodeComponent data is duplicated or copied.
   *
   * @exception RestrictedAccessException if forceDuplicate is set and
   *  this object is part of a compiled scenegraph
   *
   * @see NodeComponent#cloneNodeComponent
   * @see Node#cloneNode
   * @see Node#cloneTree
   *
   * @since Java 3D 1.2
   */
//    public void duplicateNodeComponent(NodeComponent originalNodeComponent,
//				       boolean forceDuplicate) {
//        ((javax.media.j3d.NodeComponent)wrapped).duplicateNodeComponent( 
//                                            (javax.media.j3d.NodeComponent)originalNodeComponent.wrapped, 
//                                            forceDuplicate );
//    }

  /**
   * Used to create a new instance of a NodeComponent object.  This
   * routine is called  by <code>cloneNode</code> to duplicate the 
   * current node. <br>
   * 
   * <code>cloneNodeComponent</code> should be overridden by any user
   * subclassed <i>NodeComponent</i> objects. All subclasses must have their
   * <code>cloneNodeComponent</code>
   * method consist of the following lines:
   * <P><blockquote><pre>
   *     public NodeComponent cloneNodeComponent(boolean forceDuplicate) {
   *         UserNodeComponent unc = new UserNodeComponent();
   *         unc.duplicateNodeComponent(this, forceDuplicate);
   *         return unc;
   *     }
   * </pre></blockquote>
   *
   * @param forceDuplicate when set to <code>true</code>, causes the
   *  <code>duplicateOnCloneTree</code> flag to be ignored.  When
   *  <code>false</code>, the value of each node's
   *  <code>duplicateOnCloneTree</code> variable determines whether
   *  NodeComponent data is duplicated or copied.
   *   
   * @exception RestrictedAccessException if forceDuplicate is set and
   *  this object is part of a compiled scenegraph
   *
   * @see NodeComponent#duplicateNodeComponent
   * @see Node#cloneNode
   * @see Node#cloneTree
   *
   * @since Java 3D 1.2
   */
//    public NodeComponent cloneNodeComponent(boolean forceDuplicate) {
//        throw new RuntimeException("Not Implemented");
        /*
	 * For backward compatibility !
	 *
	 * If user did not overwrite this procedure, it will fall back
	 * to call cloneNodeComponent() 
	 * So for core API, 
	 * don't implement cloneNodeComponent(boolean forceDuplicate)
	 * otherwise this prcedure will not call and the user
	 * cloneNodeComponent() will not invoke.
         */
//        NodeComponent nc; 
//	this.forceDuplicate = forceDuplicate;
//	try {
//	    nc = cloneNodeComponent();
//	} catch (RuntimeException e) {
//	    this.forceDuplicate = false;
//	    throw e;
//	}
//	this.forceDuplicate = false;
//	return nc;
//    }

}
