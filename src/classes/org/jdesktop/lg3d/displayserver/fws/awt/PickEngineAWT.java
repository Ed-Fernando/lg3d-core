/**
 * Project Looking Glass
 *
 * $RCSfile: PickEngineAWT.java,v $
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
 * $Revision: 1.17 $
 * $Date: 2007-11-30 00:02:26 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.awt;

import java.awt.event.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import javax.media.j3d.Canvas3D;
import javax.vecmath.*;
import javax.media.j3d.Node;

import org.jdesktop.lg3d.displayserver.EventProcessor;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;
import org.jdesktop.lg3d.displayserver.fws.PickEngine;
import org.jdesktop.lg3d.displayserver.fws.ActiveGrabState;
import org.jdesktop.lg3d.displayserver.fws.Grab;
import org.jdesktop.lg3d.displayserver.fws.PassiveGrab;
import org.jdesktop.lg3d.displayserver.fws.PassiveGrabButton;
import org.jdesktop.lg3d.displayserver.fws.PassiveGrabKey;
import org.jdesktop.lg3d.displayserver.fws.PointerObject;

import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEnteredEvent3D;
import org.jdesktop.lg3d.wg.event.MouseWheelEvent3D;
import org.jdesktop.lg3d.wg.event.MouseMovedEvent3D;
import org.jdesktop.lg3d.wg.event.MouseDraggedEvent3D;

import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dComponent3D;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dCursor3D;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dLgBranchGroup;

import org.jdesktop.lg3d.wg.event.KeyEvent3D;

import org.jdesktop.lg3d.scenemanager.utils.event.Frame3DAnimationFinishedEvent;
import org.jdesktop.lg3d.utils.eventadapter.GenericEventAdapter;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.wg.Component3DAnimation;
import org.jdesktop.lg3d.wg.event.LgEventConnector;

import javax.media.j3d.PickInfo;
import javax.media.j3d.Transform3D;

import org.jdesktop.lg3d.wg.event.MouseEvent3D;

/**
 * PickEnginer, manages to cursor picking every frame
 */
public class PickEngineAWT extends PickEngine 
    implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener 
{
    public static final int AUGMENTED_MODIFIERS_F = 1<<0;
    public static final int AUGMENTED_MODIFIERS_R = 1<<1;

    // It doesn't matter what passive grab modifier bits we use, so long as 
    // they are unique and don't conflict with PassiveGrab.ANY_MODIFIER.
    
    private static final int PG_SHIFT_DOWN_MASK     = 1<<0;
    private static final int PG_CTRL_DOWN_MASK      = 1<<1;
    private static final int PG_META_DOWN_MASK      = 1<<2;
    private static final int PG_ALT_DOWN_MASK       = 1<<3;
    private static final int PG_ALT_GRAPH_DOWN_MASK = 1<<4;

    private static final int PG_ALL_BUTTONS_MASK =
        PG_SHIFT_DOWN_MASK |
        PG_CTRL_DOWN_MASK  |
        PG_META_DOWN_MASK  |
        PG_ALT_DOWN_MASK   |
        PG_ALT_GRAPH_DOWN_MASK;

    private PickInfo nearestPickInfoToEye;

    private MouseEventNodeInfo lastButtonReleasedNodeInfo;

    private Vector3f tmpV3f = new Vector3f();

    private int augmentedModifiers;

