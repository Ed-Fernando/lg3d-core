
/**
 * Project Looking Glass
 *
 * $RCSfile: DefaultConfigControl.java,v $
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

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.classloader.LgClasspath;
import org.jdesktop.lg3d.scenemanager.config.ApplicationDescription;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.StartMenuItemConfig;
import org.jdesktop.lg3d.scenemanager.config.ConfigData;
import org.jdesktop.lg3d.scenemanager.config.ConfigurationCompleteEvent;
import org.jdesktop.lg3d.scenemanager.config.SceneManagerConfig;
import org.jdesktop.lg3d.scenemanager.glassy.GlassySceneManagerConfig;
import org.jdesktop.lg3d.scenemanager.SceneManager;

/**
 *
 * Reads/writes and distributes configuration information.
 *
 * @author  paulby
 */
public class DefaultConfigControl extends ConfigControl {
    
    private SceneManagerConfig sceneManagerConfig=null;    
    private ArrayList<ArrayList<ConfigData>> allConfigs = new ArrayList<ArrayList<ConfigData>>();


    /** Creates a new instance of ConfigControl */
    public DefaultConfigControl() {
        //createFile();

        String configPath = System.getProperty("lg.configpath");

        if (configPath!=null) {
            logger.warning("Config overridden by lg.configpath "+configPath);
            //TODO implement path for configpath
            if (configPath.startsWith("file"))
                processDirectory(scanDirectoryFromURL(configPath));
            else if (configPath.startsWith("resource:/")) {
                processDirectory(scanDirectoryFromResource("/etc/lg3d", this.getClass()));

                // TODO Treat lg.configpath as a path of resources instead of
                // hardcoding
                processDirectory(scanDirectoryFromResource("/config/demo", "org.jdesktop.lg3d.apps.help.Lg3dHelp"));
                processDirectory(scanDirectoryFromResource("/config/incubator", "org.jdesktop.lg3d.apps.bgmanager.BgManager"));
            } else if (configPath.startsWith("uibuilder")) {
                processDirectory(scanDirectoryFromResource("/org/jdesktop/lg3d/appkit/resource/etc/lg3dui", "org.jdesktop.lg3d.beans.internal.Lg3dConnector"));
            } else
                processDirectory(scanDirectory("../etc/lg3d-test"));
        } else {
            String configDir = System.getProperty("lg.etcdir")+File.separatorChar+"lg3d";
            if (configDir.startsWith("file"))
                processDirectory(scanDirectoryFromURL(configDir));
            else if (configDir.startsWith("resource:/"))
                processDirectory(scanDirectoryFromResource("/etc/lg3d", this.getClass()));
            else
                processDirectory(scanDirectory(configDir));
            processDirectory(scanDirectoryFromResource("/config/demo", "org.jdesktop.lg3d.apps.help.Lg3dHelp"));
            processDirectory(scanDirectoryFromResource("/config/incubator", "org.jdesktop.lg3d.apps.bgmanager.BgManager"));
        }

        // TODO search for configuration files in this order
        //        ~/.lg3d
        //        etc/lg3d/                  (actually this is in the jar)
        //        ../lg3d-demo-apps/config
        //        ../lg3d-incubator/config
	//

        try {    
            String str = System.getProperty("lg.appcodebase");
            if (str!=null) {
                URL appCodebase = new URL(str);
                processAppJars(appCodebase.getPath());
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    /** Config path syntax
     *
     * <URL>;<URL;
     *
     * URL = <protocol>:<path>
     *
     * protocol = [file|http|jar|resource]
     *
     * resource = RESOURCE:///  | RESOURCE://class/
    */
    
    // Break the configpath into individual config url strings
    private String[] tokenizeConfigPath(String configPath) {
        StringTokenizer tok = new StringTokenizer(configPath,";");
        String[] ret = new String[tok.countTokens()];
        int i=0;
        while(tok.hasMoreTokens())
            ret[i++] = tok.nextToken();
        return ret;
    }


    /**
     * process the jars that contain apps to see if they should
     * be added to the taskbar
     * @param dirname The name of the directory where the app jars are held
     */
    private void processAppJars(String dirname) {
        URL url = this.getClass().getClassLoader().getResource(dirname);
        File dir = null;
        if (url!=null) {
	    try {
		dir = new File(url.toURI());
	    } catch(Exception e) {
		logger.log(Level.SEVERE,"Error looking for configuration data",e);
		return;
	    }
	} else {
	    dir = new File( dirname );
	}

        logger.info("Reading Jars from "+dir);
        if ( dir == null ) return;
        logger.config("Reading Application Config from  directory "+dir.getAbsolutePath());

        if ( dir.exists() && dir.isDirectory() ) {
	    JarFilenameFilter filter = new JarFilenameFilter();
	    ArrayList<ConfigData> appjar = new ArrayList<ConfigData>();
            for (File f : dir.listFiles(filter) ) {
                logger.config("Reading Application Config from "+f.getName());
                if ( f.exists() ) {
                    try {
                        JarFile jar = new JarFile(f, true, JarFile.OPEN_READ);
                        Manifest man = jar.getManifest();
                        Attributes attr = man.getMainAttributes();

                        String name = attr.getValue("Implementation-Title");
                        String cfgfileAttr = attr.getValue("Config-File");

                        String classpath = attr.getValue("Class-Path");
                        if ( classpath == null ) {
                            classpath = "";
                        }
                        classpath = f.getName() + " " + classpath;
                        
                        if ( cfgfileAttr != null && cfgfileAttr.trim().length() > 0 ) {
                            String[] cfgfiles = cfgfileAttr.trim().split("\\;");
                            for ( String cfgfile : cfgfiles ) {
                                ZipEntry cfg = jar.getEntry(cfgfile);
                                if ( cfg != null ) {
                                    try {
                                        readConfig( appjar, f.toURI().toURL(), f.getParentFile(), classpath, jar.getInputStream(cfg) );
                                    } catch (Exception ex) {
                                        logger.log(Level.SEVERE, "Unable to load config file "+cfgfile + " from " + f.getPath(),ex);
                                    }
                                }
                            }
                        } else {
                            String mainclass = attr.getValue("Main-Class");

                            if ( mainclass != null && mainclass.trim().length() > 0 ) {
                                String icon = attr.getValue("Icon-Filename");
                                URL iconURL = null;
                                if ( icon != null ) { 
                                    ZipEntry entry = jar.getEntry(icon);
                                    // if the entry exists then create the URL
                                    if ( entry != null ) {
                                        String path = f.getPath().replace('\\', '/');
                                        if ( !path.startsWith("/") ) {
                                            path = "/" + path;
                                        }
                                        iconURL = new URL("jar:file://" + path + "!/" + icon);
                                    } 
                                    jar.close();
                                }
                                if ( iconURL == null ) {
                                    iconURL = this.getClass().getClassLoader().getResource("resources/images/icon/defaultapp.png");
                                }                                

                                StartMenuItemConfig appDesc = new StartMenuItemConfig();
                                appDesc.setName(name);
                                appDesc.setCommand("java " + mainclass);
//                                appDesc.setClasspath(new LgClasspath(LgClasspath.loadConfigList(f.getParentFile(), classpath)));
                                appDesc.setDisplayResourceType("ICON");
                                appDesc.setDisplayResourceUrl(iconURL);

                                appjar.add(appDesc);
                            }
                        }
                    } catch (IOException ioex) {
                        logger.log(Level.SEVERE,"Error loading configuration data ",ioex);
                    }
                }
            }
            allConfigs.add(appjar);
        }
    }

    private void processDirectory( URL[] urls ) {
        if (urls==null) return;

        for(URL url : urls) {
            try {
                ArrayList<ConfigData> configDirData = new ArrayList<ConfigData>();  // Config data from one directory;
                readConfig( configDirData, url);
                allConfigs.add(configDirData);
            } catch(Exception e) {
                logger.log(Level.SEVERE,"Error loading configuration data ",e);
            }
        }
    }

    /**
     * Return all the config files in the directory supplied.
     * The directory is located as a Resource, so must be in the
     * classpath
     */
    private URL[] scanDirectoryFromResource( String resourceDir, String className ) {
        try {
            return scanDirectoryFromResource(resourceDir, Class.forName(className));
        } catch(ClassNotFoundException cnfe) {
            logger.warning("Unable to locate class "+className+" in resourceDir "+resourceDir);

        }

        return null;
    }
    /**
     * Return all the config files in the directory supplied.
     * The directory is located as a Resource, so must be in the
     * classpath
     */
    private URL[] scanDirectoryFromResource( String resourceDir, Class clazz ) {

        File[] files = null;
        URL[] ret = null;

        if (clazz==null)
            return ret;

        try {
            URL url = clazz.getResource(resourceDir);
            if (url==null) {
                logger.config("Unable to get URL for resource "+resourceDir+" from ClassLoader for class "+clazz);
                return null;
            }

            logger.config("Class "+clazz+"\nURL "+url);
            
            if (url.getProtocol().equals("jar"))
                return scanDirectoryFromResourceJar(url);

            logger.config("Reading Config Resources from "+url.toExternalForm());
            
            assert(url.getProtocol().equalsIgnoreCase("file"));
            File f = new File(url.toURI());
            ConfigFilenameFilter filter = new ConfigFilenameFilter();

            files = f.listFiles(filter);
	    Arrays.sort(files, new FileComparator ());

            ret = new URL[files.length];
            for(int i=0; i<files.length; i++)
                ret[i] = files[i].toURI().toURL();
        } catch(Exception e) {
            logger.log(Level.SEVERE,"Error looking for configuration data",e);
        }

        return ret;
    }

    /**
     * The URL is in a Jar file, so scan the entries for
     * matching direcories and build the list of config file URLs
     */
    private URL[] scanDirectoryFromResourceJar( URL url ) {
        assert(url.getProtocol().equals("jar"));

        URL[] ret = null;

        logger.config("Loading config from Jar "+url.toExternalForm());
        try {
            JarURLConnection connection = (JarURLConnection)url.openConnection();
            JarFile jarFile = connection.getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();
            String urlStr = url.toExternalForm();
            String configDir = urlStr.substring(urlStr.lastIndexOf('!')+2, urlStr.length());
            ArrayList<URL> configURLs = new ArrayList<URL>();

            String urlPrefix = urlStr.substring(0,urlStr.lastIndexOf('!')+2);

            while(entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith(configDir) && entry.getName().endsWith("lgcfg")) {
                    URL cpurl = new URL(urlPrefix+entry.getName());
                    configURLs.add(cpurl);//new URL(urlPrefix+entry.getName()));
                }
            }

            ret = new URL[configURLs.size()];
            configURLs.toArray(ret);
	    Arrays.sort(ret, new URLComparator());

        } catch(IOException ioe) {
            logger.log(Level.SEVERE, "Unable to load Resource from Jar "+ioe.getMessage(), ioe);
        }

        return ret;
    }

    /**
     * Return all the config files in the directory supplied.
     */
    private URL[] scanDirectory( String dirName ) {
        File[] files = null;
        URL[] ret = null;

        if (dirName==null) {
            logger.severe("Unable to open config directory "+dirName);
            return null;
        }

        try {
            File dir = new File(dirName);

            if (!dir.isDirectory()) {
                logger.warning("Unable to scan directory for config files "+dir.getAbsoluteFile()+"  "+dirName);
                return ret;
            }

            logger.config("Reading Config from directory "+dir.getAbsolutePath());

            ConfigFilenameFilter filter = new ConfigFilenameFilter();

            files = dir.listFiles(filter);
	    Arrays.sort(files, new FileComparator ());

            ret = new URL[files.length];
            for(int i=0; i<files.length; i++)
                ret[i] = files[i].toURI().toURL();

        } catch(Exception e) {
            logger.log(Level.SEVERE,"Error looking for configuration data",e);
        }


        return ret;
    }

    private URL[] scanDirectoryFromURL( String urlStr ) {

        File[] files = null;
        URL[] ret = null;

        try {
            URL url;
            try {
                url = new URL(urlStr);
            } catch(MalformedURLException murl) {
                url = null;
                logger.severe("Unable to load config from url "+urlStr);
            }
            if (url==null) return null;

            if (url.getProtocol().equals("jar"))
                return scanDirectoryFromResourceJar(url);

            logger.config("Reading Config Resources from "+url.toExternalForm());
            File f = new File(url.toURI());
            ConfigFilenameFilter filter = new ConfigFilenameFilter();

            files = f.listFiles(filter);
	    Arrays.sort(files, new FileComparator ());

            ret = new URL[files.length];
            for(int i=0; i<files.length; i++)
                ret[i] = files[i].toURI().toURL();
        } catch(Exception e) {
            logger.log(Level.SEVERE,"Error looking for configuration data",e);
        }

        return ret;
    }
    
    /**
     * Return the Scene Manager
     */
    public SceneManager createSceneManager() {
        return sceneManagerConfig.createSceneManager();
    }
    
    /**
     * Process all the configuration data
     */
    public void processConfig() {
        for(ArrayList<ConfigData> dir : allConfigs) {
            for(ConfigData data : dir) {
                if (data != null && !data.isIgnoreConfig())
                    data.doConfig();
            }
        }
        logger.fine("Posting ConfigurationCompleteEvent");
        AppConnectorPrivate.getAppConnector().postEvent(new ConfigurationCompleteEvent(),null);
    }

    /** Dev method to create a sample file
     */
    private void createFile() {
        String[][] params = new String[][] {
            {"xterm", "resources/images/icon/system.png", "xterm -display :0" },
            {"help", "resources/images/icon/lg3d-help.png", "java org.jdesktop.lg3d.apps.help.Lg3dHelp" },
            {"cd", "resources/images/icon/CD-s.png", "java org.jdesktop.lg3d.apps.cdviewer.CDViewer"}
        };

        ArrayList<ConfigData> list = new ArrayList<ConfigData>();
        for(int i=0; i<params.length; i++) {
            ApplicationDescription app = new ApplicationDescription();
            app.setName(params[i][0]);
            app.setIconURL(this.getClass().getClassLoader().getResource(params[i][1]));
            app.setExec(params[i][2]);
            list.add(app);
        }
    }

    private void printList( ArrayList<ConfigData> list ) {
        for( ConfigData data : list )
            System.out.println(data.toString());
    }

    private void writeConfig( ArrayList<ConfigData> data, BufferedOutputStream out ) throws java.io.IOException {
        XMLEncoder encoder = new XMLEncoder(out);
        for( ConfigData o : data ) {
            encoder.writeObject(o);
        }
        encoder.close();
    }

    private void readConfig( ArrayList<ConfigData> data, URL url ) {
        if (url==null) {
            logger.warning("Config URL is null");
            return;
        }
        logger.config("Reading config "+url.toExternalForm());

	try {
            BufferedInputStream in = new BufferedInputStream( url.openStream() );
	    readConfig(data, url, null, null, in);
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Unable to load config file "+url.toExternalForm(),e);
        }
    }
    private void readConfig( ArrayList<ConfigData> data, URL baseURL, File baseFile, String classpath, InputStream in ) throws Exception {
        XMLDecoder decoder = new XMLDecoder(in);
        ConfigData cd;
        XMLHandler handler = new XMLHandler(baseURL.toExternalForm());
        boolean finished = false;
        decoder.setExceptionListener(handler);
        
        logger.info("Reading config file: " + baseURL + " classpath "+classpath);
        
        while(!finished) {
            try {
                cd = (ConfigData)decoder.readObject();
                logger.fine("Read "+cd);
                if (cd instanceof SceneManagerConfig &&
                    sceneManagerConfig==null) {
                    sceneManagerConfig = (SceneManagerConfig)cd;
                } else if (cd instanceof StartMenuItemConfig) {   
                    // Nothing to do
                    StartMenuItemConfig smic = ((StartMenuItemConfig)cd);
                    smic.createClassLoader();
                } else if ( cd instanceof ApplicationDescription ) {
                    ApplicationDescription ad = ((ApplicationDescription)cd);
                    LgClasspath lgclasspath = new LgClasspath(LgClasspath.loadConfigList(baseFile, classpath));
                    ad.setClasspath(lgclasspath);
                    URL iconURL = null;
                    if ( ad.getIconFilename() != null ) {
                        iconURL = lgclasspath.findResource(ad.getIconFilename());
                        if ( iconURL == null ) {
                            iconURL = this.getClass().getClassLoader().getResource(ad.getIconFilename());
                        }                    
                        ad.setIconURL(iconURL);
                    }
                }
                data.add( cd );
            } catch(ArrayIndexOutOfBoundsException aiobe ) {
                // We have read all the data.
                //aiobe.printStackTrace();
                finished = true;
            }
        }
        in.close();
    }

    class XMLHandler implements ExceptionListener {
        boolean finished = false;
        private String file;
        public XMLHandler(String file) {
            this.file = file;
        }
        
        public void exceptionThrown(Exception e) {
            logger.warning("Recoverable Error in config file "+file+ " Error : "+e);
            
         }
    }

    class ConfigFilenameFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            if (name.endsWith(".lgcfg"))
                return true;
            else
                return false;
        }
    }

    class JarFilenameFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
	    if ( name.endsWith(".jar") ) {
                return true;
            } else {
                return false;
            }
        }
    }

    class FileComparator implements Comparator {
      public int compare(Object o1, Object o2) {
	File f1 = (File)o1;
	File f2 = (File)o2;

	return f1.getName().compareTo(f2.getName());
      }
    }
    class URLComparator implements Comparator {
      public int compare(Object o1, Object o2) {
	URL u1 = (URL)o1;
	URL u2 = (URL)o2;

	return u1.toExternalForm().compareTo(u2.toExternalForm());
      }
    }
}
