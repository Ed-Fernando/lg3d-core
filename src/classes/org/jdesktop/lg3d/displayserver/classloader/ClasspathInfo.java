/**
 * Project Looking Glass
 *
 * $RCSfile: ClasspathInfo.java,v $
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

import java.io.IOException;
import java.net.URL;

/**
 * the basic definition of all ClassPaths based on a URL
 *
 * @author gameldar
 */
public abstract class ClasspathInfo {
    /** the url of this classpath */
    protected URL url;
    /**
     * create a new classpath given the URL
     * @param url the URL of the classpath
     */
    public ClasspathInfo(URL url) {
        this.url = url;
    }
    
    /**
     * get the URL of this classpath
     * @return the URL of this classpath
     */
    public URL getURL() {
        return url;
    }
    
    /**
     * get the stringified form of this classpath indicating the class
     * type of the classpath, plus the URL itself.
     * @return the stringified classpath
     */
    public String toString() {
        return this.getClass().getCanonicalName() + ": " + url.toString();
    }
    
    /**
     * find the resource on the given classpath
     * @param name the relative path of the resource
     * @return URL the URL of the resource, or null if not found
     * @throws IOException if the classpath element is invalid
     */
    public abstract URL findResource(String name) throws IOException;
}