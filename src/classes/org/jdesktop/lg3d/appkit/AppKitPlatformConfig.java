/**
 * Project Looking Glass
 *
 * $RCSfile: AppKitPlatformConfig.java,v $
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
 * $Date: 2007-01-04 22:40:14 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.appkit;

import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.PlatformConfig;
import org.jdesktop.lg3d.displayserver.UniverseFactory;

/**
 * The Default Appkit PlatformConfig class. To change which PlatformConfig is
 * use the property lg.platformConfigClass eg
 * System.setProperty("lg.platformConfigClass", "org.jdesktop.lg3d.appkit.AppKitPlatformConfig");
 *
 * @author paulby
 */
public class AppKitPlatformConfig extends PlatformConfig {
    
    public String getFoundationWinSys() {
        return "org.jdesktop.lg3d.displayserver.fws.awt.WinSysAWT";
    }
    
    public String getNativeWinIntegration() {
        return null;
    }
    
    public String getNativeWinLookAndFeel() {
        return null;
    }
    
    public boolean isClientServer() {
        return false;
    }
    
    public UniverseFactory getUniverseFactory() {
        return new org.jdesktop.lg3d.appkit.AppKitUniverseFactory();
    }
    
    public int getRmiPort() {
        return 44817;
    }
    
    public int getLgServerPort() {
        return 44816;
    }
    
    public boolean isFullScreenAntiAliasing() {
        return false;
    }
    
    public void logConfig(Logger logger) {
        logger.config("Using AppKitConfig");
    }
    
}
