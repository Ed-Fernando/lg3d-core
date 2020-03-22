/**
 * Project Looking Glass
 *
 * $RCSfile: JnlpSecurityManager.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2007-01-18 18:58:23 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver;

import java.io.FileDescriptor;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * Simple Security Manager for JNLP deployment. 
 * Asks user for permission to write to a file.
 * Logs all read and write accesses.
 *
 * @author paulby
 */
public class JnlpSecurityManager extends SecurityManager {
    
    private Logger logger = Logger.getLogger("lg.config");

    private boolean readAccessGranted = false;
    private boolean readDontAskAgain = false;
    private boolean writeAccessGranted = false;
    private boolean writeDontAskAgain = false;
    
    /** Creates a new instance of JnlpSecurityManager */
    public JnlpSecurityManager() {
    }
    
    public void checkRead(FileDescriptor filedescriptor) {
        logger.info("reading file "+filedescriptor);
        //askUser(true, filedescriptor.toString());
    }
    
    public void checkRead(String filename) {
        logger.info("reading file "+filename);
        //askUser(true, filename);
    }
    public void checkRead(String filename, Object executionContext) {
        logger.info("reading file "+filename);
        //askUser(true, filename);
    }
    public void checkWrite(FileDescriptor filedescriptor) {
        if (filedescriptor==FileDescriptor.err || filedescriptor==FileDescriptor.out)
            return;
        
        logger.info("writing to file "+filedescriptor);
        //askUser(false, filedescriptor.toString());
    }
    public void checkWrite(String filename) {
        logger.info("writing to file "+filename);
        //askUser(false, filename);
    }
    public void checkPropertyAccess(String s) { 
    }
    
    public void checkPropertiesAccess() { 
    }
    
    private synchronized void askUser(boolean readRequest, String filename) {
        if (readRequest && readDontAskAgain) 
            if (!readAccessGranted)
                throw new SecurityException("User Denied Access to file");
            else
                return;
        
        if (!readRequest && writeDontAskAgain)
            if (!writeAccessGranted)
                throw new SecurityException("User Denied Access to file");
            else
                return;
                        
        JnlpFileAccessDialog d = new JnlpFileAccessDialog(new JFrame(), true, readRequest, filename);
        d.setVisible(true);
        if (readRequest) {
            readAccessGranted = d.isAccessGranted();
            readDontAskAgain = d.dontAskAgain();
        } else {
            writeAccessGranted = d.isAccessGranted();
            writeDontAskAgain = d.dontAskAgain();  
            logger.severe("ANSWER "+writeAccessGranted+" "+writeDontAskAgain);
        }
        
        if (!d.isAccessGranted())
            throw new SecurityException("User Denied Access to file");        
    }
}
