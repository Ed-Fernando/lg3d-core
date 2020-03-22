/**
 * Project Looking Glass
 *
 * $RCSfile: ApplicationDescription.java,v $
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
 * $Revision: 1.15 $
 * $Date: 2006-12-18 18:33:57 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.config;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.net.URLClassLoader;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.displayserver.LgClassLoader;
import org.jdesktop.lg3d.displayserver.classloader.LgClasspath;
import org.jdesktop.lg3d.scenemanager.utils.taskbar.TaskbarItemConfig;
import org.jdesktop.lg3d.utils.component.Pseudo3DShortcut;
import org.jdesktop.lg3d.utils.component.SwichablePseudo3DShortcut;
import org.jdesktop.lg3d.wg.Tapp;


/**
 * Describes and registers an application with lg3d
 *
 * @author  paulby
 */
public class ApplicationDescription extends ConfigData {

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Holds value of property description.
     */
    private String description=null;

    /**
     * Holds value of property mainClass.
     */
    private String mainClass;
    
    /**
     * Holds value of property classpath.
     */
    private LgClasspath classpath;

    /**
     * Holds value of property constructorTypes.
     */
    private Class[] constructorTypes;

    /**
     * Holds value of property constructorData.
     */
    private Object[] constructorData;

    /**
     * Holds value of property iconFilename.
     */
    private String iconFilename;

    /**
     * Holds value of the property iconURL
     */
    private URL iconURL;
    
    /**
     * Holds value of property MouseEneterdFilename.
     */
    private URL mouseEnteredFilename;
    
    /**
     * Holds value of property enabled.
     */
    private boolean enabled; 
    
    private URLClassLoader appClassLoader = null;
    private String classLoaderID;

    /**
     * Holds value of property exec.
     */
    private String exec;
    
    private String[] alternateExec = null;
    
    private static boolean isSolaris = false;

    static {
	String osName = System.getProperty("os.name");
	isSolaris = osName.equals("SunOS");
    }

    /** Creates a new instance of ApplicationDescription */
    public ApplicationDescription() {
        classLoaderID = LgClassLoader.generateUniqueID();
    }

    public void setClasspathJars(String classpathJars) throws IOException {
        appClassLoader = new LgClassLoader(
				parseClassPathJars(classpathJars),
				classLoaderID, this.getClass().getClassLoader());
    }
    
