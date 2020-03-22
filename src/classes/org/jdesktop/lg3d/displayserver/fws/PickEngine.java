/**
 * Project Looking Glass
 *
 * $RCSfile: PickEngine.java,v $
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
 * $Date: 2007-11-21 21:37:35 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Locale;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.PickInfo;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.pickfast.PickCanvas;
import com.sun.j3d.utils.universe.ViewInfo;

import org.jdesktop.lg3d.displayserver.EventProcessor;
import org.jdesktop.lg3d.scenemanager.CursorModule;

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

/**
 * PickEnginer, manages to cursor picking every frame
 */
public abstract class PickEngine extends javax.media.j3d.Behavior 
{
    protected static final int INVALID_WINDOW = 0;
    
    // true if this behavior is enable
    protected boolean enable = true;
    
    private EventBuffer writeBuffer;    // The buffer the events are written to
    
    private EventBuffer readBuffer;     // The buffer the events are read from
    
    private EventBuffer tmpBuffer;
    
    protected LinkedList<LgEventSource> mouseEventSources = new LinkedList();
    
    protected HashMap<Canvas3D,PickCanvas> pickcanvasList = new HashMap<Canvas3D,PickCanvas>();
    private ViewInfo viewInfo;
    
    // These must be allocated by the subclass
    protected PointerObject pointerObjCur;
    protected PointerObject pointerObjPrev;

    protected EventProcessor eventProcessor;
    protected CursorModule cursorModule;
    
    protected Logger logger = Logger.getLogger("lg.fws");
    
    private WakeupOnElapsedFrames wakeup = new WakeupOnElapsedFrames( 0 );
    
    // Grab State
    public static final ActiveGrabState activeGrabState = new ActiveGrabState();

    // The node info of the event that started a pointer grab
    protected MouseEventNodeInfo nodeInfoPointerGrabStart;

    protected LinkedList<Node> nodePathForKeyEvent = null;
    protected long keyboardFocusWid = INVALID_WINDOW;

    protected boolean recomputePointerObjectAfterGrabIsReleased = false;		    

    protected FoundationWinSys fws;

    protected Canvas3D lastPointerCanvas;
    protected int lastPointerX, lastPointerY;

    private Vector3f tmpV3f = new Vector3f();
    
    protected boolean detailLog = (logger.getLevel()!=null && logger.getLevel().intValue()<=Level.FINE.intValue()) ? true : false;

    /**
     * Creates a PickEngine
     * @param c The Component to add the MouseListener and
     * MouseMotionListener to.
     */
    public PickEngine(Locale locale,
                      EventProcessor eventProcessor) {
        
        fws = FoundationWinSys.getFoundationWinSys();
        viewInfo = fws.getViewInfo();
        this.eventProcessor = eventProcessor;
        setName("PickEngine");
        
        // TODO Handle multi screen
	for (int i=0; i < fws.getNumCanvases(); i++) {
	    PickCanvas pickCanvas = new PickCanvas(fws.getCanvas(i),locale);
	    pickcanvasList.put(fws.getCanvas(i), pickCanvas);
	    try {
		pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
		pickCanvas.setFlags(
		    PickInfo.NODE                       | 
		    PickInfo.LOCAL_TO_VWORLD            |
		    PickInfo.CLOSEST_INTERSECTION_POINT |
		    PickInfo.CLOSEST_DISTANCE);
	    } catch (Exception e) {
		throw new RuntimeException("PickCanvas initialization failure" + e);
	    }
	}
        setSchedulingBounds( new BoundingSphere( new Point3d(), Double.POSITIVE_INFINITY ));

	// Initialize last known cursor position (for Frame3DAnimationFinishedListener)
	// TODO: upgrade this for multiscreen: how to find the default cursor screen?
	// TODO: the method of computing the last known cursor position makes the 
	// overly simplistic assumption that the cursor comes up in the center of 
        // the default cursor screen. In the future, we need to set the initial cursor
        // position based on the current native cursor position at the time of DS
        // start up. So in the future we will want to derive lastPointerX/Y from
        // the cursor module itself.
	lastPointerCanvas = fws.getCanvas(0);
        lastPointerX = lastPointerCanvas.getWidth() / 2;
        lastPointerY = lastPointerCanvas.getHeight() / 2;
    }

