/**
 * Project Looking Glass
 *
 * $RCSfile: TransparencyOrderedGroup.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2006-08-22 20:27:10 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.utils.transparency;

import java.util.Arrays;
import org.jdesktop.lg3d.sg.Group;
import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.sg.SceneGraphSetup;
import org.jdesktop.lg3d.sg.internal.wrapper.NodeWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.TransparencyOrderedGroupWrapper;

/**
 * Provides the user control over the rendering order of transparent objects
 * in the subgraphs of this Group.
 *
 * Transparent shapes in the 1st child of this group will be rendered before
 * (ie behind) transparent shapes in the 2nd child and so on.
 *
 * Transparent shapes in the same child will be ordered using the standard test
 * of distance to view.
 *
 * @author paulby
 */
public class TransparencyOrderedGroup extends Group {
    
    /**
     * Sets the childIndexOrder array.  If the specified array is
     * null, this node's childIndexOrder array is set to null.  Its
     * children will then be rendered in increasing index order.  If
     * the specified array is not null, the entire array is copied to
     * this node's childIndexOrder array.  In this case, the length of
     * the array must be equal to the number of children in this
     * group, and every entry in the array must have a unique value
     * between 0 and <code>numChildren-1</code> (i.e., there must be
     * no duplicate values and no missing indices).
     *
     * @param childIndexOrder the array that is copied into this
     * node's child index order array; this can be null
     *
     * @exception IllegalArgumentException if the specified array is
     * non-null and any of the following are true:
     * <ul>
     * <li><code>childIndexOrder.length != numChildren</code>;</li>
     * <li><code>childIndexOrder[</code><i>i</i><code>] < 0</code>,
     * for <i>i</i> in <code>[0, numChildren-1]</code>;</li>
     * <li><code>childIndexOrder[</code><i>i</i><code>] >= numChildren</code>,
     * for <i>i</i> in <code>[0, numChildren-1]</code>;</li>
     * <li><code>childIndexOrder[</code><i>i</i><code>] ==
     * childIndexOrder[</code><i>j</i><code>]</code>,
     * for <i>i</i>,<i>j</i> in <code>[0, numChildren-1]</code>,
     * <i>i</i> <code>!=</code> <i>j</i>;</li>
     * </ul>
     *
     */
    public void setChildIndexOrder(int[] childIndexOrder) {
        ((TransparencyOrderedGroupWrapper)wrapped).setChildIndexOrder(childIndexOrder);
    }
    
    
    /**
     * Retrieves the current childIndexOrder array.
     *
     * @return a copy of this node's childIndexOrder array; this
     * can be null.
     *
     */
    public int[] getChildIndexOrder() {
        return ((TransparencyOrderedGroupWrapper)wrapped).getChildIndexOrder();
    }
    
    /**
     * Appends the specified child node to this group node's list of children.
     *
     * <p>
     * If the current child index order array is non-null, the array
     * is increased in size by one element, and a new element
     * containing the index of the new child is added to the end of
     * the array.  Thus, this new child will be rendered last.
     *
     * @param child the child to add to this node's list of children
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     * @exception RestrictedAccessException if this group node is part
     * of live
     * or compiled scene graph and the child node being added is not
     * a BranchGroup node
     * @exception MultipleParentException if <code>child</code> has already
     * been added as a child of another group node.
     */
    public void addChild(Node child) {
        ((TransparencyOrderedGroupWrapper)wrapped).addChild( (NodeWrapper)child.getWrapped() );
    }
    
    /**
     * Appends the specified child node to this group node's list of
     * children, and sets the child index order array to the specified
     * array.  If the specified array is null, this node's
     * childIndexOrder array is set to null.  Its children will then
     * be rendered in increasing index order.  If the specified array
     * is not null, the entire array is copied to this node's
     * childIndexOrder array.  In this case, the length of the array
     * must be equal to the number of children in this group after the
     * new child has been added, and every entry in the array must
     * have a unique value between 0 and <code>numChildren-1</code>
     * (i.e., there must be no duplicate values and no missing
     * indices).
     *
     * @param child the child to add to this node's list of children
     *
     * @param childIndexOrder the array that is copied into this
     * node's child index order array; this can be null
     *
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     *
     * @exception RestrictedAccessException if this group node is part
     * of live
     * or compiled scene graph and the child node being added is not
     * a BranchGroup node
     *
     * @exception MultipleParentException if <code>child</code> has already
     * been added as a child of another group node.
     *
     * @exception IllegalArgumentException if the specified array is
     * non-null and any of the following are true:
     * <ul>
     * <li><code>childIndexOrder.length != numChildren</code>;</li>
     * <li><code>childIndexOrder[</code><i>i</i><code>] < 0</code>,
     * for <i>i</i> in <code>[0, numChildren-1]</code>;</li>
     * <li><code>childIndexOrder[</code><i>i</i><code>] >= numChildren</code>,
     * for <i>i</i> in <code>[0, numChildren-1]</code>;</li>
     * <li><code>childIndexOrder[</code><i>i</i><code>] ==
     * childIndexOrder[</code><i>j</i><code>]</code>,
     * for <i>i</i>,<i>j</i> in <code>[0, numChildren-1]</code>,
     * <i>i</i> <code>!=</code> <i>j</i>;</li>
     * </ul>
     */
    public void addChild(Node child, int[] childIndexOrder) {
        ((TransparencyOrderedGroupWrapper)wrapped).addChild( (NodeWrapper)child.getWrapped(), childIndexOrder );
    }
    
