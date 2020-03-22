/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: StartMenuItemConfig.java,v $
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
 *   $Revision: 1.10 $
 *   $Date: 2006-10-13 17:56:27 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.displayserver.LgClassLoader;
import org.jdesktop.lg3d.scenemanager.config.ApplicationDescription;
import org.jdesktop.lg3d.scenemanager.config.ConfigData;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.DisplayResource;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuItem;

/**
 * Configuration object for a start menu item. Each item requires
 * that the following parameters be set:
 * <ul>
 *  <li>name - The name of the item (unique to the group)</li>
 *  <li>menuGroup - The parent group of the item (unique to the menu)</li>
 *  <li>command - The command to execute for the item</li>
 * </ul>
 * In addition, the following optional parameters may be set:
 * <ul>
 *  <li>desc - A short description of the item (some models will use this for a tool-tip)</li>
 *  <li>displayResourceType - The type of display used (one of DEFAULT, ICON, or TAPP)</li>
 *  <li>displayResourceUrl - If type is set to ICON or TAPP, this field should
 *      contain the icon location url or the class name implementing the tapp, respectively</li>
 * </ul>
 */
public class StartMenuItemConfig extends ConfigData {
    
    private static Logger logger = Logger.getLogger("lg.startmenu");

    public static final String DEFAULT_GROUP= "Applications";

    /** The parent menu group */
    private String menuGroup;

    /** The menu item data */
    private MenuItem menuItem;
    
    private String classLoaderID;
    
    private String[] alternateCommands = null;
    
    /** The classpath for the application */
//    private LgClasspath classpath = null;
    
    /**
     * Default constructor
     */
    public StartMenuItemConfig() {
        menuItem= new MenuItem();
        menuGroup= StartMenuItemConfig.DEFAULT_GROUP;
        classLoaderID = LgClassLoader.generateUniqueID();
    }

    /**
     * Create the menu item described by this config object
     * @return The menu item
     */
    public MenuItem createItem() {
        return menuItem;
    }

    /**
     * Post the configuration event for this menu item
     */
    @Override
    public void doConfig() {
        //logger.warning("Checking "+getCommand() +" is available "+ApplicationDescription.isApplicationAvailable(getCommand()));
        if (ApplicationDescription.isApplicationAvailable(getCommand())) {
            logger.log(Level.FINE, "Posting item config event: " + getName() +"  "+menuItem.getDisplayResource().getUrl());

            AppConnectorPrivate.getAppConnector().postEvent(this, null);
        } else {
            //logger.warning("CHecking alternates");
            String[] alts = getAlternateCommands();
            if (alts!=null) {
                for(int i=0; i<alts.length; i++)  {
                    //logger.warning("*** checking "+alts[i]);
                    if (ApplicationDescription.isApplicationAvailable(alts[i])) {
                        //logger.warning("Using "+alts[i]);
                        setCommand(alts[i]);
                        i = alts.length;
                        AppConnectorPrivate.getAppConnector().postEvent(this, null);
                    }
                }
            } else
                logger.warning("Executable "+getCommand()+" not found, ignoring taskbar item "+getName());
        } 
    }

    /**
     * Get the command for the menu item
     * @return The command
     */
    public String getCommand() {
        return menuItem.getCommand();
    }

    /**
     * Set the command for the menu item
     * @param command The command
     */
    public void setCommand(String command) {
        menuItem.setCommand(command);
    }
    
    public void setAlternateCommands(String[] alternateCommands) {
        this.alternateCommands = alternateCommands.clone();
    }
    
    public String[] getAlternateCommands() {
        if (alternateCommands==null)
            return null;
        return alternateCommands.clone();
    }

    /**
     * Get the classpath for the menu item
     * @return The classpath
     */
//    public LgClasspath getClasspath() {
//        return menuItem.getClasspath();
//    }

    /**
     * Set the classpath for the menu item
     * @param basefile The base file of the classpath
     * @param classpath The classpath
     */
//    public void setClasspath(LgClasspath classpath) {
//        menuItem.setClasspath(classpath);
//    }
    /**
     * Get the description for the menu item
     * @return The description
     */
    public String getDesc() {
        return menuItem.getDesc();
    }

