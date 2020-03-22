/**
 * Project Looking Glass
 *
 * $RCSfile: TaskbarItemConfig.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-05-01 07:27:47 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.taskbar;

import java.util.logging.Level;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.scenemanager.config.ConfigData;
import org.jdesktop.lg3d.wg.Tapp;


/**
 * Configuration data and event for a task bar item. Any 'widget' that appears
 * in the task bar is a TaskbarItem (with the exclusion of applications which are
 * handled differently).
 *
 * @author  paulby
 */
public class TaskbarItemConfig extends ConfigData {

    /**
     * Holds value of property itemClass.
     */
    private String itemClass;
    
    /**
     * The (optional) index for the taskbar item
     */
    private int itemIndex= Integer.MAX_VALUE;
    
    /** Creates a new instance of TaskbarItemConfig */
    public TaskbarItemConfig() {
    }

    /**
     * Create taskbar. May return null if there is an error.
     * @return Value of property taskbarItem.
     */
    public Tapp createItem() {
        Tapp item=null;
        
        try {
            Class c = Class.forName(itemClass);
            item = (Tapp)c.newInstance();
        } catch(Exception e) {
            logger.log( Level.WARNING, "Failed to create "+itemClass, e);
        }
                
        return item;
    }

    /**
     * Return main class of taskbar item
     * @return Value of property itemClass.
     */
    public String getItemClass() {

        return this.itemClass;
    }

    /**
     * Setter for property itemClass.
     * @param itemClass New value of property itemClass.
     */
    public void setItemClass(String itemClass) {

        this.itemClass = itemClass;
    }

    /**
     * Get the index of the item on the taskbar
     * @return The item index
     */
    public int getItemIndex() {
        return itemIndex;
    }

    /**
     * Set the index of the item on the taskbar
     * @param itemIndex The item index
     */
    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    public void doConfig() {
        AppConnectorPrivate.getAppConnector().postEvent(this, null);
    }

    
//    public class TaskbarItemConfigEvent extends ConfigEvent {
//        private TaskbarItemConfig config;
//        
//        public TaskbarItemConfigEvent( TaskbarItemConfig config ) {
//            this.config = config;
//        }
//        
//        public TaskbarItemConfig getConfig() {
//            return config;
//        }
//    }
}
