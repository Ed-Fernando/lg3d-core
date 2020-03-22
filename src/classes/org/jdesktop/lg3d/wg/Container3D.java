/**
 * Project Looking Glass
 *
 * $RCSfile: Container3D.java,v $
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
 * $Revision: 1.10 $
 * $Date: 2006-08-08 00:46:54 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.wg.internal.wrapper.Container3DWrapper;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.BranchGroupWrapper;


/**
 * Container3D object is a component that can contain other components. 
 *
 * It also can have an associated LayoutManager3D object and a decoration.
 * The decoration object represents visual decoration of this
 * container that will not get affected by layout manager.
 * Decoration's setSize() method will be invoked as a result of
 * container's setSize() method invocation for possibly necessary
 * visual adjustment.
 */
public class Container3D extends Component3D {
    private static LayoutManager3D nullLayoutManager3D
	= new NullLayoutManager3D();

    private LayoutManager3D layoutManager = nullLayoutManager3D;
    
    /**
     * Creates a new instance of Container3D.
     *
     * A Container3D object can host Component3D objects and have
     * an associated LayoutManager3D object and a decoration.
     */
    public Container3D() {
    }
        
    Container3D(boolean visible) {
	super(visible);
    }
    
    /**
     * Sets the size hint of this component using bounding box.
     *
     * Also propagates the size hint to the decoration for possibly
     * necessary rearrangement of the decoration.
     */
    public synchronized void setPreferredSize(Vector3f preferredSize) {
	((Container3DWrapper)wrapped).setSizeHint(preferredSize.x, preferredSize.y, preferredSize.z);
	revalidate();
    }
    
    /**
     * Sets the layout manager for this container.
     * This method cannot be called when the container is not empty.
     */
    public synchronized void setLayout(LayoutManager3D layoutManager) {
        if (numChildren() != 0) {
            throw new IllegalStateException(
                "cannot set layoutmanager when the container is not empty");
        }
	if (layoutManager == null) {
	    layoutManager = nullLayoutManager3D;
	}
        if (this.layoutManager != nullLayoutManager3D) {
            this.layoutManager.setContainer(null);
        }
        layoutManager.setContainer(this);
	this.layoutManager = layoutManager;
    }
    
    /**
     * Gets the layout manager for this container.
     */
    public LayoutManager3D getLayout() {
        LayoutManager3D layout = layoutManager;
        if (layout == nullLayoutManager3D) {
            return null;
        } else {
            return layout;
        }
    }
    
    /**
     * Causes this container to lay out its components. 
     */
    public void revalidate() {
        if (layoutManager == null) {
            // this case only happens when constructing a Container3D object.
            return;
        }
        layoutManager.layoutContainer();
    }
    
    /**
     * Sets the specified Component3D object the as decoration of this container.
     */
    public void setDecoration(Component3D deco) {
        if (transparencyManager != null) {
            Component3D prevDeco = getDecoration();
            if (prevDeco != null) {
                transparencyManager.removeGraph(prevDeco);
            }
            if (deco != null) {
                transparencyManager.addGraph(deco);
            }
        }
	((Container3DWrapper)wrapped).setDecoration((Component3DWrapper)deco.getWrapped());
    }
    
    /**
     * Gets the decoration of this container.
     */
    public Component3D getDecoration() {
        Component3DWrapper deco = ((Container3DWrapper)wrapped).getDecoration();
        if (deco == null) {
            return null;
        }
        return (Component3D)deco.getUserData();
    }
    
    /**
     * Appends the specified Component3D object to this container's 
     * list of children.
     *
     * @param child  the Component3D object to move to this container.
     * @param index  at which location to insert. 
     *     The index must be a value greater than or equal to 0 
     *     and less than or equal to numChildren().
     * @param constraints  the constraints object that is passed to the
     *     layoutmanager associated with this container when adding the
     *     component and rearranging the container.
     */
    public synchronized void addChild(Component3D child, Object constraints) {
        super.addChild((Node)child);
        layoutManager.addLayoutComponent(child, constraints);
        if (child.isVisible()) {
            revalidate();
        }
	logger.finer("added: " + child + " to " + this);
    }
    
