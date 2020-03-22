/**
 * Project Looking Glass
 *
 * $RCSfile: LgClasspath.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-08-15 19:07:02 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.classloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;



/**
 * The classpath for lg3d. It handles the adding of urls to the classpath
 * and reads from a default config file which specifies two types of urls:
 * <ol>
 * <li><code>dir:</code> The directory is added as a source for classes.</li>
 * <li><code>lib:</code> The directory is a library and all jar files within
 * the directory are added to the classpath as individual urls.</li>
 * </ol>
 * The urls are cached for later reference according to their type, as a jar
 * or as a directory reference. Then when resources are required (class or 
 * other resources) the specific classpath type is used to locate the resource.
 *
 * Jars within library classpath urls are indexed in order to allow reloading
 * of the library paths.
 *
 * @author gameldar
 */
public class LgClasspath {
    
    /** 
     * Creates a new instance of LgClasspath
     * @param propName the name of the property to use for the location of the
     * config file
     */
    public LgClasspath(ArrayList<ClasspathInfo> urls) {
        // initialize the array list of classpaths
        this.urls = urls;
    }
    
    public void initialize() {
        if ( urls == null ) {
            urls = loadConfigFile(System.getProperty("lg.classpath.config"));
        }
    }

    /**
     * find the class on the classpath
     * @param name the name of the class (including the .class at the end)
     * @return the referenced class url
     */
    public ReferencedClassURL findClass(String name) {
        ReferencedClassURL rcurl = null;
       
        initialize();
        // loop through each classpath and see if the class is on it
        for ( ClasspathInfo u: urls ) {
            try {
                URL url = u.findResource(name);

                // if the class is on the classpath then return the URL and
                // the classpath url as a referenced class
                if ( url != null ) {
                    rcurl = new ReferencedClassURL(url, u.getURL());
                    break;
                }
            } catch (IOException ioex) {
                urls.remove(u);
            }
        }
        
        return rcurl;
    }
    
    /**
     * find the resource on the classpath
     * @param name the name of the resource
     * @return the URL of the resource
     */
    public URL findResource(String name) {
        URL url = null;
        
        initialize();
        // loop through each classpath and see if the resource is on it
        for ( ClasspathInfo u: urls ) {
            try {
                url = u.findResource(name);
            
                if ( url != null ) {
                    break;
                }
            } catch (IOException ioex) {
                urls.remove(u);
            }
        }
       
        return url;
    }

    /**
     * load the classpath configuration from the urls listed
     * @param basedir the base directory the url references
     * @param urlList the space separated relative classpath list
     */
    public static ArrayList<ClasspathInfo> loadConfigList(File basedir, String urlList) {
        ArrayList<ClasspathInfo> ret = new ArrayList<ClasspathInfo>();
        
        if ( urlList != null ) {
            String[] fname = urlList.split("\\ ");
            for ( int i = 0; i < fname.length; i++ ) {
                File f = new File(basedir, fname[i]);
                addURL(ret, f);
            }
        }
        
        return ret;
    }
    
