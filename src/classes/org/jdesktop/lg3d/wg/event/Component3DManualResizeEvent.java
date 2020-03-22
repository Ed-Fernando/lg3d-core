/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DManualResizeEvent.java,v $
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
 * $Date: 2005-06-24 19:48:43 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.wg.event;

public class Component3DManualResizeEvent extends Component3DEvent {    
    private boolean started = false;
    
    public Component3DManualResizeEvent(boolean started) {
        this.started = started;
    }
    
    public boolean isStarted() {
        return started;
    }

}
