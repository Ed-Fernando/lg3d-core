/**
 * Project Looking Glass
 *
 * $RCSfile: J3dComponent3D.java,v $
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
 * $Revision: 1.12 $
 * $Date: 2006-08-25 23:21:11 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dnodes;

import java.util.Enumeration;
import java.util.Vector;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3f;


/**
 * Component3D object is a top level object for widget development.
 *
 * It supports visual rendering by adding scene graph node children 
 * and event handling.
 *
 * This node actually expands into several Java 3D nodes at this stage, however
 * from the LG scene graph it still appears as one node. This is achieved by
 * overriding addChild, getChild etc. The addChildLG etc methods operate in
 * the manner expected by the LG scene graph, the addChild methods in the manner
 * of Java 3D. Specifically the decoration tree is included as a child when
 * using the java3d accessors, but not when using the LG methods.
 *
 * The internal scene graph of Component3D looks like this
 * <p>
 * <img src="doc-files/component3d-internals.gif"
 * width="600" height="360">
 * </p>
 *  
 */
public class J3dComponent3D extends J3dLgBranchGroup {
    private boolean visible = false;
    private float width;
    private float height;
    private float depth;
    private TransformOpGroup compTrans = new TransformOpGroup();
    private final Group children = new Group();
    private final Switch visibleSwitch = new Switch();
    private final Group decoration = new Group();
    private J3dCursor3D cursor;
    private J3dContainer3D parentContainer;
    
    protected PhysicsData physicsData;
    
    /**
     * Creates a new instance of Component3D.
     * 
     * By default, it is visible (i.e. getVisible() returns 'ture')
     * and it doesn't have any asscociated cursor.
     */
    public J3dComponent3D() {
	this(true);
    }

    J3dComponent3D(boolean visible) {
	// this -> visibleSwitch -> compTrans -> children
        //                                    -> decoration
	logger.fine("creating: " + this);

	setCapability(ALLOW_DETACH);
	setCapability(ALLOW_CHILDREN_READ);
	setCapability(ALLOW_CHILDREN_WRITE);
	setCapability(ALLOW_CHILDREN_EXTEND);
        
        setCapability(ALLOW_PICKABLE_READ);
        setCapability(ALLOW_PICKABLE_WRITE);
        
        setCapability(ALLOW_LOCAL_TO_VWORLD_READ);
        setCapability(ALLOW_PARENT_READ);
        
        visibleSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        visibleSwitch.setCapability(ALLOW_PARENT_READ);
        
	children.setCapability(ALLOW_CHILDREN_READ);
	children.setCapability(ALLOW_CHILDREN_WRITE);
	children.setCapability(ALLOW_CHILDREN_EXTEND);
        children.setCapability(ALLOW_LOCAL_TO_VWORLD_READ);
        children.setCapability(ALLOW_PARENT_READ);
        
	compTrans.addChild(children);
	compTrans.addChild(decoration);
        visibleSwitch.addChild(compTrans);
        
	setVisible(visible);
        super.addChild(visibleSwitch);
    }

    /**
     * Sets the size hint of this component using float values.
     *
     * The size is merely a hint and no default action will be 
     * taken except storing the information.  Subclasses of Component3D
     * can override this method and implement desired action. 
     *
     * REMINDER -- use of BoundingBox?
     * REMINDER -- associating this with setCollisionBounds()?
     */
    public void setSizeHint(float width, float height, float depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        
        if (physicsData != null) {
            physicsData.setBoxSize(width, height, depth);
        }
        
        J3dContainer3D parent = getParentContainer();
        if (parent != null) {
            // when just resizing, don't fire a Container3DRearrangedEvent
            doLayoutLGWithoutEvent(parent);
        }
    }
    
    public Vector3f getSizeHint(Vector3f ret) {
        ret.set(width, height, depth);
        return ret;
    }
    
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
        
    public float getDepth() {
        return depth;
    }
  
    /**
     * Shows or hides this component depending on the value of parameter 
     * 'visible'.
     */
    public void setVisible(boolean visible) {
        if (this.visible == visible) {
            return;
        }

        if (visible)
            visibleSwitch.setWhichChild(Switch.CHILD_ALL);
        else
            visibleSwitch.setWhichChild(Switch.CHILD_NONE);
        logger.finer("made " 
            + ((visible)?("visible"):("invisible")) + ": " + this);
        this.visible=visible;
    }
    
    public boolean isVisible() {
	return visible;
    }
    
