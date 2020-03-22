/**
 * Project Looking Glass
 *
 * $RCSfile: VerticalLayout.java,v $
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
 * $Date: 2005-12-02 17:06:46 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model.panel;

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
public class VerticalLayout implements LayoutManager3D {
    
    public enum AlignmentType { TOP, MIDDLE, BOTTOM };

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

    public VerticalLayout(AlignmentType policy, float spacing) {
        this.policy = policy;
        this.spacing = spacing;
    }

    public VerticalLayout(AlignmentType policy, float spacing,
            Component3DAnimationFactory c3daFactory) {
        this(policy, spacing);
        if (c3daFactory == null) {
            throw new IllegalArgumentException(
                    "the factory argument cannot be null");
        }
        this.c3daFactory = c3daFactory;
        c3dAnimMap = new HashMap<Component3D, Component3DAnimation>();
    }

    public void setContainer(Container3D cont) {
        this.cont = cont;
    }

    public void layoutContainer() {
        cont.getPreferredSize(tmpV3f);
        float width = tmpV3f.x;
        float height = tmpV3f.y;
        float depth = tmpV3f.z;

        float x = width * -0.5f;
        float y = 0.0f;
        float z = depth * 0.0f;

        switch (policy) {
        case MIDDLE: {
            y = getTotalHeight(cont) * -0.5f;
            break;
        }
        case BOTTOM: {
            y = height * -0.5f + spacing;
            break;
        }
        case TOP: {
            y = height * 0.5f + spacing - getTotalHeight(cont);
            break;
        }
        }

        for (Component3D comp : compList) {
            float scale = comp.getScale();
            comp.getPreferredSize(tmpV3f);
            float w = tmpV3f.y * 0.5f * scale;
            y += w;
            if (comp != compToSkip) {
                comp.changeTranslation(x + tmpV3f.x * 0.5f * scale, y, z);
            }
            y += w + spacing;
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

    private float getTotalHeight(Container3D cont) {
        float totalHeight = spacing;

        for (Component3D comp : compList) {
            float scale = comp.getScale();
            totalHeight += comp.getPreferredSize(tmpV3f).y * scale + spacing;
        }
        return totalHeight;
    }

    public void addLayoutComponent(Component3D comp, Object constraints) {
        comp.setCursor(Cursor3D.SMALL_CURSOR);
        synchronized (compList) {
            if (constraints != null && constraints instanceof Integer) {
                compList.add((Integer) constraints, comp);
            } else {
                compList.add(comp);
            }
        }
        if (c3daFactory != null) {
            Component3DAnimation prev = comp.getAnimation();
            Component3DAnimation c3da = c3daFactory.createInstance();
            comp.setAnimation(c3da);
            assert (c3dAnimMap != null);
            c3dAnimMap.put(comp, prev);
        }
    }

    public void removeLayoutComponent(Component3D comp) {
        synchronized (compList) {
            compList.remove(comp);
        }
        if (c3daFactory != null) {
            assert (c3dAnimMap != null);
            Component3DAnimation prev = c3dAnimMap.remove(comp);
            comp.setAnimation(prev);
        }
    }
    
    public int getIndexOf(Component3D comp) {
        return compList.indexOf(comp);
    }
    
    public Component3D getTopComponent() {
        return compList.get(0);
    }
    
    public Component3D getBottomComponent() {
        return compList.get(compList.size() - 1);
    }
    
    public int getComponentCount() {
        return compList.size();
    }

    public boolean rearrangeLayoutComponent(Component3D comp, Object constraints) {
        assert (compList.contains(comp));
        synchronized (compList) {
            if (constraints != null && constraints instanceof Integer) {
                int idx = (Integer) constraints;
                if (compList.indexOf(comp) == idx) {
                    return false;
                }
                compList.remove(comp);
                compList.add(idx, comp);
            } else {
                if (compList.indexOf(comp) == compList.size() - 1) {
                    return false;
                }
                compList.remove(comp);
                compList.add(comp);
            }
            return true;
        }
    }
}