    /** Initializes the behavior.
     */    
    public void initialize() {
        writeBuffer = new EventBuffer();
        readBuffer = new EventBuffer();
        
        logger.config("PickEngine initialized");
        wakeupOn( new WakeupOnElapsedFrames( 0, false ) );
    }
  
    /**
    * All mouse manipulators must implement this.
    */
    public void processStimulus(Enumeration criteria) {
        
        try {
            // Swap the buffers so we can continure to receive asynchronous events
            // while doing the pick for this frame
            synchronized(writeBuffer) {
                tmpBuffer = readBuffer;
                readBuffer = writeBuffer;
                writeBuffer = tmpBuffer;
            }

            readBuffer.processQueue();
        } catch( Exception e ) {
            logger.log( Level.SEVERE, "Exception caught in PickEngine", e );
	    System.exit(1);
        } catch( Error error ) {            
            logger.log( Level.SEVERE, "Error caught in PickEngine", error );
	    System.exit(1);
        }
        
        wakeupOn( wakeup );
    }
    
    public void enqueue (AWTEvent e) {
	if (writeBuffer == null) return;
	if (enable) {
	    synchronized (writeBuffer) {
                writeBuffer.add(e);
	    }
	}
    }

    public void setEnable(boolean state) {
	super.setEnable(state);
        this.enable = state;
	if (!enable) {
            synchronized(writeBuffer) {
                writeBuffer.clear();
                readBuffer.clear();
            }
	}
    }
    
    protected void setKeyEventSource(LinkedList<Node> nodePathForKeyEvent, long wid) {
        this.nodePathForKeyEvent = nodePathForKeyEvent;
        keyboardFocusWid = wid;
    }

    private void printKeyEvent (KeyEvent e) {

	int id = e.getID();
	switch (id) {
	case KeyEvent.KEY_PRESSED: 
	    System.err.print("KEY_PRESSED:\t");
	    break;
	case KeyEvent.KEY_RELEASED: 
	    System.err.print("KEY_RELEASED:\t");
	    break;
	case KeyEvent.KEY_TYPED: 
	    System.err.print("KEY_TYPED:\t");
	    break;
	default:
	    System.err.print("UNKNOWN KEY EVENT!:\t");
	    break;
	}

	int keyCode = e.getKeyCode();
	System.err.print("code = " + keyCode);

	String keyText = KeyEvent.getKeyText(keyCode);
	System.err.print(", text = " + keyText);

	char keyChar = e.getKeyChar();
	System.err.print(", char = '" + keyChar + "'");
    
	String modText = KeyEvent.getKeyModifiersText(e.getModifiers());
	System.err.print(", mods = " + modText);

	System.err.println();
    }

    // TODO: the "normal destination" is the null source. Right now, the 
    // concept of keyboard focus is not yet implemented; key events are
    // sent to any listener listening for a key event with a null source.
    // As a result, keyboard grabbing makes no difference to who ends up
    // receiving the event. So until keyboard is focus is implement
    // it doesn't make sense to evaluate keyboard grabs during key event
    // delivery.

    protected void processAWTEvent( KeyEvent e ) {

	// For debug
	//printKeyEvent(e);
    
        if (nodePathForKeyEvent == null) {
            return;
        }

        LgEventSource source = getKeyEventSource(nodePathForKeyEvent);
        eventProcessor.postEvent(new KeyEvent3D(e), source);
    }

    protected abstract void processAWTEvent( MouseEvent e );
    
    protected abstract void determineEventDestInfo (MouseEvent me);

    protected void deliverEventUsingNodeInfo (MouseEvent e) {

	// Calculate cursor position
        float distance = (pointerObjCur.destNode == null) 
	                    ? Float.NaN : (pointerObjCur.destNodeInfo.getEyeDistance(0));
        updateCursor(tmpV3f, pointerObjCur.destPathFromRoot, (Canvas3D)e.getSource(), 
		     e.getX(), e.getY(), distance);

        // First, deliver enter/exit events
        deliverEnterExitEvents(e, tmpV3f);

	// Then deliver event to the registered event sources for this type of event
	// based on the scene graph path
	calcSourcesAndDeliver(e, tmpV3f);         

	pointerObjPrev.copyFrom(pointerObjCur);
    }
    
