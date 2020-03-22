/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DWrapper.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2005-06-24 19:48:48 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.wrapper;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.sg.internal.wrapper.Transform3DWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.TransformGroupWrapper;

/**
 * Component3D object is a top level object for widget development.
 *
 * It supports visual rendering by adding scene graph node children 
 * and event handling.
 */
public interface Component3DWrapper extends LgBranchGroupWrapper {
 
    /**
     * Sets the capabilities of scene graph nodes under this
     * Component3D correctly so that this component can be picked.
     * Typically invoked once after finishing adding all the scene graph nodes.
     * 
     * REMINDER -- automate this ?
     */
    public void setCapabilities();

    /**
     * Post an event into the system with this Component3D object 
     * as the source of the event.
     */
//    public void postEvent(LgEvent event);
    
//    public int getNodeID() {
//        return nodeID;
//    }
    
    /**
     * Add a listener which is called whenever events of the specified
     * class are posted from this object
     */    
//    public void addListener( LgEventListener listener, Class evtClass );
    
    /**
     * Register a listener for events of class evtClass which are generated
     * from any source of the specified class.
     *
     * To listen for an event from any source pass LgEventSource.ALL_SOURCES as
     * the source
     */
//    public void addListener( LgEventListener listener, Class evtClass, Class source );
    
    /**
     * Sets a cursor associated with this component.
     */
    public void setCursor(Cursor3DWrapper cursor);

    /**
     * Returns the cursor associated with this component.
     */
    public Cursor3DWrapper getCursor();

    /**
     * Sets the size hint of this component using float values.
     *
     * The is a convenience method. 
     * method.
     */
    public void setSizeHint(float width, float height, float depth);
    
    public Vector3f getSizeHint(Vector3f ret);
    
    /**
     * Shows or hides this component depending on the value of parameter 
     * 'visible'.
     */
    public void setVisible(boolean visible);
    
    public boolean isVisible();

    public void setTransform(Transform3DWrapper t3d);
    
    public void requestParentToRevalidate();
}
