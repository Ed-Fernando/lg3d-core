/**
 * Project Looking Glass
 *
 * $RCSfile: LgConfig.java,v $
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
 * $Revision: 1.18 $
 * $Date: 2007-01-24 00:57:34 $
 * $State: Exp $
 *
 * Modified by Phil Dowell (PhilD)
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
 * Old LG Configuration object, now used exclusively in the config file. The System uses PlatformConfig
 * to access the data
 *
 * @author  paulby
 */
public class LgConfig {
        
    /**
     * Holds value of property foundationWinSys.
     */
    private String foundationWinSys;

    /**
     * Holds value of property nativeWinIntegration.
     */
    private String nativeWinIntegration;

    /**
     * Holds value of property nativeWinLookAndFeel.
     */
    private String nativeWinLookAndFeel;

    /**
     * Holds value of property clientServer.
     */
    private boolean clientServer;
    
    /**
     * Holds value of property x11IntegrationEnabled.
     */
    private boolean x11IntegrationEnabled;
    
    /**
     * Holds value of property displayConfig.
     */
    private String displayConfig;
    
    /**
     * Holds value of property rmiPort.
     */
    private int rmiPort;
    
    /**
     * Holds value of property lgServerPort.
     */
    private int lgServerPort;

    /**
     * Holds value of property fullScreenAntiAliasing.
     */
    private boolean fullScreenAntiAliasing;
    
    /** Creates a new instance of LgConfig */
    public LgConfig() {
        setDefaults();
    }
    
   
    
     /**
     * Set the default values
     */
    private void setDefaults() {
        foundationWinSys = "org.jdesktop.lg3d.displayserver.fws.awt.WinSysAWT";
        nativeWinIntegration = "";
        nativeWinLookAndFeel = "";
        clientServer = false;           
        x11IntegrationEnabled = false;  
        displayConfig = "j3d1x1";       // These files must be in etc/lg3d/displayconfig
        rmiPort = 44817;
        lgServerPort = 44816;
        fullScreenAntiAliasing = false;
    }

    /**
     * load the configuration, finding the config file from
     * the system property <code>lg.configurl</code>
     */
    static LgConfig loadConfig(final Logger logger) {
	String configUrl = System.getProperty("lg.configurl");
        LgConfig config = null;
        URL url=null;
        

        try {
            if (configUrl==null || configUrl.length()==0 ) {
                url = ClassLoader.getSystemClassLoader().getResource("/etc/lg3d/lgconfig.xml");
            } else
                url = new URL(configUrl);
            {
                BufferedInputStream in = new BufferedInputStream( url.openStream() );
                BufferedReader b = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer configBuf = new StringBuffer("Config file contents:\n");
                while ((str = b.readLine()) != null) {
                    configBuf.append("\t");
                    configBuf.append(str);
                    configBuf.append("\n");
                }
                b.close();
            }
            BufferedInputStream in = new BufferedInputStream( url.openStream() );
            XMLDecoder decoder = new XMLDecoder( in );
            decoder.setExceptionListener(new ExceptionListener() {
                public void exceptionThrown(Exception e) {
                    logger.log(Level.WARNING, "Exception parsing config : "+e);
                    e.printStackTrace();
                }
            });
            config = (LgConfig)decoder.readObject();
            decoder.close();
            logger.info("Finished loading config");
            logger.warning("NativeWin "+config.getNativeWinIntegration()+"  L&F "+config.getNativeWinLookAndFeel());
        } catch( java.net.MalformedURLException mue ) {
            logger.warning("Supplied config URL is malformed "+configUrl);
            config = null;
        } catch( java.io.IOException ioe ) {
            logger.warning("IO Error loading config "+url);
            config = null;
        } catch(Exception e) {
            config=null;
        }

        
        if (config==null) {
            logger.info("Using default configuration");
            config = new LgConfig();
        }
        
        return config;
    }
    
