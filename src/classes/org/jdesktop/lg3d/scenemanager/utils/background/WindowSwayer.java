/**
 * Project Looking Glass
 *
 * $RCSfile: WindowSwayer.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-08-15 20:09:48 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.background;


import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.utils.action.ActionFloat2;
import org.jdesktop.lg3d.utils.action.RotateActionFloat;
import org.jdesktop.lg3d.utils.actionadapter.ActionSwitcher;
import org.jdesktop.lg3d.utils.actionadapter.Float2Differ;
import org.jdesktop.lg3d.utils.actionadapter.Float2Scaler;
import org.jdesktop.lg3d.utils.actionadapter.Float2Splitter;
import org.jdesktop.lg3d.utils.smoother.SpringFloatSmoother;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseMotionEventAdapter;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;


class WindowSwayer extends Component3D {
    private Component3D rootForChild;
    private Component3D rotX;
    private Component3D rotY;
    private ActionSwitcher swayActionListener;
    
    public WindowSwayer(Component3D sensitiveArea, float level, float dist) {
        setName("WindowSwayer");
        /*
         * this -> rotY -> rotX -> rootForChild [-> children]
         */
        rootForChild = new Component3D();
        rootForChild.setTranslation(0.0f, 0.0f, -dist);
        
        NaturalMotionAnimation nmax = new NaturalMotionAnimation(8000);
        nmax.setRotationAngleSmoother(new SpringFloatSmoother());
        rotX = new Component3D();
        rotX.setAnimation(nmax);
        rotX.setRotationAxis(-1.0f, 0.0f, 0.0f);
        rotX.addChild(rootForChild);
        
        NaturalMotionAnimation nmay = new NaturalMotionAnimation(8000);
        nmay.setRotationAngleSmoother(new SpringFloatSmoother());
        rotY = new Component3D();
        rotY.setAnimation(nmay);
        rotY.setRotationAxis(0.0f, 1.0f, 0.0f);
        rotY.addChild(rotX);
        rotY.setTranslation(0.0f, 0.0f, dist);
        
        swayActionListener
            = new ActionSwitcher(
                new Float2Differ(
                    new Float2Scaler(
                        6.0f * level, 
                        3.0f * level,
                        (float)Math.toRadians(10),
                        (float)Math.toRadians(5),
                        new Float2Splitter(
                            new RotateActionFloat(rotY),
                            new RotateActionFloat(rotX)))));
        
        sensitiveArea.addListener(
            new MouseMotionEventAdapter(
                (ActionFloat2)swayActionListener));
        
        // make sure to post MouseMotionEvent3D
        setMouseEventSource(MouseMotionEvent3D.class, true);
        setMouseEventPropagatable(true);
        
        super.addChild(rotY);
    }
    
    public void setEnabled(boolean enabled) {
        swayActionListener.setEnabled(enabled);
        if (!enabled) {
            rotX.setRotationAngle(0.0f);
            rotY.setRotationAngle(0.0f);
        }
    }
    
    // TODO -- reimplement the other children manipulation methods
    public void addChild(Node child) {
        rootForChild.addChild(child);
    }
}
