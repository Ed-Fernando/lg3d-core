/**
 * Project Looking Glass
 *
 * $RCSfile: HorizontalReorderableLayout.java,v $
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
 * $Date: 2006-08-07 19:22:26 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.taskbar;

import org.jdesktop.lg3d.utils.eventaction.Component3DMover;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.utils.eventadapter.Component3DManualMoveEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseDragDistanceAdapter;
import org.jdesktop.lg3d.utils.c3danimation.Component3DAnimationFactory;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.action.ActionFloat3;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import javax.vecmath.Vector3f;
import java.util.ArrayList;


/**
 * HorizontalReorderableLayout extends HorizontalLayout by adding
 * the capability to reorder the items by mouse drag.
 */
public class HorizontalReorderableLayout extends HorizontalLayout {
    private static final long defaultAnimDuration = 175;
    
    private Component3DManualMoveEventAdapter manualMoveEventAdapter;
    private MouseDragDistanceAdapter dragEventAdapter;
    private Component3DMover componentMover;
    private Vector3f compOrigLoc = new Vector3f();
    
    public HorizontalReorderableLayout(AlignmentType policy, float spacing) {
        super(policy, spacing);
        initialize();
    }
    
    public HorizontalReorderableLayout(AlignmentType policy, float spacing, Component3DAnimationFactory c3daFactory) {
        super(policy, spacing, c3daFactory);
        initialize();
    }
    
    private void initialize() {
        componentMover = new Component3DMover(false);
        
        manualMoveEventAdapter = new Component3DManualMoveEventAdapter(
            new ActionBoolean() {
                public void performAction(LgEventSource source, boolean started) {
                    assert(source instanceof Component3D);
                    Component3D comp = (Component3D)source;
                    if (started) {
                        setComponentToSkipLayout(comp);
                        comp.setCursor(Cursor3D.SMALL_MOVE_CURSOR);
                        comp.getTranslation(compOrigLoc);
                    } else {
                        setComponentToSkipLayout(null);
                        comp.setCursor(Cursor3D.SMALL_CURSOR);
                        getTargetContainer().revalidate();
                    }
                }
            });
            
        dragEventAdapter = new MouseDragDistanceAdapter(
            new ActionFloat3() {
                public void performAction(LgEventSource source, 
                    float x, float y, float z) 
                {
                    x += compOrigLoc.x;
                    assert(source instanceof Component3D);
                    Component3D comp = (Component3D)source;
                    ArrayList<Component3D> compList = getManagedComponents();
                    Vector3f tmpV3f = new Vector3f();
                    synchronized (compList) {
                        int idx = compList.indexOf(comp);
                        int size = compList.size();
                        int newIdx = idx;
                        for (int i = idx - 1; i >= 0; i--) {
                            Component3D c = compList.get(i);
                            c.getFinalTranslation(tmpV3f);
                            if (tmpV3f.x < x) {
                                break;
                            }
                            newIdx = i;
                        }
                        if (newIdx == idx) {
                            for (int i = idx + 1; i < size; i++) {
                                Component3D c = compList.get(i);
                                c.getFinalTranslation(tmpV3f);
                                if (tmpV3f.x > x) {
                                    break;
                                }
                                newIdx = i;
                            }
                        }
                        if (newIdx != idx) {
                            getTargetContainer().rearrangeChildLayout(comp, newIdx);
                        }
                    }
                }
            });
    }
    
    /**
     * Add a component to the layout. Adding a component causes the spacing
     * of all the components to be recalculated.
     * @param comp The component to add
     * @param constraints An optional <code>Integer</code> specifying the postion to add the component
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#addLayoutComponent(org.jdesktop.lg3d.wg.Component3D, java.lang.Object)
     */
    public void addLayoutComponent(Component3D comp, Object constraints) {
        super.addLayoutComponent(comp, constraints);
        
        comp.addListener(componentMover);
        comp.addListener(manualMoveEventAdapter);
        comp.addListener(dragEventAdapter);
    }
    
    /**
     * Remove a component from the layout. The spacing of the remaining components
     * is recalculated after removal.
     * @param comp The component to remove
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#removeLayoutComponent(org.jdesktop.lg3d.wg.Component3D)
     */
    public void removeLayoutComponent(Component3D comp) {
        comp.removeListener(componentMover);
        comp.removeListener(manualMoveEventAdapter);
        comp.removeListener(dragEventAdapter);
        
        super.removeLayoutComponent(comp);
    }
}