    /**
     * Set the description for the menu item
     * @param desc The description
     */
    public void setDesc(String desc) {
        menuItem.setDesc(desc);
    }

    /**
     * Get the display resource type for the menu item
     * @return The display resource type
     */
    public String getDisplayResourceType() {
        return menuItem.getDisplayResource().getType().name();
    }

    /**
     * Set the display resource type for the menu item
     * @param type The display resource type
     */
    public void setDisplayResourceType(String type) {
        menuItem.getDisplayResource().setType(DisplayResource.Type.valueOf(type));
    }

//    /**
//     * Get the name of the display resource (filename),
//     * that is on the classpath.
//     * @return the display resource name
//     */
//    public String getDisplayResourceName() {
//        return menuItem.getDisplayResource().getFilename();
//    }
//
//    /**
//     * Set the display resource name (e.g. the relative path of the icon)
//     * @param filename the name of the file
//     */
//    public void setDisplayResourceName(String filename) {
//        menuItem.getDisplayResource().setFilename(filename);
//    }
//
    /**
     * Get the classname of the display resource (Tapp),
     * that is on the classpath.
     * @return the display resource classname
     */
    public String getDisplayResourceClassname() {
        return menuItem.getDisplayResource().getClassname();
    }

    /**
     * Set the display resource Classname (e.g. the relative Tapp)
     * @param classname the name of the class
     */
    public void setDisplayResourceClassname(String classname) {
        menuItem.getDisplayResource().setClassname(classname);
    }
    
    public void setClasspathJars(String classpathJars) throws IOException {
        menuItem.setClassLoader(new LgClassLoader(
				    parseClassPathJars(classpathJars),
                                    classLoaderID, 
                                    this.getClass().getClassLoader()));        
    }
    
    String getClasspathJars() {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Get the display resource URL for the menu item
     * @return The display resource URL
     */
    public String getDisplayResourceUrlName() {
        if (menuItem.getDisplayResource().getUrl()==null)
            return null;
        return menuItem.getDisplayResource().getUrl().toExternalForm();
    }

    /**
     * Set the display resource URL for the menu item
     * @param displayResourceUrl The display resource URL
     */
    public void setDisplayResourceUrlName(String displayResourceUrl) {
        try {
//            System.out.println("DisplayResourceUrl "+displayResourceUrl+"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            URL url = new URL(displayResourceUrl);
            if (url.getProtocol().equalsIgnoreCase("resource")) {
                url = new URL(url.getProtocol(), classLoaderID, url.getPath());
            }
            menuItem.getDisplayResource().setUrl(url);
//            System.out.println(menuItem.getDisplayResource().getUrl());
        } catch (MalformedURLException ex) {
            logger.warning("Malformed URL in configuration "+displayResourceUrl);
        }
    }
    
    public void setDisplayResourceUrl(URL displayResourceUrl) {
        menuItem.getDisplayResource().setUrl(displayResourceUrl);
    }
    
    /**
     * Get the display resource URL for the menu item
     * @return The display resource URL
     */
    public URL getDisplayResourceUrl() {
        return menuItem.getDisplayResource().getUrl();
    }
    
    /**
     * Get the parent menu group name
     * @return The group name
     */
    public String getMenuGroup() {
        return menuGroup;
    }

    /**
     * Set the parent menu group name
     * @param menuGroup The group name
     */
    public void setMenuGroup(String menuGroup) {
        this.menuGroup = menuGroup;
    }

    /**
     * Get the menu item name
     * @return The item name
     */
    public String getName() {
        return menuItem.getName();
    }

    /**
     * Set the menu item name
     * @param name The item name
     */
    public void setName(String name) {
        menuItem.setName(name);
    }
    
    /**
     * Returns the classloader for this application
     */
    public ClassLoader getAppClassLoader() {
        return menuItem.getClassLoader();
    }

    /**
     * Create a classloader if one does not already exist
     */
    public void createClassLoader() {
        if (menuItem.getClassLoader()==null)
            menuItem.setClassLoader(new LgClassLoader(
                                            new URL[0], 
                                            classLoaderID,
                                            this.getClass().getClassLoader()));        
    }
}
