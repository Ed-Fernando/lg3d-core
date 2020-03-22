/**
 * Project Looking Glass
 *
 * $RCSfile: CookedEventPoller.java,v $
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
 * $Revision: 1.18 $
 * $Date: 2006-09-26 23:13:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import org.jdesktop.lg3d.displayserver.fws.*;
import gnu.x11.Display;
import gnu.x11.Input;
import gnu.x11.Data;
import gnu.x11.Window;
import gnu.x11.event.Event;
import gnu.x11.event.KeyPress;
import gnu.x11.event.KeyRelease;
import gnu.x11.event.ButtonPress;
import gnu.x11.event.ButtonRelease;
import gnu.x11.event.MotionNotify;
import gnu.x11.event.EnterNotify;
import gnu.x11.event.LeaveNotify;
import gnu.x11.event.MappingNotify;
import gnu.x11.event.ClientMessage;

import java.io.IOException;
import javax.media.j3d.*;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.displayserver.EventProcessor;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;
import org.jdesktop.lg3d.wg.event.MouseWheelEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.Component;
import java.awt.AWTEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyListener;
import javax.media.j3d.BranchGroup;

import java.util.logging.Logger;

import com.sun.j3d.utils.picking.PickResult;

// TODO: Only for AWT from JDK 1.5.x
import java.lang.reflect.Field;

import sun.awt.X11.LgXWindow;
import sun.awt.X11.LgXTranslatedKeyEvent;

/**
 * CookedEventPoller is a thread which waits for all event types 
 * except low-level device events and damage events and distributes 
 * them to the appropriate destinations.
 */

class CookedEventPoller implements Runnable {

    protected Logger logger = java.util.logging.Logger.getLogger("lg.fws");

    // Do for JDK 1.5.x
    // TODO: get a better method added to JDK 1.6
    private Class awtEventClass;
    private Field awtEventBdataField;

    private HashMap<Long, Window> prwList = new HashMap<Long, Window>();

    private Display cepDpy;
    private long[] prws;
    private ExtensionSet exts;

    private WinSysX11 wsx;
    private CursorImageEventBroker ebCursorImage;

    private EventProcessor eventProcessor;

    private int cursorImageNotifyEventCode;
    
    private PickEngineX11 eventDeliverer;

    // For button multi-click detection
    private int buttonLastButton = 0;
    private long buttonLastWid = 0;
    private long buttonLastTime = 0;
    private int buttonLastX, buttonLastY;
    private int buttonClickCount = 0;
    private int buttonMultiClickTime;
    private int buttonRightButtonNumber;

    private static class MouseButtonsReturn {
	AWTEvent event;
	AWTEvent clickedEvent;
    }

    // The pick sequence number from the most recent ClientMessage event
    private int curPickSeq;

    // For performance analysis
    //private StatBuf sb = new StatBuf();

    private SequencedKeyEvent[] keyEventsToPost = new SequencedKeyEvent[2];
    private LgXTranslatedKeyEvent[] translatedKeyEvents = new LgXTranslatedKeyEvent[2];

    private static native void nativeInitKey (Display dpy);

    // TODO: I'm not sure this really belongs in CEP
    public static native int keysymToKeycode (Display dpy, int keysym);

// TODO: convert this to Java
//    private static native String XGetDefault (long dpy, long ds, String program, 
//					      String option);


    private LgXWindow lgXWindow;

    static {
        String soFile = System.getProperty("lg.libraries.CookedEventPoller", "");
        if (!"".equals(soFile)) {
            System.load(soFile);
        } else {
            System.loadLibrary("CookedEventPoller");
        }
    }

