/**
 * Project Looking Glass
 *
 * $RCSfile: BookshelfLayout.java,v $
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
 * $Date: 2006-02-14 00:36:56 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.layoutmanager;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import org.jdesktop.lg3d.utils.c3danimation.Component3DAnimationFactory;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Component3DAnimation;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.LayoutManager3D;
import org.jdesktop.lg3d.wg.event.Component3DParkedEvent;

/**
 * This layout manager lays out Component3D objects just like books
 * on a book shelf.
 */
public class BookshelfLayout implements LayoutManager3D {
    private static final int animDuration = 400;
    
    private Container3D cont;
    private boolean leftside;
    private float zShift;
    private float angle;
    private float spacing;
    private ArrayList<Component3D> compList = new ArrayList<Component3D>();
    private HashMap<Component3D, AuxInfo> auxInfoMap
        = new HashMap<Component3D, AuxInfo>();
    private Component3DAnimationFactory c3daFactory = null;
    private Vector3f tmpV3f = new Vector3f();
    
    
    public BookshelfLayout(boolean leftside, float angle, 
        float zShift, float spacing) 
    {
        this.leftside = leftside;
        this.zShift = zShift;
        this.angle = angle;
	this.spacing = spacing;
    }
    
    public BookshelfLayout(boolean leftside, float angle, 
        float zShift, float spacing, Component3DAnimationFactory c3daFactory) 
    {
        this(leftside, angle, zShift, spacing);
        if (c3daFactory == null) {
            throw new IllegalArgumentException("the factory argument cannot be null");
        }
        this.c3daFactory = c3daFactory;
    }
    
    /**
     * Set the container being laid out
     * @param cont The container to layout
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#setContainer(org.jdesktop.lg3d.wg.Container3D)
     */
    public void setContainer(Container3D cont) {
        this.cont = cont;
    }
    
    /**
     * Layout a container in a bookshelf view based on the specified geometry.
     */
    public void layoutContainer() {
        float width = cont.getPreferredSize(tmpV3f).x;
        float dir = ((leftside)?(1.0f):(-1.0f));
        float x = width * -0.5f * dir;
        
	for (Component3D comp : compList) {
            comp.getPreferredSize(tmpV3f);
            float d = tmpV3f.z * 0.5f * dir;
            x += d;
            float z = zShift + tmpV3f.x * -0.5f;
            comp.getFinalTranslation(tmpV3f);
            comp.changeTranslation(x, tmpV3f.y, z, animDuration);
            x += spacing * dir;
            x += d;
	}
    }
    
    /**
     * Add a component to the layout. Adding a component causes the spacing
     * of all the components to be recalculated.
     * @param comp The component to add
     * @param constraints An optional <code>Integer</code> specifying the postion to add the component
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#addLayoutComponent(org.jdesktop.lg3d.wg.Component3D, java.lang.Object)
     */
    public void addLayoutComponent(Component3D comp, Object constraints) {
        AuxInfo info = new AuxInfo();
        comp.getFinalTranslation(tmpV3f);
        info.prevX = tmpV3f.x;
        info.prevY = tmpV3f.y;
        info.prevZ = tmpV3f.z;
        
        if (c3daFactory != null) {
            Component3DAnimation prev = comp.getAnimation();
            Component3DAnimation c3da = c3daFactory.createInstance();
            comp.setAnimation(c3da);
            info.animation = prev;
        }
        
        auxInfoMap.put(comp, info);
        compList.add(comp);
        
        comp.setRotationAxis(0.0f, 1.0f, 0.0f);
        float dir = ((leftside)?(1.0f):(-1.0f));
        comp.changeRotationAngle(angle * dir, animDuration);
        
        comp.postEvent(new Component3DParkedEvent(true));
    }
    
    /**
     * Remove a component from the layout. The spacing of the remaining components
     * is recalculated after removal.
     * @param comp The component to remove
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#removeLayoutComponent(org.jdesktop.lg3d.wg.Component3D)
     */
    public void removeLayoutComponent(Component3D comp) {
        compList.remove(comp);
        AuxInfo info = auxInfoMap.remove(comp);
        assert(info != null);
        
        if (c3daFactory != null) {
            comp.setAnimation(info.animation);
        }
        
        comp.changeTranslation(info.prevX, info.prevY, info.prevZ, animDuration);
        comp.changeRotationAngle(0.0f, animDuration);
        
        comp.postEvent(new Component3DParkedEvent(false));
    }
    
    /**
     * Move the specified component to the position specified by the constraints.
     */
    public boolean rearrangeLayoutComponent(Component3D comp, Object constraints) {
        if (constraints != null && constraints instanceof Integer) {
            int idx = (Integer)constraints;
            if (compList.indexOf(comp) == idx) {
                return false;
            }
            compList.remove(comp);
            compList.add(idx, comp);
        } else {
            if (compList.indexOf(comp) == 0) {
                return false;
            }
            compList.remove(comp);
            compList.add(0, comp);
        }
        return true;
    }
    
    /**
     * Private utility class used by the layout to remember a components
     * previous information before addition to the container. This
     * allows a component to be replaced in its original position
     * (and with the correct animation, should the orbit layout use
     * a custom animation factory) after it is removed from the container.
     */
    private static class AuxInfo {
        private float prevX = Float.NaN;
        private float prevY = Float.NaN;
        private float prevZ = Float.NaN;
        private Component3DAnimation animation = null;
    }
}
