/**
 * Project Looking Glass
 *
 * $RCSfile: X11XfixesExt.java,v $
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
 * $Date: 2006-03-07 00:17:01 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import java.util.Hashtable;
import gnu.x11.Data;
import gnu.x11.Display;
import gnu.x11.Request;
import gnu.x11.Window;
import gnu.x11.Error;
import gnu.x11.XProtocolInfo;
import gnu.x11.event.Event;
import gnu.x11.extension.Extension;
import gnu.x11.extension.ErrorFactory;
import gnu.x11.extension.EventFactory;
import gnu.x11.extension.NotFoundException;
import java.awt.image.BufferedImage;

// Note: only the methods used by the DS are implemented

final class X11XfixesExt 
    extends Extension 
    implements ErrorFactory, EventFactory
{
    public static final String XFIXES_NAME = "XFIXES";

    public static final int CLIENT_MAJOR_VERSION = 4;
    public static final int CLIENT_MINOR_VERSION = 0;

    public static final int DISPLAY_CURSOR_NOTIFY_MASK = 1;

    public static final String[] MINOR_OPCODE_STRINGS = {
        "X_XfixesQueryVersion",		   // 0
	"",				   // 1
	"",				   // 2
	"X_XfixesSelectCursorInput",       // 3
	"X_XFixesGetCursorImage",          // 4
        "X_XfixesCreateRegionEmpty",	   // 5
	"",				   // 6
	"",				   // 7
	"",				   // 8
	"",				   // 9
        "X_XfixesDestroyRegion",	   // 10
        "X_XfixesSetRegion",		   // 11
	"",				   // 12
	"",				   // 13 
	"",				   // 14
	"",				   // 15
	"",				   // 16 
	"",				   // 17 
	"",				   // 18 
	"",				   // 19
	"",				   // 20 
	"",				   // 21
	"",				   // 22
	"",				   // 23
	"",				   // 24
	"",				   // 25
	"",				   // 26
	"",				   // 27
	"",				   // 28
	"X_XfixesHideCursor",		   // 29 
	"X_XfixesShowCursor",		   // 30
    };

    public int server_major_version, server_minor_version;

    private static Hashtable hashTable = new Hashtable();

    /**
     * Xfixes opcode 0 - Query version
     */
    public X11XfixesExt (Display display) throws NotFoundException { 
	super (display, XFIXES_NAME, MINOR_OPCODE_STRINGS, 1, 3); 

	// These extension requests expect replies
	XProtocolInfo.extensionRequestExpectsReply(major_opcode, 0, 32); // QueryVersion
	XProtocolInfo.extensionRequestExpectsReply(major_opcode, 4, 32); // GetCursorImage

	// check version before any other operations
	Request request = new Request (display, major_opcode, 0, 3);
	request.write4 (CLIENT_MAJOR_VERSION);
	request.write4 (CLIENT_MINOR_VERSION);

	Data reply = display.read_reply (request);
	server_major_version = reply.read4 (8);
	server_minor_version = reply.read4 (12);
    }
    
    /**
     * Xfixes opcode 3 - SelectCursorInput
     */
    public void selectCursorInput (Window win, int eventMask) { 
	Request request = new Request (display, major_opcode, 3, 3);
	request.write4(win.id);
	request.write4(eventMask);
	display.send_request (request);
    }

    /**
     * Xfixes opcode 4 - GetCursorImage
     */
    public CursorImage getCursorImage () { 
	Request request = new Request (display, major_opcode, 4, 1);

	Data reply = display.read_reply (request);

	CursorImage cursorImage = new CursorImage();
	BufferedImage bi;

	if (reply == null) {

	    // If we get an error on this request return a dummy 4x4 cursor
	    // which is all black
	    cursorImage.actualWidth = 4;
	    cursorImage.actualHeight = 4;
	    cursorImage.hotX = 0;
	    cursorImage.hotY = 0;
	    cursorImage.cursorSerial = 0;
	    cursorImage.roundedWidth = 4;
	    cursorImage.roundedHeight = 4;
	    bi = new BufferedImage(cursorImage.roundedWidth, 
				   cursorImage.roundedHeight,
				   BufferedImage.TYPE_INT_ARGB);
	    
	    for (int y = 0; y < cursorImage.actualHeight; y++) {
		for (int x = 0; x < cursorImage.actualWidth; x++) {
		    bi.setRGB(x, y, 0);
		}
	    }
	    
	} else {

	    cursorImage.actualWidth = reply.read2(12);
	    cursorImage.actualHeight = reply.read2(14);
	    cursorImage.hotX = reply.read2(16);
	    cursorImage.hotY = reply.read2(18);
	    cursorImage.cursorSerial = reply.read4(20);

	    cursorImage.roundedWidth = getPowerOfTwoUpperBound(cursorImage.actualWidth);
	    cursorImage.roundedHeight = getPowerOfTwoUpperBound(cursorImage.actualHeight);

	    bi = new BufferedImage(cursorImage.roundedWidth, 
				   cursorImage.roundedHeight,
				   BufferedImage.TYPE_INT_ARGB);

	    int pixelByteOffset = 32;
	    for (int y = 0; y < cursorImage.actualHeight; y++) {
		for (int x = 0; x < cursorImage.actualWidth; x++) {
		    int pixel = reply.read4(pixelByteOffset);
		    bi.setRGB(x, y, pixel);
		    pixelByteOffset += 4;
		}
	    }
	}

	cursorImage.image = bi;

	return cursorImage;
    }

    /**
     * Xfixes opcode 5 - CreateRegion
     */
    public long createRegionEmpty () {
	long regionId = (long) display.allocate_id(hashTable);
	Request request = new Request (display, major_opcode, 5, 2);

	request.write4 ((int)regionId);
	display.send_request (request);

	return regionId;
    }
    
    /**
     * Xfixes opcode 10 - DestroyRegion
     */
    public void destroyRegion (long regionId) {
	Request request = new Request (display, major_opcode, 10, 2);
	request.write4 ((int)regionId);
	display.send_request (request);
    }

    /**
     * Xfixes opcode 11 - SetRegionToRect
     */
    public void setRegionToRect (long regionId, int x, int y, int width, int height) {
	Request request = new Request (display, major_opcode, 11, 4);
	request.write4 ((int)regionId);
	request.write2 (x);
	request.write2 (y);
	request.write2 (width);
	request.write2 (height);
	display.send_request (request);
    }

    /**
     * Xfixes opcode 29 - HideCursor
     */
    public void hideCursor (Window win) {
	Request request = new Request (display, major_opcode, 29, 2);
	request.write4(win.id);
	display.send_request (request);
    }

    /**
     * Xfixes opcode 30 - ShowCursor
     */
    public void showCursor (Window win) {
	Request request = new Request (display, major_opcode, 30, 2);
	request.write4(win.id);
	display.send_request (request);
    }
    
    public Error build (Display display, Data data, int code, int seq_no, 
			int bad, int minor_opcode, int major_opcode) {
	String str = new String("Xfixes extension error: error code = " + bad);
	return new Error(display, str, code, seq_no, bad, minor_opcode, major_opcode);
    }

    public Event build (Display display, byte[] data, int code) {
	return new Event(display, data, 0);
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
