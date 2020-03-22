/**
 * Project Looking Glass
 *
 * $RCSfile: AppLaunchAction.java,v $
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
 * $Revision: 1.17 $
 * $Date: 2006-08-24 20:05:03 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.scenemanager.utils.event.AppLaunchEvent;



/**
 * An ActionNoArg class that launches an application based on the
 * specified command string.
 */
public class AppLaunchAction implements ActionNoArg {
    protected static final Logger logger = Logger.getLogger("lg.wg");
    private String command;
    private ClassLoader classLoader;

    public AppLaunchAction(String command, ClassLoader classloader) { 
	this.command = command;
        this.classLoader = classloader;
        logger.config("AppLaunchAction for "+command+"  with classloader "+classloader);
    }
    
//    public AppLaunchAction(String command, LgClasspath classpath) { 
//	this.command = command;
//        this.classpath = classpath;
//    }

    public void performAction(LgEventSource source) {
        
        LgEventConnector.getLgEventConnector().postEvent(
                                        new AppLaunchEvent(command), null);
        
        //
        // FIXME and WARNING 
        // -- this actually executes the java app in the same JVM!
        //
	if (command.startsWith("java ")) {
            logger.warning("Executing java app in the same JVM: " + command +"\nUsing ClassLoader "+classLoader);
            
	    final String[] cmd = command.split("\\s");
	    final String className = cmd[1];
            
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        // declare a new class loader for this app
                        Class cls = classLoader.loadClass(className);  
                        
                        Class[] argClses = {String[].class};
                        Method mainMethod = cls.getMethod("main", argClses);
                        Object[] argObjs = {new String[]{command.substring(command.indexOf(className)+className.length())}};
                        mainMethod.invoke(null, argObjs);
                    } catch(ClassNotFoundException cnfe) {
                        URL[] urls = ((URLClassLoader)classLoader).getURLs();
                        StringBuffer sb = new StringBuffer();
                        for(int i=0; i<urls.length; i++)
                            sb.append(urls[i].toExternalForm()+"\n");
                        logger.log(Level.WARNING, "Failed to find class in classpath : "+sb.toString(), cnfe);
                    } catch (Exception ex) {
                        logger.log(Level.WARNING, "Failed to start: " + className, ex);
                    }
                }
            };
            (new Thread(r)).start();
            
            return;
	}
        
        String displayName = System.getProperty("lg.lgserverdisplay");
	if (displayName == null) {
	    displayName = ":0";
	}

	try {
	    logger.finer("Executing command: " + command);
            ArrayList<String> commandArgs = new ArrayList();
            
            int start=0;
            int end=0;
            while(end<command.length()) {
                end = command.indexOf(' ', start);
                if (end==-1)
                    end = command.length();
                commandArgs.add(command.substring(start,end));
                start = end+1;
            }
                
            
	    ProcessBuilder pb = new ProcessBuilder(commandArgs);
            pb.redirectErrorStream(true);
            Map<String, String> env = pb.environment();
            env.put("DISPLAY", displayName);
	    Process p = pb.start();
            
            (new HandleOutput(command, p)).start();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }   
    
    /**
     * Handle the output of a Process
     */
    private static class HandleOutput extends Thread {
        private BufferedReader stderr;
        private String command;
        
        public HandleOutput(String command, Process p) {
            this.command = command;
            stderr = new BufferedReader(new InputStreamReader(p.getInputStream()));
        }
        
        public void run() {
            try {
                String line = stderr.readLine();
                while(line!=null) {
                    logger.info("Output from app "+command+" : "+line);
                    line = stderr.readLine();
                }
            } catch (IOException e) {
                logger.warning("Error processing Output Stream of app "+command);
            } finally {
                try {
                    stderr.close();
                } catch (IOException e) {
                    // 
                }
            }
        }
    }
}
