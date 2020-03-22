/**
 * Project Looking Glass
 *
 * $RCSfile: AlphaRemote.java,v $
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
 * $Date: 2004-06-23 18:51:01 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;


/**
 * The alpha NodeComponent object provides common methods for
 * converting a time value into an alpha value (a value in the range 0
 * to 1).  The Alpha object is effectively a function of time that
 * generates alpha values in the range [0,1] when sampled: f(t) =
 * [0,1].  A primary use of the Alpha object is to provide alpha
 * values for Interpolator behaviors.  The function f(t) and the
 * characteristics of the Alpha object are determined by
 * user-definable parameters:
 *
 * <p>
 * <ul>
 *
 * <code>loopCount</code> -- This is the number of times to run this
 * Alpha; a value of -1 specifies that the Alpha loops
 * indefinitely.<p>
 *
 * <code>triggerTime</code> -- This is the time in milliseconds since
 * the start time that this object first triggers.  If (startTime +
 * triggerTime >= currentTime) then the Alpha object starts running.<p>
 *
 * <code>phaseDelayDuration</code> -- This is an additional number of
 * milliseconds to wait after triggerTime before actually starting
 * this Alpha.<p>
 *
 * <code>mode</code> -- This can be set to INCREASING_ENABLE,
 * DECREASING_ENABLE, or the Or'ed value of the two.
 * INCREASING_ENABLE activates the increasing Alpha parameters listed
 * below; DECREASING_ENABLE activates the decreasing Alpha parameters
 * listed below.<p>
 *
 * </ul> Increasing Alpha parameters:<p> <ul>
 *
 * <code>increasingAlphaDuration</code> -- This is the period of time
 * during which Alpha goes from zero to one. <p>
 *
 * <code>increasingAlphaRampDuration</code> -- This is the period of
 * time during which the Alpha step size increases at the beginning of
 * the increasingAlphaDuration and, correspondingly, decreases at the
 * end of the increasingAlphaDuration.  This parameter is clamped to
 * half of increasingAlphaDuration.  When this parameter is non-zero,
 * one gets constant acceleration while it is in effect; constant
 * positive acceleration at the beginning of the ramp and constant
 * negative acceleration at the end of the ramp.  If this parameter is
 * zero, then the effective velocity of the Alpha value is constant
 * and the acceleration is zero (ie, a linearly increasing alpha
 * ramp).<p>
 *
 * <code>alphaAtOneDuration</code> -- This is the period of time that
 * Alpha stays at one.<p> </ul> Decreasing Alpha parameters:<p> <ul>
 *
 * <code>decreasingAlphaDuration</code> -- This is the period of time
 * during which Alpha goes from one to zero.<p>
 *
 * <code>decreasingAlphaRampDuration</code> -- This is the period of
 * time during which the Alpha step size increases at the beginning of
 * the decreasingAlphaDuration and, correspondingly, decreases at the
 * end of the decreasingAlphaDuration.  This parameter is clamped to
 * half of decreasingAlphaDuration.  When this parameter is non-zero,
 * one gets constant acceleration while it is in effect; constant
 * positive acceleration at the beginning of the ramp and constant
 * negative acceleration at the end of the ramp.  If this parameter is
 * zero, the effective velocity of the Alpha value is constant and the
 * acceleration is zero (i.e., a linearly-decreasing alpha ramp).<p>
 *
 * <code>alphaAtZeroDuration</code> -- This is the period of time that
 * Alpha stays at zero.
 *
 * </ul>
 *
 * @see Interpolator
 */

public interface AlphaRemote extends NodeComponentRemote {

    /**
     * Pauses this alpha object.  The current system time when this
     * method is called will be used in place of the actual current
     * time when calculating subsequent alpha values.  This has the
     * effect of freezing the interpolator at the time the method is
     * called.
     *
     * @since Java 3D 1.3
     */
    public void pause() throws java.rmi.RemoteException;

    /**
     * Pauses this alpha object as of the specified time.  The specified
     * time will be used in place of the actual current time when
     * calculating subsequent alpha values.  This has the effect of freezing
     * the interpolator at the specified time.  Note that specifying a
     * time in the future (that is, a time greater than
     * System.currentTimeMillis()) will cause the alpha to immediately
     * advance to that point before pausing.  Similarly, specifying a
     * time in the past (that is, a time less than
     * System.currentTimeMillis()) will cause the alpha to immediately
     * revert to that point before pausing.
     *
     * @param time the time at which to pause the alpha
     *
     * @exception IllegalArgumentException if time <= 0
     *
     * @since Java 3D 1.3
     */
    public void pause(long time) throws java.rmi.RemoteException;