    /**
     * Creates a PickEngineAWT
     * @param c The Component to add the MouseListener and
     * MouseMotionListener to.
     */
    public PickEngineAWT (javax.media.j3d.Locale locale,
                      EventProcessor eventProcessor ) {
	super(locale, eventProcessor);

	WinSysAWT wsa = (WinSysAWT) fws;
        wsa.addMouseListener(this);
        wsa.addMouseMotionListener(this);
        wsa.addMouseWheelListener(this);
        wsa.addKeyListener(this);

	pointerObjCur = new PointerObject();
	pointerObjPrev = new PointerObject();

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

    // 3 pixels
    private static final int BUTTON_CLICK_POSITION_THRESHOLD = 3;

    // Coordinate of last button press event
    private int buttonLastX, buttonLastY;

    // Returns true if the button release is close enough to the button press
    // so as to consitute a click event.
    // Note: These are Java-on-Windows behavior. There is no click event position
    // threshold on Java-on-Linux. But Hideya wants the Windows behavior.

    private final boolean buttonWithinClickThreshold (int x, int y) {
	return Math.abs(x - buttonLastX) <= BUTTON_CLICK_POSITION_THRESHOLD &&
	       Math.abs(y - buttonLastY) <= BUTTON_CLICK_POSITION_THRESHOLD;
    }

    protected void processAWTEvent( KeyEvent e ) {

	// Snoop for augmented modifiers
	int kc = e.getKeyCode();
	if (kc == KeyEvent.VK_F) {
	    int id = e.getID();
	    if        (id == KeyEvent.KEY_PRESSED) {
		augmentedModifiers |= AUGMENTED_MODIFIERS_F;
	    } else if (id == KeyEvent.KEY_RELEASED) {
		augmentedModifiers &= ~AUGMENTED_MODIFIERS_F;
	    }
	} if (kc == KeyEvent.VK_R) {
	    int id = e.getID();
	    if (id == KeyEvent.KEY_PRESSED) {
		augmentedModifiers |= AUGMENTED_MODIFIERS_R;
	    } else if (id == KeyEvent.KEY_RELEASED) {
		augmentedModifiers &= ~AUGMENTED_MODIFIERS_R;
	    }
	}
	
	super.processAWTEvent(e);
    }

    protected void processAWTEvent( MouseEvent e ) {

        //logger.warning("PEA.processAWTEvent, e = " + e);

	// Implement the click threshold. Allow click event to be 
	// passed along only
	if        (e.getID() == MouseEvent.MOUSE_PRESSED) {
	    buttonLastX = e.getX();
	    buttonLastY = e.getY();
	} else if (e.getID() == MouseEvent.MOUSE_CLICKED) {
	    if (!buttonWithinClickThreshold(e.getX(), e.getY())) {
		// Discard the event
		return;
	    }
	}

        // Record last pointer position for handling current pointer object recomputation
        // when the scene graph changes (see PickEngine.recomputePointerObjectMaybeLater);
        int type = e.getID();
        if (type == MouseEvent.MOUSE_MOVED || type == MouseEvent.MOUSE_DRAGGED) {
	    lastPointerCanvas = (Canvas3D) e.getComponent();
    	    lastPointerX = e.getX();
	    lastPointerY = e.getY();
	}

	// Decide where to send the event (to a picked or grabbed node)
	determineEventDestInfo(e);
	if (pointerObjCur.destNodeInfo == null || pointerObjCur.destNodeInfo.getNode(0) == null) {
            // If nothing is picked, position the cursor using the default distance.
            updateCursor(tmpV3f, null, (Canvas3D)e.getSource(), e.getX(), e.getY(), Float.NaN);
	    return;
	}

	// Calculated information derived from the node info
	pointerObjCur.calcDerivedInfo();
        logger.fine("destNode = " + pointerObjCur.destNode);

        // Deliver the event to the selected destination
        deliverEventUsingNodeInfo(e);
    }

    protected void determineEventDestInfo (MouseEvent e) {
	MouseEventNodeInfo nodeInfo;
        boolean deactivateGrab = false;

	pointerObjCur.intersectedNodeInfo = null;

	// Handle button clicked events specially. The mouse clicked event
	// comes after the grab has terminated. So we do this in order to 
	// force the clicked event to go to the same destination as the
	// pressed event
	if (e.getID() == MouseEvent.MOUSE_CLICKED) {
	    pointerObjCur.destNodeInfo = lastButtonReleasedNodeInfo;
	    return;
	}

        synchronized (activeGrabState) {
	    Grab currentGrab = activeGrabState.currentGrab(true);

    	    // First, determine the node info ignoring grabs
    	    nodeInfo = determineEventDestInfoNotGrabbed(e);
	    if (currentGrab == null && (nodeInfo == null || nodeInfo.getNode(0) == null)) {
		if (e.getID() == MouseEvent.MOUSE_RELEASED) {
		    lastButtonReleasedNodeInfo = null;
		}
		pointerObjCur.destNodeInfo = null;
		return;
	    }

	    // Update the grab state
	    int eventID = e.getID();
	    if (eventID == MouseEvent.MOUSE_PRESSED ||
		eventID == MouseEvent.MOUSE_RELEASED) {
		Node node = null;
		if (nodeInfo != null) {
		    node = nodeInfo.getNode(0);
		}
		deactivateGrab = evaluateButtonGrabStateChange(eventID, e, node);
		currentGrab = activeGrabState.currentGrab(true);
	    }

	    // If a grab is active, the event destination will be the grabbed node
	    if (currentGrab == null) {
		logger.fine("Deliver event normal (ungrabbed)");
		pointerObjCur.destNodeInfo = nodeInfo;
		if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
		    pointerObjCur.intersectedNodeInfo = pointerObjCur.destNodeInfo;
		} 
	    } else {
		logger.fine("Deliver event GRABBED");
		pointerObjCur.destNodeInfo = determineEventDestInfoGrabbed(e, nearestPickInfoToEye);
		if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
 		    pointerObjCur.intersectedNodeInfo = nodeInfo;
		} 
	    }

            // It is now safe to disable the grab
	    if (deactivateGrab) {
	        activeGrabState.grabDisable(true);
		if (recomputePointerObjectAfterGrabIsReleased) {
		    recomputePointerObjectAfterGrabIsReleased = false;
	    	    recomputePointerObject();
		}
		nodeInfoPointerGrabStart = null;
  	        logger.fine("******************** GRAB DISABLED");
	    }
	}

