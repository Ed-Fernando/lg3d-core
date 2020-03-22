/**
 * Project Looking Glass
 *
 * $RCSfile: ConfigControl.java,v $
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
 * $Revision: 1.18 $
 * $Date: 2007-01-04 22:40:17 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.classloader.LgClasspath;
import org.jdesktop.lg3d.scenemanager.config.ApplicationDescription;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.StartMenuItemConfig;
import org.jdesktop.lg3d.scenemanager.config.ConfigData;
import org.jdesktop.lg3d.scenemanager.config.ConfigurationCompleteEvent;
import org.jdesktop.lg3d.scenemanager.config.SceneManagerConfig;
import org.jdesktop.lg3d.scenemanager.glassy.GlassySceneManagerConfig;
import org.jdesktop.lg3d.scenemanager.SceneManager;

/**
 *
 * Reads/writes and distributes configuration information. Mostly for the scenemanager
 *
 * @author  paulby
 */
public abstract class ConfigControl {
     
    protected Logger logger = Logger.getLogger("lg.config");
    
    /**
     * Return the Scene Manager
     */
    public abstract SceneManager createSceneManager();
    
    /**
     * Process all the configuration data
     */
    public abstract void processConfig();
    
    public static ConfigControl getConfigControl() {
        String className = System.getProperty("lg.configclass");
        if (className==null)
            return new DefaultConfigControl();
        try {            
            Class clazz = Class.forName(className);
            return (ConfigControl) clazz.newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger("lg.config").severe("Unable to locate config class "+className);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            Logger.getLogger("lg.config").log(Level.SEVERE, "Unable to instantiate config class "+className, ex);
        }
        
        throw new RuntimeException("Failed to create ConfigControl");
    }


}
