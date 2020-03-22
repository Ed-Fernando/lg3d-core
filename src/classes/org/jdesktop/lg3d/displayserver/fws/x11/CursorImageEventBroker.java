/**
 * Project Looking Glass
 *
 * $RCSfile: CursorImageEventBroker.java,v $
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
 * $Revision: 1.8 $
 * $Date: 2006-09-27 20:54:24 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.jdesktop.lg3d.displayserver.fws.CursorImageListener;
import org.jdesktop.lg3d.sg.ImageComponent;
import org.jdesktop.lg3d.sg.ImageComponent2D;
import gnu.x11.Display;
import gnu.x11.event.Event;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;
import gnu.x11.event.Event;
import java.util.logging.Logger;

/**
 * CursorImageEventBroker is an event broker which receives 
 * cursor image notification events and distributes them to 
 * any listeners which may be registered.
 */

class CursorImageEventBroker extends Behavior
{
    protected Logger logger = java.util.logging.Logger.getLogger("lg.fws");

    private ExtensionSet exts;

    private LinkedList<CursorImageListener> listeners = null;

    // TODO: Currently the entries in this list are never deleted so this
    // list grows without limit. Is there anything we can do to infer
    // when a cursor has been destroyed? Because X doesn't tell us directly.
    private HashMap<Long,ImageComponent2D> cursorsList = new HashMap<Long,ImageComponent2D>();

    private HashMap<Long,CursorImage> cursorsImages = new HashMap<Long,CursorImage>();
    
    private LinkedList<Event> eventQueue = new LinkedList();

    private WakeupOnElapsedFrames wakeupCondition = new WakeupOnElapsedFrames( 0, true );

    public CursorImageEventBroker (ExtensionSet exts) {
	this.exts = exts;
    }

    public void initialize() {
        setSchedulingBounds( new BoundingSphere( new Point3d(), Double.POSITIVE_INFINITY )); 
        wakeupOn(wakeupCondition);
    }

    public synchronized int queueSize () {
	return eventQueue.size();
    }

    public synchronized void enqueue (Event event) {
	eventQueue.add(event);
    }

    public synchronized Event dequeue () {
	return eventQueue.removeFirst();
    }

    public void addListener (CursorImageListener l) {
        if (l == null) return;
	if (listeners == null) listeners = new LinkedList<CursorImageListener>();
	listeners.add(l);
    }
	
    public void removeListener(CursorImageListener l) {
        if (l == null || listeners == null) return;
	listeners.remove(l);
	if (listeners.isEmpty()) listeners = null;
    }

    public CursorImageListener[] getListeners() {
	if (listeners == null) return null;

	Iterator it = listeners.iterator();
	CursorImageListener[] lary = new CursorImageListener[listeners.size()];
	int i = 0;

	while (it.hasNext()) {
	    CursorImageListener l = (CursorImageListener) it.next();
	    lary[i++] = l;
	}

	return lary;
    }

    void invokeListeners (ImageComponent2D image, int actualWidth, int actualHeight,
			  int hotX, int hotY) {
        if (listeners == null) return; // no listener is registered.
	Iterator it = listeners.iterator();
	while (it.hasNext()) {
	    try {
		CursorImageListener l = (CursorImageListener) it.next();
		l.imageChanged(image, actualWidth, actualHeight, hotX, hotY);
	    } catch (Exception e) {
		logger.warning("CursorImageEventBroker: cursor listener threw exception: " + e);
		e.printStackTrace();
	    } catch (Error err) {
		logger.warning("CursorImageEventBroker: cursor listener threw error: " + err);
		err.printStackTrace();
	    }
	}
    }

    public void processStimulus( java.util.Enumeration e ) {
	Event event;
	CursorImage cursorImage;

        wakeupOn(wakeupCondition);

	if (queueSize() == 0) {
	    return;
	}

	event = dequeue();
	Long cursorSerial = new Long(event.read4(8));
	//System.err.println("CIEB.processStimulus: cursorSerial = " + cursorSerial);	    

	ImageComponent2D image = cursorsList.get(cursorSerial);
	if(image != null) {
	    //System.err.println("reuse the old image ");
	    cursorImage = cursorsImages.get(cursorSerial);
	    invokeListeners(image, cursorImage.actualWidth, cursorImage.actualHeight, 
			    cursorImage.hotX, cursorImage.hotY);
	    return;
	}
	
	cursorImage = exts.xfixesExtForCieb.getCursorImage();

	//System.err.println("actualWidth = " + cursorImage.actualWidth);
	//System.err.println("actualHeight = " + cursorImage.actualHeight);
	//System.err.println("roundedWidth = " + cursorImage.roundedWidth);
	//System.err.println("roundedHeight = " + cursorImage.roundedHeight);

	image = new ImageComponent2D(ImageComponent.FORMAT_RGBA, cursorImage.image);
	image.setCapability(ImageComponent.ALLOW_IMAGE_READ);
	image.setCapability(ImageComponent.ALLOW_IMAGE_WRITE);
	image.setCapability(ImageComponent.ALLOW_SIZE_READ );
	invokeListeners(image, cursorImage.actualWidth, cursorImage.actualHeight, 
			    cursorImage.hotX, cursorImage.hotY);
	cursorsList.put(new Long(cursorImage.cursorSerial),image);
	cursorsImages.put(new Long(cursorImage.cursorSerial),cursorImage);
    }

    private int getPowerOfTwoUpperBound (int value) {

	if (value < 1)
	    return value;
	
	int powerValue = 1;

	for (;;) {
	    powerValue *= 2;
	    if (value < powerValue) {
		return powerValue;
	    }
	}
    }
}
