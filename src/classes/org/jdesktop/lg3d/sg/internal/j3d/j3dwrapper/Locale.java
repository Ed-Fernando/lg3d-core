/**
 * Project Looking Glass
 *
 * $RCSfile: Locale.java,v $
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
 * $Date: 2004-06-23 18:50:39 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import javax.vecmath.*;
import java.util.Vector;
import java.util.Enumeration;
import java.util.ArrayList;

/**
 * A Locale object defines a high-resolution position within a
 * VirtualUniverse, and serves as a container for a collection of
 * BranchGroup-rooted subgraphs (branch graphs), at that position.
 * Objects within a Locale are defined using standard double-precision
 * coordinates, relative to the origin of the Locale.  This origin
 * defines the Virtual World coordinate system for that Locale.
 * <p>
 * A Locale object defines methods to set and get its high-resolution
 * coordinates, and methods to add, remove, and enumerate the branch
 * graphs.
 *
 * @see VirtualUniverse
 * @see HiResCoord
 * @see BranchGroup
 */

public class Locale extends Object {

    javax.media.j3d.Locale wrapped;

    /**
     * This constructor is for internal use only, it will be removed from a
     * future version of the API. Users should never call this.
     *
     * @deprecated
     */
    public Locale(javax.media.j3d.Locale locale ) {
        wrapped = locale;
    }

    /**
     * Constructs and initializes a new high resolution Locale object
     * from the parameters provided.
     * @param universe the virtual universe that will contain this
     * Locale object
     * @param x an eight element array specifying the x position
     * @param y an eight element array specifying the y position
     * @param z an eight element array specifying the z position
     */
//    private Locale(VirtualUniverse universe, int[] x, int[] y, int[] z) {
//	this.universe = universe;
//	this.universe.addLocale(this);
//	this.hiRes = new HiResCoord(x, y, z);
//        nodeId = universe.getNodeId();
//    }

    /**
     * Constructs and initializes a new high resolution Locale object
     * at the location specified by the HiResCoord argument.
     * @param universe the virtual universe that will contain this
     * Locale object
     * @param hiRes the HiRes coordinate to use in creating this Locale
     */
//    private Locale(VirtualUniverse universe, HiResCoord hiRes) {
//	this.universe = universe;
//	this.universe.addLocale(this);
//	this.hiRes = new HiResCoord(hiRes);
//        nodeId = universe.getNodeId();
//    }


    /**
     * Sets the HiRes coordinate of this Locale to the location
     * specified by the parameters provided.
     * @param x an eight element array specifying the x position
     * @param y an eight element array specifying the y position
     * @param z an eight element array specifying the z position
     */
//    private void setHiRes(int[] x, int[] y, int[] z) { 
//	this.hiRes.setHiResCoord(x, y, z);
//    }

    /**
     * Sets the HiRes coordinate of this Locale
     * to the location specified by the HiRes argument.
     * @param hiRes the HiRes coordinate specifying this node's new location
     */
//    private void setHiRes(HiResCoord hiRes) {
//	this.hiRes.setHiResCoord(hiRes);
//    }

    /**
     * Returns this node's HiResCoord.
     * @param hiRes a HiResCoord object that will receive the
     * HiRes coordinate of this Locale node
     */
//    private void getHiRes(HiResCoord hiRes) {
//	this.hiRes.getHiResCoord(hiRes);
//    }

    /**
     * Add a new branch graph rooted at BranchGroup to 
     * the list of branch graphs.
     * @param branchGroup root of the branch graph to be added
     * @exception IllegalStateException if this Locale has been
     * removed from its VirtualUniverse.
     * @exception MultipleParentException if the specified BranchGroup node
     * is already live.
     */
    public void addBranchGraph(BranchGroup branchGroup){
	wrapped.addBranchGraph( (javax.media.j3d.BranchGroup)branchGroup.wrapped );
    }


    /**
     * Removes a branch graph rooted at BranchGroup from 
     * the list of branch graphs.
     * @param branchGroup root of the branch graph to be removed
     * @exception IllegalStateException if this Locale has been
     * removed from its VirtualUniverse.
     * @exception CapabilityNotSetException if the ALLOW_DETACH capability is
     * not set in the specified BranchGroup node.
     */
    public void removeBranchGraph(BranchGroup branchGroup){
	wrapped.removeBranchGraph( (javax.media.j3d.BranchGroup)branchGroup.wrapped );
    }

