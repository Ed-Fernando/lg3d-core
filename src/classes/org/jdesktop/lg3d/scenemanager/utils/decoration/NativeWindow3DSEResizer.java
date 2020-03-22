/**
 * Project Looking Glass
 *
 * $RCSfile: NativeWindow3DSEResizer.java,v $
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
 * $Date: 2006-08-14 23:13:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.decoration;

import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindow3D;
import org.jdesktop.lg3d.wg.event.Component3DManualResizeEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.utils.eventaction.ListenerAggregator;
import org.jdesktop.lg3d.utils.action.ActionFloat3;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.eventadapter.MouseDragDistanceAdapter;
import javax.vecmath.Vector3f;


public class NativeWindow3DSEResizer extends ListenerAggregator {
    public NativeWindow3DSEResizer() {
        this(null);
    }
        
    public NativeWindow3DSEResizer(NativeWindow3D target) {
	Resizer action = new Resizer(target);
        setListeners(new LgEventListener[] {
            new MousePressedEventAdapter(action),
            new MouseDragDistanceAdapter(action)
        });
    }

    private static class Resizer implements ActionFloat3, ActionBoolean {
        private NativeWindow3D target;
        private float origX;
        private float origY;
        private float origW;
        private float origH;
        private Vector3f tmpV3f = new Vector3f();
        
        private Resizer(NativeWindow3D target) {
            this.target = target;
        }

        public void performAction(LgEventSource source, boolean state) {
            if (state) { // MOUSE_PRESSED
                target.getFinalTranslation(tmpV3f);
                origX = tmpV3f.x;
                origY = tmpV3f.y;
                origW = target.getBodyWidth();
                origH = target.getBodyHeight();
                target.postEvent(new Component3DManualResizeEvent(true));
            } else { // MOUSE_RELEASED -- done with the move
        	target.postEvent(new Component3DManualResizeEvent(false));
            }
        }
    
        public void performAction(LgEventSource source, float x, float y, float z) {
            // MOUSE_DRAGGED
            float newW = target.getBodyWidth() + x;
            float newH = target.getBodyHeight() - y;
            newW = origW + x;
            newH = origH - y;
            target.changeBodySize(newW, newH);
            // retrieve the size info since those might have been adjusted
            newW = target.getBodyWidth();
            newH = target.getBodyHeight();
            
            target.getFinalTranslation(tmpV3f);
            tmpV3f.x = origX + (newW - origW) * 0.5f;
            tmpV3f.y = origY - (newH - origH) * 0.5f;
            target.changeTranslation(tmpV3f, 0);
        }
    }
}

