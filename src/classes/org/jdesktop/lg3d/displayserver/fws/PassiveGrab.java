/**
 * Project Looking Glass
 *
 * $RCSfile: PassiveGrab.java,v $
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
 * $Date: 2006-08-14 23:13:31 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphPath;

public abstract class PassiveGrab extends Grab {

    // Same has the equivalent names in Escher Window.java
    public static final int ANY_KEY = 0;  
    public static final int ANY_MODIFIER = 0x8000;

    // Passive Grab Device Types
    static final int POINTER  = 0;
    static final int KEYBOARD = 1;

    // Passive Grab Types
    static final int BUTTON   = 0;
    static final int KEY      = 1;

    // Stores the per-node passive grab lists
    static HashMap nodeToButtonPassiveGrabs = new HashMap();

    // Stores the per-node passive grab lists
    static HashMap nodeToKeyPassiveGrabs = new HashMap();

    protected int device;
    protected int type;
    protected long modifiers;
    protected GrabDetail modifiersDetail;
    protected GrabDetail detail;

    protected static final LinkedList spriteTrace = new LinkedList();

    protected static LinkedList<PassiveGrab> getGrabListForGrabNode (PassiveGrab grab) {
	if (grab instanceof PassiveGrabButton) {
	    return (LinkedList<PassiveGrab>) nodeToButtonPassiveGrabs.get(grab.grabNode);
	} else {
	    return (LinkedList<PassiveGrab>) nodeToKeyPassiveGrabs.get(grab.grabNode);
	}
    }

    protected static LinkedList<PassiveGrab> getGrabListForEventType (Node node, boolean eventIsButton) {
	if (eventIsButton) {
	    return (LinkedList<PassiveGrab>) nodeToButtonPassiveGrabs.get(node);
	} else {
	    return (LinkedList<PassiveGrab>) nodeToKeyPassiveGrabs.get(node);
	}
    }

    protected static LinkedList<PassiveGrab> createGrabList (PassiveGrab grab) {
	LinkedList<PassiveGrab> passiveGrabList = new LinkedList<PassiveGrab>();
	if (grab instanceof PassiveGrabButton) {
	    nodeToButtonPassiveGrabs.put(grab.grabNode, passiveGrabList);
	} else {
	    nodeToKeyPassiveGrabs.put(grab.grabNode, passiveGrabList);
	}
	return passiveGrabList;
    }

    protected static void deleteGrabList (PassiveGrab grab) {
	if (grab instanceof PassiveGrabButton) {
	    nodeToButtonPassiveGrabs.put(grab.grabNode, null);
	} else {
	    nodeToKeyPassiveGrabs.put(grab.grabNode, null);
	}
    }

    // Returns false if there is a grab already defined which conflicts
    public synchronized static boolean add (PassiveGrab grab) {
        LinkedList<PassiveGrab> passiveGrabList = getGrabListForGrabNode(grab);

	if (passiveGrabList == null) {
	    passiveGrabList = createGrabList(grab);
	}

	for (PassiveGrab g : passiveGrabList) {
	    if (grab.matches(g)) {
		return false;
	    }
	}

	passiveGrabList.add(grab);

	return true;
    }

    // Deletes the grab from the passive grab list
    // Old X11 note: the following is kinda complicated, because we need to be able to back out
    //if any allocation fails

    public synchronized static void delete (PassiveGrab minuendGrab) {

        LinkedList<PassiveGrab> passiveGrabList = getGrabListForGrabNode(minuendGrab);
	if (passiveGrabList == null) {
	    return;
	}

	for (PassiveGrab grab: passiveGrabList) {

	    if (!grab.matches(minuendGrab)) {
		continue;
	    }

	    if (minuendGrab.supercedesSecond(grab)) {
		passiveGrabList.remove(grab);
	    } else if ((grab.detail.exact == ANY_KEY) &&
		       (grab.modifiersDetail.exact != ANY_MODIFIER)) {
		grab.detail.deleteDetailFromMask(minuendGrab.detail.exact);
		
	    } else if ((grab.modifiersDetail.exact == ANY_MODIFIER)  &&
		       (grab.detail.exact != ANY_KEY)) {
		grab.modifiersDetail.deleteDetailFromMask(minuendGrab.modifiersDetail.exact);

	    } else if ((minuendGrab.detail.exact != ANY_KEY) &&
		       (minuendGrab.modifiersDetail.exact != ANY_MODIFIER)){
		PassiveGrab newGrab;
	    
		grab.detail.deleteDetailFromMask(minuendGrab.detail.exact);

		if (minuendGrab instanceof PassiveGrabButton) {
		    PassiveGrabButton buttonGrab = (PassiveGrabButton) grab;
		    newGrab = new PassiveGrabButton(minuendGrab.detail.exact, ANY_MODIFIER,
						    buttonGrab.grabNode, buttonGrab.cursor);
		} else {
		    newGrab = new PassiveGrabKey(minuendGrab.detail.exact, ANY_MODIFIER,
						 grab.grabNode);
		}

		passiveGrabList.add(newGrab);

		newGrab.modifiersDetail = grab.modifiersDetail;
		newGrab.detail.deleteDetailFromMask(minuendGrab.modifiersDetail.exact);

	    } else if (minuendGrab.detail.exact == ANY_KEY) {
		grab.modifiersDetail.deleteDetailFromMask(minuendGrab.modifiersDetail.exact);
	    } else {
		grab.detail.deleteDetailFromMask(minuendGrab.detail.exact);
	    }
	}

	if (passiveGrabList.size() == 0) {
	    deleteGrabList(minuendGrab);
	}
    }

    // "CheckDeviceGrabs" handles both keyboard and pointer events that may cause
    // a passive grab to be activated.  If the event is a keyboard event, the
    // ancestors of the focus node are traced down and tried to see if they have
    // any passive grabs to be activated.  If the focus node itself is reached and
    // it's descendants contain they pointer, the ancestors of the node that the
    // pointer is in are then traced down starting at the focus node, otherwise no
    // grabs are activated.  If the event is a pointer event, the ancestors of the
    // node that the pointer is in are traced down starting at the root until
    // CheckPassiveGrabs causes a passive grab to activate or all the nodes are
    // tried. PRH
    //
    // pointerNode is the lowest node that the pointer is currently in.
    //
    // Returns the grab if a grab is triggered or null otherwise

    public static PassiveGrab checkDeviceGrabs (Node pointerNode, boolean eventIsButton,
						int eventDetail, int eventState) {

/* TODO: implement search of focus trace for key events
       register FocusClassPtr focus = device->focus;
       if (focus) {
           for (; i < focus->traceGood; i++) {
	       pWin = focus->trace[i];
	       if (pWin->optional &&
	           checkPassiveGrabsOnNode(pWin, device, xE, count))
	           return TRUE;
	   }
  
	   if ((focus->win == NoneWin) ||
	       (i >= spriteTraceGood) ||
	       ((i > checkFirst) && (pWin != spriteTrace[i-1])))
	       return FALSE;
       }
*/

	// Create the "sprite trace:" this is a list that contains the path through the scene
        // graph of pickable nodes from the top-most node down to the current pointer node.
        Node node = pointerNode;
        spriteTrace.clear();
	while (node != null) {
	    if (node.getCapability(Node.ENABLE_PICK_REPORTING)) {
	        spriteTrace.addLast(node);
    	    }
	    node = node.getParent();
	}

	// Traverse from top-most node downward toward current pointer node
        ListIterator it = spriteTrace.listIterator();
	while (it.hasNext()) {
	    node = (Node) it.next();
	    PassiveGrab grab = checkPassiveGrabsOnNode(node, eventIsButton, eventDetail, eventState);
	    if (grab != null) {
		return grab;
	    }
	}
	
	return null;
    }

    // CheckPassiveGrabsOnNode checks to see if the event causes a
    // passive grab set on the node to be activated.
    // Returns the grab if a grab is triggered or null otherwise

    private synchronized static PassiveGrab checkPassiveGrabsOnNode (Node node, boolean eventIsButton,
								     int eventDetail, int eventState) {
	PassiveGrab tempGrab;
	ListIterator it;

	LinkedList passiveGrabList = getGrabListForEventType(node, eventIsButton);
	if (passiveGrabList == null) {
	    return null;
	}

	if (eventIsButton) {
	    tempGrab = new PassiveGrabButton(eventDetail, eventState, node, null);
	} else {
	    tempGrab = new PassiveGrabKey(eventDetail, eventState, node);
	}
	
	it = passiveGrabList.listIterator();
	while (it.hasNext()) {
	    PassiveGrab grab = (PassiveGrab) it.next();
	    if (tempGrab.matches(grab)) {
		return grab;
	    }
	}

	return null;
    }

    public PassiveGrab (int device, int type, long modifiers, Node grabNode) {
	super(grabNode);
	this.device = device;
	this.type = type;
	this.modifiers = modifiers;
    }

    public int getDevice () { return device; }
    public int getType () { return type; }
    public long getModifiers () { return modifiers; }

    public boolean matches (PassiveGrab secondGrab) {

	if ((device != secondGrab.device) ||
	    (type != secondGrab.type)) {
	    return false;
	}

	if (supercedesSecond(secondGrab) ||
	    secondGrab.supercedesSecond(this)) {
	    return true;
	}
 
	if (secondGrab.detailSupercedesSecond(secondGrab.detail, detail, ANY_KEY) && 
	    detailSupercedesSecond(modifiersDetail, secondGrab.modifiersDetail, ANY_MODIFIER)) {
	    return true;
	}

	if (detailSupercedesSecond(detail, secondGrab.detail, ANY_KEY) &&
	    secondGrab.detailSupercedesSecond(modifiersDetail, modifiersDetail, 
					      ANY_MODIFIER)) {
	    return true;
	}

	return true;
    }

    protected boolean supercedesSecond (PassiveGrab secondGrab) {
	if (!detailSupercedesSecond(modifiersDetail, secondGrab.modifiersDetail, ANY_MODIFIER)) {
	    return false;
	}

	if (detailSupercedesSecond(detail, secondGrab.detail, ANY_KEY)) {
	    return true;
	} 

	return false;
    }

    protected boolean detailSupercedesSecond (GrabDetail firstDetail, GrabDetail secondDetail, int exception) {
	if (isInGrabMask(firstDetail, secondDetail, exception)) {
	    return true;
	}

	if (identicalExactDetails(firstDetail.exact, secondDetail.exact, exception)) {
	    return true;
	}
  
	return false;
    }

    protected boolean isInGrabMask (GrabDetail firstDetail, GrabDetail secondDetail, int exception) {
        if (firstDetail.exact == exception) {
	    if (firstDetail.maskAry == null) {
		return true;
	    }
	
	    // (at present) never called with two non-null mask arrays */
	    if (secondDetail.exact == exception) {
		return false;
	    }

	    if (firstDetail.getMaskBit(secondDetail.exact)) {
		return true;
	    }
	}
	
	return false;
    }

    protected boolean identicalExactDetails (int firstExact, int secondExact, int exception) {
	if ((firstExact == exception) || (secondExact == exception)) {
	    return false;
	}
   
	if (firstExact == secondExact) {
	    return true;
	}

	return false;
    }

    protected class GrabDetail {

	// 256 keycodes and 256 possible modifier combinations, but only	
	// 3 buttons

	static final int MASKS_PER_DETAIL_MASK = 8;

	int exact;
	long[] maskAry = null;

	GrabDetail (int exact) {
	    this.exact = exact;
	}

	GrabDetail (long exact) {
	    this.exact = (int) exact;
	}

        final long bitMask (int idx) { return ((long)1) << ((idx) & 31); }
        final int maskIdx (int idx) { return idx >> 5; }
        final long maskWord (int idx) { return maskAry[maskIdx(idx)]; }
        final void bitClear (int idx) { maskAry[maskIdx(idx)] &= ~bitMask(idx); }

        final boolean getMaskBit (int idx) {
	    long maskWord = maskWord(idx);
	    long bitMask  = bitMask(idx);
	    return (maskWord & bitMask) != 0;
	}

        void deleteDetailFromMask (int detail) {
	    long[] newMaskAry = new long[MASKS_PER_DETAIL_MASK];

	    if (maskAry != null) {
		for (int i = 0; i < MASKS_PER_DETAIL_MASK; i++) {
		    newMaskAry[i]= maskAry[i];
		}
	    } else {
		for (int i = 0; i < MASKS_PER_DETAIL_MASK; i++) {
		    newMaskAry[i]= ~0L;
		}
	    }

	    maskAry = newMaskAry; 
	    bitClear(detail);
	}
    }

    public String toString () {
	String str;

	str = "node=";
	str += grabNode;
	str += ",device=";
	str += (device == POINTER) ? "pointer" : "keyboard";
	str += ",type=";
        str += (type == BUTTON) ? "button" : "key";
	str += ",modifiers=";
	str += modifiers + " ";
	str += ",modifiersDetail=";
	str += modifiersDetail.exact;
	str += ",detail=";
	str += detail.exact;

	return str;
    }
}

