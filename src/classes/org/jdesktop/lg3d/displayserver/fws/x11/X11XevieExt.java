/**
 * Project Looking Glass
 *
 * $RCSfile: X11XevieExt.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2005-01-20 22:05:27 $
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
import gnu.x11.extension.Extension;
import gnu.x11.extension.NotFoundException;
import gnu.x11.event.Event;
import gnu.x11.event.Input;

final class X11XevieExt 
    extends Extension 
{
    public static final String XEVIE_NAME = "XEVIE";
    public static final int CLIENT_MAJOR_VERSION = 1;
    public static final int CLIENT_MINOR_VERSION = 0;
    public static final String[] MINOR_OPCODE_STRINGS = {
        "X_XevieQueryVersion",	   // 0
        "X_XevieStart",	           // 1
	"X_XevieEnd",              // 2 Not Implemented
        "X_XevieSend",		   // 3
        "X_XevieSelectInput",	   // 4
    };

    public int server_major_version, server_minor_version;

    /**
     * Xevie opcode 0 - Query version
     */
   public X11XevieExt (Display display) throws NotFoundException { 
	super (display, XEVIE_NAME, MINOR_OPCODE_STRINGS, 0, 0); 

	// These extension requests expect replies
	XProtocolInfo.extensionRequestExpectsReply(major_opcode, 0, 32); // QueryVersion
	XProtocolInfo.extensionRequestExpectsReply(major_opcode, 1, 32); // Start
	XProtocolInfo.extensionRequestExpectsReply(major_opcode, 3, 32); // SendEvent
	XProtocolInfo.extensionRequestExpectsReply(major_opcode, 4, 32); // SelectInput

	// check version before any other operations
	Request request = new Request (display, major_opcode, 0, 2);
	request.write2(CLIENT_MAJOR_VERSION);
	request.write2(CLIENT_MINOR_VERSION);

	Data reply = display.read_reply (request);
	server_major_version = reply.read2(8);
	server_minor_version = reply.read2(10);
    }
    
    /**
     * Xevie opcode 1 - Start
     */
    public void start () {
	Request request = new Request (display, major_opcode, 1, 2);
	request.write4(0/*arbitrary; not used by server*/);

	Data reply = display.read_reply (request);
    }
    
    /**
     * Xevie opcode 3 - SendEvent
     */
    public void sendEvent (DeviceEvent event) {
	Request request = new Request (display, major_opcode, 3, 104);

	//System.err.println("Enter X11XevieExt.sendEvent");
	//System.err.println("code = " + event.code());
	//System.err.println("detail = " + event.detail());
	//System.err.println("seq_no = " + event.seq_no());

	// Write in xEvent format
	// TODO: workaround: why is it necessary to do this swapping hackery?
	request.write2(event.seq_no());
	request.write1(event.detail());
	request.write1(event.code());

	request.write4(event.time());
	request.write4(event.root_id());
	request.write4(event.window_id());
	request.write4(event.child_id());

	request.write2(event.root_x());
	request.write2(event.root_y());

	request.write2(event.event_x());
	request.write2(event.event_y());

	request.write2(event.state());
	request.write1(event.same_screen());		
	request.write1(0);
 
	Data reply = display.read_reply (request);
    }

    /**
     * Xevie opcode 4 - SelectInput
     */
    public void selectInput (int eventMask) {
	Request request = new Request (display, major_opcode, 4, 2);
	request.write4(eventMask);
	Data reply = display.read_reply (request);
    }
}