    /**
     * Resumes this alpha object.  If the alpha
     * object was paused, the difference between the current
     * time and the pause time will be used to adjust the startTime of
     * this alpha.  The equation is as follows:
     *
     * <ul>
     * <code>startTime += System.currentTimeMillis() - pauseTime</code>
     * </ul>
     *
     * Since the alpha object is no longer paused, this has the effect
     * of resuming the interpolator as of the current time.  If the
     * alpha object is not paused when this method is called, then this
     * method does nothing--the start time is not adjusted in this case.
     *
     * @since Java 3D 1.3
     */
    public void resume() throws java.rmi.RemoteException;

    /**
     * Resumes this alpha object as of the specified time.  If the alpha
     * object was paused, the difference between the specified
     * time and the pause time will be used to adjust the startTime of
     * this alpha.  The equation is as follows:
     *
     * <ul><code>startTime += time - pauseTime</code></ul>
     *
     * Since the alpha object is no longer paused, this has the effect
     * of resuming the interpolator as of the specified time.  If the
     * alpha object is not paused when this method is called, then this
     * method does nothing--the start time is not adjusted in this case.
     *
     * @param time the time at which to resume the alpha
     *
     * @exception IllegalArgumentException if time <= 0
     *
     * @since Java 3D 1.3
     */
    public void resume(long time) throws java.rmi.RemoteException;

    /**
     * Returns true if this alpha object is paused.
     * @return true if this alpha object is paused, false otherwise
     *
     * @since Java 3D 1.3
     */
    public boolean isPaused() throws java.rmi.RemoteException;

    /**
     * Returns the time at which this alpha was paused.
     * @return the pause time; returns 0 if this alpha is not paused
     *
     * @since Java 3D 1.3
     */
    public long getPauseTime() throws java.rmi.RemoteException;


    /**
     * This method returns a value between 0.0 and 1.0 inclusive,
     * based on the current time and the time-to-alpha parameters
     * established for this alpha.  If this alpha object is paused,
     * the value will be based on the pause time rather than the
     * current time.
     * This method will return the starting alpha value if the alpha
     * has not yet started (that is, if the current time is less
     * than startTime + triggerTime + phaseDelayDuration). This
     * method will return the ending alpha value if the alpha has
     * finished (that is, if the loop count has expired).
     *
     * @return a value between 0.0 and 1.0 based on the current time
     */
    public float value() throws java.rmi.RemoteException;

    /**
     * This method returns a value between 0.0 and 1.0 inclusive,
     * based on the specified time and the time-to-alpha parameters
     * established for this alpha.
     * This method will return the starting alpha value if the alpha
     * has not yet started (that is, if the specified time is less
     * than startTime + triggerTime + phaseDelayDuration). This
     * method will return the ending alpha value if the alpha has
     * finished (that is, if the loop count has expired).
     *
     * @param atTime The time for which we wish to compute alpha
     * @return a value between 0.0 and 1.0 based on the specified time
     */
    public float value(long atTime) throws java.rmi.RemoteException;

    /**
      * Retrieves this alpha's startTime, the base
      * for all relative time specifications; the default value
      * for startTime is the system start time.
      * @return this alpha's startTime.
      */
    public long getStartTime() throws java.rmi.RemoteException;

    /**
     * Sets this alpha's startTime to that specified in the argument; 
     * startTime sets the base (or zero) for all relative time
     * computations; the default value for startTime is the system
     * start time.
     * @param startTime the new startTime value
     */
    public void setStartTime(long startTime) throws java.rmi.RemoteException;

    /**
      * Retrieves this alpha's loopCount.
      * @return this alpha's loopCount.
      */
    public int getLoopCount() throws java.rmi.RemoteException;

    /**
      * Set this alpha's loopCount to that specified in the argument.
      * @param loopCount the new loopCount value
      */
    public void setLoopCount(int loopCount) throws java.rmi.RemoteException;

    /**
      * Retrieves this alpha's mode.
      * @return this alpha's mode: any combination of
      * INCREASING_ENABLE and DECREASING_ENABLE
      */
    public int getMode() throws java.rmi.RemoteException;