    public CookedEventPoller (Display cepDpy, ExtensionSet exts, 
			      PickEngineX11 eventDeliverer, long[] prws, 
			      BranchGroup mgmtBG, WinSysX11 wsx) 
        throws InstantiationException
    {
	this.wsx = wsx;

	this.cepDpy = cepDpy;
	nativeInitKey(cepDpy);
	
	this.prws = prws;
	this.exts = exts;
	
	this.eventDeliverer = eventDeliverer;

	ebCursorImage = new CursorImageEventBroker(exts);
        mgmtBG.addChild(ebCursorImage);

	cursorImageNotifyEventCode = exts.xfixesExtForCep.first_event + CursorImageEvent.CODE_OFFSET;

	for (int i=0; i < prws.length; i++) {		
	    Window prwin = new Window((int)prws[i]);
	    prwList.put(prws[i], prwin);
	    prwin.display = cepDpy;
	    exts.xfixesExtForCep.selectCursorInput(prwin, X11XfixesExt.DISPLAY_CURSOR_NOTIFY_MASK);

	    prwin.select_input(
		Event.ENTER_WINDOW_MASK |
		Event.KEY_PRESS_MASK | Event.KEY_RELEASE_MASK |
                Event.BUTTON_PRESS_MASK | Event.BUTTON_RELEASE_MASK |
                Event.POINTER_MOTION_MASK);

	    // Note: The WM has already selected ClientMessage events
	    // with SUBSTRUCTURE_REDIRECT_MASK. Only one client may 
	    // select for this event.
	}

	cepDpy.check_error(); // Xsync 

	initializeMultiClickTime();
	buttonRightButtonNumber = getRightButtonNumber(cepDpy);

	lgXWindow = new LgXWindow();
	for (int i = 0; i < 2; i++) {
	    translatedKeyEvents[i] = new LgXTranslatedKeyEvent();
	}

	Thread eventThread = new Thread( AppConnectorPrivate.getThreadGroup(), this, "CookedEventPoller" );
	eventThread.start();
    }

    private int getRightButtonNumber (Display dpy) {
	Data data = dpy.input.pointer_mapping();
	return (int) data.read1(1);
    }

    CursorImageEventBroker getCursorImageEventBroker () { return ebCursorImage; }

    public void setEventProcessor (EventProcessor eventProcessor) {
	this.eventProcessor = eventProcessor;
    }

    public EventProcessor getEventProcessor () {
	return eventProcessor;
    }

    public void run () {
        Event event;
        AWTEvent awtEvent;
	
	for (;;) {
	    Event xevent;

	    // Blocking wait for next event
	    xevent = cepDpy.next_event();

	    // Distribute event to appropriate broker
            int eventCode = xevent.code();
            //System.err.println("CEP: received xevent code = " + xevent.code());
	    
	    if (eventCode == cursorImageNotifyEventCode) {
		//System.err.println("Received cursor image event");
		event = new CursorImageEvent(cepDpy, xevent.data);
    	        ebCursorImage.enqueue(event);
		continue;
	    } else if (eventCode == (ClientMessage.CODE)) {
		ClientMessage cmEvent = (ClientMessage) xevent;
		curPickSeq = cmEvent.wm_data();
		if (cmEvent.type_id() != wsx.pickSeqAtom.id) {
		    logger.warning("CookedEventPoller: client message atom is incorrect");
		}
		//System.err.println("Received CM event, pickSeq = " + curPickSeq);
		continue;
	    }

	    switch (eventCode) {

            case KeyPress.CODE:
            case KeyRelease.CODE: {
		xevent.window_offset = 12;
		gnu.x11.event.Input xeventin = (gnu.x11.event.Input) xevent;
		long rootWid = xeventin.root_id();
		long wid = xevent.window_id();
		Canvas3D canvas = wsx.getCanvasForRootWin(rootWid, wid);
		int xcode = xeventin.code();
		int keycode = xeventin.detail();
		int state = xeventin.state();
		long time = xeventin.time();
		int numKeyEvents = createKeyEvents(canvas, rootWid, wid,
						   xcode, keycode, state, time);
		for (int i = 0; i < numKeyEvents; i++) {
		    processEvent3D(keyEventsToPost[i]);
		}
		break;
	    }		
        
            case ButtonPress.CODE:
            case ButtonRelease.CODE: {
		xevent.window_offset = 12;
		gnu.x11.event.Input xeventin = (gnu.x11.event.Input) xevent;
		long rootWid = xeventin.root_id();
		long wid = xevent.window_id();
		Canvas3D canvas = wsx.getCanvasForRootWin(rootWid, wid);
		MouseButtonsReturn awtEvents;
		awtEvents = createMouseButtonEvent((gnu.x11.event.Input)xevent, 
						   canvas, rootWid, wid);
		if (awtEvents != null) {
		    processEvent3D(awtEvents.event);
		    if (awtEvents.clickedEvent != null) {
			// Important: the button click event shares the evinfo of the 
			// button press/release event
			processEvent3D(awtEvents.clickedEvent);
		    }
		}
		break;
	    }

            case MotionNotify.CODE: {
		xevent.window_offset = 12;
		gnu.x11.event.Input xeventin = (gnu.x11.event.Input) xevent;
		long rootWid = xeventin.root_id();
		long wid = xevent.window_id();
		Canvas3D canvas = wsx.getCanvasForRootWin(rootWid, wid);

                // For performance analysis
		//EventInfo3D evinfo = EventInfo3D.peek(xeventin.seq_no());
		//long nsNow = System.nanoTime();
		//float nsDelta = (float)(nsNow - evinfo.nsTimeSentToXS);
		//float msDelta = nsDelta / 1E6f;
		//sb.add(msDelta);
		
		//System.err.println("child id = " + xeventin.child_id());
		awtEvent = createMouseMotionEvent((MotionNotify)xevent, canvas, rootWid, wid);
		processEvent3D(awtEvent);
		break;
	    }
		
	    case EnterNotify.CODE: {
		xevent.window_offset = 12;
		gnu.x11.event.Input xeventin = (gnu.x11.event.Input) xevent;
		long rootWid = xeventin.root_id();
		long wid = xevent.window_id();
		Canvas3D canvas = wsx.getCanvasForRootWin(rootWid, wid);
		awtEvent = createNativeEnterEvent(xeventin, canvas, rootWid, wid);
		processEvent3D(awtEvent);
		break;
	    }

	    default:
    	        // Note: Ignore events that we don't care about such as 
    	        // UnmapNotify and DestroyNotify
	    }

	    // The only events which should arrive at the DS without a preceding 
	    // ClientMessage event are 2D events grabbed by the DS. Make sure that
	    // these events are assigned a pick sequence of -1 so that PickEngineX11
	    // will perform a pick on these events.
	    curPickSeq = -1;
	}
    }