    /**
     * load the classpath configuration using the given config file
     * @param cfgFile the configuration file
     */
    public static ArrayList<ClasspathInfo> loadConfigFile(String cfgFile) {
        
        ArrayList<ClasspathInfo> ret = new ArrayList<ClasspathInfo>();
        BufferedReader br = null;
        try {
            
            // create a buffered read so we can read line by line
            br = new BufferedReader(
                    new FileReader(
                    new File(cfgFile)
                    ));
            String line = null;
            
            // set the basedir as the lg.dir directory
            File basedir = new File(System.getProperty("lg.dir"), ".");
            
            // while there are lines in the config read them in
            while ( (line = br.readLine()) != null ) {
                // comments are allowed starting with #
                if ( !line.trim().startsWith("#") ) {

                    // split the line into its parameters
                    String[] param = line.split("\\:");
                    boolean lib = false;
                    String fname = null;
                    // if there is a : then the type is defined
                    if ( param.length == 2) {
                        // if it starts with 'lib' then it is a library path
                        fname = param[1];
                        if ( "lib".equals(param[0].toLowerCase()) ) {
                            lib = true;
                        }
                    } else {
                        // if there isn't a : then it is a directory path
                        fname = line;
                    }
                    // add the classpath using the appropriate handler
                    if ( lib ) {
                        addLibraryURL(ret, basedir, fname);
                    } else {
                        addURL(ret, new File(basedir, fname));
                    }
                }
            }
            // indicate that the initialization is complete and the classpath
            // is ready to handle resource finding requests  
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException ioex2) {
                //
            }
        }
        return ret;
    }
    
    /**
     * Add the library URL to the classpath
     * @param url the URL of the library path
     */    
    public void addLibraryURL(URL url) {
        addLibraryURL(urls, url);
    }
    
    /**
     * Add the library URL to the given classpath
     * @param urls the Classpath list to add the URL to
     * @param url the URL of the library path
     */
    public static void addLibraryURL(ArrayList<ClasspathInfo> urls, URL url) {
        File basedir = new File(url.getFile());
        addLibraryURL(urls, basedir, ".");
    }
    
    /**
     * add all the jars in the directory to the given classpath
     * @param urls the Classpath list to add the URL to
     * @param basedir the base directory for the relative path
     * @param libPath the library path relative to the basedir
     */
    private static void addLibraryURL(ArrayList<ClasspathInfo> urls, File basedir, String libPath) {
        // if there is a libPath defined
        if ( libPath != null ) {
            // open the directory
            File dir = new File(basedir, libPath);
            // if it exists and is a directory
            if ( dir.exists() && dir.isDirectory() ) {
                ArrayList<URL> jars = new ArrayList<URL>();
                
                // filter the files in the directory for jar files
                for( File f : dir.listFiles(new JarFilenameFilter()) ) {
                    // add the jar file to the classpath
                    URL u = addURL(urls, f);
                }
            }
        }
    }
    
    /**
     * add the file to the given classpath
     * @param urls the Classpath list to add the URL to
     * @param f the file to add
     * @return the URL of the classpath
     */
    private static URL addURL(ArrayList<ClasspathInfo> urls, File f) {
        URL url = null;
        // if the file exists and can be read
        if ( f != null && f.exists() && f.canRead() ) {
            try {
                // get the file's URL reference
                url = f.toURI().toURL();
                
                // test that the url can be opened
                if ( url.getProtocol().equals("jar")) {
                    JarURLConnection conn = (JarURLConnection)url.openConnection();
                    conn.setUseCaches(false);
                    conn.getInputStream().close();
                } else {
                    url.openStream().close();
                }
                // if it can be opened successfully then add it to the path
                addURL(urls, url);
            } catch (MalformedURLException murlex) {
                murlex.printStackTrace();
                url = null;
            } catch (IOException ioex) {
                ioex.printStackTrace();
                url = null;
            }
        }
        return url;
    }
    
    /**
     * add the URL to the classpath list
     * @param url the URL to add
     */
    public void addURL(URL url) {
        addURL(urls, url);
    }
    /**
     * add the URL to the given classpath list
     * @param urls the Classpath list to add the URL to
     * @param url the URL to add
     */    
    private static void addURL(ArrayList<ClasspathInfo> urls, URL url) {
        ClasspathInfo cp = null;
        // if it is a jar file then store the url as a JarClassPath
        // else store it as a File/Directory classpath
        if ( url.getPath().toLowerCase().endsWith(".jar") ) {
            cp = new JarClassPath(url);
        } else {
            cp = new FileClassPath(url);
        }
        // add the classpath to the list
        urls.add(cp);
    }
    
    
    /**
     * the filter for jar files based on the filename
     */
    static class JarFilenameFilter implements FilenameFilter {
        /**
         * accept only jar files
         * @param dir the directory the file is in
         * @param name the name of the file to check
         * @return true if it is a jar file, else false
         */
        public boolean accept(File dir, String name) {
            // if the filename is a .jar file then add it as a jar
            if ( name.toLowerCase().endsWith(".jar") ) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    


    
    /** is the classpath initialized */
    private boolean initlcp = false;
    /** is the classpath currently being initialized */
    private boolean initializing = false;
    /** the list of Classpaths in the LgClassPath instance */
    private ArrayList<ClasspathInfo> urls = null;
    
    /** if the classpath is reloadable */
    private boolean reloadable = false;
    
    /**
     * a referenced class URL, tracking the class to its classpath
     */
    public class ReferencedClassURL { 
        /** the URL of the class */
        private URL classURL;
        /** the URL of the classpath */
        private URL classpathURL;
        
        /**
         * create a new ReferencedClassURL
         * @param classURL the URL of the class
         * @param classpathURL the URL of the classpath
         */
        public ReferencedClassURL (URL classURL, URL classpathURL) {
            this.classURL = classURL;
            this.classpathURL = classpathURL;
        }
        
        /**
         * get the URL of the Class
         * @return the URL of the Class
         */
        public URL getClassURL() {
            return classURL;
        }
        
        /**
         * get the URL of the Classpath
         * @return the URL of the Classpath
         */
        public URL getClasspathURL() {
            return classpathURL;
        }
    }
}
