/**
 * Project Looking Glass
 *
 * $RCSfile: ResourceURLConnection.java,v $
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
 * $Date: 2006-04-19 17:23:21 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import org.jdesktop.lg3d.displayserver.LgClassLoader;

/**
 *
 * @author paulby
 */
public class ResourceURLConnection extends URLConnection {
    
    private URLConnection resourceConnection = null;
    
    /** Creates a new instance of ResourceURLConnection */
    public ResourceURLConnection(URL url) {
        super(url);
    }

    public void connect() throws IOException {
        if (connected)
            return;
        ClassLoader classLoader=null;
        
        if (url.getHost()!=null && url.getHost().length()>0) {
            classLoader = LgClassLoader.getClassLoader(url.getHost());
            if (classLoader==null) {
                throw new RuntimeException("Unable to locate Classloader "+url.getHost()+" from resource "+url);
            }
//            System.out.println("--------------- found classloader "+url.getHost());
        } else {
            classLoader = getClass().getClassLoader();
        }
        
        try {
            // Trim the leading / of the resource name
            String res = url.getFile();
            URL resourceURL = classLoader.getResource(res.substring(1,res.length()));
            resourceConnection = resourceURL.openConnection();
        } catch(Exception e) {
            throw new IOException("Failed to connect to resource "+url+"\n"+e);
        }
    }
    
    public InputStream getInputStream() throws IOException { 
        try {
            if (resourceConnection==null)
                connect();
            return resourceConnection.getInputStream();
        } catch(IOException e) {
            System.out.println("Failed getInputStream for "+url);
            System.out.println("Classloader "+LgClassLoader.getClassLoader(url.getHost()));
                    
            throw(e);
        }
    }
    
    public OutputStream getOutputStream() throws IOException {
        if (resourceConnection==null)
            connect();
        return resourceConnection.getOutputStream();
    }
    
}
