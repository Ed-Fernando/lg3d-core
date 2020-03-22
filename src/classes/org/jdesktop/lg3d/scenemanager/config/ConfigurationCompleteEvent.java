/**
 * Project Looking Glass
 *
 * $RCSfile: ConfigurationCompleteEvent.java,v $
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
 * $Date: 2005-01-20 22:05:34 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.scenemanager.config;

/**
 * Event indicating that all configuration data has been posted.
 *
 * @author  paulby
 */
public class ConfigurationCompleteEvent extends ConfigData {
    
    /** Creates a new instance of ConfigurationCompleteEvent */
    public ConfigurationCompleteEvent() {
    }
    
    public void doConfig() {
        // Don't actually do anything
    }
    
}
