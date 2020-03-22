/**
 * Project Looking Glass
 *
 * $RCSfile: LgXserver.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2007-04-12 23:36:19 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Launches an LG X server process
 */

class LgXserver {

    protected static final Logger logger = Logger.getLogger("lg.fws");

    private ArrayList<String> commandArgs = new ArrayList();
    
    public LgXserver () {
	commandArgs.add("../lg3d-core/src/devscripts/lg3d-app");
	commandArgs.add("-xs");
    }
   
    public void launch () {
	try {
	    ProcessBuilder pb = new ProcessBuilder(commandArgs);
            pb.redirectErrorStream(true);
	    Process p = pb.start();
            (new DiscardOutput(commandArgs, p)).start();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private static String arrayListStringToString (ArrayList<String> ary) {
	String str = "";
	for (String s : ary) {
	    str += s + " ";
	}
	return str;
    }

    // Handle the output of a process by discarding it. If we don't
    // do this on Solaris the display server will hang!

    private static class DiscardOutput extends Thread {
        private BufferedReader stderr;
        private String command;
        
        public DiscardOutput(ArrayList<String> cmdAndArgs, Process p) {
	    command = arrayListStringToString(cmdAndArgs);
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
                }
            }
        }
    }

    private void logStringArray (Level logLevel, String[] ary) {
	String str = "";
	for (int i = 0; i < ary.length; i++) {
	    str += ary[i] + " ";
	}
	logger.log(logLevel, str);
    }

    private void logStringArray (Level logLevel, ArrayList<String> ary) {
	logger.log(logLevel, arrayListStringToString(ary));
    }
}

