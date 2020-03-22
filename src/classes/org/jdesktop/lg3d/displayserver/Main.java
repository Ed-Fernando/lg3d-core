/**
 * Project Looking Glass
 *
 * $RCSfile: Main.java,v $
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
 * $Revision: 1.17 $
 * $Date: 2006-08-15 19:07:01 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import javax.swing.JOptionPane;
import org.jdesktop.lg3d.displayserver.socketconnector.ServerHandler;
import org.jdesktop.lg3d.displayserver.SplashWindow;

/**
 * The main class of LG3D DisplayServer.
 */
public final class Main {
    /**
    * The entry point of LG3D DisplayServer.
    */
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("lg.displayserver");
        Locale locale = Locale.getDefault();
        
        StringBuffer logMsg = new StringBuffer("\n");
        logMsg.append("\tLG3D Version              : " + LgBuildInfo.getVersion() + "\n");
        logMsg.append("\tLG3D Build Time           : " + LgBuildInfo.getBuildDate() + "(" 
                                                       + LgBuildInfo.getBuildTimeStamp() + ")\n");
        logMsg.append("\tLG3D Build Type           : " + LgBuildInfo.getBuildType() + "\n");
        logMsg.append("\tLG3D Java Version         : " + LgBuildInfo.getJavaVersion() + "\n");
        logMsg.append("\n");
        logMsg.append("\tJava Version              : " + System.getProperty("java.version") + "\n");
        logMsg.append("\tJava Vendor               : " + System.getProperty("java.vendor") + "\n");
        logMsg.append("\tJava Class Version        : " + System.getProperty("java.class.version") + "\n");
        logMsg.append("\tJava Class Path           : " + System.getProperty("java.class.path") + "\n");
        logMsg.append("\tApp Codebase              : " + System.getProperty("lg.appcodebase") + "\n");
        logMsg.append("\n");
        logMsg.append("\tOS Name                   : " + System.getProperty("os.name") + "\n");
        logMsg.append("\tOS Arch                   : " + System.getProperty("os.arch") + "\n");
        logMsg.append("\tOS Version                : " + System.getProperty("os.version") + "\n");
        logMsg.append("\n");
        logMsg.append("\tDef. Locale Language Code : " + locale.getLanguage() + "\n");
        logMsg.append("\tDef. Locale Country Code  : " + locale.getCountry() + "\n");
        
        logger.info(logMsg.toString());
               
	// if the logger config file is not set, then read in the config file
        if (System.getProperty("java.util.logging.config.file") == null) {
            try {
                String etcDir = System.getProperty("lg.etcdir");
                
                if (etcDir!=null) {
                    BufferedInputStream in;
                    if (etcDir.startsWith("http") || etcDir.startsWith("file:")) {
                        URL url = new URL(etcDir+"lg3d/logging.properties");
                        url.openConnection();
                        in = new BufferedInputStream(url.openStream());
                    } else {
                        File file = new File( etcDir+"lg3d/logging.properties");
                        in = new BufferedInputStream(new FileInputStream( file ));
                    }
                    LogManager.getLogManager().readConfiguration(in);     
                }
            } catch (Exception ioe) {
                logger.log(Level.WARNING, "Could not load the logging.properties file:", ioe);
            }
        }
        
        String compileJavaVersion = LgBuildInfo.getJavaVersion();
        String currentJavaVersion = System.getProperty("java.version");
        
        if (!compileJavaVersion.substring(0,5).equals(currentJavaVersion.substring(0,5))) {
//            logger.severe("Java Version mismatch !\nProject Looking Glass was compiled with Java version "
//                    +compileJavaVersion+" but the current Java version is "+currentJavaVersion+"\n"+
//                    "Please upgrade to version "+compileJavaVersion+" or newer.");
            throw new SevereRuntimeError("Java Version mismatch !\nProject Looking Glass was compiled with Java version "
                    +compileJavaVersion+" but the current Java version is "+currentJavaVersion+"\n"+
                    "Please upgrade to version "+compileJavaVersion+" or newer.");
        }
        
        if (!System.getProperty("java.version").startsWith("1.6")) {
            JOptionPane.showMessageDialog((java.awt.Component)null, "WARNING : Project Looking Glass works\n" +
                    "best when using JDK 1.6 (Mustang)\n\n"+
                    "Please upgrade at your earliest convenience\n"+
                    "http://mustang.dev.java.net", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        
        // Check this is at least Java3D 1.5 build 2, which was the first release that included pickfast
        try {
            Class clazz = Class.forName("com.sun.j3d.utils.pickfast.PickCanvas"/*, false, logger.getClass().getClassLoader()*/);
            (new SplashStarter()).start();
            new ServerHandler();
            SplashWindow.destroySplashscreen();
        } catch (ClassNotFoundException cnfe) {
            Package j3dPackage = Package.getPackage("javax.media.j3d");
            ErrorDialog.showGenericText = false;
            if (j3dPackage == null) {
                // Java3D isn't installed
                throw new SevereRuntimeError("Project Looking Glass requires Java 3D 1.5 build 2 or later \nPlease check the release notes for details");
            } else {
                // Java3D is outdated
                throw new SevereRuntimeError("Project Looking Glass requires Java 3D 1.5 build 2 or later, but version "+j3dPackage.getImplementationVersion()+" is installed.");
            }
        }
    }
}