    /**
     * Inserts the specified child node in this group node's list of
     * children at the specified index.
     * This method is only supported when the child index order array
     * is null.
     *
     * @param child the new child
     * @param index at which location to insert. The <code>index</code>
     * must be a value
     * greater than or equal to 0 and less than or equal to
     * <code>numChildren()</code>.
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     * @exception RestrictedAccessException if this group node is part of
     * live
     * or compiled scene graph and the child node being inserted is not
     * a BranchGroup node
     * @exception MultipleParentException if <code>child</code> has already
     * been added as a child of another group node.
     * @exception IndexOutOfBoundsException if <code>index</code> is invalid.
     * @exception IllegalStateException if the childIndexOrder array is
     * not null.
     *
     */
    public void insertChild(Node child, int index) {
        ((TransparencyOrderedGroupWrapper)wrapped).insertChild( (NodeWrapper)child.getWrapped(), index );
    }
    
    /**
     * Replaces the node at the specified index with the child node provided.
     * This method is only supported when the child index order array
     * is null.
     *
     * @param child the new child
     * @param index of node to replace. The <code>index</code>
     * must be a value
     * greater than or equal to 0 and less than or equal to
     * <code>numChildren()</code>.
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     * @exception RestrictedAccessException if this group node is part of
     * live
     * or compiled scene graph and the child node being inserted is not
     * a BranchGroup node
     * @exception MultipleParentException if <code>child</code> has already
     * been added as a child of another group node.
     * @exception IndexOutOfBoundsException if <code>index</code> is invalid.
     * @exception IllegalStateException if the childIndexOrder array is
     * not null.
     *
     */
    public void setChild(Node child, int index) {
        ((TransparencyOrderedGroupWrapper)wrapped).setChild( (NodeWrapper)child.getWrapped(), index );
    }
    
    /**
     * Removes the specified child node from this group node's
     * list of children.
     * If the specified object is not in the list, the list is not modified.
     *
     * <p>
     * If the current child index order array is non-null, the element
     * containing the removed child's index will be removed from the
     * child index order array, and the array will be reduced in size
     * by one element.  If the child removed was not the last child in
     * the Group, the values of the child index order array will be
     * updated to reflect the indices that were renumbered.  More
     * formally, each child whose index in the Group node was greater
     * than the removed element (before removal) will have its index
     * decremented by one.
     *
     * @param child the child node to be removed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if this group node is part of
     * live or compiled scene graph and the child node being removed is not
     * a BranchGroup node
     */
    public void removeChild(Node child) {
        ((TransparencyOrderedGroupWrapper)wrapped).removeChild( (NodeWrapper)child.getWrapped() );
    }
    
    /**
     * Removes the child node at the specified index from this group node's
     * list of children.
     *
     * <p>
     * If the current child index order array is non-null, the element
     * containing the removed child's index will be removed from the
     * child index order array, and the array will be reduced in size
     * by one element.  If the child removed was not the last child in
     * the Group, the values of the child index order array will be
     * updated to reflect the indices that were renumbered.  More
     * formally, each child whose index in the Group node was greater
     * than the removed element (before removal) will have its index
     * decremented by one.
     *
     * @param index which child to remove.  The <code>index</code>
     * must be a value
     * greater than or equal to 0 and less than <code>numChildren()</code>.
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     * @exception RestrictedAccessException if this group node is part of
     * live or compiled scene graph and the child node being removed is not
     * a BranchGroup node
     * @exception IndexOutOfBoundsException if <code>index</code> is invalid.
     */
    public void removeChild(int index) {
        ((TransparencyOrderedGroupWrapper)wrapped).removeChild( index );
    }
    
    
    protected void createWrapped() {
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"TransparencyOrderedGroup" );
    }
}
