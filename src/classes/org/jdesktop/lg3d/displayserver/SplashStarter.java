/**
 * Project Looking Glass
 *
 * $RCSfile: SplashStarter.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2007-07-24 16:43:45 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver;

import java.io.*;
import java.beans.XMLDecoder;
import java.net.URL;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * A simple thread, launching the SplashScreen.
 *
 * @author pinaraf
 */
public class SplashStarter extends Thread {
    
    private static Logger logger = Logger.getLogger("lg.displayserver");
    
    public SplashStarter() {
        super("LG-splashstarter");
    }
    
    public void run() {
        
        SplashConfig scfg = new SplashConfig();
        
        InputStream in=null;
        String etcPath = System.getProperty("lg.etcdir", "../etc/");
        String configFilePath = etcPath + "lg3d/SplashDefaults.xml";
        try {            
            if (configFilePath.startsWith("http") || configFilePath.startsWith("file:")) {
                URL url = new URL(configFilePath);
                url.openConnection();
                in = url.openStream();
            } else {
                try {
                    File f = new File(configFilePath);
                    in = new FileInputStream(f);
                } catch(IOException ioe) {
                    logger.config("Splashscreen config file not found: " + configFilePath);
                    return;
                }
            }
        
            XMLDecoder rdr = new XMLDecoder(new BufferedInputStream(in));

            scfg = (SplashConfig)rdr.readObject();
            rdr.close();
            
            if (scfg.runSplash == true) {
                SplashWindow.showSplashscreen(scfg.imageLocation);
                if (scfg.showText == true) {
                    if (scfg.messageText==null)                        
                        SplashWindow.showMessage( new String[] 
                                { LgBuildInfo.getBuildType()+" "+LgBuildInfo.getVersion(),
                                  LgBuildInfo.getBuildDate()
                                });
                    else
                        SplashWindow.showMessage(scfg.messageText);
                }
            }

        } catch (IOException ioea) {
            logger.log(Level.WARNING,"Unable to read SplashScreen config.",ioea);
        }
    }
    
}
