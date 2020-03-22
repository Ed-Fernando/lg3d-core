package org.jdesktop.lg3d.scenemanager.advanced.layout;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.wg.Container3D;

public class HorizontalSceneLayout implements SceneLayoutManager {

    private ArrayList<Container3D> desktopList = new ArrayList<Container3D>();
    private Vector3f tmpV3f = new Vector3f();
    private Container3D defaultDesktop[] = new Container3D[0];
    private float SPACING = 50.0f;
    
    public void layoutScene() {	
	int count = 0;
	
	if (defaultDesktop[0] != null) {
	    defaultDesktop[0].getPreferredSize(tmpV3f);
	    for (Container3D desktop : defaultDesktop) {
		desktop.setTranslation(tmpV3f.x*count, 0.0f, 0.0f);
		desktop.setVisible(true);
		count++;
	    }
	    
	} else {
	    count = 0;
	    if (desktopList.size() > 0) {
		desktopList.get(0).getPreferredSize(tmpV3f);
	    }
	}
	int i = 0;
	for (Container3D desktop : desktopList) {
	    if ((i < defaultDesktop.length) && (desktop == defaultDesktop[i])) {
		i++;
		continue;
	    }
	    desktop.setVisible(false);
	    desktop.setTranslation(tmpV3f.x*count + SPACING, 0.0f, 0.0f);
	    count++;
	}
    }

    public void setDefaultScene(Container3D[] desktop) {
	defaultDesktop = desktop;
    }

    public void addLayoutScene(Container3D desktop, Object constraints) {
	
	desktopList.add(desktop);
    }

    public void removeLayoutScene(Container3D desktop) {	
	
    }

    public boolean rearrangeLayoutScene(Container3D desktop, Object newConstraints) {	
	return false;
    }
    
}
