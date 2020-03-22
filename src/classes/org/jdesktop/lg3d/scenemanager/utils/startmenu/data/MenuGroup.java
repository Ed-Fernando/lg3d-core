/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: MenuGroup.java,v $
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
 *   $Date: 2005-12-02 17:06:39 $$
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.data;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a group or category in the menu. Each menu group
 * maintains its own list of children menu items, and links to
 * adjacent menu groups. There is no guarantee made be this
 * class that links are traversable in both directions. It
 * is the responsibility of the <code>StartMenuData</code>
 * class to manage the directionality and ensure the 
 * sanity of links between menu groups.
 * 
 * @see StartMenuData 
 */
public class MenuGroup {
    
    /** The menu group name */
    private String name;
    
    /** An optional description of the group */
    private String desc;
    
    // TODO: Change to use DisplayResource instead of simple String
    /** The icon to be displayed */
    private String displayResource;
    
    /** The list of all groups linked with this one */
    private Set<String> linkedGroups= new HashSet<String>();
    
    /** The list of menu items contained in this group */
    private Set<MenuItem> items= new HashSet<MenuItem>();
    
    /**
     * Default constructor.
     */
    public MenuGroup() {
        
    }
    
    /**
     * Constructs a menu group with the given name
     * @param name The menu group name
     */
    public MenuGroup(String name) {
        this.name= name;
    }
    
    /**
     * Get the description of the menu group
     * @return Returns the description
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Set the description of the menu group
     * @param desc The description to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Get the display resource for the menu group
     * @return Returns the display resource
     */
    public String getDisplayResource() {
        return displayResource;
    }

    /**
     * Set the display resource for the menu group
     * @param displayResource The display resource to set
     */
    public void setDisplayResource(String displayResource) {
        this.displayResource = displayResource;
    }

    /**
     * Get the group name
     * @return Returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the group name
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Add a link from this group to another group. To ensure the
     * existence of groups and the sanity of links, this method
     * should only be used internally by the menu. Users
     * should call <code>StartMenuData.linkGroups()</code>
     * to link two groups.
     * @param groupName The group to link to
     * @see StartMenuData#linkGroups(MenuGroup, MenuGroup)
     */
    void addLink(String groupName) {
        linkedGroups.add(groupName);
    }
    
    /**
     * Remove a link from this group to another group. To ensure the
     * existence of groups and the sanity of links, this method
     * should only be used internally by the menu. Users
     * should call <code>StartMenuData.unlinkGroups()</code>
     * to unlink two groups.
     * @param groupName The group to unlink
     * @see StartMenuData#unlinkGroups(MenuGroup, MenuGroup)
     */
    void removeLink(String groupName) {
        linkedGroups.remove(groupName);
    }
    
    /**
     * Get a list of the names of all groups that this group links to.
     * This method is used internally by the menu to map group
     * links. Users should prefer <code>StartMenuData.getLinkedGroups()</code>
     * for retrieving group links.
     * @return Returns the linked group names
     * @see StartMenuData#getLinkedGroups(MenuGroup)
     */
    Set<String> getLinkedGroups() {
        return linkedGroups;
    }
    
    /**
     * Get the number of groups that this group links to
     * @return The group link count
     */
    public int getLinkedGroupCount() {
        return linkedGroups.size();
    }

    /**
     * Add a menu item to this menu group
     * @param item The menu item
     */
    public void addItem(MenuItem item) {
        items.add(item);
    }
    
    /**
     * Remove a menu item from this menu group
     * @param item The menu item
     */
    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    /**
     * Get all the menu items for this menu group
     * @return Returns the items
     */
    public Set<MenuItem> getItems() {
        return items;
    }
    
    /**
     * Returns the name of the menu group
     * @return The group name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compares two menu groups for equality. Two groups are
     * considered equal if they have the same name.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MenuGroup) {
            return name.equals(((MenuGroup)obj).name);
        }
        return false;
    }

    /**
     * Get the hash code for this menu group (based on the group name)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if(name != null) return name.hashCode();
        else return 0;
    }

}