    /**
      * Set this alpha's mode to that specified in the argument.
     * @param mode indicates whether the increasing alpha parameters or
     * the decreasing alpha parameters or both are active.  This parameter
     * accepts the following values, INCREASING_ENABLE or
     * DECREASING_ENABLE, which may be ORed together to specify
     * that both are active.
     * The increasing alpha parameters are increasingAlphaDuration,
     * increasingAlphaRampDuration, and alphaAtOneDuration.
     * The decreasing alpha parameters are decreasingAlphaDuration,
     * decreasingAlphaRampDuration, and alphaAtZeroDuration.
      */
    public void setMode(int mode) throws java.rmi.RemoteException;

    /**
      * Retrieves this alpha's triggerTime.
      * @return this alpha's triggerTime.
      */
    public long getTriggerTime() throws java.rmi.RemoteException;

    /**
      * Set this alpha's triggerTime to that specified in the argument.
      * @param triggerTime  the new triggerTime
      */
    public void setTriggerTime(long triggerTime) throws java.rmi.RemoteException;
    /**
      * Retrieves this alpha's phaseDelayDuration.
      * @return this alpha's phaseDelayDuration.
      */
    public long getPhaseDelayDuration() throws java.rmi.RemoteException;

    /**
      * Set this alpha's phaseDelayDuration to that specified in 
      * the argument.
      * @param phaseDelayDuration  the new phaseDelayDuration
      */
    public void setPhaseDelayDuration(long phaseDelayDuration) throws java.rmi.RemoteException;
    /**
      * Retrieves this alpha's increasingAlphaDuration.
      * @return this alpha's increasingAlphaDuration.
      */
    public long getIncreasingAlphaDuration() throws java.rmi.RemoteException;

    /**
      * Set this alpha's increasingAlphaDuration to that specified in 
      * the argument.
      * @param increasingAlphaDuration  the new increasingAlphaDuration
      */
    public void setIncreasingAlphaDuration(long increasingAlphaDuration) throws java.rmi.RemoteException;

    /**
      * Retrieves this alpha's increasingAlphaRampDuration.
      * @return this alpha's increasingAlphaRampDuration.
      */
    public long getIncreasingAlphaRampDuration() throws java.rmi.RemoteException;

    /**
      * Set this alpha's increasingAlphaRampDuration to that specified 
      * in the argument.
      * @param increasingAlphaRampDuration  the new increasingAlphaRampDuration
      */
    public void setIncreasingAlphaRampDuration(long increasingAlphaRampDuration) throws java.rmi.RemoteException;

    /**
      * Retrieves this alpha's alphaAtOneDuration.
      * @return this alpha's alphaAtOneDuration.
      */
    public long getAlphaAtOneDuration() throws java.rmi.RemoteException;

    /**
     * Set this alpha object's alphaAtOneDuration to the specified 
     * value.
     * @param alphaAtOneDuration  the new alphaAtOneDuration
     */
    public void setAlphaAtOneDuration(long alphaAtOneDuration) throws java.rmi.RemoteException;

    /**
      * Retrieves this alpha's decreasingAlphaDuration.
      * @return this alpha's decreasingAlphaDuration.
      */
    public long getDecreasingAlphaDuration() throws java.rmi.RemoteException;

    /**
      * Set this alpha's decreasingAlphaDuration to that specified in 
      * the argument.
      * @param decreasingAlphaDuration  the new decreasingAlphaDuration
      */
    public void setDecreasingAlphaDuration(long decreasingAlphaDuration) throws java.rmi.RemoteException;

    /**
      * Retrieves this alpha's decreasingAlphaRampDuration.
      * @return this alpha's decreasingAlphaRampDuration.
      */
    public long getDecreasingAlphaRampDuration() throws java.rmi.RemoteException;

    /**
      * Set this alpha's decreasingAlphaRampDuration to that specified 
      * in the argument.
      * @param decreasingAlphaRampDuration  the new decreasingAlphaRampDuration
      */
    public void setDecreasingAlphaRampDuration(long decreasingAlphaRampDuration) throws java.rmi.RemoteException;

    /**
      * Retrieves this alpha's alphaAtZeroDuration.
      * @return this alpha's alphaAtZeroDuration.
      */
    public long getAlphaAtZeroDuration() throws java.rmi.RemoteException;
    /**
     * Set this alpha object's alphaAtZeroDuration to the specified 
     * value.
     * @param alphaAtZeroDuration  the new alphaAtZeroDuration
     */
    public void setAlphaAtZeroDuration(long alphaAtZeroDuration) throws java.rmi.RemoteException;

    /**
     * Query to test if this alpha object is past its activity window,
     * that is, if it has finished looping.
     * @return true if no longer looping, false otherwise
     */
    public boolean finished() throws java.rmi.RemoteException;
}