    String getClasspathJars() {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Get the name of the application. By convention the name should be
     * relatively short.
     *
     * TODO how should we deal with non unique names
     * TODO should we have a flag requesting that the name is not displayed ?
     *
     * @return Value of property name.
     */
    public String getName() {

        return this.name;
    }

    /**
     * Set the name of the application. By convention the name should be
     * relatively short.
     * @param name New value of property name.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Get a description of the app, may be null;
     * @return Value of property description.
     */
    public String getDescription() {

        return this.description;
    }

    /**
     * Set the description of the app.
     * @param description New value of property description.
     */
    public void setDescription(String description) {

        this.description = description;
    }

    /**
     * Get the fully qualified main class name of the application
     *
     * TODO for the moment we assume this is in the classpath, later
     * we will need methods for the app to specify it's own classpath
     *
     * @return Value of property mainClass.
     */
    public String getMainClass() {

        return this.mainClass;
    }

    /**
     * Setter for property mainClass.
     * @param mainClass New value of property mainClass.
     */
    public void setMainClass(String mainClass) {

        this.mainClass = mainClass;
    }

    /**
     * Get the full classpath for the application (excluding system classes)
     *
     * @return Value of property classpath.
     */
    public LgClasspath getClasspath() {

        return this.classpath;
    }

    /**
     * Setter for property classpath.
     * @param classpath New value of property classpath.
     */
    public void setClasspath(LgClasspath classpath) {

        this.classpath = classpath;
    }
    
    /**
     * Get the name of the file containing the apps icon, this will be
     * used in a getResource call so the name should be relative to the
     * apps classpath.
     *
     * @return Value of property iconFilename.
     */
    public String getIconFilename() {

        return this.iconFilename;
    }
    
    /**
     * Get the name of the file containing the apps icon, this will be
     * used in a getResource call so the name should be relative to the
     * apps classpath.
     *
     * @return Value of property iconFilename.
     */    
    public URL getIconURL() {
        return this.iconURL;
    }
    
     /**
     * Get the name of the file containing the apps icon when mouse is entered,
     * this will be used in a getResource call so the name should be relative 
     * to the apps classpath.
     *
     * @return Value of property iconFilename.
     */
    
    public URL getMouseEnteredFilename() {

        return this.mouseEnteredFilename;
    }
    
    /**
     * Setter for property iconFilename.
     * @param iconFilename New value of property iconFilename.
     */
    public void setIconFilename(String iconFilename) {

        this.iconFilename = iconFilename;
    }
    
    /**
     * Setter for the property iconURL.
     * @param iconURL new value of property iconURL
     */
    public void setIconURL(URL iconURL) {
        this.iconURL = iconURL;
    }
    
    /**
     * Setter for the property iconURL.
     * @param iconURL new value of property iconURL
     */
    public void setIconURL(String iconURLName) {
        try {
            URL url = new URL(iconURLName);
            if (url.getProtocol().equalsIgnoreCase("resource")) {
                url = new URL(url.getProtocol(), classLoaderID, url.getPath());
            }
            this.iconURL = url;
        } catch (MalformedURLException ex) {
            logger.warning("Malformed URL for iconURL : "+iconURL);
        }
    }
    
    /**
     * Setter for property mouseEnterdIconFilename.
     * @param mouseEnterdIconFilename New value of property mouseEnterdIconFilename.
     */
    
     public void setMouseEnteredFilename(URL mouseEnteredFilename) {

        this.mouseEnteredFilename = mouseEnteredFilename;
    }

//    /**
//     * Getter for property constructorData.
//     * @return Value of property constructorData.
//     */
//    public Object[] getConstructorData() {
//
//        return this.constructorData;
//    }
//
//    /**
//     * Setter for property constructorData.
//     * @param constructorData New value of property constructorData.
//     */
//    public void setConstructorData(Object[] constructorData) {
//
//        this.constructorData = constructorData;
//    }

//    /**
//     * Getter for property constructorTypes.
//     * @return Value of property constructorTypes.
//     */
//    public Class[] getConstructorTypes() {
//
//        return this.constructorTypes;
//    }
//
//    /**
//     * Setter for property constructorTypes.
//     * @param constructorTypes New value of property constructorTypes.
//     */
//    public void setConstructorTypes(Class[] constructorTypes) {
//
//        this.constructorTypes = constructorTypes;
//    }
    
    public String toString() {
        return "AppDesc : "+name;
    }

    /**
     * Returns false if the app should not 
     * be available to users within GUI.
     *
     * @return Value of property enabled.
     */
    public boolean isEnabled() {

        return this.enabled;
    }

    /**
     * Setter for property enabled.
     * @param enabled New value of property enabled.
     */
    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }

    public void doConfig() {
        if (isApplicationAvailable(getExec())) {
            AppConnectorPrivate.getAppConnector().postEvent(this, null);

            ApplicationTaskbarItemConfig taskbarItem = new ApplicationTaskbarItemConfig();        
            taskbarItem.doConfig();
        } else {
            String[] alts = getAlternateExec();
            if (alts!=null) {
                for(int i=0; i<alts.length; i++) {
                    if (isApplicationAvailable(alts[i])) {
                        setExec(alts[i]);
                        i = alts.length;
                        AppConnectorPrivate.getAppConnector().postEvent(this, null);

                        ApplicationTaskbarItemConfig taskbarItem = new ApplicationTaskbarItemConfig();        
                        taskbarItem.doConfig();
                    }
                }
            } else
                logger.warning("Executable "+getExec()+" not found, ignoring taskbar item "+getName());
        } 
            
    }
    
    private static class ExactFileNameMatchFilter implements FilenameFilter {
	String nameToMatch;

	ExactFileNameMatchFilter (String s) {
	    nameToMatch = s;
	}

	public boolean accept(File dir, String name) {
	    return nameToMatch.equals(name);
	}
    }

