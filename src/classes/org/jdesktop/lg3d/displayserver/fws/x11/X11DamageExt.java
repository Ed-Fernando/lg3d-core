/**
 * Project Looking Glass
 *
 * $RCSfile: X11DamageExt.java,v $
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
 * $Date: 2005-01-20 22:05:26 $
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
import gnu.x11.extension.ErrorFactory;
import gnu.x11.extension.EventFactory;
import gnu.x11.event.Event;

final class X11DamageExt 
    extends Extension 
    implements ErrorFactory, EventFactory
{
    public static final String DAMAGE_NAME = "DAMAGE";
    public static final int CLIENT_MAJOR_VERSION = 1;
    public static final int CLIENT_MINOR_VERSION = 0;
    public static final String[] MINOR_OPCODE_STRINGS = {
        "X_DamageQueryVersion",		   // 0
        "X_DamageCreate",	           // 1
        "X_DamageDestroy",		   // 2
        "X_DamageSubtract",		   // 3
    };

    static final int REPORT_RAW_RECTANGLES	    = 0;
    static final int REPORT_DELTA_RECTANGLES        = 1;
    static final int REPORT_BOUNDING_BOX            = 2;
    static final int REPORT_NON_EMPTY               = 3;

    public int server_major_version, server_minor_version;

    private static Hashtable hashTable = new Hashtable();

    /**
     * Damage opcode 0 - Query version
     */
   public X11DamageExt (Display display) throws NotFoundException { 
	super (display, DAMAGE_NAME, MINOR_OPCODE_STRINGS, 1, 1); 

	// These extension requests expect replies
	XProtocolInfo.extensionRequestExpectsReply(major_opcode, 0, 32); // QueryVersion

	// check version before any other operations
	Request request = new Request (display, major_opcode, 0, 3);
	request.write4 (CLIENT_MAJOR_VERSION);
	request.write4 (CLIENT_MINOR_VERSION);

	Data reply = display.read_reply (request);
	server_major_version = reply.read4 (8);
	server_minor_version = reply.read4 (12);
    }
    
    /**
     * Damage opcode 1 - Create
     */
    public long create (long drawable, int level) {
	long damageId = (long) display.allocate_id(hashTable);
	Request request = new Request (display, major_opcode, 1, 4);
	request.write4 ((int)damageId);
	request.write4 ((int)drawable);
	request.write1 (level);
	request.write1 (0);
	request.write2 (0);
	display.send_request (request);
	return damageId;
    }
    
    /**
     * Damage opcode 2 - Destroy
     */
    public void destroy (long damageId) {
	Request request = new Request (display, major_opcode, 2, 2);
	request.write4 ((int)damageId);
	display.send_request (request);
    }

    /**
     * Damage opcode 3 - Subtract
     */
    public void subtract (long damageId, long repairId, long partsId) {
	Request request = new Request (display, major_opcode, 3, 4);
	request.write4 ((int)damageId);
	request.write4 ((int)repairId);
	request.write4 ((int)partsId);
	display.send_request (request);
    }

    public Error build (Display display, Data data, int code, int seq_no, 
			int bad, int minor_opcode, int major_opcode) {
	String str = new String("Damage extension error: error code = " + bad);
	return new Error(display, str, code, seq_no, bad, minor_opcode, major_opcode);
    }

    public Event build (Display display, byte[] data, int code) {
	return new Event(display, data, 0);

/* Debug
if (code != 118) {
    System.err.println("X11DamageExt: unrecognized event code = " + code);
}

Event xevent = new Event(display, data, 0);

Event event = new DamageEvent(display, xevent.data);
System.err.print("    X11DamageExt: event = " + event);

return xevent;
*/
    }
}
