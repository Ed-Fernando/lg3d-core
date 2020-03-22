/**
 * Project Looking Glass
 *
 * $RCSfile: LowLevelX11Picker.java,v $
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
 * $Revision: 1.26 $
 * $Date: 2007-04-10 22:58:41 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.PickInfo;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import com.sun.j3d.utils.pickfast.PickCanvas;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dNativePane;
import org.jdesktop.lg3d.displayserver.fws.x11.WinSysX11;
import gnu.x11.Display;
import gnu.x11.Input;
import gnu.x11.Window;
import gnu.x11.event.Event;
import gnu.x11.event.MotionNotify;
import gnu.x11.event.ButtonPress;
import gnu.x11.event.ButtonRelease;
import gnu.x11.event.ClientMessage;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dCursor3D;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dComponent3D;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dLgBranchGroup;
import org.jdesktop.lg3d.scenemanager.CursorModule;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEnteredEvent3D;
import org.jdesktop.lg3d.wg.event.MouseWheelEvent3D;
import org.jdesktop.lg3d.wg.event.MouseMovedEvent3D;
import org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.SceneGraphObject;

// Added for Behavior modei
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.WakeupOnElapsedFrames;

/**
 * Low level picker for X11 integration. This is only
 * used if the FoundationWinSys concrete class is WinSysX11
 *
 * This will be moved into another class to maintain the seperation
 * for the LG core and the underlying Window System.
 */

//Thread Mode (currently inactive)
//public class LowLevelX11Picker extends Thread {

// Added for Behavior mode
public class LowLevelX11Picker extends Behavior {

    protected java.util.logging.Logger logger = java.util.logging.Logger.getLogger("lg.fws");
    
    private HashMap<Canvas3D, CanvasCache> pickerMap = new HashMap();
    
    private int[] intersectionPt = new int[2];

    private WinSysX11 wsx;
    private boolean firstEvent = true;
    private CanvasCache canvasCache;

    private DeviceEventSource deviceEventSource;    
    private Display devDpy;
    private ExtensionSet exts;

    private Canvas3D[] canvases;
    private long[] prws;
    private HashMap<Canvas3D,Long> canvasList = new HashMap<Canvas3D,Long>();

    // The picking information for the most recent pick operation
    PickInfo[] pickInfos = null;

    // Pick sequence number range (0 is invalid)
    private static final int LG3D_PICK_SEQ_MIN = 0x1;
    private static final int LG3D_PICK_SEQ_MAX = 0xffff;

    protected int nextPickSeq = LG3D_PICK_SEQ_MIN;

    private CursorModule cursorModule;
    private Vector3f tmpV3f = new Vector3f();

    // Added for Behavior mode
    private WakeupOnElapsedFrames wakeupCondition = new WakeupOnElapsedFrames( 0, true );

    // Print frame rate approximately every minute
    private static final int FRAME_COUNTER_MAX = 100;
    
    private boolean frameCounterEnable;

    private int frameCounter;
    private long timeStart;

    private void frameCounterInit () {
	frameCounter = 0;
	timeStart = System.nanoTime();
    }

    private void frameCounterTick () {
	frameCounter++;
	if (frameCounter >= FRAME_COUNTER_MAX) {
	    long timeStop = System.nanoTime();
	    long timeDeltaNS = timeStop - timeStart;
	    double timeDeltaS = (double)timeDeltaNS / 1000000000.0;
	    double frameRate = (double)frameCounter / timeDeltaS;

	    System.err.println("Frame rate (fps) = " + frameRate);

	    // For debug
            // damageEventCounterPrint();

	    // Reset counter and time
	    frameCounterInit();
	}
    }

    private int damageEventCounter = 0;

    synchronized void damageEventCounterIncr () {
	damageEventCounter++;
    }

    /* For debug
    private synchronized void damageEventCounterPrint () {
        System.err.println("damageEventCounter = " + damageEventCounter);
        System.err.println("frameCounter = " + frameCounter);
	float damageEventsPerFrame = (float)damageEventCounter / (float)frameCounter;
	System.err.println("Number of damage events per frame = " + damageEventsPerFrame);
	damageEventCounter = 0;
    }
    */

