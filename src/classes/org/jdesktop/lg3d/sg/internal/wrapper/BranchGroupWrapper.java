/**
 * Project Looking Glass
 *
 * $RCSfile: BranchGroupWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

/**
 * The BranchGroup serves as a pointer to the root of a
 * scene graph branch; BranchGroup objects are the only objects that
 * can be inserted into a Locale's set of objects. A subgraph, rooted
 * by a BranchGroup node can be thought of as a compile unit. The
 * following things may be done with BranchGroup:
 * <P><UL>
 * <LI>A BranchGroup may be compiled by calling its compile method. This causes the
 * entire subgraph to be compiled. If any BranchGroup nodes are contained within the
 * subgraph, they are compiled as well (along with their descendants).</LI>
 * <p>
 * <LI>A BranchGroup may be inserted into a virtual universe by attaching it to a
 * Locale. The entire subgraph is then said to be live.</LI>
 * <p>
 * <LI>A BranchGroup that is contained within another subgraph may be reparented or
 * detached at run time if the appropriate capabilities are set.</LI>
 * </UL>
 * Note that that if a BranchGroup is included in another subgraph, as a child of
 * some other group node, it may not be attached to a Locale.
 *
 * @version 	1.54, 04/01/28 13:11:07188

 */

public interface BranchGroupWrapper extends GroupWrapper {

  /**
   * Detaches this BranchGroup from its parent.
   */
    public void detach() ;

  /**
   * Returns an array referencing all the items that are pickable below this
   * <code>BranchGroup</code> that intersect with PickShape.
   * The resultant array is unordered.
   *
   * @param pickShape the PickShape object
   *
   * @see SceneGraphPath
   * @see Locale#pickAll
   * @see PickShape
   * @exception IllegalStateException if BranchGroup is not live.
   *
   */
    public SceneGraphPathWrapper[] pickAll( PickShapeWrapper pickShape ) ;

  /**
   * Returns a sorted array of references to all the Pickable items that 
   * intersect with the pickShape. Element [0] references the item closest 
   * to <i>origin</i> of PickShape successive array elements are further 
   * from the <i>origin</i>
   *
   * Note: If pickShape is of type PickBounds, the resulting array 
   * is unordered.
   * @param pickShape the PickShape object
   * 
   * @see SceneGraphPath
   * @see Locale#pickAllSorted
   * @see PickShape
   * @exception IllegalStateException if BranchGroup is not live.
   *  
   */
    public SceneGraphPathWrapper[] pickAllSorted( PickShapeWrapper pickShape ) ;

  /**
   * Returns a SceneGraphPath that references the pickable item 
   * closest to the origin of <code>pickShape</code>.
   *
   * Note: If pickShape is of type PickBounds, the return is any pickable node
   * below this BranchGroup.
   * @param pickShape the PickShape object
   *
   * @see SceneGraphPath
   * @see Locale#pickClosest
   * @see PickShape
   * @exception IllegalStateException if BranchGroup is not live.
   *  
   */
    public SceneGraphPathWrapper pickClosest( PickShapeWrapper pickShape ) ;

  /**
   * Returns a reference to any item that is Pickable below this BranchGroup that
   * intersects with <code>pickShape</code>.
   *
   * @param pickShape the PickShape object
   * @see SceneGraphPath
   * @see Locale#pickAny
   * @see PickShape
   * @exception IllegalStateException if BranchGroup is not live.
   *  
   */
    public SceneGraphPathWrapper pickAny( PickShapeWrapper pickShape ) ;

}
