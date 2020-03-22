/**
 * Project Looking Glass
 *
 * $RCSfile: DisplayServerAppInterface.java,v $
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
 * $Revision: 1.8 $
 * $Date: 2007-05-02 20:51:52 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;



import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.scenemanager.CursorModule;


/**
 * The definition of the connection layer between an application and the
 * looking glass display server. The implementation should handle the adding
 * and removing of frames (applications) and cursors to the display server
 * by the client applications. Additionally it should handle the listening
 * methods for the client.
 * @author  Paul
 */

public interface DisplayServerAppInterface {

    /**
     * Initialise the Display Server with the supplied maximum frame
     * interval
     * @param frame_interval_time the maximum frame interval
     */
    public void initialize(int frame_interval_time);
    
    /**
     * Add the Frame3D to the DisplayServer (which will pass it to the
     * scene manager )
     * @param frame the Frame3D to add
     * @see #removeFrame3D
     */
    public void addFrame3D( Frame3D frame );
    
    /**
     * Remove the Frame3D 
     * @param frame the Frame3D to remove
     * @see #addFrame3D
     */
    public void removeFrame3D( Frame3D frame );
    
    /**
     * For Debug: print all nodes in the current Wonderland scene graph
     */
    public void printWonderlandSceneGraph ();

    /**
     * Add a Cursor3D to the system for this application
     * @param cursor the cursor to add
     * @see #removeCursor3D
     */
    public void addCursor3D( Cursor3D cursor );
    
    /**
     * Remove a Cursor3D from the system, only cursors previously added
     * by this app can be removed
     * @param cursor the cursor to remove
     * @see #addCursor3D
     */
    public void removeCursor3D( Cursor3D cursor );
        
    /**
     * Return the branch group on which to attach cursors
     */
    public BranchGroup getCursorRoot ();

    /**
     * Returns the display server's current cursor module
     */
    public CursorModule getCursorModule ();

    /**
     * Add a listener which is called whenever events of the specified
     * class are posted
     * @param listener the listener to add
     * @param evtClass the class of event to listen for
     */    
    public void addListener( LgEventListener listener, Class evtClass );

    /**
     * Register a listener for events of class evtClass which are generated
     * from the specified source object
     * @param listener the listener to add
     * @param evtClass the class of event to listen for
     * @param source the source object to listen to
     */
    public void addListener( LgEventListener listener, Class evtClass, LgEventSource source );

    /**
     * Register a listener for events of class evtClass which are generated
     * from any source of the specified class
     * @param listener the listener to add
     * @param evtClass the class of event to listen for
     * @param source the class of the source objects to listen to
     */
    public void addListener( LgEventListener listener, Class evtClass, Class source );

    /**
     * Remove the listener
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     */
    public void removeListener(LgEventListener listener, Class evtClass );

    /**
     * Remove the listener
     * @param listener LgEventListener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the class of the source to listen to
     */    
    public void removeListener(LgEventListener listener, Class evtClass, Class sourceClass );

    /**
     * Remove the listener
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the source object to listen to
     */    
    public void removeListener(LgEventListener listener, Class evtClass, Object sourceObject );

    /**
     * Post an event into the system
     * @param event the event to post
     * @param source the source of the event
     */
    public void postEvent( LgEvent event, LgEventSource source );
    
    /**
     * Get a unique NodeID for this client
     * @param clientID the id of the client
     * @return the unique NodeID of the client
     */
    public NodeID getNodeID( int clientID );
}

