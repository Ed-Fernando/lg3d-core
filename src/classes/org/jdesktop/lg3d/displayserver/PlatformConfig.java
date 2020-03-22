/**
 * Project Looking Glass
 *
 * $RCSfile: PlatformConfig.java,v $
 *
 * Copyright (c) 2005, Sun Microsystems, Inc., All Rights Reserved
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
 * $Date: 2007-01-04 22:40:19 $
 * $State: Exp $
 *
 */

package org.jdesktop.lg3d.displayserver;

import java.net.URL;
import java.net.URISyntaxException;
import java.beans.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Abstract configuration class for platform information.
 *
 * A similar class holds the scene manager config
 *
 * @author  paulby
 */
public abstract class PlatformConfig {
    
    protected static PlatformConfig config = null;
    protected static Logger logger = Logger.getLogger("lg.displayserver");
    
    /**
     * get the LgConfig singleton, loading a new one if it is null
     * @return the LgConfig singleton
     */
    public static PlatformConfig getConfig() {
        if (config==null) {
            String className = System.getProperty("lg.platformConfigClass", 
                        "org.jdesktop.lg3d.displayserver.DefaultPlatformConfig");
            try {                
                Class clazz = Class.forName(className);
                config = (PlatformConfig) clazz.newInstance();
                config.logConfig(logger);                    
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                System.exit(1);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(1);
            } catch (InstantiationException ex) {
                ex.printStackTrace();
                System.exit(1);
            }

        }
        
        return config;
    }
    

    /**
     * Getter for property foundationWinSys.
     * @return Value of property foundationWinSys.
     */
    public abstract String getFoundationWinSys();
    
    /**
     * Returns the fully qualified class name that implements native window
     * integration. If this is null or an empty string the native window
     * integration subsystem will not be loaded.
     */
    public abstract String getNativeWinIntegration();
    
    /**
     * Returns the fully qualified class name that provides the Look and Feel
     * for all native windows.
     */
    public abstract String getNativeWinLookAndFeel();
    
    /**
     * Getter for property clientServer.
     *
     * If true run the applications in client server mode.
     * If false the client application will instantiate the server
     * in the same VM.
     * @return Value of property clientServer.
     */
    public abstract boolean isClientServer();
    
    /**
     * Returns the universe factory which will create the Universe and
     * Window that displays it
     */
    public abstract UniverseFactory getUniverseFactory();
    
    /**
     * Getter for property rmiPort.
     * @return Value of property rmiPort.
     */
    public abstract int getRmiPort();
    
    /**
     * Getter for property lgServerPort.
     * @return Value of property lgServerPort.
     */
    public abstract int getLgServerPort();
    
    /**
     * Getter for property fullScreenAntiAliasing.
     * @return Value of property fullScreenAntiAliasing.
     */
    public abstract boolean isFullScreenAntiAliasing();

   /**
     * Output the configuration to this logger at log
     * level config
		 * @param logger the logger to log the config info to
     */
    public abstract void logConfig( java.util.logging.Logger logger );
    
}
