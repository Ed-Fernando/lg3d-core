/**
 * Project Looking Glass
 *
 * $RCSfile: LgBranchGroupWrapper.java,v $
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
 * $Date: 2005-04-14 23:05:32 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.wrapper;

import org.jdesktop.lg3d.sg.internal.wrapper.BranchGroupWrapper;

public interface LgBranchGroupWrapper extends BranchGroupWrapper {
    
    public void setMouseEventSource(Class eventClass, boolean enabled);
    
    public boolean isMouseEventSource(Class eventClass);
    
    public void setMouseEventEnabled(boolean enable);
    
    public boolean isMouseEventEnabled();
    
    public void setMouseEventPropagatable(boolean enable);
    
    public boolean isMouseEventPropagatable();
    
    public void setKeyEventSource(boolean enabled);
    
    public boolean isKeyEventSource();
    
    public void setNodeID( org.jdesktop.lg3d.displayserver.NodeID nodeID );
    
    /**
     * Sets the capabilities of this BranchGroup and all its children correctly.
     */
    public void setCapabilities();
}
