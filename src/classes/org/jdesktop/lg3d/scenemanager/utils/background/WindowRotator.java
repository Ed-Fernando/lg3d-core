/**
 * Project Looking Glass
 *
 * $RCSfile: WindowRotator.java,v $
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
 * $Date: 2006-08-09 23:29:36 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.background;


import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.action.RotateActionFloat;
import org.jdesktop.lg3d.utils.actionadapter.Float2Scaler;
import org.jdesktop.lg3d.utils.actionadapter.Float2Splitter;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseDragDistanceAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventSource;


class WindowRotator extends Component3D {
    private static final int origHeight = 1331;
    private static final int origWidth = 649;
    
    private Component3D rootForChild;
    
    public WindowRotator(Component3D handle) {
        setName("WindowRotator");
        
        /*
         * this -> rotY -> rootForChild [-> children]
         */
        rootForChild = new Component3D();
        rootForChild.setAnimation(new NaturalMotionAnimation(2000));
        rootForChild.setRotationAxis(1.0f, 0.0f, 0.0f);
        final RotateActionFloat raX = new RotateActionFloat(rootForChild);
        
        Component3D rotY = new Component3D();
        rotY.setAnimation(new NaturalMotionAnimation(2000));
        rotY.setRotationAxis(0.0f, 1.0f, 0.0f);
        final RotateActionFloat raY = new RotateActionFloat(rotY);
        rotY.addChild(rootForChild);
        
        handle.addListener(
            new MouseDragDistanceAdapter(
                new Float2Scaler(100, -100,
                    new Float2Splitter(raY, raX))));
        
        // being very coservative, picking up all the mouse release and
        // reset the rotation
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new MousePressedEventAdapter(
                new ActionBoolean() {
                    public void performAction(LgEventSource source, boolean state) {
                        if (!state) { // mouse released
                            raY.performAction(source, 0.0f);
                            raX.performAction(source, 0.0f);
                        }
                    }}));
        
        super.addChild(rotY);
    }    
    
    // TODO -- reimplement the other children manipulation methods
    public void addChild(Node child) {
        rootForChild.addChild(child);
    }
    
    public void removeChild(Node child) {
        rootForChild.removeChild(child);
    }
    
    public void removeChild(int child) {
        rootForChild.removeChild(child);
    }
}
