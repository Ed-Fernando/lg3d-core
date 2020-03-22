/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: StartMenuData.java,v $
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
 *   $Date: 2005-12-02 17:06:40 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The main data structure for the start menu. This class
 * maintains all the groups in the menu, as well as references
 * to the current and default groups. It is also responsible
 * for creating and maintaining links between groups, and ensuring
 * the sanity and validity of the links.
 */
public class StartMenuData {
    
    /** All groups in the menu, mapped by group name */
    private Map<String, MenuGroup> groupNameMap= new HashMap<String, MenuGroup>();
    
    /** The current group */
    private MenuGroup currentGroup;
    
    /** The default group */
    private MenuGroup defaultGroup;
    
    /**
     * Default constructor
     */
    public StartMenuData() {
        
    }
    
    /**
     * Add a new group to the menu
     * @param group The menu group
     */
    public void addGroup(MenuGroup group) {
        addGroup(group, false);
    }
    
    /**
     * Add a new group to the menu and possibly set it as
     * the default group. Note: this group will be set as the
     * default group if there is not already a default, regarless
     * of the <code>isDefaultGroup</code> parameter.
     * @param group The menu group
     * @param isDefaultGroup <code>true</code> if this group should be the default group
     */
    public void addGroup(MenuGroup group, boolean isDefaultGroup) {
        assert(group != null && group.getName() != null);
        groupNameMap.put(group.getName(), group);
        if(defaultGroup == null || isDefaultGroup) 
            defaultGroup= group;
    }
    
    /**
     * Get a menu group by name
     * @param groupName The menu group name
     * @return The menu group
     */
    public MenuGroup getGroup(String groupName) {
        return groupNameMap.get(groupName);
    }
    
    /**
     * Get all groups in the menu
     * @return All menu groups
     */
    public Collection<MenuGroup> getAllGroups() {
        return groupNameMap.values();
    }
    
    /**
     * Get the current menu group. If there is no current group,
     * the current group is set to the default group.
     * @return The current group
     */
    public MenuGroup getCurrentGroup() {
        if(currentGroup == null)
            currentGroup= getDefaultGroup();
        return currentGroup;
    }
    
    /**
     * Set the current menu group
     * @param group The current group
     */
    public void setCurrentGroup(MenuGroup group) {
        assert(group != null);
        if(!groupNameMap.containsKey(group.getName())) addGroup(group);
        currentGroup= group;
    }
    
    /**
     * Get the default menu group
     * @return The default group
     */
    public MenuGroup getDefaultGroup() {
        return defaultGroup;
    }

    /**
     * Set the default menu group
     * @param defaultGroup The default group
     */
    public void setDefaultGroup(MenuGroup defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    /**
     * Remove a group from the menu. This also removes all
     * links to the group, and all menu items contained
     * within it.
     * @param group The group to remove
     */
    public void removeGroup(MenuGroup group) {
        if(group == null)
            return;
        for(MenuGroup linkGroup : getLinkedGroups(group)) {
            linkGroup.removeLink(group.getName());
        }
        groupNameMap.remove(group.getName());
    }
    
    /**
     * Create a link between two groups. Currently, this creates
     * a bi-directional link, but the option for uni-directional
     * links may be added in the future.
     * @param first One menu group
     * @param second Another menu group
     */
    public void linkGroups(MenuGroup first, MenuGroup second) {
        assert(first != null && second != null);
        if(!groupNameMap.containsKey(first.getName())) addGroup(first);
        if(!groupNameMap.containsKey(second.getName())) addGroup(second);
        
        first.addLink(second.getName());
        second.addLink(first.getName());
    }
    
    /**
     * Get all the groups that a given menu group links to.
     * @param source The local group
     * @return All the remote groups
     */
    public Set<MenuGroup> getLinkedGroups(MenuGroup source) {
        Set<MenuGroup> links= new HashSet<MenuGroup>();
        
        for(String groupName : source.getLinkedGroups()) {
            links.add(groupNameMap.get(groupName));
        }
        
        return(links);
    }
    
    /**
     * Remove a link between two groups. This removes the link
     * in both groups, although support may be added for only
     * removing one side of a link in the future.
     * @param first One menu group
     * @param second Another menu group
     */
    public void unlinkGroups(MenuGroup first, MenuGroup second) {
        assert(first != null && second != null);
        first.removeLink(second.getName());
        second.removeLink(first.getName());
    }
    
}