    // The scene graph path from the current pointer node upward to the root.

    public LowLevelX11Picker (WinSysX11 wsx, Display devDpy, 
			      DeviceEventSource deviceEventSource,
			      ExtensionSet exts, Canvas3D[] canvases, long[] prws) {

        //Thread Mode 
//        super( org.jdesktop.lg3d.displayserver.AppConnectorPrivate.getThreadGroup(),
//	       "LowLevelX11Picker");

	this.wsx = wsx;
	this.devDpy = devDpy;
	this.exts = exts;
	this.canvases = canvases;
	for (int i=0; i < prws.length; i++) {		
	    canvasList.put(canvases[i], prws[i]);		
	}
	this.deviceEventSource = deviceEventSource;

        //Thread Mode 
//        this.setDaemon(true);
//        this.start();

        wsx = (WinSysX11)FoundationWinSys.getFoundationWinSys();

	frameCounterEnable = System.getProperty("lg.fws.frameCounterEnable") != null;

	if (frameCounterEnable) {
	    frameCounterInit();
	}
    }

    // Added for Behavior mode
    public void initialize() {
        setSchedulingBounds( new BoundingSphere( new Point3d(), Double.POSITIVE_INFINITY )); 
        wakeupOn(wakeupCondition);
    }

    private boolean findIntersectionPoint (PickInfo pi, Point3d eyePos, 
					   J3dNativePane nativePane,
					   int[] intPtWinCoords) {
	try {

	    // Get intersection point in object coordinates
	    Point3d intPt = pi.getClosestIntersectionPoint();

	    float intX = (float) intPt.x;
	    float intY = -(float) intPt.y;

	    //System.err.println("intXY = " + intX + ", " + intY);
 
	    float paneWidth = nativePane.getWidth();
	    float paneHeight = nativePane.getHeight();

	    //System.err.println("paneWH = " + paneWidth + ", " + paneHeight);

	    int nativeWidth  = nativePane.getNativeWidth();
	    int nativeHeight = nativePane.getNativeHeight();

	    //System.err.println("nativeWH = " + nativeWidth + ", " + nativeHeight);

	    intPtWinCoords[0] = (int)((intX / paneWidth)  * nativeWidth);
	    intPtWinCoords[1] = (int)((intY / paneHeight) * nativeHeight);

	    //System.err.println("intPtWin = " + intPtWinCoords[0] + ", " + intPtWinCoords[1]);

	} catch (RuntimeException ex) {
	    // TODO: there is a (possible) Java3D bug which causes an "interpolation
	    // point outside quad" exception at seemingly random times. Ignore this
	    // exception; just generate a warning.
	    //logger.log(Level.WARNING,"Error in findIntersectionPoint " + ex, ex);
	    return false;
	}

	return true;
    }

    void bumpPickSeq () {
	if (++nextPickSeq >= LG3D_PICK_SEQ_MAX) {
	    nextPickSeq = LG3D_PICK_SEQ_MIN;
	}
    }

    //Thread Mode 
//    public void run() {
//       while(true) {
//          processStimulus(null);
//       }
//    }

    // TODO It might be better to have one low level picker per
    // canvas3D. This would avoid the hash map lookup for every
    // event

