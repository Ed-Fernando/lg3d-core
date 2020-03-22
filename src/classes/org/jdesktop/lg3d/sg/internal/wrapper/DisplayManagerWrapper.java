/**
 * Project Looking Glass
 *
 * $RCSfile: DisplayManagerWrapper.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2004-06-23 18:51:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

/**
 *
 * @author  Paul
 */
public interface DisplayManagerWrapper {
        
    /*
     * Add a graph to the DisplayManager, All the capabilitiy bits necessary
     * to perform picking will be set when the BranchGroup is added.
     *
     * @param BranchGroup The BranchGroup to attach to this Display
     */
    public void addBranchGraph(BranchGroupWrapper bg) ;

    /**
     * Return the ViewPlatformTransform 
     */
//    public TransformGroup getViewPlatformTransform();
    
    /**
     * Get the width of the Canvas
     */
    public int getWidth() ;
    
    /**
     * Get the height of the Canvas
     */
    public int getHeight() ;
    
    /**
     * Get the pixels per meter in X
     */
    public float getPPMX() ;
    
    /**
     * Get the pixels per meter in Y
     */
    public float getPPMY() ;

    /**
     * Set the Keyboard focus to node
     */
//    public void setKeyboardFocus( LgBranchGroup node );
    
    /**
     * Returns the Locale object associated with this scene graph.
     *
     * @return The Locale object used in the construction of this scene
     *  graph.
     */
    //public Locale getLocale() ;
    
    /**
//     * Create the window backing store for the X window wid
//     *
//     * SHOULD WE HAVE A SUBCLASS OF LGBRANCHGROUP WHICH REPRESENTS
//     * AN X APPLICATION, IN WHICH CASE THIS CALL BELONGS IN THAT CLASS
//     */
//    public ImageComponent2D createWindowBackingStore( int wid );
//    
//    /**
//     * Set the X WID of the node
//     *
//     * This should be in LgBranchGroup
//     */
//    public void setWID( LgBranchGroup node, int wid );
//    
//    /**
//     * Get the WID for this node
//     *
//     * THIS SHOULD BE IN LGBRANCHGROUP
//     */
//    public int getWID( LgBranchGroup node );
//    
//    public void sendEvent( int TBD );
//    
//    public void enableSendEvent( boolean enable );
    
    /**
     * Return the object factory for scene graph objects
     */
    //public SGObjectFactoryWrapper getSGObjectFactory() ;
}