    /**
     * Search the objects in the node path for the given node 
     * for event sources that can generate specified mouse events.
     * First find out the index to the highest node whose mouse event is
     * disable by searching from the root to the leaf. 
     * Then searches to the root from the node one above the one pointed 
     * by the index.
     */
     // TODO: it might be more efficient for this to take a node instead of a nodePath
     protected void getMouseEventSources(LinkedList nodePath,int numObjectsToSkip,
					 LinkedList<LgEventSource> eventSources,
					 Class mouseEventClass) {
        eventSources.clear();
        if (nodePath == null) {
             return;
        }
        
        Iterator it;
	int j;

	/*
	System.err.println("--------------------------------------");
	System.err.println("Node scene graph:");
        it = nodePath.iterator();
	j = 0;
        while (it.hasNext()) {
	    Node n = (Node) it.next();
            System.out.println(j + " Node: " + n);
	    org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.LgBranchGroup ud = 
		(org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.LgBranchGroup)n.getUserData();
            System.out.println(j + " Node.ud:        " + ud);
	    if (ud != null) {
		Object udud = ud.getUserData();
		System.out.println(j + " Node.ud.ud:                " + udud);
	    }
	    j++;
	}
	System.err.println("--------------------------------------");
        */

        int startIndex = getHighestNodeWithMouseEventDisabled(nodePath) - 1;
        int mouseEventMask = J3dLgBranchGroup.getMouseEventMask(mouseEventClass);
        assert(mouseEventMask > 0);
        for (int i = startIndex; i >= numObjectsToSkip; i--) {
	    Node node = (Node) nodePath.get(i);
            if (node instanceof J3dLgBranchGroup) {
                J3dLgBranchGroup jlb = (J3dLgBranchGroup)node;
                if (jlb.isMouseEventSource(mouseEventMask)) {
                    if (node.getUserData() instanceof org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.LgBranchGroup) {
                        org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.LgBranchGroup j3dlbg = (org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.LgBranchGroup)node.getUserData();
                        org.jdesktop.lg3d.wg.Component3D c3d = (org.jdesktop.lg3d.wg.Component3D)j3dlbg.getUserData();

                        eventSources.add(c3d);
                    } else {
                        // Wonderland, we dont use the wrappers
                        eventSources.add(jlb);
                    }
		    //System.err.println("Add source = " + c3d);
                }
                // if this node is a mouse event source, and the propagatable
                // flag is not set, finish searching when the first source node
                // is identified.
                if (jlb.isMouseEventSource() 
                    && !jlb.isMouseEventPropagatable()
                    && !mouseEventClass.equals(MouseEnteredEvent3D.class)) 
                {
                    break;
                }
            }
        }
    }
    
    private LgEventSource getKeyEventSource (LinkedList nodePath) {
        if (nodePath == null) {
             return null;
        }
        
        LgEventSource ret = null;
        int startIndex = nodePath.size() - 1;
        for (int i = startIndex; i >= 0; i--) {
            Node node = (Node) nodePath.get(i);
            if (node instanceof J3dLgBranchGroup) {
                J3dLgBranchGroup jlb = (J3dLgBranchGroup)node;
                if (jlb.isKeyEventSource()) {
//                    System.out.println("--------> "+node);
//                    System.out.println("--------> "+((org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.LgBranchGroup)node.getUserData()));
//                    System.out.println("--------> "+((LgBranchGroup)node.getUserData()).getUserData());
                    if (node.getUserData() instanceof org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.LgBranchGroup) {
			ret = (org.jdesktop.lg3d.wg.Component3D)((org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.LgBranchGroup)node.getUserData()).getUserData();
		    } else {
                        // Wonderland, we dont use the wrappers
                        ret = jlb;
		    }
                    break;
                }
            }
        }
        return ret;
    }

