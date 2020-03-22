/**
 * Project Looking Glass
 *
 * $RCSfile: HorizontalLayout.java,v $
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
 * $Date: 2006-08-07 19:22:27 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.taskbar;

import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.LayoutManager3D;
import org.jdesktop.lg3d.wg.Component3DAnimation;
import org.jdesktop.lg3d.utils.c3danimation.Component3DAnimationFactory;
import java.util.ArrayList;
import java.util.HashMap;
import javax.vecmath.Vector3f;


/**
 *
 */
public class HorizontalLayout implements LayoutManager3D {
    public enum AlignmentType {CENTER, LEFT, RIGHT};
    
    private Container3D cont;
    private AlignmentType policy;
    private float spacing;
    private ArrayList<Component3D> compList = new ArrayList<Component3D>();
    // FIXME -- it is inefficient to have one more HashMap, 
    // but works for now...
    private HashMap<Component3D, Component3DAnimation> c3dAnimMap = null;
    private Component3DAnimationFactory c3daFactory = null;
    private Component3D compToSkip = null;
    private Vector3f tmpV3f = new Vector3f();
    
    public HorizontalLayout(AlignmentType policy, float spacing) {
        this.policy = policy;
	this.spacing = spacing;
    }
    
    public HorizontalLayout(AlignmentType policy, float spacing, Component3DAnimationFactory c3daFactory) {
        this(policy, spacing);
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
     * Layout a container in a horizontal manner based on the specified geometry.
     */
    public void layoutContainer() {
        cont.getPreferredSize(tmpV3f);
	float width = tmpV3f.x;
	float height = tmpV3f.y;
	float depth = tmpV3f.z;

	float x = 0.0f;
	float y = height * -0.5f;
	float z = depth * 0.0f;

	switch (policy) {
	    case CENTER: {
		x = getTotalWidth(cont) * -0.5f; 
		break;
	    }
	    case LEFT: {
		x = width * -0.5f + spacing;
		break;
	    }
	    case RIGHT: {
		x = width * 0.5f + spacing - getTotalWidth(cont);
		break;
	    }
	}
        
        for (Component3D comp : compList) {
            float scale = comp.getScale();
            comp.getPreferredSize(tmpV3f);
            float w = tmpV3f.x * 0.5f * scale;
	    x += w;
            if (comp != compToSkip) {
                comp.changeTranslation(x, y + tmpV3f.y * 0.5f * scale, z);
            }
	    x += w + spacing;
	}
    }

    protected void setComponentToSkipLayout(Component3D compToSkip) {
        this.compToSkip = compToSkip;
    }
    
    protected Component3D getComponentToSkipLayout() {
        return compToSkip;
    }
    
    protected Container3D getTargetContainer() {
        return cont;
    }
    
    protected ArrayList<Component3D> getManagedComponents() {
        return compList;
    }
    
    protected AlignmentType getAlignmentPolicy() {
        return policy;
    }
    
    protected float getSpacing() {
        return spacing;
    }
    
    private float getTotalWidth(Container3D cont) {
	float totalWidth = spacing;
        
        for (Component3D comp : compList) {
            float scale = comp.getScale();
	    totalWidth += comp.getPreferredSize(tmpV3f).x * scale + spacing;
	}
	return totalWidth;
    }
    
    /**
     * Add a component to the layout. Adding a component causes the spacing
     * of all the components to be recalculated.
     * @param comp The component to add
     * @param constraints An optional <code>Integer</code> specifying the postion to add the component
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#addLayoutComponent(org.jdesktop.lg3d.wg.Component3D, java.lang.Object)
     */
    public void addLayoutComponent(Component3D comp, Object constraints) {
        comp.setCursor(Cursor3D.SMALL_CURSOR);
        synchronized (compList) {
            if (constraints != null && constraints instanceof Integer) {
                compList.add((Integer)constraints, comp);
            } else {
                compList.add(comp);
            }
        }
        if (c3daFactory != null) {
            Component3DAnimation prev = comp.getAnimation();
            Component3DAnimation c3da = c3daFactory.createInstance();
            comp.setAnimation(c3da);
            assert(c3dAnimMap != null);
            c3dAnimMap.put(comp, prev);
        }
    }
    
    /**
     * Remove a component from the layout. The spacing of the remaining components
     * is recalculated after removal.
     * @param comp The component to remove
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#removeLayoutComponent(org.jdesktop.lg3d.wg.Component3D)
     */
    public void removeLayoutComponent(Component3D comp) {
        synchronized (compList) {
            compList.remove(comp);
        }
        if (c3daFactory != null) {
            assert(c3dAnimMap != null);
            Component3DAnimation prev = c3dAnimMap.remove(comp);
            if(prev != null) {
                comp.setAnimation(prev);
            }
        }
    }
    
    /**
     * Move the specified component to the position indexed by the constraints.
     */
    public boolean rearrangeLayoutComponent(Component3D comp, Object constraints) {
        assert(compList.contains(comp));
        synchronized (compList) {
            if (constraints != null && constraints instanceof Integer) {
                int idx = (Integer)constraints;
                if (compList.indexOf(comp) == idx) {
                    return false;
                }
                compList.remove(comp);
                compList.add(idx, comp);
            } else {
                if (compList.indexOf(comp) == compList.size() -1) {
                    return false;
                }
                compList.remove(comp);
                compList.add(comp);
            }
            return true;
        }
    }
}