    public void processStimulus( java.util.Enumeration e ) {
        PickCanvas pickCanvas=null;
        Canvas3D canvas = null;
        DeviceEvent event = null;
        long prw = -1;

	// For debug
        //damageEventCounterPrint();
	
	if (frameCounterEnable) {
	    frameCounterTick();
	}

        // Added for behavior mode
        wakeupOn(wakeupCondition);

	// Process all events 
	// TODO: we may want to set an upper limit on this by either
	// double buffering the input queue or by using a max count
	while (true) {

	    try {
	    
		// Added for behavior mode
		if (!deviceInputEventAvailable()) {
		    return;
		}

		// Block waiting for a device event
		event = receiveDeviceInputEvent();
		
		// TODO: opt: can skip this lookup for key events?
		canvas = event.getCanvas();
		canvasCache = pickerMap.get( canvas );
		if (canvasCache==null) {
		    pickCanvas = new PickCanvas( canvas, wsx.getLocale() );
		    try {
			pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
			pickCanvas.setFlags(
			    PickInfo.NODE                       | 
			    PickInfo.LOCAL_TO_VWORLD            |
			    PickInfo.CLOSEST_INTERSECTION_POINT |
			    PickInfo.CLOSEST_DISTANCE);
		    } catch (Exception ex) {
			throw new RuntimeException("PickCanvas initialization failure" + ex);
		    }
		    Long prwval = canvasList.get(canvas);
		    if (prwval == null) {
			throw new RuntimeException("PRW window for given Canvas not found");
		    }
		    prw = prwval.longValue();
		    pickerMap.put( canvas, new CanvasCache( pickCanvas, prw ));
		} else {
		    pickCanvas = canvasCache.pickCanvas;
		    prw = canvasCache.wid;
		}
	    } catch( Exception e1 ) {
		logger.log(Level.SEVERE,"receiveEvent threw ", e1 );
	    }
            
	    // Perform the pick for mouse motion and button events. (We need 
	    // to pick for button events in case the geometry is animating). 
	    // Also perform the pick on first event seen no matter what its type.
	    //
	    // Note: The delivery of key events is controlled by the keyboard
	    // focus, not the current pointer window, so keyboard events
	    // just inherit the pick info of the previous mouse event
	    // (except if the key event is the first event encountered).

	    int x = event.root_x();
	    int y = event.root_y();

	    // Pick on motion and button events; key events inherit the pick infos
	    // of the previous motion or button event
	    if (event.isMotion() || 
		event.isButton() ||
		firstEvent) {

		firstEvent = false;
		pickCanvas.setShapeLocation(x, y);

		try {
		    pickInfos = pickCanvas.pickAllSorted();
		} catch (RuntimeException except) {
		    // TODO: Sometimes NullPointerException gets thrown in the 
		    // above pick operation.  This seems to be a Java 3D bug.
		    // Report the exception, but keep the system running.
		    //logger.log(Level.WARNING, "Error in performPick " + except, except);
		    logger.warning("Error in LowLevelX11Picker " + except);
		    pickInfos = null;
		}
	    } 

	    // Obtain the pickInfo of the hit node.  Null if nothing is picked.
	    PickInfo pickInfo = (pickInfos == null) ? null : pickInfos[0];

	    // Motion events update the cursor and may change the current pointer node path
	    if (event.code() == MotionNotify.CODE) {
		//System.err.println("event time = " + event.time());
		if (cursorModule != null) {
		    updateCursorPosition(pickInfo, x, y, canvas);
		}
	    }

	    if (pickInfo == null) {

		logger.fine("Pick missed");
		EventInfo3D evinfo = hit3DOrMiss(event, prw, x, y, null);
		sendDeviceInputEvent(event, evinfo);

	    } else {
		J3dNativePane nativePane = checkForNativePane( pickInfo );
                    
		// If we started dragging outside the nativePane and continue to
		// drag into the nativePane then process the event as if it's
		// a 3D object not a nativePane

		Point3d eyePos = pickCanvas.getStartPosition();

		if (nativePane != null) {
		    logger.fine("Pick hit native window");
		    long wid = nativePane.getWid();
		    // TODO: opt: is this getting called for keyboard events? Can we skip it?
		    if (!findIntersectionPoint(pickInfo, eyePos, nativePane, intersectionPt)) {
			return;
		    }

		    event.set_window((int)wid);
		    event.set_event_x(intersectionPt[0]);
		    event.set_event_y(intersectionPt[1]);
		    sendDeviceInputEvent(event, null);

		} else {

		    logger.fine("Pick 3D object");
		    EventInfo3D evinfo = hit3DOrMiss(event, prw, x, y, pickInfos);
		    sendDeviceInputEvent(event, evinfo);
		}
	    }
	}
    }
    
