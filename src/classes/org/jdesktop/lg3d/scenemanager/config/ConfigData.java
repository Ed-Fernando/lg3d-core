/**
 * Project Looking Glass
 *
 * $RCSfile: ConfigData.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-08-14 23:13:22 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.util.logging.Logger;
import org.jdesktop.lg3d.wg.event.LgEvent;

/**
 * Superclass of all configuration data
 *
 * @author  paulby
 */
public abstract class ConfigData extends LgEvent {
    
    protected Logger logger = Logger.getLogger("lg.config");
    
    private boolean ignore = false;
    
    /**
     * Default constructor is required for XMLEncoder/Decoder
     */
    public ConfigData() {    
    }
    
    public void setIgnoreConfig(boolean ignore) {
        this.ignore = ignore;
    }
    
    public boolean isIgnoreConfig() {
        return ignore;
    }
    
    /**
     * Do whatever is necessary to apply this configuration. In most
     * cases this will post this object as an event
     */
    public abstract void doConfig();

    protected URL[] parseClassPathJars(String classpathJars) throws IOException {
        ArrayList<URL> urls = new ArrayList();
        StringTokenizer tok = new StringTokenizer(classpathJars);
        String str=null;
        String nextJar=null;
        String appCodebase = System.getProperty("lg.appcodebase");

        while(tok.hasMoreTokens()) {            
	    URL url = null;
	    nextJar = tok.nextToken();

            try {
                str = new String(appCodebase + "/" + nextJar);
                url = new URL(str);
                try {                    
                    // Open a stream to check that the URL is valid
                    InputStream in = url.openConnection().getInputStream();
                    in.close();
		    logger.config("Adding URL "+url);
		    urls.add(url);
		    continue;

                } catch (IOException ex) {
                    // throw new IOException("Unable to locate jar "+url.toExternalForm());
                }
            } catch (MalformedURLException ex) {
                // logger.warning("Bad URL "+str);
                // ex.printStackTrace();
            }

	    logger.info("Unable to locate jar [" + nextJar + "] in [" + appCodebase + "].");
	    logger.info("Trying to open it directly...");

            try {
                url = new URL(nextJar);
                try {                    
                    // Open a stream to check that the URL is valid
                    InputStream in = url.openConnection().getInputStream();
                    in.close();
		    logger.config("Adding URL "+url);
		    urls.add(url);
		    continue;

                } catch (IOException ex) {
                    // throw new IOException("Unable to locate jar "+url.toExternalForm());
                }
            } catch (MalformedURLException ex) {
                // logger.warning("Bad URL "+str);
                // ex.printStackTrace();
            }

	    logger.warning("Unable to locate jar [" + nextJar + "] directly or in [" + appCodebase + "].");
        }
                
	return urls.toArray(new URL[urls.size()]);
    }
}
