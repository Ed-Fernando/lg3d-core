/**
 * Project Looking Glass
 *
 * $RCSfile: SpringTest.java,v $
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
 * $Date: 2005-09-29 18:58:34 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.test;
import org.jdesktop.lg3d.scenemanager.utils.springdamper.Spring;
import org.jdesktop.lg3d.scenemanager.utils.springdamper.SpringProcessorBehavior;
import org.jdesktop.lg3d.scenemanager.utils.springdamper.SprungComponent3D;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.shape.Box;
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 *
 * @author paulby
 */
public class SpringTest {
    
    /** Creates a new instance of SpringTest */
    public SpringTest() {
        Frame3D f3d = new Frame3D();
        
        //SpringProcessorBehavior.getSpringProcessorBehavior().singleStep(true);
        f3d.addListener(
            new MouseClickedEventAdapter(
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        SpringProcessorBehavior.getSpringProcessorBehavior().postId(SpringProcessorBehavior.STEP);
                    }

        }));

        
        SprungComponent3D comp1 = new SprungComponent3D();
        SprungComponent3D comp2 = new SprungComponent3D();
        SprungComponent3D comp3 = new SprungComponent3D();
        
        Spring spring = new Spring(comp1, comp2, 0.06f);
        Spring spring2 = new Spring(comp1, comp3, 0.07f);
        new Spring(comp2, comp3, 0.07f);
        
        Appearance app = new Appearance();
        
        comp1.addChild(new Sphere(0.01f));
        comp2.addChild(new Box(0.01f, 0.01f, 0.01f, app));
        comp3.addChild(new Sphere(0.015f));
        
        comp1.setTranslation(-0.02f,0.0f, 0.0f);
        comp2.setTranslation(0.02f, 0.0f, 0.0f);
        comp3.setTranslation(0.02f, 0.02f, 0.0f);
        
        f3d.addChild(comp1);
        f3d.addChild(comp2);
        f3d.addChild(comp3);
        
        f3d.setEnabled(true);
        f3d.setVisible(true);
        
    }
    
    public static void main(String args[]) {
        new SpringTest();
    }
}
