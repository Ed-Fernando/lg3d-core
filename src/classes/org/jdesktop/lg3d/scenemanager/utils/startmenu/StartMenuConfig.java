/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: StartMenuConfig.java,v $
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
 *   $Revision: 1.3 $
 *   $Date: 2006-05-01 22:45:55 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu;

import java.util.logging.Logger;

import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.scenemanager.config.ConfigData;

/**
 * General application configuration parameters for the start menu.
 * At the present time, the only configuration option is the
 * class name which implements the menu model.
 */
public class StartMenuConfig extends ConfigData {

    /** The full class name implmenting the 3D menu interface */
    String menuModelClass;

    /**
     * Default constructor
     */
    public StartMenuConfig() {

    }

    /**
     * Get the menu model class
     * @return The model class
     */
    public String getMenuModelClass() {
        return menuModelClass;
    }

    /**
     * Set the menu model class
     * @param menuModelClass The model class to set
     */
    public void setMenuModelClass(String menuModelClass) {
        this.menuModelClass = menuModelClass;
    }

    /**
     * Post the start menu configuration event
     */
    @Override
    public void doConfig() {
        AppConnectorPrivate.getAppConnector().postEvent(this, null);
    }

}
