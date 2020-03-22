/**
 * Project Looking Glass
 *
 * $RCSfile: Switch.java,v $
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
 * $Date: 2004-06-23 18:50:30 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import org.jdesktop.lg3d.sg.internal.wrapper.SwitchWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.NodeWrapper;
import java.util.BitSet;

/**
 * The Switch node controls which of its children will be rendered.
 * It defines a child selection value (a switch value) that can either
 * select a single child, or it can select 0 or more children using a
 * mask to indicate which children are selected for rendering.
 * The Switch node contains an ordered list of children, but the
 * index order of the children in the list is only used for selecting
 * the appropriate child or children and does not specify rendering
 * order.
 */

public class Switch extends Group {
  
    /**
     * Specifies that this node allows reading its child selection
     * and mask values and its current child.
     */
    public static final int
    ALLOW_SWITCH_READ = CapabilityBits.SWITCH_ALLOW_SWITCH_READ;

    /**
     * Specifies that this node allows writing its child selection
     * and mask values.
     */
    public static final int
    ALLOW_SWITCH_WRITE = CapabilityBits.SWITCH_ALLOW_SWITCH_WRITE;

    /**
     * Specifies that no children are rendered.
     * This value may be used in place of a non-negative child
     * selection index.
     */
    public static final int CHILD_NONE = -1;

    /**
     * Specifies that all children are rendered. This setting causes
     * the switch node to function as an ordinary group node.
     * This value may be used in place of a non-negative child
     * selection index.
     */
    public static final int CHILD_ALL  = -2;

    /**
     * Specifies that the childMask BitSet is
     * used to select which children are rendered.
     * This value may be used in place of a non-negative child
     * selection index.
     */
    public static final int CHILD_MASK = -3;

    /**
     * Constructs a Switch node with default parameters.
     * The default values are as follows:
     * <ul>
     * child selection index : CHILD_NONE<br>
     * child selection mask : false (for all children)<br>
     * </ul>
     */
    public Switch() {
    }
    
    protected void createWrapped() {
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"Switch");
    }

    /**
     * Constructs and initializes a Switch node using the specified
     * child selection index.
     * @param whichChild the initial child selection index
     */
    public Switch(int whichChild) {
        setWhichChild(whichChild);
    }

    /**
     * Constructs and initializes a Switch node using the specified
     * child selection index and mask.
     * @param whichChild the initial child selection index
     * @param childMask the initial child selection mask
     */
    public Switch(int whichChild, BitSet childMask){
        setWhichChild(whichChild);
        setChildMask(childMask);
    }

    /**
     * Sets the child selection index that specifies which child is rendered.
     * If the value is out of range, then no children are drawn.
     * @param child a non-negative integer index value, indicating a
     * specific child, or one of the following constants: CHILD_NONE,
     * CHILD_ALL, or CHILD_MASK.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see #CHILD_NONE
     * @see #CHILD_ALL
     * @see #CHILD_MASK
     */
    public void setWhichChild(int child) {
	((SwitchWrapper)wrapped).setWhichChild(child);
    }

    /**
     * Retrieves the current child selection index that specifies which
     * child is rendered.
     * @return a non-negative integer index value, indicating a
     * specific child, or one of the following constants: CHILD_NONE,
     * CHILD_ALL, or CHILD_MASK
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see #CHILD_NONE
     * @see #CHILD_ALL
     * @see #CHILD_MASK
     */
    public int getWhichChild() {
	return ((SwitchWrapper)wrapped).getWhichChild();
    }

    /**
     * Sets the child selection mask.  This mask is used when
     * the child selection index is set to CHILD_MASK.
     * @param childMask a BitSet that specifies which children are rendered
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setChildMask(BitSet childMask) {
	((SwitchWrapper)wrapped).setChildMask(childMask);
    }

    /**
     * Retrieves the current child selection mask.  This mask is used when
     * the child selection index is set to CHILD_MASK.
     * @return the child selection mask
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public BitSet getChildMask() {
	return ((SwitchWrapper)wrapped).getChildMask();
    }

    /**
     * Retrieves the currently selected child. If the child selection index
     * is out of range or is set to CHILD_NONE, CHILD_ALL, or CHILD_MASK,
     * then this method returns null.
     * @return a reference to the current child chosen for rendering
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public Node currentChild() {
	NodeWrapper j3dNode = ((SwitchWrapper)wrapped).currentChild();
        if (j3dNode==null)
            return null;
        else
            return (Node)j3dNode.getUserData();
    }

}