    /**
     * Replaces the Component3D object at the specified index 
     * in this container's list of children with the specified component.
     *
     * @param child  the Component3D object to move to this container.
     * @param index  at which location to insert. 
     *     The index must be a value greater than or equal to 0 
     *     and less than or equal to numChildren().
     * @param constraints  the constraints object that is passed to the
     *     layoutmanager associated with this container when setting the
     *     component and rearranging the container.
     */
    private synchronized void setChild(Component3D child, int index, Object constraints) {
        Node prevNode = getChild(index);
        boolean prevVisible = true;
        if (prevNode instanceof Component3D) {
            prevVisible = ((Component3D)prevNode).isVisible();
            layoutManager.removeLayoutComponent((Component3D)prevNode);
        }
	super.setChild((Node)child, index);
        layoutManager.addLayoutComponent(child, constraints);
        if (child.isVisible() != prevVisible) {
            revalidate();
        }
    }
    
    /**
     * Inserts the specified Component3D object in this container's list 
     * of children at the specified index.
     *
     * @param child  the Component3D object to move to this container.
     * @param index  at which location to insert. 
     *     The index must be a value greater than or equal to 0 
     *     and less than or equal to numChildren().
     * @param constraints  the constraints object that is passed to the
     *     layoutmanager associated with this container when inserting the
     *     component and rearranging the container.
     */
    private synchronized void insertChild(Component3D child, int index, Object constraints) {
	super.insertChild((Node)child, index);
        layoutManager.addLayoutComponent(child, constraints);
	if (child.isVisible()) {
            revalidate();
        }
    }
    
    /**
     * Appends the specified Component3D object to this container's 
     * list of children.
     *
     * @param child  the Component3D object to move to this container.
     * @param index  at which location to insert. 
     *     The index must be a value greater than or equal to 0 
     *     and less than or equal to numChildren().
     */
    public void addChild(Component3D child) {
        addChild(child, null);
    }
    
    /**
     * Replaces the Component3D object at the specified index 
     * in this container's list of children with the specified component.
     *
     * @param child  the Component3D object to move to this container.
     * @param index  at which location to insert. 
     *     The index must be a value greater than or equal to 0 
     *     and less than or equal to numChildren().
     */
    public void setChild(Component3D child, int index) {
        setChild(child, index, index);
    }
    
    /**
     * Inserts the specified Component3D object in this container's list 
     * of children at the specified index.
     *
     * @param child  the Component3D object to move to this container.
     * @param index  at which location to insert. 
     *     The index must be a value greater than or equal to 0 
     *     and less than or equal to numChildren().
     */
    public void insertChild(Component3D child, int index) {
        insertChild(child, index, index);
    }
    
    /**
     * Moves the specified Component3D object from its existing location
     * to the end of this container's list of children.
     *
     * @param child  the Component3D object to move to this container.
     */
    public void moveTo(Component3D child) {
        moveTo(child, null);
    }
    
    /**
     * Moves the specified Component3D object from its existing location
     * to the end of this container's list of children.
     * Note that child's translation will be changed so that it won't move 
     * visually when moving between the containers.
     *
     * @param child  the Component3D object to move to this container.
     * @param constraints  the constraints object that is passed to the
     *     layoutmanager associated with this container when moving the
     *     component and rearranging the container.
     */
    public void moveTo(Component3D child, Object constraints) {
        // Translate the location of the child so that it won't move 
        // visually when moving it between the containers.
        Vector3f diff = getTranslationTo(child, new Vector3f());
        child.setTranslation(diff.x, diff.y, diff.z);
        
        // Previous container's revalidate() will be invoked on the 
        // J3dContainer3D side.
	((Container3DWrapper)wrapped).moveTo((BranchGroupWrapper)child.getWrapped());
        layoutManager.addLayoutComponent(child, null);
        if (child.isVisible()) {
            revalidate();
        }
    }
    
    private void checkChildClass(Node child) {
        // Null child is not allowed in order to make
        // layout manager's job a bit easier.
        if (child == null) {
            throw new IllegalArgumentException(
		"Null child is not allowed for Container3D");
        }
	if (!(child instanceof Component3D)) {
	    throw new IllegalArgumentException(
		"cannot add a non-Component3D object to Container3D");
	}
    }
    
    /**
     * Appends the specified Component3D object to this container's 
     * list of children.
     * 
     * Although the argument type is Node, only a Component3D object is 
     * allowed to be specified.  In order to leverage compilation
     * time type checking, a version that takes a Component3D as the
     * argument is provided and this method is marked as deprecated
     * (despite the fact that argument's type will be checked dynamically). 
     *
     * @param child  the Component3D object to move to this container.
     * @throws IllegalArgumentException if non-Component3D object is specified.
     *
     * @deprecated  Only a Component3D object can be applied.
     */
    public void addChild(Node child) {
        checkChildClass(child);
        addChild((Component3D)child, null);
    }

