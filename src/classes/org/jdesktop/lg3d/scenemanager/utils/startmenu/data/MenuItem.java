/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: MenuItem.java,v $
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
 *   $Date: 2006-08-17 18:28:20 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.data;

/**
 * An individual menu item, representing an application. A menu item
 * is always a leaf node, and belongs to a single parent group.
 */
public class MenuItem {
    
    /** The menu item name */
    private String name;
    
    /** An optional description of the menu item */
    private String desc;
    
    /** The display resource for the menu item */
    private DisplayResource displayResource;
    
    /** The executable command for the menu item */
    private String command;
    
    /** The classpath for the application */
    //private LgClasspath classpath;
    
    private ClassLoader classLoader;
    
    /**
     * Default constructor
     */
    public MenuItem() {
        displayResource= new DisplayResource();
    }
    
    /**
     * Constructs a menu item with the given name
     * @param name The menu item name
     */
    public MenuItem(String name) {
        this.name= name;
    }
    
    
    
    /**
     * Get the command to execute for the menu item
     * @return The command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Set the command to execute for the menu item
     * @param command The command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

//    /**
//     * Get the classpath to execute for the menu item
//     * @return The classpath
//     */
//    public LgClasspath getClasspath() {
//        return classpath;
//    }
//
//    /**
//     * Set the classpath to execute for the menu item
//     * @param classpath The classpath to set
//     */
//    public void setClasspath(LgClasspath classpath) {
//        this.classpath = classpath;
//    }
    
    /**
     * Get the description of the menu item
     * @return The description
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Set the description of the menu item
     * @param desc The description to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Get the display resource for the menu item
     * @return The display resource
     */
    public DisplayResource getDisplayResource() {
        return displayResource;
    }

    /**
     * Set the display resource for the menu item
     * @param displayResource The display resource to set.
     */
    public void setDisplayResource(DisplayResource displayResource) {
        if(displayResource == null)
            throw new IllegalArgumentException("Menu item display resource cannot be null");
        this.displayResource = displayResource;
    }

    /**
     * Get the name of the menu item
     * @return The item name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the menu item
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Compare two menu items for equality. Two menu items are considered
     * equal if they have the same name. (<b>Note:</b> This may change in
     * the future, as it seems reasonable that two items in different
     * groups could share the same name yet not represent the
     * same menu item.)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MenuItem) {
            return name.equals(((MenuItem)obj).name);
        }
        return false;
    }

    /**
     * Get the hash code of the menu item
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if(name == null) return 0;
        else return name.hashCode();
    }

    /**
     * Get the string representation of the menu item
     * @return The menu item name
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
}