    protected int getHighestNodeWithMouseEventDisabled (LinkedList nodePath) {
        int i = 0;
        Iterator it = nodePath.iterator();
	if (it == null) return 0;
        while (it.hasNext()) {
    	    Node node = (Node) it.next();
            if (node instanceof J3dLgBranchGroup
                && !((J3dLgBranchGroup)node).isMouseEventEnabled()) 
            {
                break;
            }
	    i++;
        }
        return i;
    }
     
    /**
     * Process any cursor changes and call the CursorModule with any updates.
     */

    protected abstract Vector3f updateCursor (Vector3f cursorPos, LinkedList nodePath, Canvas3D canvas,
					      int x, int y, float distance);

    // Update the current cursor based on the appropriate cursor in the given node path
    protected void setCursorForNodePath (LinkedList nodePath) {
        // ignore mouse cursor settings for nodes under a node whose mouse
        // events are disabled.
        int startIndex = getHighestNodeWithMouseEventDisabled(nodePath) - 1;
        for (int i = startIndex; i >= 0; i--) {
            Node node = (Node) nodePath.get(i);
            if (node instanceof J3dComponent3D) {
                J3dCursor3D cursor = ((J3dComponent3D)node).getCursor();
                if (cursor!=null) {
                    //logger.info("Should use cursor "+cursor.getName());
                    cursorModule.setCursor( (org.jdesktop.lg3d.wg.Cursor3D)((org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.Cursor3D)cursor.getUserData()).getUserData());
                    break;
                }
            }
        }
    }

    public void setCursorModule( CursorModule cursorModule ) {
        this.cursorModule = cursorModule;
    }

    public CursorModule getCursorModule () {
	return cursorModule;
    }

    protected void deliverEnterExitEvents (MouseEvent e, Vector3f cursorPos) {
        if (pointerObjCur.equals(pointerObjPrev)) return;

	int skipCount = 0;

        // Obtain the number of objects from the root that appear
        // both the node paths of destNodePrev and destNode.
        // The count is used to avoid posting a bogus mouse 
        // exit/enter event pair against higher level scenegraph
        // objects.
        skipCount = pointerObjCur.countSameObjectsFromRoot(pointerObjPrev);

	// Send exit events to sources in the previous path

	if (pointerObjPrev.destNode != null) {
	    getMouseEventSources(pointerObjPrev.destPathFromRoot,skipCount, 
				 mouseEventSources, MouseEnteredEvent3D.class);

	    for (LgEventSource s : mouseEventSources) {
		//logger.fine("POST EXIT EVENT");
		eventProcessor.postEvent(
		    new MouseEnteredEvent3D(e, MouseEvent.MOUSE_EXITED, 
					    pointerObjPrev.destNodeInfo, cursorPos), 
		    s);
	    }
            mouseEventSources.clear();
	}

	if (pointerObjCur != null) {

	    getMouseEventSources(pointerObjCur.destPathFromRoot, skipCount, 
				 mouseEventSources, MouseEnteredEvent3D.class);

	    for (LgEventSource s : mouseEventSources) {
		//logger.fine("POST ENTER EVENT");
		eventProcessor.postEvent( 
		    new MouseEnteredEvent3D(e,  MouseEvent.MOUSE_ENTERED, 
					    pointerObjCur.destNodeInfo, cursorPos), 
		    s);
	    }
            mouseEventSources.clear();
	}

	// Set the key event source based on a focus-follows-mouse policy
        // TODO: this needs to be changed to have higher-levels of the DS
        // implement the specific policy and just have a setKeyboardFocus(Node)
        // method to the FWS
        implementFocusFollowsMousePolicy(pointerObjCur);

    }

    protected abstract void implementFocusFollowsMousePolicy (PointerObject po);