	if (e.getID() == MouseEvent.MOUSE_RELEASED) {
	    lastButtonReleasedNodeInfo = nodeInfo;
	}
    }

    private boolean evaluateButtonGrabStateChange (int eventID, MouseEvent e, Node pointerNode) {
	Grab grab = null;
        boolean terminate = false;
	int buttonNum = convertAwtEventButtonToPassiveGrabButton(e.getButton());
	int modifiers = convertAwtEventModifiersToPassiveGrabModifiers(e.getModifiers());

        // First, check for passive grab state change
        if (eventID == MouseEvent.MOUSE_PRESSED) {
	    /********************** PASSIVE GRABS ARE NO LONGER SUPPORTED
	     ** Because PassiveGrab.spriteTrace is hanging onto a reference to AppWindowX11
	     ** even after the window has died.
	    // Button Press
	    grab = PassiveGrab.checkDeviceGrabs(pointerNode, true, buttonNum, modifiers);
	    */
	} else {
	    // Button Release: X11 semantic: terminate grab only when all buttons are released	
	    if ((modifiers & PG_ALL_BUTTONS_MASK) == 0) {
		terminate = true;
            }
	}

        // If no state change from above, determine whether a default button grab triggers
	if (grab == null && !terminate) {
	    if (eventID == MouseEvent.MOUSE_PRESSED) {
		// Button Press
		grab = new PassiveGrabButton(buttonNum, modifiers, pointerNode, null);
	    } else {
		// Button Release: X11 semantic: terminate grab only when all buttons are released	
		if ((modifiers & PG_ALL_BUTTONS_MASK) == 0) {
		    terminate = true;
        	}
	    }
	}

	if (grab != null) {
	    activeGrabState.grabEnable(true, grab, true);
	    logger.fine("******************** GRAB ENABLED (passive), grab = " + grab + ", isPointer = " + true);
	} 

	return terminate;
    }

    private int convertAwtEventButtonToPassiveGrabButton (int buttonNum) {
        if        (buttonNum == MouseEvent.BUTTON1) {
	    return 1;
	} else if (buttonNum == MouseEvent.BUTTON2) {
	    return 2;
	} else if (buttonNum == MouseEvent.BUTTON3) {
	    return 3;
	}

	throw new RuntimeException("Invalid button number, buttonNum = " + buttonNum);
    }

    private int convertAwtEventModifiersToPassiveGrabModifiers (int modifiers) {
	int pgModifiers = 0;

        if ((modifiers & InputEvent.SHIFT_DOWN_MASK) != 0) {
	    pgModifiers |= PG_SHIFT_DOWN_MASK;
	}

        if ((modifiers & InputEvent.CTRL_DOWN_MASK) != 0) {
	    pgModifiers |= PG_CTRL_DOWN_MASK;
	}

	if ((modifiers & InputEvent.META_DOWN_MASK) != 0) {
	    pgModifiers |= PG_META_DOWN_MASK;
	}

	if ((modifiers & InputEvent.ALT_DOWN_MASK) != 0) {
	    pgModifiers |= PG_ALT_DOWN_MASK;
	}

	if ((modifiers & InputEvent.ALT_GRAPH_DOWN_MASK) != 0) {
	    pgModifiers |= PG_ALT_GRAPH_DOWN_MASK;
	}

	return pgModifiers;
    }

    // Determine the destination nodes and asssociated info for the event without 
    // considering grabbing. In other words, do picking using the current state of 
    // the scene graph.

    private MouseEventNodeInfo determineEventDestInfoNotGrabbed (MouseEvent e) {
        PickInfo[] pickInfos = performPick(e);
	if (pickInfos == null) return null;
	MouseEventNodeInfo nodeInfo = createNodeInfoFromPickInfos(pickInfos);
        nearestPickInfoToEye = pickInfos[0];
	return nodeInfo;
    }

    // Determine the destination node and associated info for the event in the 
    // case where the event is grabbed. The event destination node will be
    // the grab node.

    protected MouseEventNodeInfo determineEventDestInfoGrabbed (MouseEvent e,
					PickInfo pickInfo) {

	if (nodeInfoPointerGrabStart == null) {
	    Node[] nodes = new Node[1];
	    Point3f[] pointsLocal = new Point3f[1];
	    Point3f[] pointsVW = new Point3f[1];
	    float[] eyeDistances = new float[1];

	    // The node info geometry of a mouse pressed is based on nearest-to-the-eye
	    // pick info for that event
	    try {
		nodes[0] = pickInfo.getNode();
		Point3d intersectionPt = pickInfo.getClosestIntersectionPoint();
		pointsLocal[0] = new Point3f(intersectionPt);
		Transform3D t3d = pickInfo.getLocalToVWorld();
		pointsVW[0] = new Point3f(intersectionPt);
                t3d.transform(pointsVW[0]);
		eyeDistances[0] = (float) pickInfo.getClosestDistance();
	    } catch (RuntimeException except) {
		// TODO: there is a (possible) Java3D bug which causes an "interpolation
		// point outside quad" exception at seemingly random times. Ignore this
		// exception and skip the event
		//logger.log(Level.WARNING,"Error in findIntersectionPoint " + except, except);
		return null;
	    }

	    nodeInfoPointerGrabStart = new MouseEventNodeInfo(nodes, pointsLocal, pointsVW, 
							      eyeDistances, true);
	    
	}
	
	return nodeInfoPointerGrabStart;
    }

    protected void deliverMouseEventToSources (MouseEvent e, Vector3f cursorPos) {
        MouseEvent3D me;

        switch(e.getID()) {

	case MouseEvent.MOUSE_CLICKED:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseButtonEvent3D( e, e.getID(), pointerObjCur.destNodeInfo, cursorPos);
		me.setAugmentedModifiers(augmentedModifiers);
                //logger.fine("Deliver MouseClicked for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;

        case MouseEvent.MOUSE_PRESSED:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseButtonEvent3D( e, e.getID(), pointerObjCur.destNodeInfo, cursorPos );
		me.setAugmentedModifiers(augmentedModifiers);
                //logger.fine("Deliver MousePressed for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;

        case MouseEvent.MOUSE_RELEASED:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseButtonEvent3D( e, e.getID(), pointerObjCur.destNodeInfo, cursorPos );
		me.setAugmentedModifiers(augmentedModifiers);
                //logger.fine("Deliver MouseReleased for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;

        case MouseEvent.MOUSE_MOVED:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseMovedEvent3D( e, pointerObjCur.destNodeInfo, cursorPos );
		me.setAugmentedModifiers(augmentedModifiers);
                //logger.fine("Deliver MouseMoved for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;

        case MouseEvent.MOUSE_DRAGGED:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseDraggedEvent3D( e, pointerObjCur.intersectedNodeInfo, 
					      pointerObjCur.destNodeInfo, cursorPos );
		me.setAugmentedModifiers(augmentedModifiers);
                //logger.fine("Deliver MouseDragged for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;

        case MouseEvent.MOUSE_WHEEL:
            for (LgEventSource s : mouseEventSources) {
		me = new MouseWheelEvent3D( e, pointerObjCur.destNodeInfo, cursorPos );
		me.setAugmentedModifiers(augmentedModifiers);
                //logger.fine("Deliver MouseWheel for source " + s + ", event = " + me);
                eventProcessor.postEvent(me, s);
            }
            break;
        }
    }

    protected Vector3f updateCursor(Vector3f cursorPos, LinkedList nodePath, Canvas3D canvas, 
				    int awtX, int awtY, float distance) {
        
	if (cursorModule == null) return null;

	// Move the cursor to the current event coordinates
	cursorModule.setCursorPosition(cursorPos, awtX, awtY, canvas, distance);

        if (nodePath != null) {
	    setCursorForNodePath(nodePath);
	}

        return cursorPos;
    }

    protected void recomputePointerObject () {
	MouseEvent me = new MouseEvent(lastPointerCanvas, MouseEvent.MOUSE_MOVED, 0, 0, 
                                       lastPointerX, lastPointerY, 0, false, MouseEvent.NOBUTTON);
        processAWTEvent(me);
    }

    public void mouseClicked(MouseEvent e) {
	enqueue(e);
    }
    
    public void mouseEntered(MouseEvent e) {
	enqueue(e);
    }

    public void mouseExited(MouseEvent e) {
	enqueue(e);
    }

    public void mousePressed(MouseEvent e) {
	enqueue(e);
    }

    public void mouseReleased(MouseEvent e) {
	enqueue(e);
    }

    public void mouseDragged(MouseEvent e) {
	enqueue(e);
    }

    public void mouseMoved(MouseEvent e) {
	enqueue(e);
    }
    
    public void mouseWheelMoved(MouseWheelEvent e) {
	enqueue(e);
    }
    
    public void keyPressed(KeyEvent e) {
	enqueue(e);
    }

    public void keyReleased(KeyEvent e) {
	enqueue(e);
    }

    public void keyTyped(KeyEvent e) {
	enqueue(e);
    }
    
    protected void implementFocusFollowsMousePolicy (PointerObject po) {
        if (po != null) {
            setKeyEventSource(po.destPathFromRoot, INVALID_WINDOW);
        } else {
            setKeyEventSource(null, INVALID_WINDOW);
        }
    }
}