    /**
     * Checks if the application is runnable.
     *
     * If exec does not start with java then this method checks if the command
     * can be found on the system.
     *
     * Returns true if the app is available.
     */
    public static boolean isApplicationAvailable(String exec) {
        if (exec.startsWith("java"))
            return true;
        
        int end = exec.indexOf(' ');    // Somewhat evil, no space allowed in path or filename
        if (end==-1) 
            end = exec.length();
        String command = exec.substring(0, end);
        
        if (command.indexOf(File.separatorChar)!=-1) {
            // Command contains / so assume it is a path, check if the file is
            // available
            File f = new File(command);
            if (f.isFile())
                return true;
            else
                return false;
        } else {        
	    if (isSolaris) {
		
		// Solaris case
		//
		// NOTE: forking a which command on Solaris causes occasional
		// hangs on startup (see bug 700). So use a different technique
		// to check the path on Solaris.

		String pathEnv = System.getenv("PATH");
		StringTokenizer st = new StringTokenizer(pathEnv, ":");
		while (st.hasMoreTokens()) {
		    String dirName = st.nextToken();
		    File dir = new File(dirName);
		    if (dir == null) continue;
		    ExactFileNameMatchFilter filter = new ExactFileNameMatchFilter(command);
		    File[] files = dir.listFiles(filter);
		    if (files == null || files.length == 0) {
			continue;
		    } else if (files.length == 1) {
			return true;
		    } else {
			throw new RuntimeException("Invalid number of file name filter matches, numMatches = " +  files.length);
		    }
		}

	    } else {

		// Execute a which on the command to see if it is in the path, obviously
		// unix specific
		Process p;
		try {
		    p = new ProcessBuilder("which", command).start();
		    p.waitFor();
		    discardProcessStreamContents(p);
		    if (p.exitValue()==0) {
			//System.out.println("** App Available "+exec);
			return true;
		    }
		    //System.out.println("** No such App "+exec);
		} catch (IOException ex) {
		    return false;
		} catch (InterruptedException ex) {
		    return false;
		}       
	    }
	}

        return false;
   }

    // Discards any data on the processes stdout and stderr streams
    private static void discardProcessStreamContents (Process p) {
	discardStreamContents(p.getInputStream());
	discardStreamContents(p.getErrorStream());
    }

    private static void discardStreamContents (InputStream s) {
	byte[] dummyBuf = new byte[64];
	int numBytesRead;

	do {
	    try {
		numBytesRead = s.read(dummyBuf);
	    } catch (IOException e) {
		return;
	    }
	} while (numBytesRead >= 0);
    }

    /**
     * Getter for property exec.
     * @return Value of property exec.
     */
    public String getExec() {

        return this.exec;
    }

    /**
     * Setter for property exec.
     * @param exec New value of property exec.
     */
    public void setExec(String exec) {

        this.exec = exec;
    }
    
    public void setAlternateExec(String[] exec) {
        this.alternateExec = exec.clone();
    }
    
    public String[] getAlternateExec() {
        if (alternateExec==null)
            return null;
        return alternateExec.clone();
    }
    
    public ClassLoader getClassLoader() {
	if (appClassLoader == null)
	    appClassLoader = new LgClassLoader(
		    new URL[0], classLoaderID, this.getClass().getClassLoader());

        return appClassLoader;
    }
    
//    public class ApplicationDescriptionEvent extends ConfigEvent {
//        /**
//         * Holds value of property appDescription.
//         */
//        private ApplicationDescription appDescription;
//
//        public ApplicationDescriptionEvent(ApplicationDescription app) {
//            appDescription = app;
//        }
//        
//        /**
//         * Getter for property appDescription.
//         * @return Value of property appDescription.
//         */
//        public ApplicationDescription getAppDescription() {
//
//            return this.appDescription;
//        }
//
//        /**
//         * Setter for property appDescription.
//         * @param appDescription New value of property appDescription.
//         */
//        public void setAppDescription(ApplicationDescription appDescription) {
//
//            this.appDescription = appDescription;
//        }
//        
//    }
    
    public class ApplicationTaskbarItemConfig extends TaskbarItemConfig {
        public Tapp createItem() {
            Tapp tapp = new Tapp();
            try {
                if(mouseEnteredFilename == null){
                    tapp.addChild(new Pseudo3DShortcut(getIconURL(), getExec(), getClassLoader()));
                } else {
                    tapp.addChild(new SwichablePseudo3DShortcut(
                            getIconURL(),
                            getMouseEnteredFilename(), 
                            getExec(),
                            getClassLoader()
                          ));
                }
                tapp.setPreferredSize(new Vector3f(0.01f, 0.01f, 0.01f));
            } catch(Exception e) {
                logger.warning("Unable to create Icon from URL "+getIconURL()+" for command "+getExec());
                 
            }

            return tapp;
        }
    }
}
