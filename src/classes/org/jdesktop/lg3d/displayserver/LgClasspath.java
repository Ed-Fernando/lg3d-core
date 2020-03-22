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
 * $Revision: 1.5 $
 * $Date: 2006-08-14 23:13:19 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
    public LgClasspath(String propName, boolean reloadable) {
        // initialize the array list of classpaths
        urls = new ArrayList<ClassPath>();
        // initialize the hashtable of library classpath to jar url
        liburls = new Hashtable<String, ArrayList<URL>>();

        this.propName = propName;
        this.reloadable = reloadable;
// Commented out because if we initialize the classpath before the dependent
// classes are loaded we entered a recursive loop
//        if ( !"lg.classpath.config".equals(this.propName) ) {
//            loadConfig();
//        }
    }

    /**
     * Return if the classpath is initialized
     * @return true the classpath is initialized, false it isn't
     */
    public boolean isInitialized() {
        return initlcp;
    }

    /**
     * initialize the classpath (if it isn't currently being initialized
     */
    public void initialize() {
        if ( !initializing ) {
            initializing = true;
            // load in the classes from the config
            loadConfig();        
        }
    }
    
    /**
     * find the class on the classpath
     * @param name the name of the class (including the .class at the end)
     * @return the referenced class url
     */
    public ReferencedClassURL findClass(String name) {
        ReferencedClassURL rcurl = null;
        // loop through each classpath and see if the class is on it
        for ( ClassPath u: urls ) {
            URL url = u.findResource(name);
            // if the class is on the classpath then return the URL and
            // the classpath url as a referenced class
            if ( url != null ) {
                rcurl = new ReferencedClassURL(url, u.getURL());
                break;
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
        
        // loop through each classpath and see if the resource is on it
        for ( ClassPath u: urls ) {
            url = u.findResource(name);
            if ( url != null ) {
                break;
            }
        }
       
        return url;
    }
    
    /**
     * get the list of jar files on the given library path
     * @param libPath the library path to get the urls for
     * @return the list of jar URLs as an array list
     */
    public ArrayList<URL> getLibraryURLs(String libPath) {
        return liburls.get(libPath);
    }
    
    /**
     * load the classpath configuration using the <code>propName</code> to get
     * the location of the config file
     */
    private void loadConfig() {
        // get the location of the config file
        String etcDir = System.getProperty("lg.etcdir", "/etc");
        String cfname = System.getProperty(propName, etcDir+"/lg3d/classpath/classpath.cfg");
        
        BufferedReader br = null;
        try {
            
            // create a buffered read so we can read line by line
            br = new BufferedReader(
                    new FileReader(
                    new File(cfname)
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
                        addLibraryURL(basedir, fname);
                    } else {
                        addURL(new File(basedir, fname));
                    }
                }
            }
            br.close();
            // indicate that the initialization is complete and the classpath
            // is ready to handle resource finding requests
            initlcp = true;        
        } catch (IOException ioex) {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioex2) {
                    //
                }
            }
            ioex.printStackTrace();
        }
    }
    
    /**
     * Add the library URL to the classpath
     * @param url the URL of the library path
     */
    public void addLibraryURL(URL url) {
        File basedir = new File(url.getFile());
        addLibraryURL(basedir, ".");
    }
    
    /**
     * add all the jars in the directory to the classpath
     * @param basedir the base directory for the relative path
     * @param libPath the library path relative to the basedir
     */
    private void addLibraryURL(File basedir, String libPath) {
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
                    URL u = addURL(f);
                    // if the url was added then add the jar to the list of
                    // jars in the library path
                    if ( u != null ) {
                        jars.add(u);
                    }
                }
                // add the list of jars to the hashtable
                liburls.put(libPath, jars);
            }
        }
    }
    
    /**
     * add the file to the classpath
     * @param f the file to add
     * @return the URL of the classpath
     */
    private URL addURL(File f) {
        URL url = null;
        // if the file exists and can be read
        if ( f != null && f.exists() && f.canRead() ) {
            try {
                // get the file's URL reference
                url = f.toURI().toURL();
                
                // test that the url can be opened
                if ( reloadable && url.getProtocol().equals("jar")) {
                    JarURLConnection conn = (JarURLConnection)url.openConnection();
                    conn.setUseCaches(false);
                    conn.getInputStream().close();
                } else {
                    url.openStream().close();
                }
                // if it can be opened successfully then add it to the path
                addURL(url);
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
        ClassPath cp = null;
        // if it is a jar file then store the url as a JarClassPath
        // else store it as a File/Directory classpath
        if ( url.getPath().toLowerCase().endsWith(".jar") ) {
            try {
                cp = new JarClassPath(url);
            } catch (IOException ioe) {
                System.out.println("IOException while loading ClassPath");
                ioe.printStackTrace();
                return;
            } catch (URISyntaxException usex) {
                System.out.println("URISyntaxException while loading ClassPath");
                usex.printStackTrace();
                return;
            }
        } else {
            cp = new FileClassPath(url);
        }
        // add the classpath to the list
        urls.add(cp);
    }
    
    
    /**
     * the filter for jar files based on the filename
     */
    class JarFilenameFilter implements FilenameFilter {
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
    
    /**
     * the basic definition of all ClassPaths based on a URL
     */
    private abstract class ClassPath {
        /** the url of this classpath */
        protected URL url;
        /**
         * create a new classpath given the URL
         * @param url the URL of the classpath
         */
        public ClassPath(URL url) {
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
         */
        public abstract URL findResource(String name);
    }
    
    /**
     * the ClassPath for jar files. It searches the classpath by checking if
     * the file is in the index of the files of the jar file.
     */
    private class JarClassPath extends ClassPath{
        /**
         * create a new jar classpath with the given URL of the jar file
         * @param url the URL of the jar file
         */
        private JarFile jar;
        
        public JarClassPath(URL url) throws IOException, URISyntaxException {
            super(url);
            jar = new JarFile(new File(url.toURI()));
        }
        /**
         * find the file within the jar
         * @param name the relative path of the file
         * @return the URL of the file within the jar, or null if it isn't in it
         */
        public URL findResource(String name) {
            URL ret = null;
            try {
                // get the entry for the file in the jar
                ZipEntry entry = jar.getEntry(name);
                // if the entry exists then create the URL
                if ( entry != null ) {
                    ret = new URL("jar:file://" + url.getFile() + "!/" + name);
                }
                
//                ret.openStream().close();
            } catch (MalformedURLException murlex) {
//                murlex.printStackTrace();
                ret = null;
            } catch (IOException ioex) {
//                ioex.printStackTrace();
                ret = null;
            }
            
            return ret;
        }
    }

    /**
     * the ClassPath for directories. It searches the classpath by checking if
     * the file can be found relative to this directory.
     */
    private class FileClassPath extends ClassPath{
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
         */
        public URL findResource(String name) {
            URL ret = null;
            
            try {
                /**  Commented out because it is a potential security issue!
                 *   Plus it is not the standard java behaviour for getting
                 *   resources - since it doesn't require the resource to be
                 *   on the classpath
                 *
                File t = new File(name);
                // check to see if it is an absolute path, if so then use it
                // and don't worry about the base directory
                
                if ( t.getAbsolutePath().equals(name) ) {
                    if (t.exists() && t.canRead() ) ret = t.toURL();
                }
                
                if ( ret == null ) {
                */
                    // create the file reference
                    File f = new File(new File(url.toURI()), name );
                    // if it exists then create the URL reference from the file
                    if ( f.exists() && f.canRead() ) ret = f.toURI().toURL();
                //}
            } catch (URISyntaxException usex) {
                usex.printStackTrace();
                ret = null;
            } catch (MalformedURLException murlex) {
                murlex.printStackTrace();
                ret = null;
            } catch (IOException ioex) {
                ioex.printStackTrace();
                ret = null;
            }
            
            return ret;
        }
    }
    
    /** is the classpath initialized */
    private boolean initlcp = false;
    /** is the classpath currently being initialized */
    private boolean initializing = false;
    /** the list of Classpaths in the LgClassPath instance */
    private ArrayList<ClassPath> urls = null;
    
    /** the mapping of jars to a library path */
    private Hashtable<String, ArrayList<URL>> liburls = null;
    
    /** 
     * the name of the system property that contains the config file for this
     * LgClassPath
     */
    private String propName = null;
    
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