    private void calcSourcesAndDeliver (MouseEvent e, Vector3f cursorPos) {

	// Determine event sources
        if (pointerObjCur.destNode != null) {
            Class mouseEventClass = null;
            switch(e.getID()) {
                case MouseEvent.MOUSE_CLICKED:
                case MouseEvent.MOUSE_PRESSED:
                case MouseEvent.MOUSE_RELEASED:
                    mouseEventClass = MouseButtonEvent3D.class;
                    break;
                case MouseEvent.MOUSE_MOVED:
                    mouseEventClass = MouseMovedEvent3D.class;
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    mouseEventClass = MouseDraggedEvent3D.class;
                    break;
                case MouseEvent.MOUSE_WHEEL:
                    mouseEventClass = MouseWheelEvent3D.class;
                    break;
                case MouseEvent.MOUSE_ENTERED:
                case MouseEvent.MOUSE_EXITED:
                    mouseEventClass = MouseEnteredEvent3D.class;
                    break;
                default:
                    assert(false);
            }
	    getMouseEventSources(pointerObjCur.destPathFromRoot, 0, 
				 mouseEventSources, mouseEventClass);
        }
        
	// If there are no sources inject a null source so that
        // we generate mouse motion events. This is for the case
        // when the background is not an event source (or the cursor
        // has moved off the background).

        if (mouseEventSources.size()==0) {
            mouseEventSources.add(null);
	}
        
        // Now we actually deliver the event. To do this, we post an event for
        // every source in the source list.
	deliverMouseEventToSources(e, cursorPos);

	mouseEventSources.clear();
    }

    protected void deliverMouseEventToSources (MouseEvent e, Vector3f cursorPos) {
        MouseEvent3D me;

        switch(e.getID()) {

	case MouseEvent.MOUSE_CLICKED:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseButtonEvent3D( e, e.getID(), pointerObjCur.destNodeInfo, cursorPos);
                //logger.fine("Deliver MouseClicked for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;

        case MouseEvent.MOUSE_PRESSED:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseButtonEvent3D( e, e.getID(), pointerObjCur.destNodeInfo, cursorPos );
                //logger.fine("Deliver MousePressed for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;

        case MouseEvent.MOUSE_RELEASED:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseButtonEvent3D( e, e.getID(), pointerObjCur.destNodeInfo, cursorPos );
                //logger.fine("Deliver MouseReleased for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;

        case MouseEvent.MOUSE_MOVED:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseMovedEvent3D( e, pointerObjCur.destNodeInfo, cursorPos );
                //logger.fine("Deliver MouseMoved for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;

        case MouseEvent.MOUSE_DRAGGED:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseDraggedEvent3D( e, pointerObjCur.intersectedNodeInfo, 
					      pointerObjCur.destNodeInfo, cursorPos );
                //logger.fine("Deliver MouseDragged for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;

        case MouseEvent.MOUSE_WHEEL:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseWheelEvent3D( e, pointerObjCur.destNodeInfo, cursorPos );
                //logger.fine("Deliver MouseWheel for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;
        }
    }

    protected MouseEventNodeInfo createNodeInfoFromPickInfos (PickInfo[] pickInfos) {
	if (pickInfos == null || pickInfos[0] == null) return null;
        //System.err.println("createNodeInfoFromPickInfos");
	Node[] nodes = new Node[pickInfos.length];
	Point3f[] pointsLocal = new Point3f[pickInfos.length];
	Point3f[] pointsVW = new Point3f[pickInfos.length];
	float[] eyeDistances = new float[pickInfos.length];

	for (int i = 0; i < pickInfos.length; i++) {
	    nodes[i] = pickInfos[i].getNode();
	    try {
		Point3d intersectionPt = pickInfos[i].getClosestIntersectionPoint();
	        pointsLocal[i] = new Point3f(intersectionPt);
		Transform3D t3d = pickInfos[i].getLocalToVWorld();
	        pointsVW[i] = new Point3f(intersectionPt);
                t3d.transform(pointsVW[i]);
		eyeDistances[i] = (float) pickInfos[i].getClosestDistance();
	    } catch (RuntimeException except) {
		// TODO: there is a (possible) Java3D bug which causes an "interpolation
		// point outside quad" exception at seemingly random times. Ignore this
		// exception and skip the event
		//logger.log(Level.WARNING,"Error in findIntersectionPoint " + except, except);
		return null;
	    }
	}

	MouseEventNodeInfo nodeInfo = new MouseEventNodeInfo(nodes, pointsLocal, pointsVW, 
							     eyeDistances, false);

	return nodeInfo;
    }

