/**
 * Project Looking Glass
 *
 * $RCSfile: DeviceEventSource.java,v $
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
 * $Date: 2006-03-07 00:16:57 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import java.util.HashMap;
import java.util.logging.Logger;
import javax.media.j3d.Canvas3D;
import gnu.x11.Data;
import gnu.x11.Display;
import gnu.x11.Window;
import gnu.x11.event.Event;
import gnu.x11.event.MotionNotify;

/**
 * An abstract class which contains the common code for DeviceEventSourceSession
 * and DeviceEventSourceApp.
 */

abstract class DeviceEventSource extends EventBroker
{
    // Based on Xproto.h:xEvent
    private static final int KEYBUTPTR_U_TYPE_OFFSET = 0;
    private static final int KEYBUTPTR_U_DETAIL_OFFSET = 1;
    private static final int KEYBUTPTR_U_SEQ_OFFSET = 2;
    private static final int KEYBUTPTR_TIME_OFFSET = 4;
    private static final int KEYBUTPTR_ROOT_X_OFFSET = 20;
    private static final int KEYBUTPTR_ROOT_Y_OFFSET = 22;
    private static final int KEYBUTPTR_STATE_OFFSET = 28;

    protected static final Logger logger = Logger.getLogger("lg.x11");

    protected Display devDpy;
    protected Canvas3D[] canvases;
    protected Window[] prwins;

    // The screen absolute position of the last motion event received.
    // TODO: derive from display
    private Integer scrAbsPositionLock = new Integer(0);
    private int lastScrAbsX;
    private int lastScrAbsY;
    private Canvas3D lastPointerCanvas;
    private Window lastPointerPrwin;

    public DeviceEventSource (Display devDpy, Window[] prwins, Canvas3D[] canvases) {
	this.devDpy = devDpy;
	this.canvases = canvases;
	this.prwins = prwins;

	synchronized (scrAbsPositionLock) {

	    // TODO: multiscreen: what canvas to assign?
	    // We probably want to use XQueryPointer and map the root_return
	    // to a canvas.
	    lastPointerCanvas = canvases[0];
	    lastPointerPrwin = prwins[0];

	    // TODO: the method of computing the last known cursor position makes the 
	    // overly simplistic assumption that the cursor comes up in the center of 
	    // the default cursor screen. In the future, we need to set the initial cursor
	    // position based on the current native cursor position at the time of DS
	    // start up. So in the future we will want to derive lastPointerX/Y from
	    // the cursor module itself.
	    lastScrAbsX = lastPointerCanvas.getWidth() / 2;
	    lastScrAbsY = lastPointerCanvas.getHeight() / 2;
	}
    }

    protected void trackLastPosition (DeviceEvent devEvent) {
	synchronized (scrAbsPositionLock) {
	    if (devEvent.code() == MotionNotify.CODE) {
		// Track last position for motion events
		lastPointerCanvas = devEvent.getCanvas();
		lastPointerPrwin = devEvent.getPrwin();
		lastScrAbsX = devEvent.root_x();
		lastScrAbsY = devEvent.root_y();
	    } else {
		// Non-motion events inherit the latest root xy position
		devEvent.set_root_x(lastScrAbsX);
		devEvent.set_root_y(lastScrAbsY);
	    }
	}
    }

    protected void writeMotionData (Data data, int time, int x, int y) {
	data.write1(KEYBUTPTR_U_TYPE_OFFSET, MotionNotify.CODE);
	data.write2(KEYBUTPTR_U_SEQ_OFFSET, 0);
	data.write4(KEYBUTPTR_TIME_OFFSET, time);
	data.write2(KEYBUTPTR_ROOT_X_OFFSET, x);
	data.write2(KEYBUTPTR_ROOT_Y_OFFSET, y);
    }

    protected void writeButtonData (Data data, int code, int buttonNum, int time, int state) {
	data.write1(KEYBUTPTR_U_TYPE_OFFSET, code);
	data.write1(KEYBUTPTR_U_DETAIL_OFFSET, buttonNum);
	data.write2(KEYBUTPTR_U_SEQ_OFFSET, 0);
	data.write4(KEYBUTPTR_TIME_OFFSET, time);
	data.write4(KEYBUTPTR_STATE_OFFSET, state);
    }

    protected void writeKeyData (Data data, int code, int keycode, int time, int state) {
	data.write1(KEYBUTPTR_U_TYPE_OFFSET, code);
	data.write1(KEYBUTPTR_U_DETAIL_OFFSET, keycode);
	data.write2(KEYBUTPTR_U_SEQ_OFFSET, 0);
	data.write4(KEYBUTPTR_TIME_OFFSET, time);
	data.write4(KEYBUTPTR_STATE_OFFSET, state);
    }

    // Constructs a synthetic motion event with coordinates of the last
    // pointer position and injects it into the pipeline going to the Picker.
    // This forces a recomputation in the X Server of the current sprite window
    // (for delivery of keyboard events) and also forces a recomputation in the
    // Event Deliverer of the current pointer object. It also causes the necessary
    // 3D enter/exit events to be sent.

    void enqueueMoveToLastPosition () {
	DeviceEvent devEvent;
	Data data = new Data(32);

	synchronized (scrAbsPositionLock) {
	    writeMotionData(data, 0, lastScrAbsX, lastScrAbsY);
	    devEvent = new DeviceEvent(devDpy, new MotionNotify(devDpy, data.data));
 	    devEvent.window_offset = 12;
	    devEvent.setCanvas(lastPointerCanvas);
	    devEvent.setPrwin(lastPointerPrwin);
        }

	enqueue(devEvent);
    }

    public synchronized int queueSize () {
	return eventQueue.size();
    }

    public synchronized void enqueue (Event event) {

	// Don't duplicate consecutive mouse motion events
	int eventCode = event.code();
	if (eventCode == MotionNotify.CODE && eventQueue.size() != 0) {
	    Event lastEvent = eventQueue.getLast();
	    if (lastEvent.code() == MotionNotify.CODE) {
		eventQueue.removeLast();
	    }
	}            

	eventQueue.add(event);
    }

    public synchronized Event dequeue () {
	return eventQueue.removeFirst();
    }
}
