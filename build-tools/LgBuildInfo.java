/**
 * Project Looking Glass
 *
 * $RCSfile: LgBuildInfo.java,v $
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
 * $Date: 2006-06-06 23:48:18 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

class LgBuildInfo {
    /**
     * Constant that indicates whether or not this is
     * a debug build.
     */
    static final boolean isDebug = @IS_DEBUG@;

    private static final String BUILD_TIME_STAMP = "@BUILD_TIME_STAMP@";
    
    private static final String BUILD_DATE = "@BUILD_DATE@";
    
    private static final String BUILD_TYPE = "@BUILD_TYPE@";
    
    private static final String BUILD_VERSION = "@BUILD_VERSION@";
    
    private static final String JAVA_VERSION = "@JAVA_VERSION@";

    /**
     * Returns the build time stamp, in the form yyMMddHHmm.
     * This is set by the build.xml file.
     *
     * @return the build time stamp
     */
    static String getBuildTimeStamp() {
        return BUILD_TIME_STAMP;
    }
    
    /**
     * Returns the build date, this is a more readable form
     * of the build time stamp
     */
    static String getBuildDate() {
        return BUILD_DATE;
    }
    
    /**
     * Returns the build type, either user, daily
     */
    static String getBuildType() {
        return BUILD_TYPE;
    }
    
    /**
     * Returns the version, dev-0-6-1-1
     */
    static String getVersion() {
        return BUILD_VERSION;
    }
    
    /**
     * Returns the java version used to compile lg3d
     */
    static String getJavaVersion() {
        return JAVA_VERSION;
    }

    /**
     * Do not construct an instance of this class.
     */
    private LgBuildInfo() {
    }
}