    // The event hit a 3D object or was a miss (pickInfos == null).
    // Assign the PRW, coordinates and next pickseq to the event.

    private EventInfo3D hit3DOrMiss (DeviceEvent event, long prw, int x, int y, 
				     PickInfo[] pickInfos) {

	event.set_window((int)prw);
	event.set_event_x(x);
	event.set_event_y(y);

	EventInfo3D evinfo = EventInfo3D.create(nextPickSeq, pickInfos);
	bumpPickSeq();

	return evinfo;
    }

    public void setCursorModule( CursorModule cursorModule ) {
        this.cursorModule = cursorModule;
    }

    public CursorModule getCursorModule () {
	return cursorModule;
    }

    private void updateCursorPosition (PickInfo pickInfo, int x, int y,
				       Canvas3D canvas) {
        float distance;

	if (pickInfo != null) {
	    distance = (float) pickInfo.getClosestDistance();
	} else {
            // Use the default distance if nothing is picked.
	    distance = Float.NaN;
	}

        cursorModule.setCursorPosition(tmpV3f, x, y, canvas, distance);
     }
     
    private J3dNativePane checkForNativePane( PickInfo pickInfo ) {
        J3dNativePane ret = null;

	Node node = pickInfo.getNode();
	if (node == null) return null;
        
	while (node != null && ret == null) {
	    if (node instanceof J3dNativePane) {
		if (((J3dNativePane)node).isRemwin()) {
		    return null;
		} else {
		    ret = (J3dNativePane) node;
		}
	    }
	    node = node.getParent();
	}

        return ret;
    }
    
    class CanvasCache {
        public PickCanvas pickCanvas;
        long wid=-1;
        
        public CanvasCache( PickCanvas pickCanvas, long wid ) {
            this.pickCanvas = pickCanvas;
            this.wid = wid;
        }
    }

    public boolean deviceInputEventAvailable () {
	return deviceEventSource.queueSize() > 0;
    }

    /**
     * Gets the next device input event from the X server.
     * This method blocks until an event becomes available.
     *
     * When the event is received, if it is a motion or button event, 
     * the caller is expected to perform a pick using the scrAbsX and 
     * scrAbsY attributes of the event. After the pick, the client is 
     * expected to update the following attributes of the event: wid, 
     * winRelX, winRelY.
     *
     * If the event is not a motion or button event, the caller should not 
     * modify any attributes of the event.
     *
     * When the event has been processed in the above described manner
     * it should be sent back to the X server via sendDeviceInputEvent.
     *
     * @return the next device event
     */
    public DeviceEvent receiveDeviceInputEvent () {
	DeviceEvent devEvent = (DeviceEvent) deviceEventSource.dequeue();
	//System.err.println("receiveDeviceInputEvent: " + devEvent);
	return devEvent;
    }

    /** 
     * Sends a (possibly) modified event to the the X server
     *
     * @param event the event to send
     */
    public void sendDeviceInputEvent (DeviceEvent devEvent, EventInfo3D evinfo)  {
	//System.err.println("Sending devEvent = " + devEvent.toString(true));

	if (evinfo != null) {

	    // Precede 3D events with a ClientMessage event which contains the pickSeq.
	    // Send the event to the CookedEventPoller (it is the only DS client 
	    // which receives PRW pointer events).

	    ClientMessage event = new ClientMessage(devDpy);
	    event.set_format(32);
	    event.set_wm_data(evinfo.pickSeq);
	    event.set_type(wsx.pickSeqAtom);
	    Window prwin = devEvent.getPrwin();
	    prwin.send_event(false, Event.POINTER_MOTION_MASK, event);
	}

	// Next send the picked event
	exts.lgeExtForDev.sendEvent(devEvent);

	devDpy.flush();
    }
}