    // Send events to the upper part of the event deliverer.
    // Assign them the current pick sequence number

    private void processEvent3D (AWTEvent awtEvent) {

	if (awtEvent instanceof SequencedMouseEvent) {
	    SequencedMouseEvent mouseEvent = (SequencedMouseEvent) awtEvent;
	    if (awtEvent.getID() != MouseEvent.MOUSE_CLICKED) {
		mouseEvent.setPickSeq(curPickSeq);
	    }
	} else if (awtEvent instanceof SequencedMouseWheelEvent) {
	    SequencedMouseWheelEvent mouseWheelEvent = (SequencedMouseWheelEvent) awtEvent;
	    mouseWheelEvent.setPickSeq(curPickSeq);
	} else if (awtEvent instanceof SequencedKeyEvent) {
	    SequencedKeyEvent keyEvent = (SequencedKeyEvent) awtEvent;
	    keyEvent.setPickSeq(curPickSeq);
	}

	//System.err.println("Send event to EDU: event = " + awtEvent);
	eventDeliverer.enqueue(awtEvent);
    }

    private AWTEvent createMouseMotionEvent (MotionNotify xevent, Canvas3D canvas, long rootWid, long wid) {
	long when = LgXWindow.nowMillisUTC_offset((long)xevent.time());
	int state = xevent.state();
	int modifiers = awtEventGetModifiers(state, 0, 0);
	boolean dragging = (state & 
			    (Input.BUTTON1_MASK | 
			     Input.BUTTON2_MASK | 
			     Input.BUTTON3_MASK)) != 0;
	int type = dragging ? MouseEvent.MOUSE_DRAGGED : MouseEvent.MOUSE_MOVED;
	int x = (xevent.event_x()<<16)>>16;
	int y = (xevent.event_y()<<16)>>16;
	
	return new SequencedMouseEvent(canvas, rootWid, wid, type, when, modifiers, x, y, 
				       0, false, MouseEvent.NOBUTTON);
    }

    private AWTEvent createNativeEnterEvent (gnu.x11.event.Input xevent, Canvas3D canvas, long rootWid, long wid) {
	long when = LgXWindow.nowMillisUTC_offset((long)xevent.time());

	/* 
	** Note: unlike other events, which use the event coordinates, this event
	** uses the root coordinates. 
	*/
	int x = xevent.root_x();
	int y = xevent.root_y();
	
	return new NativeEnterEvent(canvas, rootWid, wid, when, x, y);
    }

