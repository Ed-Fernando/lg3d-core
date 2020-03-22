/**
 * Project Looking Glass
 *
 * $RCSfile: ZLayeredMovableLayout.java,v $
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
 * $Date: 2006-08-24 16:59:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.appcontainer;

import org.jdesktop.lg3d.utils.c3danimation.Component3DAnimationFactory;
import org.jdesktop.lg3d.utils.eventaction.Component3DMover;
import org.jdesktop.lg3d.utils.eventaction.Component3DRotator;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import java.util.HashMap;


/**
 * This layout manager extends the feature of ZLayeredLayout by adding
 * a capability to move contained components.  If a component is 
 * Frame3D or its subclass, this adds a bahavior to request to move it
 * front when clicked.
 */
public class ZLayeredMovableLayout extends ZLayeredLayout {
    private HashMap<Component3D,Cursor3D> cursorInfo
        = new HashMap<Component3D,Cursor3D>();
    private Component3DMover componentMover;
    // Component3DRotator is added for testing purpose only.  will be removed.
    private Component3DRotator componentRotator;
    
    public ZLayeredMovableLayout(float spacing) {
	super(spacing);
        
        componentMover = new Component3DMover();
        componentRotator = new Component3DRotator(org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId.CTRL);
    }
    
    public ZLayeredMovableLayout(float spacing, Component3DAnimationFactory c3daFactory) {
        super(spacing, c3daFactory);
        
        componentMover = new Component3DMover();
        componentRotator = new Component3DRotator();
    }
    
    /**
     * Set the container being laid out
     * @param cont The container to layout
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#setContainer(org.jdesktop.lg3d.wg.Container3D)
     */
    public void setContainer(Container3D cont) {
        super.setContainer(cont);
        componentMover.setContainer(cont);
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
        comp.addListener(componentRotator);
        Cursor3D cursor = comp.getCursor();
        if (cursor == null) {
            comp.setCursor(Cursor3D.MOVE_CURSOR);
        }
        cursorInfo.put(comp, cursor);
    }
    
    /**
     * Remove a component from the layout. The spacing of the remaining components
     * is recalculated after removal.
     * @param comp The component to remove
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#removeLayoutComponent(org.jdesktop.lg3d.wg.Component3D)
     */
    public void removeLayoutComponent(Component3D comp) {
        comp.removeListener(componentMover);
        comp.removeListener(componentRotator);
        Cursor3D cursor = cursorInfo.remove(comp);
        comp.setCursor(cursor);
        
        super.removeLayoutComponent(comp);
    }
}
