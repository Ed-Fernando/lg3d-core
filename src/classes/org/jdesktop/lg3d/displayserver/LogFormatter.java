/**
 * Project Looking Glass
 *
 * $RCSfile: LogFormatter.java,v $
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
 * $Date: 2004-09-10 23:29:41 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

/**
 * A formatter to format the logs as follows:
 *
 * LEVEL MESSAGE CLASSNAME.METHODNAME THREADID:TIME_OFFSET\n
 * Thrown THROWN\n
 * Cause CAUSE\n
 * STACK_TRACE
 *
 * @author  Paul
 */
public class LogFormatter extends java.util.logging.Formatter {
    
    /** When the formatter was initialized */
    private long startTime = System.currentTimeMillis();
    
    /** Creates a new instance of LogFormatter */
    public LogFormatter() {
    }
    
    /**
     * format the log record
     * @param record the log record to format
     * @return the formatted log record
     */
    public String format(java.util.logging.LogRecord record) {
        StringBuffer buf = new StringBuffer();
        
        buf.append( record.getLevel().getName()+" ");
        buf.append( record.getMessage()+" " );
        
        String classname = record.getSourceClassName().replaceFirst("org.jdesktop.", "");
        buf.append( classname+".");
        buf.append( record.getSourceMethodName()+" " );
        buf.append( record.getThreadID()+":"+(record.getMillis()-startTime) );
        
        buf.append("\n");
        
        Throwable t = record.getThrown();
        if (t!=null) {
            buf.append( "Thrown "+t+"\n" );
            buf.append( "Cause "+t.getCause()+"\n" );
            StackTraceElement[] ste = t.getStackTrace();
            for(int i=0; i<ste.length; i++)
                buf.append("    "+ste[i].toString()+"\n");
            buf.append("\n");
        }
        
        
        return buf.toString();
    }
    
}
