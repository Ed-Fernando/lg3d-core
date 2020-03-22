/**
 * Project Looking Glass
 *
 * $RCSfile: NativeWindow3DSResizer.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2006-08-14 23:13:22 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.decoration;

import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindow3D;
import org.jdesktop.lg3d.wg.event.Component3DManualResizeEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.utils.action.ActionFloat3;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.eventaction.ListenerAggregator;
import org.jdesktop.lg3d.utils.eventadapter.MouseDragDistanceAdapter;
import javax.vecmath.Vector3f;


public class NativeWindow3DSResizer extends ListenerAggregator {
    public NativeWindow3DSResizer() {
        this(null);
    }
        
    public NativeWindow3DSResizer(NativeWindow3D target) {
	Resizer action = new Resizer(target);
        setListeners(new LgEventListener[] {
            new MousePressedEventAdapter(action),
            new MouseDragDistanceAdapter(action)
        });
    }

    private static class Resizer implements ActionFloat3, ActionBoolean {
        private NativeWindow3D target;
        private float origY;
        private float origH;
        private Vector3f tmpV3f = new Vector3f();
        
        private Resizer(NativeWindow3D target) {
            this.target = target;
        }

        public void performAction(LgEventSource source, boolean state) {
            if (state) { // MOUSE_PRESSED
                target.getFinalTranslation(tmpV3f);
                origY = tmpV3f.y;
                origH = target.getBodyHeight();
                target.postEvent(new Component3DManualResizeEvent(true));
            } else { // MOUSE_RELEASED -- done with the move
        	target.postEvent(new Component3DManualResizeEvent(false));
            }
        }
    
        public void performAction(LgEventSource source, float x, float y, float z) {
            float newH = target.getBodyHeight() - y;
            newH = origH - y;
            target.changeBodySize(target.getBodyWidth(), newH);
            // retrieve the size info since those might have been adjusted
            newH = target.getBodyHeight();
            
            target.getFinalTranslation(tmpV3f);
            tmpV3f.y = origY - (newH - origH) * 0.5f;
            target.changeTranslation(tmpV3f, 0);
        }
    }
}
