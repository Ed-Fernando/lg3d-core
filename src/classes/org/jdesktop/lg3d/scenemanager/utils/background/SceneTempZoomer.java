/**
 * Project Looking Glass
 *
 * $RCSfile: SceneTempZoomer.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-08-14 19:03:14 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.background;


import org.jdesktop.lg3d.scenemanager.utils.appcontainer.AppContainer;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.AppContainerReorderedEvent;
import org.jdesktop.lg3d.utils.action.ActionFloat2;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.actionadapter.ActionSwitcher;
import org.jdesktop.lg3d.utils.actionadapter.BooleanToNoArgConverter;
import org.jdesktop.lg3d.utils.animation.SpringScaleAnimationFloat;
import org.jdesktop.lg3d.utils.eventadapter.GenericEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseDragDistanceAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.wg.AnimationGroup;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventSource;


class SceneTempZoomer extends AnimationGroup {
    private ActionSwitcher zoomActionListener;
    
    public SceneTempZoomer(Component3D handle, final float zoomOutLevel) {
        setName("SceneTempZoomer");
        
        final SpringScaleAnimationFloat bgScaleAnim 
                = new SpringScaleAnimationFloat(4000);
        setAnimation(bgScaleAnim);
        
        zoomActionListener = new ActionSwitcher(bgScaleAnim);
        
        ActionNoArg tempBgScaleAction 
            = new ActionNoArg() {
                public void performAction(LgEventSource source) { 
                    zoomActionListener.performAction(source, -0.1f * zoomOutLevel);
                }                
            }; 
          
        LgEventConnector.getLgEventConnector().addListener(
            AppContainer.class,
            new GenericEventAdapter(AppContainerReorderedEvent.class,
                tempBgScaleAction));
            
        handle.addListener(
            new MouseDragDistanceAdapter(
                new ActionFloat2() {
                    public void performAction(LgEventSource source, 
                        float x, float Y) 
                    {
                        zoomActionListener.performAction(source, -0.05f);
                    }
                }));
        
        // be convervative -- reset the animation whenever the mouse
        // button is released.
        handle.addListener(
            new MousePressedEventAdapter(
                new BooleanToNoArgConverter(false/*released*/,
                    new ActionNoArg() {
                        public void performAction(LgEventSource source) { 
                            zoomActionListener.performAction(source, -0.4f);
                        }                
                    })));
    }
    
    public void setEnabled(boolean enabled) {
        zoomActionListener.setEnabled(enabled);
    }
}
