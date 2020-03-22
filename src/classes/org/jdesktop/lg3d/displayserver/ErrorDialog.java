/**
 * Project Looking Glass
 *
 * $RCSfile: ErrorDialog.java,v $
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
 * $Revision: 1.10 $
 * $Date: 2006-08-14 23:13:19 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.LogRecord;

/**
 * This is a small ErrorDialog, shown using LogHandler when a severe error occurs.
 * It implements Runnable to prevent blocking other Threads...
 * @author pinaraf
 */
public class ErrorDialog extends JDialog implements Runnable {
    /* These are the two "components" of the dialog */
    private JTextArea message, trace;
    
    /**
     * This is the exit flag, as described in issue 435 
     * It is necessary because every severe error isn't going to "crash" Looking Glass.
     */
    private Boolean mustExit = false;
    
    /**
     * This is to show a custom message instead of the "generic" message of the ErrorDialog.
     * It is useful for Java3D installation errors for instance.
     */
    static Boolean showGenericText = true;
    
    /**
     * The severe LogRecord which causes the apparition of the ErrorDialog
     */
    public LogRecord logRecord = null;
    
    /**
     * Creates a new instance of the ErrorDialog
     */
    public ErrorDialog() {
	super((java.awt.Frame) null, false);
	setTitle ("An exception occured");
	Container ccf = this.getContentPane();
	ccf.setLayout(new BorderLayout(4,2));
	JButton OKBtn = new JButton("OK");
	OKBtn.setText("OK");
	OKBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mustExit) {
                    SplashWindow.destroySplashscreen();
                    System.exit(1);
                }
                setVisible(false);
            }
        });
	ccf.add("South", OKBtn);
	JTabbedPane tabs = new JTabbedPane();
	ccf.add("Center", tabs);
	message = new JTextArea();
        JScrollPane messageScroll = new JScrollPane(message);
	tabs.add(messageScroll);
	tabs.setTitleAt(0, "Message");
        
	trace = new JTextArea();
        JScrollPane traceScroll = new JScrollPane(trace);
	tabs.add(traceScroll);
	tabs.setTitleAt(1, "Trace");
	setSize(400, 400);
    }
    
    /**
     * Set the trace content
     */
    public void setTrace (String traceMessage) {
        trace.setText(traceMessage);
    }

    /**
     * Fill in the message box and show the dialog...
     */
    public void run() {

        if (logRecord != null) {
            if (logRecord.getThrown() instanceof SevereRuntimeError) {
                mustExit = true;
            }
        }
        
        String message_text = "";
        if (showGenericText) {
            message_text += "A severe error occured !\n"
                        + "You should save your work as quickly as possible.\n";
            if (mustExit) {
                message_text += "Looking Glass can't continue to work, it'll stop when you click on OK...\n";
            } else {
                message_text += "Looking Glass may be able to continue to work, but no guarantee...\n";
            }
            message_text += "Please report the error using the crash reporter : \n"
                        + "http://pinaraf.robertlan.eu.org/LG3D/crash_reporter\n\n";
        }
        if (logRecord != null) {
            message_text += logRecord.getMessage();
        }
        
        try {
            message.setText(message_text);
            setVisible(true);
        } catch (Throwable th) {
            // It is possible that exceptions like NullPointerException happen
            // at far deep in the above calls.
            // Try to display the message_text anyway.
            System.err.println("\n\n********************************\n" 
                    + message_text
                    + "\n********************************\n\n");
        }
    }
}