    static int awtEventGetModifiers (int state, int button, int keyCode) {
        int modifiers = 0;

        if (((state & Input.SHIFT_MASK) != 0) ^ (keyCode == KeyEvent.VK_SHIFT)) {
            modifiers |= InputEvent.SHIFT_DOWN_MASK;
        }
        if (((state & Input.CONTROL_MASK) != 0) ^ (keyCode == KeyEvent.VK_CONTROL)) {
            modifiers |= InputEvent.CTRL_DOWN_MASK;
        }

        /* TODO: how to implement these?         
        if (((state & XToolkit.metaMask) != 0) ^ (keyCode == KeyEvent.VK_META)) {
            modifiers |= InputEvent.META_DOWN_MASK;
        }
        if (((state & XToolkit.altMask) != 0) ^ (keyCode == KeyEvent.VK_ALT)) {
            modifiers |= InputEvent.ALT_DOWN_MASK;
        }
        if (((state & XToolkit.modeSwitchMask) != 0) ^ (keyCode == KeyEvent.VK_ALT_GRAPH)) {
            modifiers |= InputEvent.ALT_GRAPH_DOWN_MASK;
        }
        */

        if (((state & Input.BUTTON1_MASK) != 0) ^ (button == Input.BUTTON1)) {
            modifiers |= InputEvent.BUTTON1_DOWN_MASK;
        }
        if (((state & Input.BUTTON2_MASK) != 0) ^ (button == Input.BUTTON2)) {
            modifiers |= InputEvent.BUTTON2_DOWN_MASK;
        }
        if (((state & Input.BUTTON3_MASK) != 0) ^ (button == Input.BUTTON3)) {
            modifiers |= InputEvent.BUTTON3_DOWN_MASK;
        }

        return modifiers;
    }

    private void initializeMultiClickTime () {
/*
TODO: convert XGetDefault to Java. Until then the multi click timeout is hardwired
        try {
	    String multiclick_time_query = XGetDefault(xlibdpy, ds, "*", "multiClickTime");
	    if (multiclick_time_query != null) {
		buttonMultiClickTime = (int)Long.parseLong(multiclick_time_query);
	    } else {
		multiclick_time_query = XGetDefault(xlibdpy, ds, "OpenWindows", "MultiClickTimeout");
		if (multiclick_time_query != null) {
		    // Note: OpenWindows.MultiClickTimeout is in tenths of 
		    // a second, so we need to multiply by 100 to convert to
		    // milliseconds 
		    buttonMultiClickTime = (int)Long.parseLong(multiclick_time_query) * 100;
		} else {
		    buttonMultiClickTime = 200;
		}
	    }        
	} catch (NumberFormatException nf) {
	    buttonMultiClickTime = 200;            
	} catch (NullPointerException npe) {            
	    buttonMultiClickTime = 200;            
	}
*/

        if (buttonMultiClickTime == 0) {
            buttonMultiClickTime = 200;
        }
    }

    // 3 pixels
    private static final int BUTTON_CLICK_POSITION_THRESHOLD = 3;

    // Returns true if the button release is close enough to the button press
    // so as to consitute a click event.
    // Note: These are Java-on-Windows behavior. There is no click event position
    // threshold on Java-on-Linux. But Hideya wants the Windows behavior.

    private final boolean buttonWithinClickThreshold (int x, int y) {
	return Math.abs(x - buttonLastX) <= BUTTON_CLICK_POSITION_THRESHOLD &&
	       Math.abs(y - buttonLastY) <= BUTTON_CLICK_POSITION_THRESHOLD;
    }

