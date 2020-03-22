/**
 * Project Looking Glass
 *
 * $RCSfile: AppLaunchEvent.java,v $
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
 * $Date: 2005-08-26 18:49:37 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.event;

import org.jdesktop.lg3d.wg.event.LgEvent;

/**
 * This event is fired when a new application is started.  
 * 
 * @author harsh
 */
public class AppLaunchEvent extends LgEvent {    
    /**
     * The command that was used to launch this Application.
     */
    private String command;
    
    /**
     * @param command
     */
    public AppLaunchEvent(String command) {
        if (command == null) {
            throw new IllegalArgumentException(
			"Argument to method ApplicationStartedEvent is null");
        }
        this.command = command;
    }
	
    /**
     * 
     * @return the command that was used
     */
    public String getCommand() {
        return command;
    }
}
