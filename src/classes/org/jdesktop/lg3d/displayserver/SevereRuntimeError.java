/**
 * Project Looking Glass
 *
 * $RCSfile: SevereRuntimeError.java,v $
 *
 * Copyright (c) 2006, Sun Microsystems, Inc., All Rights Reserved
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
 * $Date: 2006-03-11 20:24:30 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver;

import java.lang.RuntimeException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * When a SevereRuntimeError is thrown, a severe error is logged
 * and the error dialog appears with mustExit flag set to true.
 * 
 * @author pinaraf
 */
public class SevereRuntimeError extends RuntimeException {
    
    /**
     * Create a new SeverRuntimeError.
     * Two arguments : a message and the cause
     */
    public SevereRuntimeError (String message, Throwable cause) {
        super(message, cause);
        Logger logger = java.util.logging.Logger.getLogger("lg.displayserver");
        logger.log(Level.SEVERE, message, this);
    }
    
    /**
     * Create a new SeverRuntimeError.
     * One argument : a message
     */
    public SevereRuntimeError (String message) {
        super(message);
        Logger logger = java.util.logging.Logger.getLogger("lg.displayserver");
        logger.log(Level.SEVERE, message, this);
    }
}
