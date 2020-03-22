/**
 * Project Looking Glass
 *
 * $RCSfile: BehaviorWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;


import java.util.Enumeration;

/**
 * The Behavior leaf node provides a framework for adding user-defined
 * actions into the scene graph.  Behavior is an abstract class that
 * defines two methods that must be overridden by a subclass: An
 * <code>initialization</code> method, called once when the behavior
 * becomes "live," and a <code>processStimulus</code> method called
 * whenever appropriate by the Java 3D behavior scheduler. The
 * Behavior node also contains an enable flag, a scheduling region,
 * a scheduling interval, and a wakeup condition.
 *
 * <P>
 * The <i>scheduling region</i> defines a spatial volume that serves
 * to enable the scheduling of Behavior nodes. A Behavior node is
 * <i>active</i> (can receive stimuli) whenever an active ViewPlatform's
 * activation volume intersects a Behavior object's scheduling
 * region. Only active behaviors can receive stimuli.
 *
 * <P>
 * The <i>scheduling interval</i> defines a partial order of execution
 * for behaviors that wake up in response to the same wakeup condition
 * (that is, those behaviors that are processed at the same "time").
 * Given a set of behaviors whose wakeup conditions are satisfied at
 * the same time, the behavior scheduler will execute all behaviors in
 * a lower scheduling interval before executing any behavior in a
 * higher scheduling interval.  Within a scheduling interval,
 * behaviors can be executed in any order, or in parallel.  Note that
 * this partial ordering is only guaranteed for those behaviors that
 * wake up at the same time in response to the same wakeup condition,
 * for example, the set of behaviors that wake up every frame in
 * response to a WakeupOnElapsedFrames(0) wakeup condition.
 *
 * <P>
 * The <code>initialize</code> method allows a Behavior object to
 * initialize its internal state and specify its initial wakeup
 * condition(s). Java 3D invokes a behavior's initialize code when the
 * behavior's containing BranchGroup node is added to the virtual
 * universe. Java 3D does not invoke the initialize method in a new
 * thread.  Thus, for Java 3D to regain control, the initialize method
 * must not execute an infinite loop; it must return. Furthermore, a
 * wakeup condition must be set or else the behavior's processStimulus
 * method is never executed.
 *
 * <P>
 * The <code>processStimulus</code> method receives and processes a
 * behavior's ongoing messages. The Java 3D behavior scheduler invokes
 * a Behavior node's processStimulus method when an active ViewPlatform's
 * activation volume intersects a Behavior object's scheduling region
 * and all of that behavior's wakeup criteria are satisfied. The
 * processStimulus method performs its computations and actions
 * (possibly including the registration of state change information
 * that could cause Java 3D to wake other Behavior objects),
 * establishes its next wakeup condition, and finally exits.
 * A typical behavior will modify one or more nodes or node components
 * in the scene graph.  These modifications can happen in parallel
 * with rendering.  In general, applications cannot count on behavior
 * execution being synchronized with rendering.  There are two
 * exceptions to this general rule:
 * <ol>
 * <li>All modifications to scene graph objects (not including geometry
 * by-reference or texture by-reference) made from the
 * <code>processStimulus</code> method of a single behavior instance
 * are guaranteed to take effect in the same rendering frame.</li>
 * <li>All modifications to scene graph objects (not including geometry
 * by-reference or texture by-reference) made from the
 * <code>processStimulus</code> methods of the set of behaviors that
 * wake up in response to a WakeupOnElapsedFrames(0) wakeup condition
 * are guaranteed to take effect in the same rendering frame.</li>
 * </ol>
 *
 * Note that modifications to geometry by-reference or texture
 * by-reference are not guaranteed to show up in the same frame as
 * other scene graph changes.
 *
 * <P>
 * <b>Code Structure</b>
 * <P>
 * When the Java 3D behavior scheduler invokes a Behavior object's
 * processStimulus method, that method may perform any computation it
 * wishes.  Usually, it will change its internal state and specify its
 * new wakeup conditions.  Most probably, it will manipulate scene
 * graph elements. However, the behavior code can only change those
 * aspects of a scene graph element permitted by the capabilities
 * associated with that scene graph element. A scene graph's
 * capabilities restrict behavioral manipulation to those
 * manipulations explicitly allowed.
 *
 * <P>
 * The application must provide the Behavior object with references to
 * those scene graph elements that the Behavior object will
 * manipulate. The application provides those references as arguments
 * to the behavior's constructor when it creates the Behavior
 * object. Alternatively, the Behavior object itself can obtain access
 * to the relevant scene graph elements either when Java 3D invokes
 * its initialize method or each time Java 3D invokes its
 * processStimulus method.
 *
 * <P>
 * Behavior methods have a very rigid structure. Java 3D assumes that
 * they always run to completion (if needed, they can spawn
 * threads). Each method's basic structure consists of the following:
 *
 * <P>
 * <UL>
 * <LI>Code to decode and extract references from the WakeupCondition
 * enumeration that caused the object's awakening.</LI>
 * <LI>Code to perform the manipulations associated with the
 * WakeupCondition</LI>
 * <LI>Code to establish this behavior's new WakeupCondition</LI>
 * <LI>A path to Exit (so that execution returns to the Java 3D
 * behavior scheduler)</LI>
 * </UL>
 *
 * <P>
 * <b>WakeupCondition Object</b>
 * <P>
 * A WakeupCondition object is an abstract class specialized to
 * fourteen different WakeupCriterion objects and to four combining
 * objects containing multiple WakeupCriterion objects.  A Behavior
 * node provides the Java 3D behavior scheduler with a WakeupCondition
 * object. When that object's WakeupCondition has been satisfied, the
 * behavior scheduler hands that same WakeupCondition back to the
 * Behavior via an enumeration.
 *
 * <P>
 * <b>WakeupCriterion Object</b>
 * <P>
 * Java 3D provides a rich set of wakeup criteria that Behavior
 * objects can use in specifying a complex WakeupCondition. These
 * wakeup criteria can cause Java 3D's behavior scheduler to invoke a
 * behavior's processStimulus method whenever
 *
 * <UL>
 * <LI>The center of a ViewPlatform enters a specified region</LI>
 * <LI>The center of a ViewPlatform exits a specified region</LI>
 * <LI>A behavior is activated</LI>
 * <LI>A behavior is deactivated</LI>
 * <LI>A specified TransformGroup node's transform changes</LI>
 * <LI>Collision is detected between a specified Shape3D node's
 * Geometry object and any other object</LI>
 * <LI>Movement occurs between a specified Shape3D node's Geometry
 * object and any other object with which it collides</LI>
 * <LI>A specified Shape3D node's Geometry object no longer collides
 * with any other object</LI>
 * <LI>A specified Behavior object posts a specific event</LI>
 * <LI>A non-Behavior object posts a specific event</LI>
 * <LI>A specified AWT event occurs</LI>
 * <LI>A specified time interval elapses</LI>
 * <LI>A specified number of frames have been drawn</LI>
 * <LI>The center of a specified Sensor enters a specified region</LI>
 * <LI>The center of a specified Sensor exits a specified region</LI>
 * </UL>
 *
 * <p>
 * A Behavior object constructs a WakeupCriterion by constructing the
 * appropriate criterion object. The Behavior object must provide the
 * appropriate arguments (usually a reference to some scene graph
 * object and possibly a region of interest). Thus, to specify a
 * WakeupOnViewPlatformEntry, a behavior would specify the region that
 * will cause the behavior to execute if an active ViewPlatform enters it.
 *
 * <p>
 * Note that a unique WakeupCriterion object must be used with each
 * instance of a Behavior. Sharing wakeup criteria among different
 * instances of a Behavior is illegal.
 *
 * @see WakeupCondition
 */