    /**
     * Replaces the Component3D object at the specified index 
     * in this container's list of children with the specified component.
     * 
     * Although the argument type is Node, only a Component3D object is 
     * allowed to be specified.  In order to leverage compilation
     * time type checking, a version that takes a Component3D as the
     * argument is provided and this method is marked as deprecated
     * (despite the fact that argument's type will be checked dynamically). 
     *
     * @param child  the Component3D object to move to this container.
     * @throws IllegalArgumentException if non-Component3D object is specified.
     *
     * @deprecated  Only a Component3D object can be applied.
     */
    public void setChild(Node child, int index) {
        checkChildClass(child);
        setChild((Component3D)child, index, null);
    }

    /**
     * Inserts the specified Component3D object in this container's list 
     * of children at the specified index.
     * 
     * Although the argument type is Node, only a Component3D object is 
     * allowed to be specified.  In order to leverage compilation
     * time type checking, a version that takes a Component3D as the
     * argument is provided and this method is marked as deprecated
     * (despite the fact that argument's type will be checked dynamically). 
     *
     * @param child  the Component3D object to move to this container.
     * @throws IllegalArgumentException if non-Component3D object is specified.
     * 
     * @deprecated  Only a Component3D object can be applied.
     */
    public void insertChild(Node child, int index) {
        checkChildClass(child);
        insertChild((Component3D)child, index, null);
    }
    
    /**
     * Moves the specified Component3D object from its existing location
     * to the end of this container's list of children.
     * 
     * Although the argument type is Node, only a Component3D object is 
     * allowed to be specified.  In order to leverage compilation
     * time type checking, a version that takes a Component3D as the
     * argument is provided and this method is marked as deprecated
     * (despite the fact that argument's type will be checked dynamically). 
     *
     * @param child  the Component3D object to move to this container.
     * @throws IllegalArgumentException if non-Component3D object is applied.
     * 
     * @deprecated  Only a Component3D object can be applied.
     */
    public void moveTo(BranchGroup child) {
        throw new UnsupportedOperationException(
                "Only a Component3D object can be applied");
    }
    
    /**
     * Removes the child node at the specified index from this 
     * container's list of children.
     */
    public synchronized void removeChild(int index) {
        Component3D child = (Component3D)getChild(index);
        if (child == null) {
            return;
        }
        layoutManager.removeLayoutComponent(child);
	super.removeChild(child);
	if (child.isVisible()) {
            revalidate();
        }
    }
    
    /**
     * Removes the specified child node from this container's 
     * list of children. If the specified object is not in the list, 
     * the list is not modified.
     */
    public synchronized void removeChild(Node child) {
        if (child != null && indexOfChild(child) == -1) {
            return;
        }
        layoutManager.removeLayoutComponent((Component3D)child);
	super.removeChild(child);
	if (child instanceof Component3D && ((Component3D)child).isVisible()) {
            revalidate();
        }
	logger.finer("removed: " + child + " from " + this);
    }
    
    /**
     * Removes all children from this container.
     */
    public synchronized void removeAllChildren() {
        int num = super.numChildren();
        for (int i = 0; i < num; i++) {
            Component3D child = (Component3D)super.getChild(i);
	    layoutManager.removeLayoutComponent(child);
	}
	super.removeAllChildren();
	revalidate();
    }
    
    /**
     * Requests the layoutmanager to rearrange the specified child using
     * the newConstraints object.  If the specified child is not found in
     * this container, do nothing.
     *
     * @param child the child to be rearranged
     * @param newConstraints where/how the component is added to the layout.
     */
    public synchronized boolean rearrangeChildLayout(Component3D child, Object newConstraints) {
        if (child != null && indexOfChild(child) == -1) {
            return false;
        }
        if (layoutManager.rearrangeLayoutComponent(child, newConstraints)) {
            revalidate();
            return true;
        }
        return false;
    }
    
    /**
     * Implementation detail, do not call from user code.
     * May be removed in future.
     */
    protected void createWrapped() {
        wrapped = instantiate("Container3D");
        wrapped.setUserData(this);
    }
    
    private static class NullLayoutManager3D implements LayoutManager3D {
	public void setContainer(Container3D container) { }
        public void layoutContainer() { }
        public void addLayoutComponent(Component3D comp, Object constraints) { }
        public void removeLayoutComponent(Component3D comp) { }
        public boolean rearrangeLayoutComponent(Component3D comp, Object newConstraints) {
            return false;
        }
    }
}
