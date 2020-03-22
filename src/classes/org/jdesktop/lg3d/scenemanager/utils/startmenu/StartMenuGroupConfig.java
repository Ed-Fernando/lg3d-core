/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: StartMenuGroupConfig.java,v $
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
 *   $Date: 2005-12-02 17:06:37 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.scenemanager.config.ConfigData;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;

/**
 * Configuration object for a start menu group. Each group requires
 * the following parameter to be set:
 * <ul>
 *  <li>name - The name of the group (unique to the menu)</li>
 * </ul>
 * In addition, the following optional parameters are available:
 * <ul>
 *  <li>desc - A short description of the group (some models may use this for a tool-tip)</li>
 *  <li>groupLinks - An array of unique group names to which one-way links should be
 *      created from this group. Two-way links are created by including a corresponding
 *      groupLink in the other group for the reverse link.</li>
 * </ul>
 */
public class StartMenuGroupConfig extends ConfigData {

    private static Logger logger = Logger.getLogger("lg.startmenu");

    /** The menu group name */
    private String name;

    /** The description of the menu group */
    private String desc;

    /** The names of all groups this group links to */
    private String[] groupLinks;

    /** Whether or not this is the default group */
    private boolean defaultGroup;

    /**
     * Create the menu group that this configuration object describes
     * @return The menu group
     */
    public MenuGroup createGroup() {
        MenuGroup group= new MenuGroup();
        group.setName(name);
        group.setDesc(desc);
        return group;
    }

    /**
     * Post the configuration event for this menu group
     */
    @Override
    public void doConfig() {
        logger.log(Level.FINE, "Posting group config event: " + name);
        AppConnectorPrivate.getAppConnector().postEvent(this, null);
    }

    /**
     * Get whether this menu group is the default group
     * @return <code>true</code> if this group should be the default
     */
    public boolean isDefaultGroup() {
        return defaultGroup;
    }

    /**
     * Set whether or not this should be the default group
     * @param defaultGroup <code>true</code> to set this group as the default
     */
    public void setDefaultGroup(boolean defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    /**
     * Get the description for the menu group
     * @return The description
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Set the description for the menu group
     * @param desc The description
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Get the name of all the menu groups that this group links to
     * @return The menu group names
     */
    public String[] getGroupLinks() {
        return groupLinks;
    }

    /**
     * Set the names of the menu groups that this group links to
     * @param groupLinks The menu group names
     */
    public void setGroupLinks(String[] groupLinks) {
        this.groupLinks = groupLinks;
    }

    /**
     * Get the name of the menu group
     * @return The group name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the menu group
     * @param name The group name
     */
    public void setName(String name) {
        this.name = name;
    }

}
