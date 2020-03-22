/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dSystem.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2006-04-25 23:04:57 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

/**
 * Provide Lg3d versions of time methods to allow us to build reproducable
 * test cases where animations are correct no matter what the frame rate.
 *
 * @author paulby
 */
public class Lg3dSystem {
    
    private static final long START_TIME = System.nanoTime();
    
    private enum Mode { TIME, FRAME };
    
    private static Mode currentMode = Mode.TIME;
    
    private static long frameTime = 0L;
    
    public static long currentTimeMillis() {
        switch(currentMode) {
            case TIME :
                return (System.nanoTime()-START_TIME)/1000000L;
            case FRAME :
                return frameTime;
        }
        
        throw new RuntimeException("Illegal Timer Mode");
    }
    
    public static long nanoTime() {
        switch(currentMode) {
            case TIME :
                return (System.nanoTime()-START_TIME);
            case FRAME :
                return frameTime;
        }
        
        throw new RuntimeException("Illegal Timer Mode");        
    }
    
    public static void nextFrame() {
        frameTime++;
    }
    
    public static long getCurrentFrame() {
        return frameTime; 
    }
}
