/**
 * Project Looking Glass
 *
 * $RCSfile: JarClassPath.java,v $
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
 * $Date: 2006-04-19 17:27:20 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.classloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * the ClasspathInfo for jar files. It searches the classpath by checking if
 * the file is in the index of the files of the jar file.
 */
public class JarClassPath extends ClasspathInfo{
    /**
     * create a new jar classpath with the given URL of the jar file
     * @param url the URL of the jar file
     */
    public JarClassPath(URL url) {
        super(url);
    }
    /**
     * find the file within the jar
     * @param name the relative path of the file
     * @return the URL of the file within the jar, or null if it isn't in it
     */
    public URL findResource(String name) {
        URL ret = null;
        try {
            // create a new JarFile reference to the url
            JarFile jar = new JarFile(new File(url.getFile()));
            
            // get the entry for the file in the jar
            ZipEntry entry = jar.getEntry(name);
            // if the entry exists then create the URL
            if ( entry != null ) {
                ret = new URL("jar:file://" + url.getFile() + "!/" + name);
            }
            jar.close();
        } catch (MalformedURLException murlex) {
            ret = null;
        } catch (IOException ioex) {
            ret = null;
        }
        
        return ret;
    }
}
