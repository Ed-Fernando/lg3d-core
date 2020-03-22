
/**
 * Project Looking Glass
 *
 * $RCSfile: StatBuf.java,v $
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
 * $Date: 2005-06-24 19:48:08 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import java.lang.Math;

public class StatBuf {

    public static final int STATBUF_DEFAULT_SIZE = 1000;

    // For debug
    private static final boolean dumpVerbose = false;

    private int    size;
    private int    next;
    private float[]  buffer;
    private float  min;
    private float  max;
    private float  average;
    private float  stdDev;

    public StatBuf () {
	this(STATBUF_DEFAULT_SIZE);
    }

    public StatBuf (int size) {
	this.size = size;
	this.next = 0;
	this.buffer = new float[size];
    }

    public void add (float meas) {
	if (next >= size) {
	    dumpStatsAllSets();
	    next = 0;
	}

	buffer[next] = meas;
	next++;
    }

    public void dumpAndClear () {
	dumpStatsAllSets();
	next = 0;
    }

    public void dumpStatsAllSets () {
	if (size >= 10) {
	    dumpStats(10);
	}
	if (size >= 100) {
	    dumpStats(100);
	}
	dumpStats(size);
    }

    private void dumpStats (int n) {
	calcStats(n);

	System.err.println();
	System.err.println("Stats for first " + n + " measurements");
	System.err.println("Average = " + average);
	System.err.println("Std Dev = " + stdDev);
	System.err.println("Max     = " + max);
	System.err.println("Min     = " + min);

	if (dumpVerbose) {
	    for (int i = 0; i < n; i++) {
		float meas = buffer[i];
		if        (meas > average) {
		    System.err.print("+ ");
		} else if (meas < average) {
		    System.err.print("- ");
		}
		System.err.println(meas);
	    }
	}    
    }

    private void calcStats (int n) {
	float sum = 0;

	min = Float.MAX_VALUE;
	max = Float.MIN_VALUE;

	// Calculate min,max and prepare for average 
	for (int i = 0; i < n; i++) {
	    float meas = buffer[i];
	    sum += meas;
	    if (meas < min) {
		min = meas;
	    }
	    if (meas > max) {
		max = meas;
	    }
	}

	average = sum / (float)n; 

	// Calculate standard deviation 
	sum = 0;
	for (int i = 0; i < n; i++) {
	    float meas = buffer[i];
	    float term = meas - average;
	    term = term * term;
	    sum += term;
	}
	sum /= (float)n;
	stdDev = (float)Math.sqrt((double)sum);
    }
}
