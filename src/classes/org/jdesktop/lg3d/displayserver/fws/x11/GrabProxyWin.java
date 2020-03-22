/**
 * Project Looking Glass
 *
 * $RCSfile: GrabProxyWin.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2006-03-07 00:16:58 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import java.util.HashMap;
import java.util.LinkedList;
import javax.media.j3d.Node;
import gnu.x11.Display;
import gnu.x11.Rectangle;
import gnu.x11.Window;

/**
 * This class provides x11 window objects which are associated
 * with grabbable nodes in the scene graph. They area also
 * used for active grabs. When the Event Deliverer receives
 * an event for one of these windows the input device is
 * being grabbed by this window.
 */

// TODO: is it necessary to partition these among screens,
// or is it okay to just define them on the default screen?

public class GrabProxyWin extends Window {

    // Start out with this many in the free pool
    private static final int INITIAL_FREE_POOL_SIZE = 5;

    // All proxy wins are 1x1 and placed at (0, 0)
    private static Rectangle rect = new Rectangle(0, 0, 1, 1);

    private static HashMap<Node,GrabProxyWin> proxyWinForNode = 
        new HashMap<Node,GrabProxyWin>();

    private static HashMap<Long,GrabProxyWin> proxyWinForWid = 
        new HashMap<Long,GrabProxyWin>();

    private Node node;
    private GrabProxyWin parent;
    private LinkedList children;

    // Indicates whether this proxy win is being used for an 
    // active grab
    private boolean active = true;

    /**
     * Returns the GrabProxyWin associated with the
     * given window ID. Return null if not found.
     */
    static GrabProxyWin get (long wid) {
	return proxyWinForWid.get(wid);
    }

    /**
     * Returns the GrabProxyWin associated with the
     * given scene graph node. If it doesn't exist, 
     * one for use with passive grabs is created.
     */
    static GrabProxyWin get (Display dpy, Node node) {
	GrabProxyWin gpw = proxyWinForNode.get(node);
	if (gpw == null) {
	    gpw = new GrabProxyWin(dpy, node, false);
	}
	return gpw;
    }

    /**
     * Creates a 1x1 proxy win child of the default root window
     */
    public GrabProxyWin (Display dpy, boolean active) {
	super(dpy.default_root, rect);
	this.active = active;
    }

    /**
     * Creates a 1x1 proxy win child of the default root window
     * and associates it with the given node.
     */
    public GrabProxyWin (Display dpy, Node node, boolean active) {
	this(dpy, active);
	this.active = active;
	bind(this, node);
    }


    /**
     * Associate the GrabProxyWin with the given scene graph node.
     */
    public void bind (GrabProxyWin gpw, Node node) {
    }


    /**
     * Indicates that the GrabProxyWin is no longer in use.
     */
    public void release () {
        // TODO: remove associations
        // Free X11 window
    }

    public Node getNode () { return node; }
    public boolean isActive () { return active; }
}
