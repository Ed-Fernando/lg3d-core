/**
 * Project Looking Glass
 *
 * $RCSfile: FileClassPath.java,v $
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
 * $Date: 2006-04-19 17:27:19 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.classloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * the ClasspathInfo for directories. It searches the classpath by checking if
 * the file can be found relative to this directory.
 * @author gameldar
 */
public class FileClassPath extends ClasspathInfo{
    /**
     * create a new file classpath given the URL
     * @param url the URL of the directory
     */
    public FileClassPath(URL url) {
        super(url);
    }
    /**
     * find the resource relative to the base directory
     * @param name the name of the file relative to the directory
     * @return the URL of the file, or null if it doesn't exist.
     * @throws IOException if the classpath is invalid
     */
    public URL findResource(String name) throws IOException {
        URL ret = null;
        
        try {
            /**  Commented out because it is a potential security issue!
             *   Plus it is not the standard java behaviour for getting
             *   resources - since it doesn't require the resource to be
             *   on the classpath
             *
             * File t = new File(name);
             * // check to see if it is an absolute path, if so then use it
             * // and don't worry about the base directory
             *
             * if ( t.getAbsolutePath().equals(name) ) {
             * if (t.exists() && t.canRead() ) ret = t.toURL();
             * }
             *
             * if ( ret == null ) {
             */
            // create the file reference
            File f = new File(new File(url.getPath()), name );
            // if it exists then create the URL reference from the file
            if ( f.exists() && f.canRead() ) ret = f.toURI().toURL();
            //}
        } catch (MalformedURLException murlex) {
            murlex.printStackTrace();
            ret = null;
        }
        
        return ret;
    }
}