    protected PickInfo[] performPick (MouseEvent e) {
	return performPick((Canvas3D)e.getSource(), e.getX(), e.getY());
    }

    protected PickInfo[] performPick (Canvas3D canvas, int x, int y) {
	PickCanvas pickCanvas = pickcanvasList.get(canvas);
	pickCanvas.setShapeLocation(x, y);

	PickInfo[] pickInfos = null;
        
        try {
            pickInfos = pickCanvas.pickAllSorted();
        } catch (RuntimeException except) {
		// TODO: Sometimes NullPointerException gets thrown in the 
                // above pick operation.  This seems to be a Java 3D bug.
                // Report the exception, but keep the system running.
		//logger.log(Level.WARNING, "Error in performPick " + except, except);
                logger.warning("Error in performPick " + except);
		return null;
        }

	if (pickInfos == null || pickInfos[0] == null) {
	    // TODO: SOMEDAY: for some reason sometimes pick infos are null!
	    // TODO: does this bug still exist in J3D 1.4?
    	    // Skip the event
	    return null;
        }

        return pickInfos;
    }

    // TODO: sometimes java3d picks fail. This is a bug workaround.
    // But sometimes even 5 times isn't enough for the pick to succeed
    // (Example: 348).
    protected PickInfo[] performPickAggressive (MouseEvent e) {
	final int numTries = 5;
	PickInfo[] pickInfos;

	for (int i = 0; i < numTries; i++) {
	    pickInfos = performPick(e);
	    if (pickInfos != null) {
		return pickInfos;
	    }
	}

	return null;
    }

    // This is called whenever we have detected a change in the scene graph.
    // (TODO: currently, we can only do this by detecting when Frame3D animations have 
    // completed. This is not complete. It would be nice to be able to detect scene graph
    // changes in other situations but this may be difficult to achieve).
    // This method conses up a mouse event at the last known pointer position
    // and injects it into the event queue. This has the effect of recomputing the
    // current pointer object based on the last pointer position and the 
    // current state of the scene graph.
    //
    // Note: if the pointer is currently grabbed, we postpone the injection until after the 
    // grab is terminated.

    protected void recomputePointerObjectMaybeLater () {
        synchronized (activeGrabState) {
	    Grab currentGrab = activeGrabState.currentGrab(true);
            if (currentGrab != null) {
    	        recomputePointerObjectAfterGrabIsReleased = true;		    
	    } else {
		recomputePointerObject();
            }
	}
    }

    protected abstract void recomputePointerObject ();

    private class EventBuffer {
        LinkedList<AWTEvent> events = new LinkedList();
        AWTEvent lastEvent = null;
        
        /**
         * Clear out all the events
         */
        public void clear() {
            events.clear();
            lastEvent = null;
        }
        
        /**
         * Add an event to this buffer
         *
         * This method implements the event policy, determining
         * if any duplicate events should be dropped
         */
        public void add( AWTEvent e ) {
            int id = e.getID();

            // Don't duplicate consecutive mouse motion events. Discard the older one.
            if ((id == MouseEvent.MOUSE_MOVED || id == MouseEvent.MOUSE_DRAGGED) &&
		lastEvent != null && id == lastEvent.getID()) {
		
		// TODO: HACK to fix Wonderland bug 50: a Wonderland EventModeChanged event 
		// is not a normal type of motion event. Never discard of the queue. This 
		// event is signified by a mouse motion event with isPopupTrigger == true.

                AWTEvent ev = events.getLast();
		MouseEvent me = (MouseEvent) ev;
		if (!me.isPopupTrigger()) { 
		    events.removeLast();
		}
	    }            

            events.add( e );
            lastEvent = e;
        }
        
        /**
         * Process the event queue, creating and posting MouseEvent3D for each event in the queue. 
         * This does not make any changes to the
         * queue itself. Normally clear() will be called after this method
         */
        public void processQueue() {
            while (events.size() != 0) {
                AWTEvent e = events.removeFirst();
                if        (e instanceof MouseEvent) {
                    processAWTEvent((MouseEvent)e);
		} else if (e instanceof KeyEvent) {
                    processAWTEvent((KeyEvent)e);
                }
            }
            lastEvent = null;
        }
    }
}



