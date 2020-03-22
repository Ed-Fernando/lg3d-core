/**
 * Project Looking Glass
 *
 * $RCSfile: SceneGraphSetup.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2007-01-04 22:40:21 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import java.util.logging.Level;
import org.jdesktop.lg3d.displayserver.PlatformConfig;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;

/**
 *
 * Each implementation of the scene graph must contain a concreate implementation
 * of this class (with the same name)
 *
 * The DisplayServer and AppConnector will instantiate the concrete class
 * and call the appropriate initialisation methods.
 *
 * @author  paulby
 */
public abstract class SceneGraphSetup {
    
    protected static String wrapperPackage=null;
    protected static String widgetWrapperPackage=null;
    protected java.util.logging.Logger logger = java.util.logging.Logger.getLogger("lg.sg");
    
    
    public static void initializeServer() {
        if (PlatformConfig.getConfig().isClientServer()) {
            // Init RMI Server (which is in rmiclient.SceneGraphSetup
            try {
                Class clazz = Class.forName("org.jdesktop.lg3d.sg.internal.rmi.rmiclient.SceneGraphSetup");
                org.jdesktop.lg3d.sg.SceneGraphSetup sgs = (org.jdesktop.lg3d.sg.SceneGraphSetup)clazz.newInstance();
                sgs.initialiseServer();
            } catch(Exception e) {
                java.util.logging.Logger logger = java.util.logging.Logger.getLogger("lg.sg");
                logger.log(Level.SEVERE, "Unable to initialize Server "+e);
                System.exit(1);
            }
        } else {
            // Init Java 3D wrapper
             org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.SceneGraphSetup sgs = new
                   org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.SceneGraphSetup();
             sgs.initialiseServer();
        }
    }
    
    public static void initializeClient() {
        if (PlatformConfig.getConfig().isClientServer()) {
            // Init RMI Client
            try {
                Class clazz = Class.forName("org.jdesktop.lg3d.sg.internal.rmi.rmiclient.SceneGraphSetup");
                org.jdesktop.lg3d.sg.SceneGraphSetup sgs = (org.jdesktop.lg3d.sg.SceneGraphSetup)clazz.newInstance();
                sgs.initialiseClient();
            } catch(Exception e) {                
                java.util.logging.Logger logger = java.util.logging.Logger.getLogger("lg.sg");
                logger.log(Level.SEVERE, "Unable to initialize Client "+e);
                System.exit(1);
            }
         } else {
            // Init Java 3D wrapper
             org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.SceneGraphSetup sgs = new
                   org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.SceneGraphSetup();
             sgs.initialiseClient();
        }
    }
    
    /**
     * Return the package name of the Scene Graph Wrapper
     */
    public static String getWrapperPackage() {
        if (wrapperPackage==null) {
            if (PlatformConfig.getConfig().isClientServer() &&
                !AppConnectorPrivate.getAppConnector().isServer() )
                wrapperPackage = "org.jdesktop.lg3d.sg.internal.rmi.rmiclient.";
            else
                wrapperPackage = "org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.";
        }
        
        return wrapperPackage;
   }
    
    /**
     * Return the package name for the Widgets wrappers
     */
    public static String getWidgetWrapperPackage() {
        if (widgetWrapperPackage==null) {
            if (PlatformConfig.getConfig().isClientServer() &&
                !AppConnectorPrivate.getAppConnector().isServer()) 
                widgetWrapperPackage = "org.jdesktop.lg3d.wg.internal.rmi.rmiclient.";
            else
                widgetWrapperPackage = "org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.";
        }
        
        return widgetWrapperPackage;
    }
    
    /**
     * Initialise the client scene graph API
     */
    protected abstract void initialiseClient();
        
    /**
     * Initialise the server scene graph API
     */
    protected abstract void initialiseServer();
    

}