    /**
     *
     *
     * TODO used to be used For use by the Physics system
     */
    public void setTransform( Transform3D t3d ) {
        compTrans.setTransform( t3d );
    }

    /**
     * Appends the specified child node to this component's list of children.
     */ 
    public void addChildLG(Node child) {
        //checkChildClass(child);
        children.addChild(child);
        if (this instanceof J3dContainer3D) {
            assert(child instanceof J3dComponent3D);
            ((J3dComponent3D)child).setParentContainer((J3dContainer3D)this);
        }
    }
    
    public void addChild(Node child) {
        addChildLG(child);
    }
    
    void addBGForBehavior(BranchGroup child) {
        compTrans.addChild(child);
    }
    
    /**
     * Replaces the child node at the specified index in this component's
     * list of children with the specified child.
     */ 
    public void setChildLG(Node child, int index) {
	//checkChildClass(child);
	children.setChild(child, index);
        if (this instanceof J3dContainer3D) {
            assert(child instanceof J3dComponent3D);
            ((J3dComponent3D)child).setParentContainer((J3dContainer3D)this);
        }
    }

    public void setChild(Node child, int index ) {
        setChildLG(child, index-1);
    }
    
    /**
     * Inserts the specified child node in this component's list of
     * children at the specified index.
     */
    public void insertChildLG(Node child, int index) {
        //checkChildClass(child);
	children.insertChild(child, index);
        if (this instanceof J3dContainer3D) {
            assert(child instanceof J3dComponent3D);
            ((J3dComponent3D)child).setParentContainer((J3dContainer3D)this);
        }
    }
    
    public void insertChild(Node child, int index) {
        insertChildLG(child, index-1);
    }

    /**
     * Moves the specified branch group node from its existing location
     * to the end of this component's list of children.
     */
    public void moveTo(BranchGroup child) {
	//checkChildClass(child);
	children.moveTo(child);
        if (this instanceof J3dContainer3D) {
            assert(child instanceof J3dComponent3D);
            J3dContainer3D parent = ((J3dComponent3D)child).getParentContainer();
            if (parent != null) {
                setParentContainer(null);
                ((J3dComponent3D)child).removeLayoutLG(parent);
            }
            ((J3dComponent3D)child).setParentContainer((J3dContainer3D)this);
        }
    }
    
    /**
     * Removes the specified child node from this component's list
     * of children. If the specified object is not in the list, the
     * list is not modified.
     */
    public void removeChild(Node child) {
	children.removeChild(child);
        if (this instanceof J3dContainer3D) {
            assert(child instanceof J3dComponent3D);
            ((J3dComponent3D)child).setParentContainer(null);
        }
    }

    /**
     * Removes the child node at the specified index from this component's
     * list of children.
     */
    public void removeChildLG(int index) {
        Node child = getChild(index);
	children.removeChild(index);
        if (this instanceof J3dContainer3D) {
            assert(child instanceof J3dComponent3D);
            ((J3dComponent3D)child).setParentContainer(null);
        }
    }
    
    public void removeChild(int index) {
        removeChildLG(index-1);
    }

    /**
     * Removes all children from this component.
     */
    public void removeAllChildren() {
        Enumeration e = children.getAllChildren();
        if (this instanceof J3dContainer3D) {
            while (e.hasMoreElements()) {
                Object child = e.nextElement();
                assert(child instanceof J3dComponent3D);
                ((J3dComponent3D)child).setParentContainer(null);
            }
        }
	children.removeAllChildren();   
    }

    /**
     * Replaces the child node at the specified index in this component's
     * list of children with the specified child.
     */
    public javax.media.j3d.Node getChildLG(int index) {
	return children.getChild(index);
    }
    
    /**
     * Returns the child at the specific index.
     * The decoration is always child 0
     */
    public javax.media.j3d.Node getChild(int index) {
        if (index==0)
            return getDecoGroup();
        else
            return getChildLG(index-1);
    }

    /**
     * Returns an Enumeration object of this component's list of children.
     */
    public Enumeration getAllChildrenLG() {
        Vector collection = new Vector();
        Enumeration e = children.getAllChildren();
        while (e.hasMoreElements()) {
            javax.media.j3d.Node j3dC3d = ((javax.media.j3d.Node)e.nextElement());
            if (j3dC3d==null)
                collection.add(null);
            else {
//                System.out.println(". "+j3dC3d);
//                System.out.println(": "+j3dC3d.getUserData());
//                System.out.println("+ "+((org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.Node)
//                            j3dC3d.getUserData()).getUserData());
//                System.out.println("---------------");
//                org.jdesktop.lg3d.sg.Node comp 
//                    = (org.jdesktop.lg3d.sg.Node)
//                        ((org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.Node)
//                            j3dC3d.getUserData()).getUserData();
                collection.add(j3dC3d);
            }
        }
	return collection.elements();
    }
    
