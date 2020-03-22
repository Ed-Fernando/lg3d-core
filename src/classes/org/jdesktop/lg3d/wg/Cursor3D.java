/**
 * Project Looking Glass
 *
 * $RCSfile: Cursor3D.java,v $
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
 * $Revision: 1.10 $
 * $Date: 2007-07-22 23:16:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import java.util.HashMap;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.wg.internal.wrapper.Cursor3DWrapper;


/**
 *
 * @author  Paul
 */
public class Cursor3D extends Component3D {
    public static Cursor3D NULL_CURSOR;
    public static Cursor3D DEFAULT_CURSOR;
    public static Cursor3D MEDIUM_CURSOR;
    public static Cursor3D SMALL_CURSOR;
    public static Cursor3D SMALL_MOVE_CURSOR;
    public static Cursor3D MOVE_CURSOR;
    public static Cursor3D N_RESIZE_CURSOR;
    public static Cursor3D S_RESIZE_CURSOR;
    public static Cursor3D E_RESIZE_CURSOR;
    public static Cursor3D W_RESIZE_CURSOR;
    public static Cursor3D NE_RESIZE_CURSOR;
    public static Cursor3D NW_RESIZE_CURSOR;
    public static Cursor3D SE_RESIZE_CURSOR;
    public static Cursor3D SW_RESIZE_CURSOR;
    
    // Wonderland cursors
    public static Cursor3D MOVE_CURSOR_WL;
    public static Cursor3D SE_RESIZE_CURSOR_WL;
    public static Cursor3D MOVE_Z_CURSOR_WL;
    public static Cursor3D ROTATE_Y_CURSOR_WL;

    private static HashMap cursors = new HashMap();

    /**
     * Creates a new Cursor3D. If this is a duplicate of an
     * existing cursor in this application an exception is thrown.
     *
     * The cursor is not made live until it is explicitly added to the CursorModule
     * or setCursor is called on a Component3D.
     *
     */
    public Cursor3D(String name, Node node) {
        super(false);
        super.setName(name); // see the setName() implementation below
	this.setPickable(false);
        
        if (name != null) {
            if (cursors.containsKey(name)) {
                throw new IllegalArgumentException("Cursor " + name + " already exists");
            }
            cursors.put(name, this);
        }
        
        if (node != null) {
            addChild(node);
        }
	logger.fine("cursor created : " + name);
    }
    
    public Cursor3D(String name) {
	this(name, null);
    }
    
    public Cursor3D(Node node) {
        this(null, node);
    }
    
    public Cursor3D() {
        this(null, null);
    }
    
    /**
     * Returns a Cursor3D object registered with the name.
     * This will first look for an application cursor with the
     * supplied name, if one does not exist it will check for a system
     * cursor with the name.
     */
    public static Cursor3D get(String name) {
	if (name == null) {
	    name = Cursor3D.DEFAULT_CURSOR.getName();
	}
	name = name.intern();
	Cursor3D ret = (Cursor3D)cursors.get(name);
	if (ret == null) {
            // Check for a system cursor
	    logger.finer("cursor not found: " + name);
	    ret = (Cursor3D)cursors.get(Cursor3D.DEFAULT_CURSOR);
	}
	logger.finer("cursor read: " + name + " => " + ret);
	return ret;
    }
    
    /**
     * A cursors name can not be changed, this method will throw
     * a RuntimeException
     */
    public void setName( String name ) {
        throw new RuntimeException("Cursor names can not be changed");
    }

    /**
     * Implementation detail, do not call from user code.
     * May be removed in future.
     */
    protected void createWrapped() {
        wrapped = instantiate( "Cursor3D" );
        wrapped.setUserData(this);
    }
    
}
