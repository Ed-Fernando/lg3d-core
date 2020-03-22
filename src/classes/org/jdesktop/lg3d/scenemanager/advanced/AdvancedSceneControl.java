package org.jdesktop.lg3d.scenemanager.advanced;

import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.wg.Component3D;

public interface AdvancedSceneControl extends SceneControl {
    
    public AdvancedSceneControl[] getSceneControls();
    
    public void addComponent3DTo(AdvancedSceneControl sceneCntrl, Component3D comp3d);
    
    public AdvancedSceneControl getCurrentSceneControl();

}
