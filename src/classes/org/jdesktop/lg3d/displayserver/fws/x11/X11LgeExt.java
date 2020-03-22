/**
 * Project Looking Glass
 *
 * $RCSfile: X11LgeExt.java,v $
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
 * $Revision: 1.12 $
 * $Date: 2006-08-14 23:13:29 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import gnu.x11.Data;
import gnu.x11.Display;
import gnu.x11.Request;
import gnu.x11.Window;
import gnu.x11.XProtocolInfo;
import gnu.x11.extension.Extension;
import gnu.x11.extension.NotFoundException;
import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.SevereRuntimeError;

final class X11LgeExt extends Extension {

    private Logger logger = Logger.getLogger("lg.fws");

    public static final String LGE_NAME = "LGE";
    public static final int CLIENT_MAJOR_VERSION = 4;
    public static final int CLIENT_MINOR_VERSION = 0;
    public static final String[] MINOR_OPCODE_STRINGS = {
	"X_LgeQueryVersion",               // 0
	"X_LgeRegisterClient",             // 1
	"X_LgeRegisterScreen",             // 2
	"X_LgeControlLgMode",              // 3
	"X_LgeSendEvent",                  // 4
    };

    public static final int CLIENT_GENERIC         = 0;
    public static final int CLIENT_PICKER          = 1;
    public static final int CLIENT_EVENT_DELIVERER = 2;

    private int server_major_version, server_minor_version;
    private int server_implementation;

    static private final String interfaceRequiredProperty = "lg.fws.x11.interfaceRequired";
    static private final String interfaceRequiredPropertyFormat = "<majorNum>.<minorNum>";
    static private final String interfaceFormatErrorMsg =  
                            "Invalid format for property " + interfaceRequiredProperty + 
			    ". Format expected: " + interfaceRequiredPropertyFormat;

    static private String interfaceReq;
    static private int majorReq;
    static private int minorReq;

    /**
     * Lge opcode 0 - query version
     */
    public X11LgeExt (Display display) throws NotFoundException { 
	super (display, LGE_NAME, MINOR_OPCODE_STRINGS); 

	// These extension requests expect replies
	XProtocolInfo.extensionRequestExpectsReply(major_opcode, 0, 32); // QueryVersion

	// Fetch X server interface version
	Request request = new Request (display, major_opcode, 0, 1);
	Data reply = display.read_reply (request);
	server_major_version = reply.read4 (8);
	server_minor_version = reply.read4 (12);
	server_implementation = reply.read4 (16);

	logger.info("X server interface version = " + 
		    server_major_version + "." + 
		    server_minor_version + ", " +
                    "implementation = " + 
		    server_implementation);

	checkVersion();
    }
    
    private void checkVersion () {

	// Get the X Server interface which is required (maj.min)
	if (interfaceReq == null) {
	    interfaceReq = System.getProperty(interfaceRequiredProperty);
	    if (interfaceReq == null || interfaceReq.length() == 0) {
		logger.severe("Property " + interfaceRequiredProperty +
			      " is not specified.");
		throw new RuntimeException("Invalid property format");
	    }
	    int index = interfaceReq.indexOf('.');
	    if (index < 0) {
		logger.severe(interfaceFormatErrorMsg);
		logger.severe("Actual: " + interfaceReq);
		throw new RuntimeException("Invalid property format");
	    }
	    String majorStr = interfaceReq.substring(0, index);
	    String minorStr = interfaceReq.substring(index+1);
            // substring doesn't return null
	    if (majorStr.length() == 0 || minorStr.length() == 0) {
		logger.severe(interfaceFormatErrorMsg);
		logger.severe("Actual: " + interfaceReq);
		throw new RuntimeException("Invalid property format");
	    }
	    majorReq = Integer.parseInt(majorStr);
	    minorReq = Integer.parseInt(minorStr);
	}

	// Perform version check on interface
	// The server interface is compatible with the required
	// version if and only if:
	//
	// 1. The required major version number equals the server major version number
	//         -and-
	// 2. The required minor number is less than or equal to the server 
	//    minor version number

	if (majorReq != server_major_version ||
	    minorReq > server_minor_version) {
            new SevereRuntimeError ("Incompatible X11 server interface.\n" +
                    "Required server interface does not match actual server interface\n" +
                    "Required server interface version = " + majorReq + "." + minorReq + "\n" +
                    "Actual server interface version   = " + server_major_version +
			  "." + server_minor_version + "\n" +
                    "Incompatible X11 server interface");
	}
    }

    /**
     * Lge opcode 1 - RegisterClient
     */
    public void registerClient (int clientType) {
	switch (clientType) {
	case CLIENT_GENERIC:
	case CLIENT_PICKER:
	case CLIENT_EVENT_DELIVERER:
	    break;
	default:
	    throw new RuntimeException("Invalid client type argument");
	}

	Request request = new Request (display, major_opcode, 1, 2);
	request.write1(clientType);
	request.write1(0);  // sendEventDirect defaults to false
	display.send_request (request);
    }

    /**
     * Lge opcode 1 - RegisterClient
     * The picker can pass an additional argument: sendEventDirect.
     */
    public void registerClient (int clientType, boolean sendEventDirect) {
        if (clientType != CLIENT_PICKER) {
	    throw new RuntimeException("Invalid client type argument");
	}

	Request request = new Request (display, major_opcode, 1, 2);
	request.write1(clientType);
	request.write1(sendEventDirect ? 1 : 0); 
	display.send_request (request);
    }

    /**
     * Lge opcode 2 - DSIsAlive
     */
    public void registerScreen (Window prwin) {
	Request request = new Request (display, major_opcode, 2, 2);
	request.write4(prwin.id);
	display.send_request (request);
    }
    
    /**
     * Lge opcode 3 - ControlLgMode
     */
    public void controlLgMode (boolean enable) {
	Request request = new Request (display, major_opcode, 3, 2);
	request.write1(enable ? 1 : 0);
	display.send_request (request);
    }

    /**
     * Lge opcode 4 - SendEvent
     */
    public void sendEvent (DeviceEvent event) {
	Request request = new Request (display, major_opcode, 4, 9);

	/*
	System.err.println("Enter X11LgeExt.sendEvent");
	System.err.println("code = " + event.code());
	System.err.println("detail = " + event.detail());
	System.err.println("seq_no = " + event.seq_no());
        System.err.println("time   = " + event.time());
        System.err.println("root   = " + event.root_id());
        System.err.println("event  = " + event.window_id());
        System.err.println("child  = " + event.child_id());
        System.err.println("rootx  = " + event.root_x());
        System.err.println("rooty  = " + event.root_y());
        System.err.println("eventx = " + event.event_x());
        System.err.println("eventy = " + event.event_y());
        System.err.println("state  = " + event.state());
	*/

	// Write in xEvent format
	request.write1(event.code());
	request.write1(event.detail());
	request.write2(event.seq_no());

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
 
	display.send_request (request);
    }
}



