/**
 * Project Looking Glass
 *
 * $RCSfile: SatinGestureModule.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2006-03-01 19:31:24 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.gesture;

import edu.berkeley.guir.lib.satin.recognizer.Classification;
import edu.berkeley.guir.lib.satin.recognizer.Recognizer;
import edu.berkeley.guir.lib.satin.recognizer.rubine.RubineRecognizer;
import edu.berkeley.guir.lib.satin.stroke.TimedStroke;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.scenemanager.utils.event.Component3DGestureMoveLeftEvent;
import org.jdesktop.lg3d.scenemanager.utils.event.Component3DGestureMoveRightEvent;
import org.jdesktop.lg3d.scenemanager.utils.event.Component3DGestureFlipLeftEvent;

/**
 * Implementation of the Project Looking Glass gesture system on top
 * of the SATIN system from Berkley http://guir.berkeley.edu/projects/satin
 */
class SatinGestureModule extends GestureModuleBase {

    private Recognizer recognizer;
    
    public SatinGestureModule() {
        super();
        try {
            logger.fine("Loading Gesture data "+getClass().getResource("/resources/core-gestures.gsa"));
            Reader reader = new InputStreamReader( getClass().getResourceAsStream("/resources/core-gestures.gsa" ));
            recognizer = new RubineRecognizer( reader );
            logger.fine("Gesture recognizer configured");
        } catch(Exception e) {
            logger.log( Level.SEVERE, "Failed to load gesture definitions", e);
        }
    }
    
    /**
     * Convert the gestureEvents in SATIN TimedStrokes and see
     * if SATIN recognises any gestures
     */
    void processGesture() {
        logger.finer("Starting processGesture, eventQueue size "+gestureEvents.size());
        TimedStroke ts = new TimedStroke();
        
        /** SATIN has a FILTER_THRESHOLD constant which discards points that
         * are closer than 4, so we have to scale our very close points so that
         * they are not filtered.
         */
        float scale = 4000f;
        
        for( MouseMotionEvent3D evt : gestureEvents ) {
            ts.addPoint( evt.getImagePlateX()*scale, evt.getImagePlateY()*scale, evt.getWhen() );
        }
        
        Classification classification = recognizer.classify( ts );
        //System.out.println("Classification "+classification);
        logger.info("Gesture "+classification.getFirstKey());
        
        Frame3D startFrame3D = (Frame3D)startEvent.getIntersectedComponent3D(0, Frame3D.class);
        
        if (startFrame3D==null) return;
        
        String gestureName = (String)classification.getFirstKey();
        if (gestureName.equals("Left")) {
            logger.fine("Posting Component3DGestureMoveEvent LEFT");
            AppConnectorPrivate.getAppConnector().postEvent( 
                    new Component3DGestureMoveLeftEvent(), 
                    startFrame3D);
        } else if (gestureName.equals("Right")) {
            logger.fine("Posting Component3DGestureMoveEvent Right");
            AppConnectorPrivate.getAppConnector().postEvent( 
                    new Component3DGestureMoveRightEvent(), 
                    startFrame3D);
        } else if (gestureName.equals("ZoomInA")) {
            logger.fine("Posting GestureFlipLeftEvent");
            AppConnectorPrivate.getAppConnector().postEvent( 
                    new Component3DGestureFlipLeftEvent(), 
                    startFrame3D);
        } else if (gestureName.equals("FlipRight")) {
            logger.fine("Recognized FlipRight");
//            AppConnectorPrivate.getAppConnector().postEvent( 
//                    new GestureFlipLeftEvent(), 
//                    startFrame3D);
        }
    }
}