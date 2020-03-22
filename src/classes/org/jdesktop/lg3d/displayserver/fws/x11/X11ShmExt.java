/**
 * Project Looking Glass
 *
 * $RCSfile: X11ShmExt.java,v $
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
import gnu.x11.Pixmap;
import gnu.x11.Error;
import gnu.x11.XProtocolInfo;
import gnu.x11.extension.Extension;
import gnu.x11.extension.NotFoundException;

// Note: only the methods used by the DS are implemented

final class X11ShmExt 
    extends Extension 
{
    public static final String SHM_NAME = "MIT-SHM";
    public static final String[] MINOR_OPCODE_STRINGS = {
        "X_ShmQueryVersion",		   // 0
        "X_ShmAttach",	                   // 1
        "X_ShmDetach",	                   // 2
        "",                                // 3
        "",                                // 4
        "X_ShmCreatePixmap",		   // 5
    };

    public int server_major_version, server_minor_version;

    private static Hashtable hashTable = new Hashtable();

    /**
     * Shm opcode 0 - Query version
     */
    public X11ShmExt (Display display) throws NotFoundException { 
	super (display, SHM_NAME, MINOR_OPCODE_STRINGS, 0, 0); 

	// These extension requests expect replies
	XProtocolInfo.extensionRequestExpectsReply(major_opcode, 0, 32); // QueryVersion

	// check version before any other operations
	Request request = new Request (display, major_opcode, 0, 1);

	Data reply = display.read_reply (request);
	server_major_version = reply.read4 (8);
	server_minor_version = reply.read4 (12);
    }
    
    /**
     * Shm opcode 1 - Attach
     */
    public void attach (ShmSegInfo shmInfo) {
	shmInfo.shmSegId = (long) display.allocate_id(hashTable);
	Request request = new Request (display, major_opcode, 1, 4);
	request.write4((int)shmInfo.shmSegId);
	request.write4(shmInfo.shmid);
	request.write1(shmInfo.readOnly ? 1 : 0);
	request.write1(0);
	request.write2(0);
	display.send_request (request);
    }
    
    /**
     * Shm opcode 2 - Detach
     */
    public void detach (ShmSegInfo shmInfo) {
	Request request = new Request (display, major_opcode, 2, 2);
	request.write4((int)shmInfo.shmSegId);
	display.send_request (request);
    }

    /**
     * Damage opcode 5 - CreatePixmap
     */
    public Pixmap createPixmap (Window win, long addr, ShmSegInfo shmInfo,
			        int width, int height, int depth) {
	long pixmapId = (long) display.allocate_id(hashTable);
	Pixmap pixmap = new Pixmap((int)pixmapId);
	pixmap.display = display;

	Request request = new Request (display, major_opcode, 5, 7);

	request.write4((int)pixmapId);
	request.write4(win.id);
	request.write2(width);
	request.write2(height);
	request.write1(depth);
	request.write1(0);
	request.write1(0);
	request.write1(0);
	request.write4((int)shmInfo.shmSegId);
	request.write4((int)(addr - shmInfo.shmAddr));
	display.send_request (request);

	return pixmap;
    }
}
