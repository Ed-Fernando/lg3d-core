/**
 * Project Looking Glass
 *
 * $RCSfile: LogHandler.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2006-03-12 13:05:10 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver;

import java.lang.Thread;
import java.util.logging.LogRecord;
import java.util.logging.Level;
import java.util.logging.Handler;

/**
 * Implements a Log Handler. It is instancied by the logging system, 
 * after parsing the logging.properties file.
 * This LogHandler opens the error dialog when a severe error occurs.
 * @author pinaraf
 */
public class LogHandler extends Handler {
    private ErrorDialog dialog;
    
    /** Creates a new instance of LogHandler */
    public LogHandler() {
	// No need to instanciate the dialog yet : often, no error will occur
	// so that's useless to always have an ErrorDialog using memory...
	dialog = null;
    }
    
    public void publish(LogRecord record) {
	if (record.getLevel() == Level.SEVERE) {
	    if (dialog == null) {
		dialog = new ErrorDialog();
	    } else {
		dialog.setTrace("");
	    }
            dialog.logRecord = record;
	    
	    StringBuffer buf = new StringBuffer();
	    Throwable t = record.getThrown();
	    if (t != null) {
		buf.append( "Thrown "+t+"\n" );
		buf.append( "Cause "+t.getCause()+"\n" );
		StackTraceElement[] ste = t.getStackTrace();
		for(int i=0; i<ste.length; i++)
		    buf.append("    "+ste[i].toString()+"\n");
		buf.append("\n");
	    }
	    dialog.setTrace(buf.toString());
            // We open in a new thread not to freeze other applications.
	    new Thread(dialog).start();
	}
    }
    
    public void close () {
    }
    
    public void flush () {
    }
}
