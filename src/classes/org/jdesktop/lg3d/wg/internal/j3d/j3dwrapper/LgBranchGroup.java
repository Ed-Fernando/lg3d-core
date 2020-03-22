/**
 * Project Looking Glass
 *
 * $RCSfile: LgBranchGroup.java,v $
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
 * $Date: 2005-04-14 23:05:18 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper;

import org.jdesktop.lg3d.wg.internal.wrapper.LgBranchGroupWrapper;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.BranchGroup;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dLgBranchGroup;

/**

 * The root class for all higher level looking glass components

 *

 * @author  Paul

 */

public class LgBranchGroup extends BranchGroup implements LgBranchGroupWrapper, LgEventSource {

    /** Creates a new instance of LgBranchGroup */

    public LgBranchGroup() {
    }
    
    protected void createWrapped() {
        wrapped = new J3dLgBranchGroup();
        wrapped.setUserData( this );
    }
    
    public void setMouseEventSource(Class eventClass, boolean enabled) {
        ((J3dLgBranchGroup)wrapped).setMouseEventSource(eventClass, enabled);
    }
    
    public boolean isMouseEventSource(Class eventClass) {
        return ((J3dLgBranchGroup)wrapped).isMouseEventSource(eventClass);
    }
    
    public void setMouseEventEnabled(boolean enabled) {
        ((J3dLgBranchGroup)wrapped).setMouseEventEnabled(enabled);
    }
    
    public boolean isMouseEventEnabled() {
        return ((J3dLgBranchGroup)wrapped).isMouseEventEnabled();
    }
    
    public void setMouseEventPropagatable(boolean enabled) {
        ((J3dLgBranchGroup)wrapped).setMouseEventPropagatable(enabled);
    }
    
    public boolean isMouseEventPropagatable() {
        return ((J3dLgBranchGroup)wrapped).isMouseEventPropagatable();
    }
    
    public void setKeyEventSource(boolean enabled) {
        ((J3dLgBranchGroup)wrapped).setKeyEventSource(enabled);
    }
    
    public boolean isKeyEventSource() {
        return ((J3dLgBranchGroup)wrapped).isKeyEventSource();
    }
    
    public void postEvent(LgEvent evt) {
    }
    
    public void addListener(LgEventListener listener, Class evtClass) {
    }
    
    public void addListener(LgEventListener listener, Class evtClass, Class source) {
    }
    
    public org.jdesktop.lg3d.displayserver.NodeID getNodeID() {
        return ((J3dLgBranchGroup)wrapped).getNodeID();
    }
    
    public void setNodeID(org.jdesktop.lg3d.displayserver.NodeID nodeID) {
        ((J3dLgBranchGroup)wrapped).setNodeID( nodeID );
    }
    
    public void setCapabilities() {
        ((J3dLgBranchGroup)wrapped).setCapabilities();
    }
}

