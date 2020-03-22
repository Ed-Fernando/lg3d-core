/**
 * Project Looking Glass
 *
 * $RCSfile: DefaultUniverseFactory.java,v $
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
 * $Date: 2007-01-04 22:40:17 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

import com.sun.j3d.utils.universe.ConfigContainer;
import com.sun.j3d.utils.universe.ConfiguredUniverse;
import java.net.URL;
import java.util.logging.Logger;

/**
 *
 * @author paulby
 */
public class DefaultUniverseFactory extends UniverseFactory {
    
    private Logger logger = Logger.getLogger("lg.displayserver");
    private URL defaultConfigURL;
    
    DefaultUniverseFactory(URL defaultConfigURL) {
        this.defaultConfigURL = defaultConfigURL;
    }

    /**
     * Create the Universe, if configURL is null the config set in the constructor 
     * will be used.
     */
    public ConfiguredUniverse createUniverse(URL configURL) {        
        if (configURL==null) {
            configURL = this.defaultConfigURL;
            logger.config("Using display configuration "+configURL);
        }

        ConfigContainer universeConfig = new ConfigContainer( configURL, true, 1 );
        logger.info(universeConfig.toString());
        // create the configured universe
        return new com.sun.j3d.utils.universe.ConfiguredUniverse(universeConfig);
    }
        
}
