package org.jdesktop.lg3d.scenemanager.advanced.event;

import org.jdesktop.lg3d.scenemanager.advanced.AdvancedSceneControl;
import org.jdesktop.lg3d.wg.event.LgEvent;

public class CurrentVirtualSceneEvent extends LgEvent {
    private AdvancedSceneControl sceneControl;

    public CurrentVirtualSceneEvent(AdvancedSceneControl sceneControl) {
	this.sceneControl = sceneControl;
    }

    public AdvancedSceneControl getCurrentSceneControl() {
	return sceneControl;
    }
}
