/**
 * Project Looking Glass
 *
 * $RCSfile: X11XtestExt.java,v $
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
 * $Date: 2005-01-20 22:05:28 $
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


final class X11XtestExt extends Extension {
    public static final String XTEST_NAME = "XTEST";
    public static final int CLIENT_MAJOR_VERSION = 0;
    public static final int CLIENT_MINOR_VERSION = 1;
    public static final String[] MINOR_OPCODE_STRINGS = {
	"X_XTestGetVersion",               // 0
	"",                                // 1
	"",                                // 2
	"X_XTestGrabControl",              // 3
    };

    public int server_major_version, server_minor_version;

    /**
     * XTest opcode 0 - GetVersion
     */
    public X11XtestExt (Display display) throws NotFoundException { 
	super (display, XTEST_NAME, MINOR_OPCODE_STRINGS); 

	// These extension requests expect replies
	XProtocolInfo.extensionRequestExpectsReply(major_opcode, 0, 32); // QueryVersion

	// check version before any other operations
	Request request = new Request (display, major_opcode, 0, 2);
	request.write1(CLIENT_MAJOR_VERSION);
	request.write1(0);
	request.write2(CLIENT_MINOR_VERSION);

	Data reply = display.read_reply (request);
	server_major_version = reply.read1 (1);
	server_minor_version = reply.read2 (8);
    }
    
    /**
     * XTest opcode 3 - GrabControl
     */
    public void grabControl (boolean impervious) {
	Request request = new Request (display, major_opcode, 3, 2);
	request.write1(impervious ? 1 : 0);
	display.send_request (request);
    }
}