public interface BehaviorWrapper extends LeafWrapper {

    /**
     * Set the Behavior's scheduling region to the specified bounds.
     * This is used when the scheduling bounding leaf is set to null.
     * @param region the bounds that contains the Behavior's new scheduling
     * region
     */  
    public void setSchedulingBounds(BoundsWrapper region) ;
    /**  
     * Retrieves the Behavior node's scheduling bounds.
     * @return this Behavior's scheduling bounds information
     */  
    public BoundsWrapper getSchedulingBounds() ;

    /**
     * Set the Behavior's scheduling region to the specified bounding leaf.
     * When set to a value other than null, this overrides the scheduling
     * bounds object.
     * @param region the bounding leaf node used to specify the Behavior
     * node's new scheduling region
     */  
//    private void setSchedulingBoundingLeaf(BoundingLeaf region) {
//	((javax.media.j3d.Behavior)wrapped).setSchedulingBoundingLeaf(region.wrapped);
//    }

    /**  
     * Retrieves the Behavior node's scheduling bounding leaf.
     * @return this Behavior's scheduling bounding leaf information
     */  
//    private BoundingLeaf getSchedulingBoundingLeaf() {
//        throw new RuntimeException("Not Implemented");
//	//return ((BehaviorRetained)this.retained).getSchedulingBoundingLeaf();
//    }