    public Enumeration getAllChildren() {
        Vector collection = new Vector();
        collection.add(getDecoGroup());
        Enumeration e = children.getAllChildren();
        while(e.hasMoreElements())
            collection.add(e.nextElement());
        
        return collection.elements();
    }

    /**
     * Returns a count of this component's children.
     */
    public int numChildrenLG() {
	return children.numChildren();
    }
    
    /**
     * Returns the number of children, plus the decoration 'child'
     */
    public int numChildren() {
        return children.numChildren()+1;
    }

    /**
     * Retrieves the index of the specified child node in this component's
     * list of children.
     */
    public int indexOfChildLG(Node child) {
	return children.indexOfChild(child);
    }
    
    public int indexOfChild(Node child) {
        int i = children.indexOfChild(child);
        if (i == -1) {
            if (child == getDecoGroup()) {
                return 0;
            }
            return -1;
        }
        return children.indexOfChild(child)+1;
    }
    
    /**
     * Returns Group where decoration is to be added.
     */
    Group getDecoGroup() {
	return decoration;
    }
    
    /**
     * Set the cursor of this component.
     */
    public void setCursor( J3dCursor3D cursor ) {
        this.cursor = cursor;
    }
    
    /**
     * Get the cursor of this component.
     */
    public J3dCursor3D getCursor() {
        return cursor;
    }
    
    /**
     * Insert the insert group between target and all of it's children
     */
    private void insertGroup( Group target, Group insert ) {
        for(int i=0; i<target.numChildren(); i++) {
            Node n = target.getChild(0);
            target.removeChild(0);
            insert.addChild(n);
        }

        target.addChild(insert);
    }
   
    /**
     * Detaches this BranchGroup from its parent.
     */
    public void detach() {
        super.detach();
        J3dContainer3D parent = getParentContainer();
        if (parent != null) {
            setParentContainer(null);
            removeLayoutLG(parent);
        }
    }
    
    /**
     * Setter for the parent container info.
     */
    private void setParentContainer(J3dContainer3D parent) {
        parentContainer = parent;
    }
    
    /**
     * Getter for the parent container info.
     */
    private J3dContainer3D getParentContainer() {
        return parentContainer;
    }
    
    private void doLayoutLG(J3dContainer3D cont) {
        org.jdesktop.lg3d.wg.Container3D contClient
            = (org.jdesktop.lg3d.wg.Container3D)
                ((org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.Container3D)
                    cont.getUserData()).getUserData();
        contClient.revalidate();
    }
    
    private void doLayoutLGWithoutEvent(J3dContainer3D cont) {
        org.jdesktop.lg3d.wg.Container3D contClient
            = (org.jdesktop.lg3d.wg.Container3D)
                ((org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.Container3D)
                    cont.getUserData()).getUserData();
        org.jdesktop.lg3d.wg.LayoutManager3D layoutmanager
            = contClient.getLayout();
        if (layoutmanager != null) {
            layoutmanager.layoutContainer();
        }
    }
    
    private void removeLayoutLG(J3dContainer3D cont) {
        org.jdesktop.lg3d.wg.Container3D contClient
            = (org.jdesktop.lg3d.wg.Container3D)
                ((org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.Container3D)
                    cont.getUserData()).getUserData();
        org.jdesktop.lg3d.wg.Component3D childClient
            = (org.jdesktop.lg3d.wg.Component3D)
                ((org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.Component3D)
                    this.getUserData()).getUserData();
        org.jdesktop.lg3d.wg.LayoutManager3D layoutmanager
            = contClient.getLayout();
        if (layoutmanager != null) {
            layoutmanager.removeLayoutComponent(childClient);
        }
        contClient.revalidate();
    }   

    public void getLocalToVworld(Transform3D t3d) {
        children.getLocalToVworld(t3d);
    }
    
    /**
     * Prompts the layout manager to lay out this component.
     */
    public void requestParentToRevalidate() {
        J3dContainer3D parent = getParentContainer();
        if (parent != null) {
            // when just resizing, don't fire a Container3DRearrangedEvent
            doLayoutLG(parent);
        }
    }
}
