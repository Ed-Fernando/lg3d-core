/**
 * Project Looking Glass
 *
 * $RCSfile: LgClassLoader.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2006-04-19 17:23:20 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.WeakHashMap;

/**
 * The LgClassLoader allows the reloading of classes that run within
 * the application space. It defines two types of class paths, non-reloadable
 * and reloadable classpaths, and there is two subsequent caches for
 * loaded classes. The non-reloadable classes are stored in a static reference
 * that is utilized by all instances of the LgClassLoader. Reloadable classes
 * are stored in a local instance and therefore the data is reloaded with each
 * new instance of the LgClassLoader.
 *
 * Credit is due to the author(s) of the Escher ReloadableClassPath which set
 * me on the right path.
 *
 * @author gameldar
 */
public class LgClassLoader extends URLClassLoader {
    
    private static int nextID=0;
    private static WeakHashMap<String, LgClassLoader> classLoaders = new WeakHashMap();
    private String id;
    
    public LgClassLoader(URL[] urls, String uniqueID, ClassLoader parent) {
        super(urls, parent);
        classLoaders.put(uniqueID, this);
        id = uniqueID;
//        System.out.println("Creating ClassLoader with ID "+uniqueID);
    }
    
    /**
     * Generate a unqie classloader id
     */
    public static String generateUniqueID() {
        return Integer.toString(nextID++);
    }
    
    /**
     * Return the class loader with the unique id, or null if it does
     * not exist
     */
    public static LgClassLoader getClassLoader(String uniqueID) {
        return classLoaders.get(uniqueID);
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer(super.toString()+" ID = "+id);
        buf.append("\nClasspath = ");
        URL[] urls = getURLs();
        for(int i=0; i<urls.length; i++)
            buf.append(urls[i].toExternalForm()+" : ");
        return buf.toString();
    }
    
    /** the non-reloadable classpath */
//    protected LgClasspath lgClasspath = null;
//    
//    private boolean initialized = false;
//    
//    static final boolean classLoaderDebug = false;
//    
//    /**
//     * create a new LgClassLoader with a null parent
//     */
//    public LgClassLoader() {
//        this(null, null);
//    }
//    
//    public LgClassLoader(ClassLoader parent) {
//        this(parent, null);
//    }
//    /**
//     * create a new LgClassLoader
//     * @param parent the parent classloader
//     */
//    public LgClassLoader(ClassLoader parent, LgClasspath classpath) {
//        super(parent);
//        
//        // if there isn't a lgClasspath then create one
//        if ( lgClasspath == null && classpath != null ) {
//            lgClasspath = classpath;
//        } else {
//            lgClasspath = new LgClasspath(null);
//        }
//        
//    }
//    
//    
//    public LgClasspath getClasspath() {
//        return lgClasspath;
//    }
//    
//    protected Class findClass(String name) throws ClassNotFoundException {
//        Class ret=null;
//        
//        ReferencedClassURL rcurl = null;
//        
//        // find the class on the non-reloadable classpath
//        rcurl = lgClasspath.findClass(name.replaceAll("\\.", "/") + ".class");
//        
//        // if the url for the class was found
//        if ( rcurl != null ) {
//            // get the class data from the url
//            URL u = rcurl.getClassURL();
//            try {
//                byte[] classdata = null;
//                URLConnection conn = u.openConnection();
//                ((JarURLConnection)conn).setUseCaches(false);
//                DataInputStream is = new DataInputStream(conn.getInputStream());
//                
//                // if the byte available is not defined then read 1024 bytes
//                // at a time
//                if ( is.available() <= 0 ) {
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    int bc = 0;
//                    byte[] buf = new byte[1024];
//                    while ( (bc = is.read(buf)) > 0 ) {
//                        baos.write(buf);
//                        if ( bc < 1024) break;
//                    }
//                    baos.close();
//                    classdata = baos.toByteArray();
//                } else {
//                    // read the full bytes
//                    classdata = new byte[is.available()];
//                    is.readFully(classdata);
//                }
//                
//                is.close();
//                
//                try {
//                    // define the class using the static class loader instance
//                    ret = defineClass(name, classdata, 0, classdata.length);
//                } catch ( java.lang.LinkageError err) {
//                    System.err.println("Linkage error loading: " + name);
//                    
//                }
//            } catch (IOException ioex) {
//                System.err.println("Failed to load class: " + name);
//                ioex.printStackTrace();
//            }
//        }
//        if ( ret == null ) {
//            throw new ClassNotFoundException(name);
//        }
//        return ret;
//    }
//    
//    /**
//     * find the resource on the classpath
//     * @param name the name/reference of the resource
//     * @return the URL of the resource
//     */
//    public URL findResource(String name) {
//        URL ret = null;
//        
//        // if the resource isn't on the reloadable classpath
//        // then check on the non-reloadable resources
//        if ( ret == null ) {
//            ret = lgClasspath.findResource(name);
//        }
//        
//        // if the resource still can be found use
//        // the system/boot classloader to find the resource
//        if ( ret == null) {
//            ret = super.findResource(name);
//        }
//        
//        return ret;
//    }
//    
//    /**
//     * Add a library path to the classpath, specifying if it is reloadable
//     * @param url the URL of the library to add
//     */
//    public void addLibraryURL(URL url) {
//        lgClasspath.addLibraryURL(url);
//    }
//    
//    /**
//     * Add the url to the classpath, specifying if it is reloadable
//     * @param url the URL to add
//     */
//    public void addURL(URL url) {
//        lgClasspath.addURL(url);
//    }
//    
//    
//    
//    /**
//     * a referenced class, that keeps track of the classpath where
//     * the class was loaded from
//     */
//    private class ReferencedClass {
//        // the loaded class
//        private Class classDef = null;
//        // the url of the classpath
//        private URL classPath = null;
//        /**
//         * contruct a referenced class
//         * @param classDef the loaded class
//         * @param classPath the URL of the classpath
//         */
//        public ReferencedClass(Class classDef, URL classPath) {
//            this.classDef = classDef;
//            this.classPath = classPath;
//        }
//        
//        /**
//         * get the class
//         * @return the loaded class
//         */
//        public Class getClassDef() {
//            return classDef;
//        }
//        /**
//         * get the classpath
//         * @return the classpath URL
//         */
//        public URL getClassPath() {
//            return classPath;
//        }
//    }
}