    /**
    * Save the configuration
    * @param config the config to save
    * @param file the file to save the config to
    * @throws IOException if unable to write to the file
    */
    public static void saveConfig( LgConfig config, File file ) throws java.io.IOException {
        BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( file ));
        XMLEncoder encoder = new XMLEncoder( out );
        encoder.writeObject( config );
        encoder.close();
    }
    
    /**
     * Getter for property foundationWinSys.
     * @return Value of property foundationWinSys.
     */
    public String getFoundationWinSys() {
        return this.foundationWinSys;
    }
    
    /**
     * Setter for property foundationWinSys.
     * @param foundationWinSys New value of property foundationWinSys.
     */
    public void setFoundationWinSys(String foundationWinSys) {
        this.foundationWinSys = foundationWinSys;
    }

    /**
     * Getter for property nativeWinIntegration.
     * @return Value of property nativeWinIntegration.
     */
    public String getNativeWinIntegration() {
        return this.nativeWinIntegration;
    }
    
    /**
     * Setter for property nativeWinIntegration.
     * @param nativeWinIntegration New value of property nativeWinIntegration.
     */
    public void setNativeWinIntegration(String nativeWinIntegration) {
        this.nativeWinIntegration = nativeWinIntegration;
    }
    
    /**
     * Getter for property nativeWinLookAndFeel.
     * @return Value of property nativeWinLookAndFeel.
     */
    public String getNativeWinLookAndFeel() {
        return this.nativeWinLookAndFeel;
    }
    
    /**
     * Setter for property nativeWinLookAndFeel.
     * @param nativeWinLookAndFeel New value of property nativeWinLookAndFeel.
     */
    public void setNativeWinLookAndFeel(String nativeWinLookAndFeel) {
        this.nativeWinLookAndFeel = nativeWinLookAndFeel;
    }
    
    /**
     * Getter for property clientServer.
     *
     * If true run the applications in client server mode.
     * If false the client application will instantiate the server
     * in the same VM.
     * @return Value of property clientServer.
     */
    public boolean isClientServer() {
        return this.clientServer;
    }

    /**
     * Setter for property clientServer.
    @param clientServer New value of property clientServer.
     */
    public void setClientServer(boolean clientServer) {
        this.clientServer = clientServer;
	}
    
    /**
     * Getter for property x11IntegrationEnabled.
     *
     * If X11IntegrationEnabled returns false no X11 integration
     * modules will be loaded.
     *
     * @return Value of property x11IntegrationEnabled.
     */
    public boolean isX11IntegrationEnabled() {
        return this.x11IntegrationEnabled;
    }
    
    /**
     * Setter for property x11IntegrationEnabled.
     * @param x11IntegrationEnabled New value of property x11IntegrationEnabled.
     */
    public void setX11IntegrationEnabled(boolean x11IntegrationEnabled) {
        this.x11IntegrationEnabled = x11IntegrationEnabled;
    }
    
    /**
     * Get the display config file name. This file must be located in
     * etc/jz/displayconfig
     *
     * @return Value of property displayConfig.
     */
    public String getDisplayConfig() {
        return this.displayConfig;
    }
    
    /**
     * Setter for property displayConfig.
     * @param displayConfig New value of property displayConfig.
     */
    public void setDisplayConfig(String displayConfig) {
        this.displayConfig = displayConfig;
    }
    
    /**
     * Getter for property rmiPort.
     * @return Value of property rmiPort.
     */
    public int getRmiPort() {
	    return this.rmiPort;
    }
    
    /**
     * Setter for property rmiPort.
     * @param rmiPort New value of property rmiPort.
     */
    public void setRmiPort(int rmiPort) {
        this.rmiPort = rmiPort;
    }
    
    /**
     * Getter for property lgServerPort.
     * @return Value of property lgServerPort.
     */
    public int getLgServerPort() {
        return this.lgServerPort;
	}
    
    /**
     * Setter for property lgServerPort.
     * @param lgServerPort New value of property lgServerPort.
     */
    public void setLgServerPort(int lgServerPort) {
        this.lgServerPort = lgServerPort;
	}

    /**
     * Getter for property fullScreenAntiAliasing.
     * @return Value of property fullScreenAntiAliasing.
     */
    public boolean isFullScreenAntiAliasing() {
	    
	    return this.fullScreenAntiAliasing;
    }

    /**
     * Setter for property fullScreenAntiAliasing.
     * @param fullScreenAntiAliasing New value of property fullScreenAntiAliasing.
     */
    public void setFullScreenAntiAliasing(boolean fullScreenAntiAliasing) {

        this.fullScreenAntiAliasing = fullScreenAntiAliasing;
	}
    
    public interface LgConfigInterface {
        public void setConfig(LgConfig config);
    }
}
