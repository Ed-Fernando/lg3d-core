/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: ChangeGroupAction.java,v $
 *   
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the
 *   Free Software Foundation, Inc.,
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *   
 *   Author: Colin M. Bullock
 *   cmbullock@gmail.com
 *   
 *   $Revision: 1.1 $
 *   $Date: 2005-12-02 17:06:30 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.action;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.MenuGroupChangeEvent;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 * Action used to initiate a change from the current
 * menu group to a new group.
 */
public class ChangeGroupAction implements ActionNoArg {
    
    private static Logger logger = Logger.getLogger("lg.startmenu");
    
    /** The new menu group */
    private MenuGroup newMenuGroup;
    
    /**
     * Default constructor
     * @param newGroup The new group
     */
    public ChangeGroupAction(MenuGroup newGroup) {
        this.newMenuGroup= newGroup;
    }

    /**
     * Change current group to a new menu group
     */
    public void performAction(LgEventSource source) {
        if(source != null) {
            logger.log(Level.FINE, "Changing groups to " + newMenuGroup);
            AppConnectorPrivate.getAppConnector().postEvent(new MenuGroupChangeEvent(newMenuGroup), null);
        }
    }  
    
}