/**
 * Project Looking Glass
 *
 * $RCSfile: AppConnectorPrivate.java,v $
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
 * $Revision: 1.24 $
 * $Date: 2007-06-23 00:16:34 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.lg3d.displayserver.socketconnector.ClientHandler;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseEvent3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.scenemanager.CursorModule;

/**
 *
 * Provides the connection layer between an application and the looking glass
 * display server. Users should never call this, all interaction should take 
 * place through Frame3D
 *
 * @author  Paul
 */
public class AppConnectorPrivate implements DisplayServerAppInterface {
    /** the logger instance */
    private Logger logger = Logger.getLogger("lg.displayserver");
    /** the AppConnector singleton */
    private static AppConnectorPrivate appConnector;
    /** the static instance of the display server the AppConnector spawned */
    private static DisplayServerAppInterface displayServer;
    /** the LG thread group */
    private static ThreadGroup threadGroup = new ThreadGroup( "LG" );
    /** is this the server version */
    protected boolean isServer;
    
    /**
     * create a new AppConnector
     * @param isServer the connection is a server connection
     */
    AppConnectorPrivate(boolean isServer) {
        this.isServer = isServer;
        
//        try {
//            Class c = Class.forName("javax.jnlp.BasicService");
//            logger.warning("Running within JNLP, installing security manager.");
//            System.setSecurityManager(new JnlpSecurityManager());
//        } catch(SecurityException se) {
//            logger.log(Level.SEVERE, "Error setting security manager, aborting.", se);
//            System.exit(1);
//        } catch(ClassNotFoundException cnfe) {
//            // Do nothing
//        }

        logger.fine("Starting App Connector");
    }
    
    /**
     * Returns the client app connector singleton. Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @return the client app connector singleton
     */
    public static AppConnectorPrivate getAppConnector() {
        return getAppConnector(false);
    }
    
    /**
     * Returns a client or server app connector depending on 
     * isServer setting. Also takes into account setting of isClientServer
     * in PlatformConfig
     * Users should never call this
     * instead they should use the methods in Frame3D.
     * @param isServer true - get a server instance,
     *                 false - get a client instance
     */
    public static AppConnectorPrivate getAppConnector( boolean isServer ) {
        // if there is no singleton instance create a new one
        if (appConnector == null) {
            appConnector = new AppConnectorPrivate(isServer);

            if (PlatformConfig.getConfig().isClientServer() && !isServer) {
                displayServer = new ClientHandler();
            } else {
                displayServer = new DisplayServerControl();
            }
            int maxFrameRate = Integer.valueOf(System.getProperty("lg.maxfps", "30"));
            displayServer.initialize(maxFrameRate);
	}        
        return appConnector;
    }
    
    /**
     * get the LG Thread group
     * @return the LG thread group
     */
    public static ThreadGroup getThreadGroup() {
        return threadGroup;
    }
    
    /**
     * Add the application to the current scene. This will cause the
     * app to become live. Users should never call this
     * instead they should use the methods in Frame3D.
     * @param app the application to add to the display server
     * @see #removeFrame3D
     */
    public void addFrame3D( Frame3D app ) {
        displayServer.addFrame3D( app );
    }
    
    /**
     * Add a listener which is called whenever events of the specified
     * class are posted. Users should never call this
     * instead they should use the methods in Frame3D.
     * @param listener the LGEventListener to add
     * @param evtClass the class of events to listen to
     */    
    public void addListener( LgEventListener listener, Class evtClass ) {  
        displayServer.addListener( listener, evtClass );
    }
    
    /**
     * Register a listener for events of class evtClass which are generated
     * from the specified source object
     *
     * Note MouseEvents and MouseMotionEvents are only generated by 
     * Component3D's if the respective property is set on the Component3D.
     * Large numbers of these events can be generated so these properties
     * should be set with caution.
     *
     * This method will log a warning if an attempt is made to add a listener
     * for a MouseMotionEvent3D or MouseEvent3D to a Component3D that does not
     * have the necessary property set.
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @param listener the LGEventListener to add
     * @param evtClass the class of events to listen to
     * @param source the source of the event
     */
    public void addListener( LgEventListener listener, Class evtClass, LgEventSource source ) {
        displayServer.addListener( listener, evtClass, source );
	// if the event is a MouseEvent3D and
	// the source is a Component3D then if the necessary property set
	// is not set a warning will be sent
        if (MouseEvent3D.class.isAssignableFrom(evtClass) 
            && source instanceof Component3D 
            && !((Component3D)source).isMouseEventSource(evtClass))
        {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            StringBuffer sb = new StringBuffer();
            for(int i = 3; i < trace.length; i++) {
                sb.append(trace[i].toString() + "\n");
            }
            logger.warning(
                "MouseEventListener added to Component3D which is not a MouseEventSource "
                    + sb.toString());
        }
    }
    
