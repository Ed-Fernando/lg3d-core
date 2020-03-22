/**
 * Project Looking Glass
 *
 * $RCSfile: SceneGraphObject.java,v $
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
 * $Date: 2005-06-24 19:48:27 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import java.util.Hashtable;
import org.jdesktop.lg3d.sg.internal.wrapper.SceneGraphObjectWrapper;

/**
 * SceneGraphObject is a common superclass for
 * all scene graph component objects.  This includes Node,
 * Geometry, Appearance, etc.
 */
public abstract class SceneGraphObject implements SceneGraphObjectWrapper {
   // Any global flags? (e.g., execution cullable, collideable)

    protected javax.media.j3d.SceneGraphObject wrapped=null;
    
    private Object userData;

    /**
     * Constructs a SceneGraphObject with default parameters.  The default
     * values are as follows:
     * <ul>
     * capability bits : clear (all bits)<br>
     * isLive : false<br>
     * isCompiled : false<br>
     * user data : null<br>
     * </ul>
     */
    public SceneGraphObject() {
        createWrapped();
    }
    
    protected abstract void createWrapped();
    
    /**
     * Retrieves the specified capability bit.  Note that only one capability
     * bit may be retrieved per method invocation--capability bits cannot
     * be ORed together.
     * @param bit the bit whose value is returned
     * @return true if the bit is set, false if the bit is clear
     */
    public final boolean getCapability(int bit) {
	return wrapped.getCapability( bit );
    }

    /**
     * Sets the specified capability bit.  Note that only one capability bit
     * may be set per method invocation--capability bits cannot be ORed
     * together.
     * @param bit the bit to set
     * @exception RestrictedAccessException if this object is part of live
     * or compiled scene graph
     */
    public final void setCapability(int bit) {
	wrapped.setCapability( bit );
    }

    /**
     * Clear the specified capability bit.  Note that only one capability bit
     * may be cleared per method invocation--capability bits cannot be ORed
     * together.
     * @param bit the bit to clear
     * @exception RestrictedAccessException if this object is part of live
     * or compiled scene graph
     */
    public final void clearCapability(int bit) {
        wrapped.clearCapability( bit );
    }

    /**
     * Returns a flag indicating whether the node is part of a live
     * scene graph.
     * @return true if node is part of a live scene graph, else false
     */
    public final boolean isLive() {
	return wrapped.isLive();
    }

    /**
     * Sets the userData field associated with this scene graph object.
     * The userData field is a reference to an arbitrary object
     * and may be used to store any user-specific data associated
     * with this scene graph object--it is not used by the Java 3D API.
     * If this object is cloned, the userData field is copied
     * to the newly cloned object.
     * @param userData a reference to the new userData field
     */
    public void setUserData(Object userData) {
	this.userData = userData;
    }

    /**
     * Retrieves the userData field from this scene graph object.
     * @return the current userData field
     */
    public Object getUserData() {
	return userData;
    }
    
    public Object getWrapped() {
        return wrapped;
    }
    
    public String getName() {
        return wrapped.getName();
    }
    
    public void setName(String name) {
        wrapped.setName(name);
    }

}

