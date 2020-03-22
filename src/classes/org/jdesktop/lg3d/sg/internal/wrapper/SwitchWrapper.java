/**
 * Project Looking Glass
 *
 * $RCSfile: SwitchWrapper.java,v $
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

public interface SwitchWrapper extends GroupWrapper {
  
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
    public void setWhichChild(int child);

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
    public int getWhichChild();

    /**
     * Sets the child selection mask.  This mask is used when
     * the child selection index is set to CHILD_MASK.
     * @param childMask a BitSet that specifies which children are rendered
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setChildMask(BitSet childMask);

    /**
     * Retrieves the current child selection mask.  This mask is used when
     * the child selection index is set to CHILD_MASK.
     * @return the child selection mask
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public BitSet getChildMask();

    /**
     * Retrieves the currently selected child. If the child selection index
     * is out of range or is set to CHILD_NONE, CHILD_ALL, or CHILD_MASK,
     * then this method returns null.
     * @return a reference to the current child chosen for rendering
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public NodeWrapper currentChild();

}