    /**
     * Register a listener for events of class evtClass which are generated
     * from any source of the specified class.
     *
     * Note MouseEvents and MouseMotionEvents are only generated by
     * Component3D's if the respective property is set on the Component3D.
     * Large numbers of these events can be generated so these properties
     * should be set with caution.
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     * 
     * @param listener the LGEventListener to add
     * @param evtClass the class of events to listen to
     * @param source the class of source objects to listen to
     */
    public void addListener( LgEventListener listener, Class evtClass, Class source ) {
        displayServer.addListener( listener, evtClass, source );
    }
    
    /**
     * Remove the listener
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     */
    public void removeListener(LgEventListener listener, Class evtClass ) {
        displayServer.removeListener( listener, evtClass );
    }

    /**
     * Remove the listener
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @param listener LgEventListener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the class of the source to listen to
     */    
    public void removeListener(LgEventListener listener, Class evtClass, Class sourceClass ) {        
        displayServer.removeListener( listener, evtClass, sourceClass );        
    }

    /**
     * Remove the listener
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the source object to listen to
     */    
    public void removeListener(LgEventListener listener, Class evtClass, Object sourceObject ) {        
        displayServer.removeListener( listener, evtClass, sourceObject );        
    }
    /**
     * Post an event into the system
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @param event the event to post
     * @param source the source of the event
     */
    public void postEvent( LgEvent event, LgEventSource source ) {
        displayServer.postEvent( event, source );
    }
    
    /**
     * Get a unique nodeID for this client.
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @param clientID the id of the client
     * @return the new node id
     */
    public NodeID getNodeID(int clientID) {
        // The correct clientID (instead of MIN_VALUE
        // will be set by the displayServer
        return displayServer.getNodeID(clientID);
    }
    
    /**
     * Get a unique nodeID for this client.
     *
     * Users should never call this
     * instead they should use the methods in Frame3D. 
     *
     * @return a new NodeID for the client, with a clientID of -1
     */
    public NodeID getNodeID() {
        // The correct clientID (instead of MIN_VALUE
        // will be set by the displayServer
        return displayServer.getNodeID(-1);
    }

    /**
     * initialise the AppConnector
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @param maxFramesPerSecond the maximum frames per second
     */
    public void initialize(int maxFramesPerSecond) {
        // Do nothing
    }
    
    /**
     * add a 3D cursor to the display server
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @param cursor the cursor to add
     * @see #removeCursor3D
     */
    public void addCursor3D(Cursor3D cursor) {
        displayServer.addCursor3D(cursor);
    }
    
    /**
     * remove the given cursor from the display server
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @param cursor the cursor to remove
     * @see #addCursor3D
     */
    public void removeCursor3D(Cursor3D cursor) {
        displayServer.removeCursor3D(cursor);
    }
    
    /**
     * Return the branch group on which to attach cursors
     */
    public BranchGroup getCursorRoot () {
	return displayServer.getCursorRoot();
    }

    /**
     * remove the given frame from the display server
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @param frame the frame to remove
     * @see #removeFrame3D
     */
    public void removeFrame3D(Frame3D frame) {
        displayServer.removeFrame3D(frame);
    }
    
    /**
     * Returns the display server's current cursor module
     */
    public CursorModule getCursorModule () {
        return displayServer.getCursorModule();
    }

    /**
     * For Debug: print all nodes in the current scene graph
     */
    public void printWonderlandSceneGraph () {
	displayServer.printWonderlandSceneGraph();
    }

    /**
     * Is this AppConnector a client or server
     *
     * Users should never call this
     * instead they should use the methods in Frame3D.
     *
     * @return true - is a server
     *         false - is a client
     */
    public boolean isServer() {
        return isServer;
    }
    
//    /**
//     * Returns the index of the version number seperator (. - _ eol space), 
//     * searching from start position in string.
//     */
//    private int indexOfVersionNumSeparator(String str, int start) {
//        int ret=-1;
//        int i;
//        
//        for(i=start; (i<str.length() && ret==-1); i++) {
//            int ch = str.charAt(i);
//            if (ch=='.' || ch=='-' || ch=='_' || ch==' ')
//                ret = i;
//        }
//        
//        if (i==str.length())
//            ret = i;
//        
//        return ret;
//    }
}
