/**
 * Project Looking Glass
 *
 * $RCSfile: ZLayeredLayout.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2006-08-14 19:03:16 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.appcontainer;

import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.LayoutManager3D;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import org.jdesktop.lg3d.utils.c3danimation.Component3DAnimationFactory;
import org.jdesktop.lg3d.wg.Component3DAnimation;

/**
 * This layout manager lays out Component3D objects along with the Z axis
 * with inserting a specified space between two objects.
 * The object whose index is 0 in the compList is located at Z == 0 
 * and the other objects are located in order along the Z axis toward
 * lesser Z value.
 */
public class ZLayeredLayout implements LayoutManager3D {
    private Container3D cont;
    private float spacing;
    private ArrayList<Component3D> compList = new ArrayList<Component3D>();
    // FIXME -- it is inefficient to have one more HashMap, 
    // but works for now...
    private HashMap<Component3D, Component3DAnimation> c3dAnimMap = null;
    private Component3DAnimationFactory c3daFactory = null;
    private Vector3f c1Trans = new Vector3f();
    private Vector3f c1Size = new Vector3f();
    private Vector3f c2Trans = new Vector3f();
    private Vector3f c2Size = new Vector3f();
    
    public ZLayeredLayout(float spacing) {
	this.spacing = spacing;
    }
    
    public ZLayeredLayout(float spacing, Component3DAnimationFactory c3daFactory) {
	this(spacing);
        if (c3daFactory == null) {
            throw new IllegalArgumentException("the factory argument cannot be null");
        }
        this.c3daFactory = c3daFactory;
        c3dAnimMap = new HashMap<Component3D, Component3DAnimation>();
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
     * Layout a container in a Z-layerd manner based on the specified geometry.
     */
    public void layoutContainer() {
        float contHeight = cont.getPreferredSize(c1Trans/*temporary use*/).y * 0.5f;
        
        // sort the components in terms of Z order
        for (int i = 0; i < compList.size(); i++) {
            Component3D comp = compList.get(i);
            float z1 = comp.getFinalTranslation(c1Trans).z;
            z1 += 0.5f * getCompSize(comp, c1Size).z;
            for (int j = 0; j < i; j++) {
                Component3D c2 = compList.get(j);
                float z2 = c2.getFinalTranslation(c2Trans).z;
                z2 += 0.5f * getCompSize(c2, c2Size).z;
                if (z2 < z1) {
                    compList.remove(i);
                    compList.add(j, comp);
                    break;
                }
            }
        }
        
        // reposition the components
        boolean reordered = false;
        for (Component3D comp : compList) {
            if (!comp.isFinalVisible()) {
                continue;
            }
            comp.getFinalTranslation(c1Trans);
            getCompSize(comp, c1Size);
            
            float z = 0.0f;
            for (Component3D c2 : compList) {
                if (c2 == comp) {
                    break;
                }
                if (!c2.isFinalVisible()) {
                    continue;
                }
                c2.getFinalTranslation(c2Trans);
                getCompSize(c2, c2Size);
                
                if (c1Trans.x - 0.5f * c1Size.x > c2Trans.x + 0.5f * c2Size.x
                        || c1Trans.x + 0.5f * c1Size.x < c2Trans.x - 0.5f * c2Size.x
                        || c1Trans.y - 0.5f * c1Size.y > c2Trans.y + 0.5f * c2Size.y
                        || c1Trans.y + 0.5f * c1Size.y < c2Trans.y - 0.5f * c2Size.y) {
                    continue;
                }
                float c2z = c2Trans.z - c2Size.z - spacing;
                if (c2z < z) {
                    z = c2z;
                }
            }
            if (z == 0.0f) {
                // avoid the top part of Frame3D goes out of the screen
                if (c1Size.y > 0.0f && c1Trans.y + 0.5f * c1Size.y > contHeight) {
                    c1Trans.y = contHeight - 0.5f * c1Size.y;
                }
            }
            
            z -= 0.5f * c1Size.z;
            // keep the Z depth is the comp seems to be pushed away manually
            if (z < c1Trans.z + spacing * 2.0f && !(z == c1Trans.z)) {
                if (Math.abs(c1Trans.z - z) > spacing) {
                    reordered = true;
                }
                c1Trans.z = z;
                comp.changeTranslation(c1Trans, 300);
            }
	}
        if (reordered) {
            cont.postEvent(new AppContainerReorderedEvent());
        }
    }
    
    private Vector3f getCompSize(Component3D comp, Vector3f ret) {
        if (comp instanceof Container3D) {
            Component3D deco = ((Container3D)comp).getDecoration();
            if (deco != null && deco.getPreferredSize(ret).y > 0.0f) {
                // ret already holds deco's preferredSize
            } else {
                comp.getPreferredSize(ret);
            }
        } else {
            comp.getPreferredSize(ret);
        }
        return ret;    
    }
    
    /**
     * Add a component to the layout. Adding a component causes the spacing
     * of all the components to be recalculated.
     * @param comp The component to add
     * @param constraints An optional <code>Integer</code> specifying the postion to add the component
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#addLayoutComponent(org.jdesktop.lg3d.wg.Component3D, java.lang.Object)
     */
    public void addLayoutComponent(Component3D comp, Object constraints) {
        compList.add(0, comp);
        if (c3daFactory != null) {
            Component3DAnimation prev = comp.getAnimation();
            Component3DAnimation c3da = c3daFactory.createInstance();
            comp.setAnimation(c3da);
            assert(c3dAnimMap != null);
            c3dAnimMap.put(comp, prev);
        }
        cont.postEvent(new AppContainerReorderedEvent());
    }
    
    /**
     * Remove a component from the layout. The spacing of the remaining components
     * is recalculated after removal.
     * @param comp The component to remove
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#removeLayoutComponent(org.jdesktop.lg3d.wg.Component3D)
     */
    public void removeLayoutComponent(Component3D comp) {
        compList.remove(comp);
        if (c3daFactory != null) {
            assert(c3dAnimMap != null);
            Component3DAnimation prev = c3dAnimMap.remove(comp);
            comp.setAnimation(prev);
        }
        cont.postEvent(new AppContainerReorderedEvent());
    }
    
    /**
     * Move the specified component to the position indexed by the constraints.
     */
    public boolean rearrangeLayoutComponent(Component3D comp, Object constraints) {
        assert(compList.contains(comp));
        int i = compList.indexOf(comp);
        if (i == -1) {
            return false;
        }
        int idx = 0;
        if (constraints != null && constraints instanceof Integer) {
            idx = (Integer)constraints;
            if (idx >= compList.size()) {
                // add to the last
                idx = compList.size() - 1;
            }
        }
        compList.remove(i);
        compList.add(idx, comp);
        if (idx == 0) {
            // make sure to move this component to the top
            comp.getFinalTranslation(c1Trans);
            getCompSize(comp, c1Size);
            c1Trans.z = -0.5f * c1Size.z + 0.001f;
            comp.changeTranslation(c1Trans, 1000);
        }
        return true;
    }
}
