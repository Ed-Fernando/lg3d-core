/**
 * Project Looking Glass
 *
 * $RCSfile: DefaultPlatformConfig.java,v $
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
 * $Date: 2007-01-04 22:40:17 $
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
 * Static LG Configuration object and file reader/writer. 
 *
 * @author  paulby
 */
public class DefaultPlatformConfig extends PlatformConfig {
    
    /** the LgConfig singleton */
    protected LgConfig lgConfig;
    /** the logger instance */

    /** Creates a new instance of LgConfig */
    public DefaultPlatformConfig() {
        lgConfig = LgConfig.loadConfig(logger);
    }
        
    /**
     * Output the configuration to this logger at log
     * level config
		 * @param logger the logger to log the config info to
     */
    public void logConfig( java.util.logging.Logger logger ) {
        logger.config("FoundationWinSys "+lgConfig.getFoundationWinSys() );
        logger.config("NativeWinIntegration "+lgConfig.getNativeWinIntegration() );
        logger.config("NativeWinLookAndFeel "+lgConfig.getNativeWinLookAndFeel() );
        logger.config("ClientServer "+lgConfig.isClientServer() );
    }

    public String getFoundationWinSys() {
        return lgConfig.getFoundationWinSys();
    }

    public String getNativeWinIntegration() {
        return lgConfig.getNativeWinIntegration();
    }

    public String getNativeWinLookAndFeel() {
        return lgConfig.getNativeWinLookAndFeel();
    }

    public boolean isClientServer() {
        return lgConfig.isClientServer();
    }

    /**
     * Returns the DefaultUniverseFactory and set the default configuraiton in
     * the factory to be the file from lgConfig.
     */
    public UniverseFactory getUniverseFactory() {
        URL url = getClass().getClassLoader().getSystemResource(
                        "etc/lg3d/displayconfig/"+lgConfig.getDisplayConfig());
        UniverseFactory ufi = new DefaultUniverseFactory(url);
                    
        return ufi;
    }

    public int getRmiPort() {
        return lgConfig.getRmiPort();
    }

    public int getLgServerPort() {
        return lgConfig.getLgServerPort();
    }

    public boolean isFullScreenAntiAliasing() {
        return lgConfig.isFullScreenAntiAliasing();
    }
    

}