    /**
     * Defines this behavior's wakeup criteria.  This method
     * may only be called from a Behavior object's initialize
     * or processStimulus methods to (re)arm the next wakeup.
     * It should be the last thing done by those methods.
     * @param criteria the wakeup criteria for this behavior
     * @exception IllegalStateException if this method is called by
     * a method <i>other than</i> initialize or processStimulus
     */
//    protected void wakeupOn(WakeupCondition criteria) {
//        ((javax.media.j3d.Behavior)wrapped).wakeupOn( criteria.wrapped );
//    }

    /**
     * Retrieves this behavior's current wakeup condition as set by
     * the wakeupOn method.  If no wakeup condition is currently
     * active, null will be returned.  In particular, this means that
     * null will be returned if Java 3D is executing this behavior's
     * processStimulus routine and wakeupOn has not yet been called to
     * re-arm the wakeup condition for next time.
     *
     * @return the current wakeup condition for this behavior
     *
     * @since Java 3D 1.3
     */
//    protected WakeupCondition getWakeupCondition() {
//        throw new RuntimeException( "Not Implemented" );
//	//return ((Behavior)this.retained).getWakeupCondition();
//    }

    /**
     * Posts the specified postId to the Behavior Scheduler. 
     * <p>
     * If this method is invoked from a behavior object, this will satisfy the 
     * wakeup condition for any behaviors which have registered WakeupOnBehaviorPost 
     * with this behavior, or a behavior of null, and whose postId equals this postID 
     * or 0.
     * </p>
     * <p>
     * If this method is invoked from a non-behavior object, this will satisfy the 
     * wakeup condition for any behaviors which have registered WakeupOnBehaviorPost 
     * with a null behavior and whose postId equals this postId or 0.
     * </p>
     * <p>
     * This feature allows applications to send arbitrary events into the
     * behavior scheduler stream.  It can be used as a notification scheme
     * for communicating events to behaviors in the system.
     * @param postId the Id being posted
     * </p>
     * @see WakeupOnBehaviorPost
     */
    public void postId(int postId) ;

    /**
     * Enables or disables this Behavior.  The default state is enabled.
     * @param  state  true or false to enable or disable this Behavior
     */
    public void setEnable(boolean state) ;

    /**
     * Retrieves the state of the Behavior enable flag.
     * @return the Behavior enable state
     */
    public boolean getEnable() ;

    /**
     * Returns the number of scheduling intervals supported by this
     * implementation of Java 3D.  The minimum number of supported
     * intervals must be at least 10.  The default scheduling interval
     * for each behavior instance is set to
     * <code>numSchedulingIntervals / 2</code>.
     *
     * @return the number of supported scheduling intervals
     *
     * @since Java 3D 1.3
     */
    //public static int getNumSchedulingIntervals() ;


