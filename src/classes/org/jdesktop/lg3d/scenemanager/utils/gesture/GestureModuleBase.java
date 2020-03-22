/**
 * Project Looking Glass
 *
 * $RCSfile: GestureModuleBase.java,v $
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
 * $Date: 2006-02-28 17:15:33 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.gesture;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.vecmath.Point3f;
import org.jdesktop.lg3d.scenemanager.GestureModule;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.MouseDraggedEvent3D;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.utils.shape.ExtendableLine;

/**
 * A base implementation of the Gesture system, requires a concrete
 * specialisation to wrap the underlying gesture framework.
 *
 * This module collects 3D mouse event
 * and then posts lg3d events when it recognises a gesture.
 *
 * The SceneManager and User applications can register gesture shapes 
 * which should be tracked (TODO).
 *
 * @author  paulby
 */
public abstract class GestureModuleBase implements GestureModule, LgEventListener {
    protected static final Logger logger = Logger.getLogger("lg.gesture");
    protected ArrayList<MouseDraggedEvent3D> gestureEvents = new ArrayList<MouseDraggedEvent3D>();
    private boolean gestureStarted = false;
    private BranchGroup gestureBG;
    private BranchGroup detachableBG;
    private ExtendableLine line3D;
    private Point3f tmpP3f = new Point3f();
    /** The event which started the gesture */
    protected MouseButtonEvent3D startEvent;
    
    
    /** Creates a new instance of SatinGestureModule */
    public GestureModuleBase() {
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new TriggerListener());
    }
    
    public void processEvent( LgEvent evt ) {
        gestureEvents.add((MouseDraggedEvent3D)evt);
        
        if (line3D!=null) {
            line3D.addVertex( ((MouseDraggedEvent3D)evt).getCursorPosition(tmpP3f));                              
        }
    }
    
    public Class<LgEvent>[] getTargetEventClasses() {
        return new Class[] {MouseDraggedEvent3D.class};
    }
    
    /**
     * Start capturing data for a gesture
     *
     * @param evt the event that initiated the gesture
     */
    protected void startGesture(MouseButtonEvent3D evt) {
        synchronized( gestureEvents ) {
            if (gestureStarted) 
                return;
            LgEventConnector.getLgEventConnector().addListener(LgEventSource.ALL_SOURCES, this);
            gestureStarted = true;
        }
        startEvent = evt;
        
        logger.fine("startGesture triggered by button "+startEvent.getButton());
        
        if (gestureBG!=null) {
            detachableBG = new BranchGroup();
            detachableBG.setCapability(BranchGroup.ALLOW_DETACH);
            line3D = new ExtendableLine();
            detachableBG.addChild( line3D );
            gestureBG.addChild(detachableBG);
        }
    }
    
    /**
     * Finish capturing data and process the data collected so far.
     * Post event for any recognised gesture and clear the data queue
     */
    protected void endGesture() {
        synchronized(gestureEvents) {
            if (!gestureStarted) return;
            LgEventConnector.getLgEventConnector().removeListener(
                LgEventSource.ALL_SOURCES, this);
            
            processGesture();
            
            gestureEvents.clear();
            gestureStarted = false;
        }
        
        logger.fine("endGesture");
        
        if (gestureBG!=null) {
            detachableBG.detach();
            detachableBG = null;
            line3D = null;
        }
    }
    
    /**
     * Process the gesture data and post any resulting events.
     */
    abstract void processGesture();
    
    public static final GestureModule createGestureModule() {
        return new SatinGestureModule();
     }
    
    public void setModuleRoot( BranchGroup rootBG ) {
        gestureBG = rootBG;
    }
    
    /**
     * Listens for the event that triggers a gesture
     */
    class TriggerListener implements LgEventListener {
        public void processEvent( LgEvent evt ) {
//            logger.severe("GM Received event "+evt.getClass());
            MouseButtonEvent3D mevt = (MouseButtonEvent3D)evt;
            
            // Gesture recognition is too heavyweight to be done
            // in the event thread so we shoudl use a gesture thread to
            // asyncronously process the gesture data
            
            if (mevt.isPressed() 
                && mevt.getButton() == MouseButtonEvent3D.ButtonId.BUTTON2) 
            {
                startGesture(mevt);
            } else if (mevt.isReleased() 
                && mevt.getButton() == MouseButtonEvent3D.ButtonId.BUTTON2) 
            {
                endGesture();
            }
        }
        
        public Class<LgEvent>[] getTargetEventClasses() {
            return new Class[] {MouseButtonEvent3D.class};
        }
    }
}
