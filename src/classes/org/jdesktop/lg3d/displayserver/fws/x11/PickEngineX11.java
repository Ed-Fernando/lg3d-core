/**
 * Project Looking Glass
 *
 * $RCSfile: PickEngineX11.java,v $
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
 * $Revision: 1.23 $
 * $Date: 2006-09-26 23:13:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.awt.event.*;
import java.awt.AWTEvent;
import java.awt.Component;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.vecmath.*;
import javax.media.j3d.Node;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.PickInfo;

import org.jdesktop.lg3d.displayserver.EventProcessor;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;
import org.jdesktop.lg3d.displayserver.fws.PickEngine;
import org.jdesktop.lg3d.displayserver.fws.PointerObject;

import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseEvent3D;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEnteredEvent3D;
import org.jdesktop.lg3d.wg.event.MouseWheelEvent3D;
import org.jdesktop.lg3d.wg.event.MouseMovedEvent3D;
import org.jdesktop.lg3d.wg.event.MouseDraggedEvent3D;

import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dComponent3D;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dCursor3D;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dLgBranchGroup;

import org.jdesktop.lg3d.wg.event.KeyEvent3D;

import org.jdesktop.lg3d.wg.Component3D;

import org.jdesktop.lg3d.scenemanager.utils.event.Frame3DAnimationFinishedEvent;
import org.jdesktop.lg3d.utils.eventadapter.GenericEventAdapter;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.wg.Component3DAnimation;
import org.jdesktop.lg3d.wg.event.LgEventConnector;

import gnu.x11.Cursor;
import gnu.x11.Display;
import gnu.x11.Input;
import gnu.x11.Window;
import gnu.x11.event.Event;


public class PickEngineX11 extends PickEngine
{
    private HashMap<Long,NativeWindowTrackingInfo> nativeWindowTrackingInfoMap = 
        new HashMap();

    private MouseEventNodeInfo lastButtonReleasedNodeInfo;

    PointerObjectX11 pointerObjTmp = new PointerObjectX11();
    private LinkedList<LgEventSource> nativeButtonPressSources = new LinkedList();

    Canvas3D lastPointerCanvas;
    long lastPointerRootWid;
    long lastPointerWid;

    private Vector3f tmpV3f = new Vector3f();

    // For performance analysis
    //private StatBuf sb = new StatBuf();

    /**
     * Creates a PickEngineX11
     * @param c The Component to add the MouseListener and
     * MouseMotionListener to.
     */
    public PickEngineX11 (javax.media.j3d.Locale locale,
			  EventProcessor eventProcessor, long[] rootWids,
			  WinSysX11 wsx) {
        super(locale, eventProcessor);

	pointerObjCur = new PointerObjectX11();
	pointerObjPrev = new PointerObjectX11();

	lastPointerRootWid = rootWids[0];
	lastPointerWid = rootWids[0];
	lastPointerCanvas = wsx.getCanvasForRootWin(lastPointerRootWid, 
						    lastPointerWid);

        // Register Frame3DAnimationFinishedListener
        LgEventConnector.getLgEventConnector().addListener(
	    Component3DAnimation.class,
	    new GenericEventAdapter(Frame3DAnimationFinishedEvent.class,
		new ActionNoArg() {
		    public void performAction(LgEventSource source) {
			recomputePointerObjectMaybeLater();
		    }
		}));
    }

    // TODO: does this really need to be synchronized? If so, then do
    // we need to make PickEngineAWT.processAWTEvent synchronized and also 
    // PickEvent.processAWTEvent(KeyEvent)?

    protected synchronized void processAWTEvent( MouseEvent e ) {

        //System.err.println("****************** PEX.processAWTEvent, e = " + e);

	// Deliver native enter events in a special way
	if (e instanceof NativeEnterEvent) {
	    deliverNativeEnterEvent((NativeEnterEvent)e);
	    return;

	// Deliver native button press events in a special way
	} else if (e instanceof NativeButtonPressEvent) {
	    deliverNativeButtonPressEvent((NativeButtonPressEvent)e);
	    return;
	}

	// Deliver button/move 3D mouse events in the normal way

	// Decide where to send the event (to a picked or grabbed node)
	determineEventDestInfo(e);
	if (pointerObjCur.destNodeInfo == null || pointerObjCur.destNodeInfo.getNode(0) == null) {
 	    //System.err.println("********* TOSS EVENT: " + e);
	    return;
	}

	// Calculated information derived from the node info
	pointerObjCur.calcDerivedInfo();
        //logger.fine("destNode = " + pointerObjCur.destNode);

        // Deliver the event to the selected destination
	deliverEventUsingNodeInfo(e);

        // Record last pointer position for handling current pointer object recomputation
        // when the scene graph changes (see PickEngine.recomputePointerObjectMaybeLater);
        int type = e.getID();
        if (type == MouseEvent.MOUSE_MOVED || type == MouseEvent.MOUSE_DRAGGED) {
	    lastPointerCanvas = (Canvas3D) e.getSource();
	    lastPointerRootWid = ((SequencedMouseEvent)e).getRootWid();
	    lastPointerWid = ((SequencedMouseEvent)e).getWid();
    	    lastPointerX = e.getX();
	    lastPointerY = e.getY();
	}
    }
    
    private void deliverNativeEnterEvent (NativeEnterEvent e) {
        pointerObjCur.reset();
	((PointerObjectX11)pointerObjCur).wid = e.getWid();

	// Determine node info (the null evinfo always forces a pick)
	// Also, the pick must succeeds because we cannot simply drop a
	// native enter event. 
	// Note: null may be returned if the scene graph object has been detached.
	pointerObjCur.destNodeInfo = destInfoForEvent(e, null, true);
	if (pointerObjCur.destNodeInfo == null || 
	    pointerObjCur.destNodeInfo.getNode(0) == null) {
            pointerObjPrev.copyFrom(pointerObjCur);
	    return;
	}

	// Calculate information derived from the node info
	pointerObjCur.calcDerivedInfo();
        //logger.fine("destNode = " + pointerObjCur.destNode);

	// Calculate cursor position
        float distance = (pointerObjCur.destNode == null)
	                     ? Float.NaN : ((float)pointerObjCur.destNodeInfo.getEyeDistance(0));
	updateCursor(tmpV3f, pointerObjCur.destPathFromRoot, (Canvas3D)e.getSource(), 
		     e.getX(), e.getY(), distance);

        // Now check to see whether we need to deliver any 3D enter/exit events
        deliverEnterExitEvents(e, tmpV3f);

	// Now we are done with the enter event
	pointerObjPrev.copyFrom(pointerObjCur);

        // Record last pointer position for handling current pointer object recomputation
        // when the scene graph changes (see PickEngine.recomputePointerObjectMaybeLater);
	lastPointerCanvas = (Canvas3D) e.getSource();
	lastPointerRootWid = ((SequencedMouseEvent)e).getRootWid();
	lastPointerWid = ((SequencedMouseEvent)e).getWid();
	lastPointerX = e.getX();
	lastPointerY = e.getY();
    }

    // If a button press is received for a window other than the PRW,
    // the user is clicking the window to the top of the stack
    // These events do not go through any of the normal event
    // processing (determination of node info, grab evaluation, etc.)
    
    private void deliverNativeButtonPressEvent (NativeButtonPressEvent e) {

	//System.err.println("Enter deliverNativeButtonPressEvent");

	Canvas3D canvas = (Canvas3D)e.getSource();
	long wid = e.getWid();
        int rootX = e.getRootX();
        int rootY = e.getRootY();

	// Determine figure out what node the button press hit
	PickInfo[] pickInfos;
	logger.fine("perform pick");
	pickInfos = performPick(canvas, rootX, rootY);
	if (pickInfos == null) return;
	pointerObjTmp.destNodeInfo = createNodeInfoFromPickInfos(pickInfos);
	if (pointerObjTmp.destNodeInfo == null) {
	    pointerObjTmp.reset();
	    return;
	}

	pointerObjTmp.calcDerivedInfo();

	NativeWindowTrackingInfo nwtInfo = nativeWindowTrackingInfoMap.get(wid);
	if (nwtInfo == null) {
	    pointerObjTmp.reset();
	    return;
	}

	// TODO: It is strange, but in the Glassy Native Window LAF, click-to-front
	// doesn't happen on Button Press, but only on a Mouse clicked event (which 
	// is generated by a sequence of Button Press and Release in the same window.
	// So here we need to convert the native button press event into a button clicked
	// event. This isn't strictly correct, as a button press event may not always
	// be followed by a button clicked event. But it's good enough for now. 
        // Fix this later.

	Component source = (Component) e.getSource();
        int modifiers = e.getModifiers(); 
        boolean popupTrigger = e.isPopupTrigger();
        int button = e.getButton();
	long when = e.getWhen();
	MouseEvent clickedEvent = new MouseEvent(source, MouseEvent.MOUSE_CLICKED, 
						 when, modifiers, 
						 rootX, rootY, 1, popupTrigger, button);

	// Determine sources clicked events
	getMouseEventSources(pointerObjTmp.destPathFromRoot, 0, nativeButtonPressSources, 
	                     MouseButtonEvent3D.class);

	// Calculate cursor position

        float distance = (pointerObjTmp.destNode == null)
	                     ? Float.NaN : ((float)pointerObjTmp.destNodeInfo.getEyeDistance(0));
	updateCursor(tmpV3f, pointerObjTmp.destPathFromRoot, canvas, rootX, rootY, distance);

	// Deliver clicked events to these sources
	for (LgEventSource s : nativeButtonPressSources) {
	    MouseEvent3D me = new MouseButtonEvent3D( clickedEvent, MouseEvent.MOUSE_CLICKED, 
						      pointerObjTmp.destNodeInfo, tmpV3f);
            if (detailLog)
                logger.fine("Deliver MouseClicked for source " + s + ", event = " + me);
	    eventProcessor.postEvent(me, s);
        }
	nativeButtonPressSources.clear();

	// TODO: we should make sure that the window has been actually popped to
	// the top before unfreezing!
	// TODO: need to come up with a better method than postEventAndWait

	// Now unfreeze the mouse
	// Note: "REPLY_POINTER" is an Escher misnomer. It should be
	// "REPLAY_POINTER."
	nwtInfo.win.display.input.allow_events(Input.REPLY_POINTER, Display.CURRENT_TIME);
	nwtInfo.win.display.flush();

	pointerObjTmp.reset();
    }

    // Note: pick miss events are eventually discarded because their
    // nodeinfo is null, but they can cause trigger grabs (per X11 semantics).

    protected void determineEventDestInfo (MouseEvent e) {
	MouseEventNodeInfo nodeInfo;
	EventInfo3D evinfo;
	long wid;

        pointerObjCur.intersectedNodeInfo = null;

	// Is the event for a proxy win? If so, send it to the
	// node associated with that proxy win.
	
	if        (e instanceof SequencedMouseEvent) {
	    wid = ((SequencedMouseEvent)e).getWid();
	} else if (e instanceof SequencedMouseWheelEvent) {
	    wid = ((SequencedMouseWheelEvent)e).getWid();
	} else {
	    logger.warning("Invalid event type. Event is ignored.");
	    pointerObjCur.destNodeInfo = null;	    
	    return;
	}

	GrabProxyWin gpw = GrabProxyWin.get(wid);
	if (gpw != null) {
	    Node node = gpw.getNode();
	    if (node != null) {
		// TODO: proxywin: create MouseEventNodeInfo for node and return it
		// TODO: also need to call gpw.release on but release with modifiers = 0
		throw new RuntimeException("Grab proxy windows are not yet implemented");
	    } else {
		logger.warning("Error: No node for grab proxy window. Event is ignored.");
		pointerObjCur.destNodeInfo = null;	    
	    }
	}

	// Event must be for the PRW. The PRW is subject to the 
	// X11 default button grab, so we need to check the 
	// grab state.

	switch (e.getID()) {

	case MouseEvent.MOUSE_PRESSED:
	    if (nodeInfoPointerGrabStart != null) {
		// If pointer is already grabbed, send the button press
		// to the grab node
		if (detailLog) logger.fine("**** Grab active; send event to grab node");
		pointerObjCur.destNodeInfo = nodeInfoPointerGrabStart;
		return; 
	    } else {
		// Determine the destination node info of the button 
		// press and trigger the grab. All subsequent events
		// up until a grab deactivate will be sent to this
		// node.
		if (detailLog) logger.fine("********** GRAB TRIGGER");
		evinfo = determineEvinfo(e);
		if (evinfo == null) evInfoError(e);
		nodeInfoPointerGrabStart = destInfoForEvent(e, evinfo, false);
		pointerObjCur.destNodeInfo = nodeInfoPointerGrabStart;
		return;
	    }
	    
	case MouseEvent.MOUSE_RELEASED: 
	    if (nodeInfoPointerGrabStart != null) {
		// Pointer is grabbed. If all buttons are released
		// the grab must be deactivated.
		nodeInfo = nodeInfoPointerGrabStart;
		if (e.getModifiersEx() == 0) {
		    // Deactivate grab
		    if (detailLog) logger.fine("********** GRAB TERMINATE");
		    if (recomputePointerObjectAfterGrabIsReleased) {
			recomputePointerObjectAfterGrabIsReleased = false;
			recomputePointerObject();
		    }
		    nodeInfoPointerGrabStart = null;
		}
	    } else {
		// Pointer is not grabbed. Just send to the event node.
		evinfo = determineEvinfo(e);
		nodeInfo = destInfoForEvent(e, evinfo, false);
	    }
	    lastButtonReleasedNodeInfo = nodeInfo;
	    pointerObjCur.destNodeInfo = nodeInfo;
	    return;

	case MouseEvent.MOUSE_CLICKED:
	    // Handle button clicked events specially. Per Java AWT semantics
	    // a mouse clicked event comes after each button release. So just
	    // send the clicked event to where we sent the release.
	    pointerObjCur.destNodeInfo = lastButtonReleasedNodeInfo;
	    return;

	case MouseEvent.MOUSE_MOVED:
	case MouseEvent.MOUSE_WHEEL:
	    if (nodeInfoPointerGrabStart != null) {
		// Pointer is grabbed; send event to the grab node
		if (detailLog) logger.fine("**** Grab active; send event to grab node");
		pointerObjCur.destNodeInfo = nodeInfoPointerGrabStart;
		return;
	    } else {
		// Pointer is not grabbed, send event to the event node
		evinfo = determineEvinfo(e);
		pointerObjCur.destNodeInfo = destInfoForEvent(e, evinfo, false);
		return;
	    }

        // Drag events are slightly different. In order to support drag-and-drop
	// operations, not only need to keep track of the node info to which
	// we are going to send the event but we also need to keep track of
	// the node info of the last pick operation.

	case MouseEvent.MOUSE_DRAGGED:
	    evinfo = determineEvinfo(e);
	    pointerObjCur.intersectedNodeInfo = destInfoForEvent(e, evinfo, false);
	    if (nodeInfoPointerGrabStart != null) {
		// Pointer is grabbed; send event to the grab node
		if (detailLog) logger.fine("**** Grab active; send event to grab node");
		pointerObjCur.destNodeInfo = nodeInfoPointerGrabStart;
		return;
	    } else {
		// Pointer is not grabbed, send event to the picked node
		pointerObjCur.destNodeInfo = pointerObjCur.intersectedNodeInfo;
		return;
	    }


	default:
	    logger.warning("Invalid event ID. Event is ignored. ID = " + e.getID());
	    pointerObjCur.destNodeInfo = null;
	    return;
	}
    }

    // There are some cases for which the event has a corresponding evinfo.
    // These cases are:
    // 
    //     Mouse motion events
    //     Mouse wheel events
    //     Mouse button press/release events
    //     TODO: also Key events?
    // 
    // There are other cases for which the event has no corresponding evinfo
    // but the previous evinfo can be used. These cases are:
    // 
    //     Mouse clicked events
    //     Mouse enter/exit events
    // 

    EventInfo3D determineEvinfo (MouseEvent e) {
	EventInfo3D evinfo;
	int pickSeq = 0;

	if (e instanceof SequencedMouseEvent) {
	    pickSeq = ((SequencedMouseEvent)e).getPickSeq();
	} else if (e instanceof SequencedMouseWheelEvent) {
	    pickSeq = ((SequencedMouseWheelEvent)e).getPickSeq();
	} else {
	    throw new RuntimeException("Invalid mouse event type");
	}

        //System.err.println("PEX.determineEvinfo: received pickSeq = " + pickSeq);

	if (pickSeq == -1) {

	    // Force a pick to happen for synthetic mouseFrame3DAnimationFinished events
	    evinfo = null;

        } else if (pickSeq == 0) {

	    // Mouse enter/exit and mouse clicked events
	    //System.err.println("Mouse enter/exit or clicked event: inherit previous evinfo");
	    evinfo = ((PointerObjectX11)pointerObjPrev).evinfo;

	} else {

	    // Otherwise (for mouse motion, mouse wheel, mouse button press/release 
	    // and key events) match this event up with its evinfo

	    evinfo = EventInfo3D.dequeue(pickSeq);
	}

	return evinfo;
    }

    private void evInfoError (MouseEvent e) {
	int pickSeq = 0;

	if (e instanceof SequencedMouseEvent) {
	    pickSeq = ((SequencedMouseEvent)e).getPickSeq();
	} else if (e instanceof SequencedMouseWheelEvent) {
	    pickSeq = ((SequencedMouseWheelEvent)e).getPickSeq();
	} else {
	    throw new RuntimeException("Invalid mouse event type");
	}

	//throw new RuntimeException("Invalid EventInfo3D, pickSeq = " + pickSeq);
	System.err.println("Invalid EventInfo3D, pickSeq = " + pickSeq);
	System.err.println("Event = " + e);
	System.exit(1);
    }

    // Determine the destination nodes and asssociated info for the event without 
    // considering grabbing. If there is a valid evinfo we can extract this
    // data from the evinfo; this allows us to derive the information from the 
    // pick for this event which has already occured in the low level picker..
    //  However, if the evinfo is null, then we need to calculate this data by 
    // doing a pick on the current state of the scene graph.

    private MouseEventNodeInfo destInfoForEvent (MouseEvent e, EventInfo3D evinfo, 
						 boolean aggressive) {
	PickInfo[] pickInfos;

	// Check for LLXP pick miss. These events will have a non-null
	// evinfo with a null pickInfos. Discard miss events.
	if (evinfo != null && evinfo.pickInfos == null) {
	    return null;
	}

	boolean performPick = (evinfo == null);
            
	if (performPick) {
	    if (detailLog) logger.fine("perform pick");
	    if (aggressive) {
		pickInfos = performPickAggressive(e);
	    } else {
		pickInfos = performPick(e);
	    }
	    if (pickInfos == null) return null;
	} else {
	    if (detailLog) logger.fine("get pickInfos from evinfo");
	    pickInfos = evinfo.pickInfos;
	}

	return createNodeInfoFromPickInfos(pickInfos);
    }    

    protected Vector3f updateCursor (Vector3f cursorPos, LinkedList nodePath, Canvas3D canvas, 
				     int x, int y, float distance) {

	// Note: cursor tracking occurs in LowLevelX11Picker. So here we just 
	// translate the event coordinates into Vworld coordinates

	cursorModule.cursorPositionInVworld(cursorPos, x, y, canvas, distance);
        if (nodePath != null) {
	    setCursorForNodePath(nodePath);
	}

        return cursorPos;
    }

    class NativeWindowTrackingInfo {
        Window win;
    }

    // Start tracking enter/exit events and button presses for the given 
    // native window

    public void trackNativeWindow (long wid, Window win) {
	NativeWindowTrackingInfo nwtInfo = new NativeWindowTrackingInfo();
	nwtInfo.win = win;
	nativeWindowTrackingInfoMap.put(wid, nwtInfo);

	// Register a synchronous grab on left mouse button press for the window
	// TODO: do I need to deal with pointer remapping here?
	win.grab_button(1, 0, false, Event.BUTTON_PRESS_MASK, 
			   Window.SYNCHRONOUS, Window.ASYNCHRONOUS,
			   Window.NONE, Cursor.NONE);
    }    

    // Stop tracking enter/exit events and button presses for the given 
    // native window

    public void untrackNativeWindow (long wid) {
	NativeWindowTrackingInfo nwtInfo = nativeWindowTrackingInfoMap.get(wid);
	if (nwtInfo == null) return;

	// Delete the synchronous grab
	// TODO: do I need to deal with pointer remapping here?
	nwtInfo.win.ungrab_button(1, 0);

	// We no longer need the tracking info
	nativeWindowTrackingInfoMap.remove(wid);
    }

    protected void recomputePointerObject () {

	// Send a synthetic motion event to the X server so that the
	// keyboard focus gets updated properly.
	((WinSysX11)fws).enqueueMoveToLastPosition();

	// Also, send a synthetic motion event directly through the Event Deliverer.
	// This is necessary because the previous synthetic motion event sent to 
	// the X server will not come back to the DS (because it may hit a native 
	// window. A native enter event may be generated but (for some reason which
        // I don't understand) this is insufficient to make the Event Deliverer 
	// notice the change.
	MouseEvent sme = new SequencedMouseEvent(lastPointerCanvas, lastPointerRootWid, 
				  lastPointerWid, MouseEvent.MOUSE_MOVED, 0, 0, 
				  lastPointerX, lastPointerY, 0, false, 
				  MouseEvent.NOBUTTON);
        processAWTEvent(sme);
    }

    // This window has been destroyed Throw away any references the PickEngine
    // has which refer to this window.

    void destroyWindow (long wid) {
	if (((PointerObjectX11)pointerObjCur).wid == wid) {
	    pointerObjCur.reset();
	}
	if (((PointerObjectX11)pointerObjPrev).wid == wid) {
	    pointerObjPrev.reset();
        }
        if (keyboardFocusWid == wid) {
            setKeyEventSource(null, INVALID_WINDOW);
        }
    }

    protected void implementFocusFollowsMousePolicy (PointerObject po) {
        if (po != null) {
            setKeyEventSource(po.destPathFromRoot, ((PointerObjectX11)po).wid);
        } else {
            setKeyEventSource(null, INVALID_WINDOW);
        }
    }

}



