/**
 * Project Looking Glass
 *
 * $RCSfile: BackgroundChangeRequestEvent.java,v $
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
 * $Date: 2005-06-24 19:48:18 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.event;
 
import org.jdesktop.lg3d.scenemanager.utils.background.Background;
import org.jdesktop.lg3d.wg.event.LgEvent;

/**
 */
public class BackgroundChangeRequestEvent extends LgEvent {
    private Background background;
    
    public BackgroundChangeRequestEvent(Background background) {
        this.background = background;
    }
    
    public Background getBackground() {
        return background;
    }
}