    /**
     * Sets the scheduling interval of this Behavior node to the
     * specified value.
     *
     * The scheduling interval defines a partial order of execution
     * for behaviors that wake up in response to the same wakeup
     * condition (that is, those behaviors that are processed at the
     * same "time").  Given a set of behaviors whose wakeup conditions
     * are satisfied at the same time, the behavior scheduler will
     * execute all behaviors in a lower scheduling interval before
     * executing any behavior in a higher scheduling interval.  Within
     * a scheduling interval, behaviors can be executed in any order,
     * or in parallel.  Note that this partial ordering is only
     * guaranteed for those behaviors that wake up at the same time in
     * response to the same wakeup condition, for example, the set of
     * behaviors that wake up every frame in response to a
     * WakeupOnElapsedFrames(0) wakeup condition.
     *
     * The default value is <code>numSchedulingIntervals / 2</code>.
     *
     * @param schedulingInterval the new scheduling interval
     *
     * @exception IllegalArgumentException if
     * <code>schedulingInterval</code> < 0 or
     * <code>schedulingInterval</code> >=
     * <code>numSchedulingIntervals</code>
     *
     * @since Java 3D 1.3
     */
    public void setSchedulingInterval(int schedulingInterval) ;

    /**
     * Retrieves the current scheduling interval of this Behavior
     * node.
     *
     * @return the current scheduling interval
     *
     * @since Java 3D 1.3
     */
    public int getSchedulingInterval() ;

    /**
     * Returns the primary view associated with this behavior.  This method
     * is useful with certain types of behaviors (e.g., Billboard, LOD) that
     * rely on per-View information and with behaviors in general in regards
     * to scheduling (the distance from the view platform determines the
     * active behaviors).   The "primary" view is defined to be the first
     * View attached to a live ViewPlatform, if there is more than one active
     * View.  So, for instance, Billboard behaviors would be oriented toward
     * this primary view, in the case of multiple active views into the same
     * scene graph.
     */ 
//    protected View getView() {
//        return ((BehaviorRetained)this.retained).getView();
//    }



    /**
     * Callback used to allow a node to check if any scene graph objects
     * referenced
     * by that node have been duplicated via a call to <code>cloneTree</code>.
     * This method is called by <code>cloneTree</code> after all nodes in
     * the sub-graph have been duplicated. The cloned Leaf node's method
     * will be called and the Leaf node can then look up any object references
     * by using the <code>getNewObjectReference</code> method found in the
     * <code>NodeReferenceTable</code> object.  If a match is found, a
     * reference to the corresponding object in the newly cloned sub-graph
     * is returned.  If no corresponding reference is found, either a
     * DanglingReferenceException is thrown or a reference to the original
     * object is returned depending on the value of the
     * <code>allowDanglingReferences</code> parameter passed in the
     * <code>cloneTree</code> call.
     * <p>
     * NOTE: Applications should <i>not</i> call this method directly.
     * It should only be called by the cloneTree method.
     *
     * @param referenceTable a NodeReferenceTableObject that contains the
     *  <code>getNewObjectReference</code> method needed to search for
     *  new object instances.
     *
     * @see NodeReferenceTable
     * @see Node#cloneTree
     * @see DanglingReferenceException
     */
//    public void updateNodeReferences(NodeReferenceTable referenceTable) {
//        super.updateNodeReferences(referenceTable);
//
//	BehaviorRetained rt = (BehaviorRetained) retained;
//	BoundingLeaf bl=  rt.getSchedulingBoundingLeaf();
//
//        // check for schedulingBoundingLeaf
//        if (bl != null) {
//	    Object o = referenceTable.getNewObjectReference(bl);
//            rt.setSchedulingBoundingLeaf((BoundingLeaf) o);
//			      
//        }
//    }

}