    private MouseButtonsReturn createMouseButtonEvent (gnu.x11.event.Input xevent, 
						       Canvas3D canvas, long rootWid, 
						       long wid) {
	MouseButtonsReturn awtEvents = new MouseButtonsReturn();
        int modifiers; 
        boolean popupTrigger = false;
        int awtButton = 0;
        boolean mouseClicked = false;
        boolean wheel_mouse = false;
        int button = xevent.detail();
	long when = LgXWindow.nowMillisUTC_offset((long)xevent.time());
        int x = xevent.event_x();
        int y = xevent.event_y();
	int code = xevent.code();

	awtEvents.clickedEvent = null;

        if (code == ButtonPress.CODE) {
	    //System.err.println("ButtonPress");

            /* multiclick checking */
            if (buttonLastWid == wid && 
		buttonLastButton == button && 
		buttonWithinClickThreshold(x, y) &&
		(when - buttonLastTime) < buttonMultiClickTime) {
                buttonClickCount++;
            } else {
                buttonClickCount = 1;
                buttonLastWid = wid;
                buttonLastButton = button;
		buttonLastX = x;
		buttonLastY = y;
            }
            buttonLastTime = when;

            /* Check for popup trigger. Set popupTrigger to true if 
	       this button is the rightmost button */
            if (button == buttonRightButtonNumber || button > 2) {
                popupTrigger = true; 
            } else {
                popupTrigger = false;
            }

        } else {
	    //System.err.println("ButtonRelease");
	    //System.err.println("buttonLastWid = " + buttonLastWid);
	    //System.err.println("wid = " + wid);
            if (buttonLastWid == wid &&
		buttonWithinClickThreshold(x, y)) {
                mouseClicked = true;
            }
        }

        modifiers = awtEventGetModifiers(xevent.state(), button, 0);

        if      (button == Input.BUTTON1)
            awtButton = MouseEvent.BUTTON1;
        else if (button == Input.BUTTON2)
            awtButton = MouseEvent.BUTTON2;
        else if (button == Input.BUTTON3)
            awtButton = MouseEvent.BUTTON3;
        else if (button == Input.BUTTON4) { 
            awtButton = 4;
            wheel_mouse = true;
        } else if (button == Input.BUTTON5) {
            awtButton = 5;
            wheel_mouse = true;
        }

        if (!wheel_mouse) { 
	    int type = (code == ButtonPress.CODE) ? 
		       MouseEvent.MOUSE_PRESSED : MouseEvent.MOUSE_RELEASED;

	    boolean isPRW = false;
	    for (int i = 0; i < prws.length; i++) {
		if (wid == prws[i]) {
		    isPRW = true;
		    break;
		}
	    }
	    
	    if (type == MouseEvent.MOUSE_PRESSED && !isPRW) {
		awtEvents.event = new NativeButtonPressEvent(canvas, rootWid, wid, 
					   when, modifiers,  x, y, buttonClickCount, 
					   popupTrigger, awtButton, 
					   xevent.root_x(), xevent.root_y());
	    } else {
		awtEvents.event = new SequencedMouseEvent(canvas, rootWid, wid, type, 
					   when, modifiers, x, y, buttonClickCount, 
					   popupTrigger, awtButton);
	    }

	    //System.err.println("Created AWT MouseEvent");
	    //System.err.println("type = " + type);
	    //System.err.println("modifiers = " + modifiers);
	    //System.err.println("xy = " + x + "," + y);
	    //System.err.println("clickCount = " + buttonClickCount);
	    //System.err.println("popupTrigger = " + popupTrigger);
	    //System.err.println("awtButton = " + awtButton);

            if (mouseClicked) {
		// Note: the clicked event will share the pick sequence of the 
		// previous button press event. This is okay.
                awtEvents.clickedEvent = new SequencedMouseEvent(canvas, rootWid, wid,  
						  MouseEvent.MOUSE_CLICKED, when, 
						  modifiers, x, y, buttonClickCount, 
						  false, awtButton);
		//System.err.println("Created AWT MouseEvent Clicked");
		//System.err.println("modifiers = " + modifiers);
		//System.err.println("xy = " + x + "," + y);
		//System.err.println("clickCount = " + buttonClickCount);
		//System.err.println("popupTrigger = " + popupTrigger);
		//System.err.println("awtButton = " + awtButton);
            }

        } else {

	    // Ignore the release part of the mouse wheel event
	    if (code == ButtonRelease.CODE) return null;

	    awtEvents.event = new SequencedMouseWheelEvent(canvas, rootWid, wid, MouseEvent.MOUSE_WHEEL, 
							   (long)when, modifiers, x, y, 
							   buttonClickCount, 
							   false, MouseWheelEvent.WHEEL_UNIT_SCROLL,
							   3, (awtButton==4) ?  -1 : 1);
        }

	return awtEvents;
    }

    private int createKeyEvents (Canvas3D canvas, long rootWid, long wid, int xcode, 
				 int keycode, int state, long time) {
	int numKeyEvents;

	numKeyEvents = lgXWindow.translateKeyEvent(translatedKeyEvents, xcode, 
						   keycode, state, time);
	for (int i = 0; i < numKeyEvents; i++) {
	    keyEventsToPost[i] = new SequencedKeyEvent(canvas, rootWid, wid, 
			       translatedKeyEvents[i].id, translatedKeyEvents[i].when, 
			       translatedKeyEvents[i].modifiers, translatedKeyEvents[i].keyCode,
			       translatedKeyEvents[i].keyChar, translatedKeyEvents[i].keyLocation);
	}

	return numKeyEvents;
    }

//    void addCursorImageListener (CursorImageListener l) {
//	ebCursorImage.addListener(l);
//    }
//	
//    void removeCursorImageListener(CursorImageListener l) {
//	ebCursorImage.removeListener(l);
//    }

    CursorImageListener[] getCursorImageListeners() {
        return ebCursorImage.getListeners();
    }

    public int keysymToKeycode (int keysym) {
	return keysymToKeycode(cepDpy, keysym);
    }
}