    /**
     * Replaces the branch graph rooted at oldGroup in the list of 
     * branch graphs with the branch graph rooted at
     * newGroup.
     * @param oldGroup root of the branch graph to be replaced.
     * @param newGroup root of the branch graph that will replace the old
     * branch graph.
     * @exception IllegalStateException if this Locale has been
     * removed from its VirtualUniverse.
     * @exception CapabilityNotSetException if the ALLOW_DETACH capability is
     * not set in the old BranchGroup node.
     * @exception MultipleParentException if the new BranchGroup node
     * is already live.
     */
    public void replaceBranchGraph(BranchGroup oldGroup,
			    BranchGroup newGroup){

	wrapped.replaceBranchGraph( (javax.media.j3d.BranchGroup)oldGroup.wrapped, 
                                    (javax.media.j3d.BranchGroup)newGroup.wrapped );
    }


    /**
     * Get number of branch graphs in this Locale.
     * @return number of branch graphs in this Locale.
     */
    public int numBranchGraphs(){
	return  wrapped.numBranchGraphs();
    }
    
    /**
     * Gets an Enumeration object of all branch graphs in this Locale.
     * @return an Enumeration object of all branch graphs.
     * @exception IllegalStateException if this Locale has been
     * removed from its VirtualUniverse.
     */
    public Enumeration getAllBranchGraphs(){
	Enumeration e = wrapped.getAllBranchGraphs();
        Vector list = new Vector( wrapped.numBranchGraphs() );
        
        while(e.hasMoreElements()) {
            list.add( ((javax.media.j3d.BranchGroup)e.nextElement()).getUserData() );
        }
        
        return list.elements();
    }
    
    /**
     * Returns an array referencing all the items that are pickable below this
     * <code>Locale</code> that intersect with PickShape.
     * The resultant array is unordered.
     *
     * @param pickShape the description of this picking volume or area.
     *
     * @exception IllegalStateException if this Locale has been
     * removed from its VirtualUniverse.
     *
     * @see BranchGroup#pickAll
     */
    public SceneGraphPath[] pickAll( PickShape pickShape ) {
        javax.media.j3d.SceneGraphPath[] j3dSGP = wrapped.pickAll( (javax.media.j3d.PickShape)pickShape.wrapped );
        if (j3dSGP==null)
            return null;
        SceneGraphPath[] ret = new SceneGraphPath[j3dSGP.length];
        
        for(int i=0; i<ret.length; i++)
            ret[i] = new SceneGraphPath( j3dSGP[i] );
        
        return ret;
    }


    /**
     * Returns a sorted array of references to all the Pickable items
     * that intersect with the pickShape. Element [0] references the
     * item closest to <i>origin</i> of PickShape successive array
     * elements are further from the <i>origin</i>
     * <br>
     * NOTE: If pickShape is of type PickBounds, the resulting array
     * is unordered.
     *
     * @param pickShape the description of this picking volume or area.
     *
     * @exception IllegalStateException if this Locale has been
     * removed from its VirtualUniverse.
     *
     * @see BranchGroup#pickAllSorted
     */
    public SceneGraphPath[] pickAllSorted( PickShape pickShape ) {
        javax.media.j3d.SceneGraphPath[] j3dSGP = wrapped.pickAllSorted( (javax.media.j3d.PickShape)pickShape.wrapped );
        if (j3dSGP==null)
            return null;
        SceneGraphPath[] ret = new SceneGraphPath[j3dSGP.length];
        
        for(int i=0; i<ret.length; i++)
            ret[i] = new SceneGraphPath( j3dSGP[i] );
        
        return ret;
    }


    /**
     * Returns a SceneGraphPath which references the pickable item
     * which is closest to the origin of <code>pickShape</code>.
     * <br>
     * NOTE: If pickShape is of type PickBounds, the return is any
     * pickable node below this Locale.
     *
     * @param pickShape the description of this picking volume or area.
     *
     * @exception IllegalStateException if this Locale has been
     * removed from its VirtualUniverse.
     *
     * @see BranchGroup#pickClosest
     */
    public SceneGraphPath pickClosest( PickShape pickShape ) {
	return new SceneGraphPath( wrapped.pickClosest( pickShape.wrapped ));
    }


    /**
     * Returns a reference to any item that is Pickable below this
     * Locale which intersects with <code>pickShape</code>.
     *
     * @param pickShape the description of this picking volume or area.
     *
     * @exception IllegalStateException if this Locale has been
     * removed from its VirtualUniverse.
     *
     * @see BranchGroup#pickAny
     */
    public SceneGraphPath pickAny( PickShape pickShape ) {
	return new SceneGraphPath(wrapped.pickAny( pickShape.wrapped ));
    }

}
