/**
 * Project Looking Glass
 *
 * $RCSfile: LgPreferencesHelper.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-04-27 07:28:12 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.prefs;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.prefs.Preferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.logging.Logger;

/**
 * <p>
 * A helper class that simplifies preference usage by LG3D apps
 * by implementing LG3D's conventions regarding the default initial
 * preferences settings.
 * </p>
 * <p>
 * For LG3D app classes, the convention is to place the "<tt>prefs.xml</tt>"  
 * file in the subdirectory that represents app class's package.  For example,  
 * the default initial preferences file for <tt>org.jdesktop.lg3d.apps.Test</tt> 
 * is stored in the <tt>/org/jdesktop/lg3d/apps</tt> subdirectory in the format  
 * exported by <tt>Preferences.exportNode()</tt>.
 * </p>
 * <p>
 * Here is a typical usage:
 * <code>
 *   Preferences prefs = LgPreferencesHelper.userNodeForPackage(getClass());
 *   int highScore = prefs.getInt("high-score", 0);
 * </code>
 * </p>
 * <p>
 * For LG3D system classes, the <tt>prefs.xml</tt> file is located in the
 * <tt>/etc/lg3d/skel/</tt> directory.
 * </p>
 */
public final class LgPreferencesHelper {
    private static final Logger logger = Logger.getLogger("lg.utils");
    
    /**
     * Returns the preference node from the calling user's preference tree 
     * that is associated (by convention) with the specified class's package. 
     * <p/>
     * In case for the very first time to reference this preference node,
     * it loads the default initial preferences from a file and populate 
     * it into the preference tree.  
     * In the LG3D environment, the default initial preferences file is
     * expected to be named "<tt>prefs.xml</tt>" and located in the 
     * subdirectory that represents app class's package.  
     *
     * @param c the class for whose package a user preference node is desired.
     * @return the user preference node associated with the package of which c is a member.
     */
    public static Preferences userNodeForPackage(Class<?> c) {
        String prefsPath = "/" + getPackageName(c).replace('.', '/');
        Preferences prefs = null;
        
        initLg3dDefaultIfNeeded();
        
        try {
            // check the existence of the preferences node first in the
            // following manner, since the userNodeForPackage() call creates
            // the node and returns non-null even if it doesn't exist yet.
            if (Preferences.userRoot().nodeExists(prefsPath)) {
                prefs = Preferences.userNodeForPackage(c);
            }
        } catch (BackingStoreException bse) {
            // ignore
        }
        if (prefs == null) {
            loadDefaultPreferences(c);
            prefs = Preferences.userNodeForPackage(c);
            Preferences.systemNodeForPackage(c); // just touch the system prefs
        }
//        try {
//            prefs.exportNode(System.err);
//        } catch (Exception e) {}
        
        return prefs;
    }
    
    /**
     * Returns the preference node from the system preference tree 
     * that is associated (by convention) with the specified class's package. 
     * <p/>
     * In case for the very first time to reference this preference node,
     * it loads the default initial preferences from a file and populate 
     * it into the preference tree.  
     * In the LG3D environment, the default initial preferences file is
     * expected to be named "<tt>prefs.xml</tt>" and located in the 
     * subdirectory that represents app class's package. 
     * 
     * @param c the class for whose package a user preference node is desired.
     * @return the user preference node associated with the package of which c is a member.
     */
    public static Preferences systemNodeForPackage(Class<?> c) {
        String prefsPath = "/" + getPackageName(c).replace('.', '/');
        Preferences prefs = null;
        
        initLg3dDefaultIfNeeded();
        
        try {
            // check the existence of the preferences node first in the
            // following manner, since the userNodeForPackage() call creates
            // the node and returns non-null even if it doesn't exist yet.
            if (Preferences.systemRoot().nodeExists(prefsPath)) {
                prefs = Preferences.systemNodeForPackage(c);
            }
        } catch (BackingStoreException bse) {
            // ignore
        }
        if (prefs == null) {
            loadDefaultPreferences(c);
            prefs = Preferences.systemNodeForPackage(c);
            Preferences.userNodeForPackage(c); // just touch the user prefs
        }
//        try {
//            prefs.exportNode(System.err);
//        } catch (Exception e) {}
        
        return prefs;
    }
    
    private static boolean initialized = false; // to be removed
    
    private static void initLg3dDefaultIfNeeded() {
        // FIXME -- for now, load the default preferences all the time
//        try {
//            // check the existence of the preferences node first in the
//            // following manner, since the userNodeForPackage() call creates
//            // the node and returns non-null even if it doesn't exist yet.
//            if (!Preferences.userRoot().nodeExists("/org/jdesktop/lg3d")) {
                if (initialized) return; // To be removed
                loadDefaultPreferences(LgPreferencesHelper.class, "/etc/lg3d/skel/prefs.xml");
                logger.info("LG3D Preferences have been initialized");
                initialized = true; // To be removed
//            }
//        } catch (BackingStoreException bse) {
//            // ignore
//        }
    }
    
    private static boolean loadDefaultPreferences(Class<?> c) {
        String packageName = getPackageName(c);
        return loadDefaultPreferences(c, "/" + packageName.replace('.', '/') + "/prefs.xml");
    }
    
    private static boolean loadDefaultPreferences(Class<?> c, String prefsPath) {
        InputStream is = c.getResourceAsStream(prefsPath);
        
        if (is == null) {
            logger.info("No default preferences file found: " + prefsPath);
            return false;
        }
        try {
            Preferences.importPreferences(is);
        } catch (IOException ioe) {
            logger.warning("Failed to load the default preferences: " + prefsPath);
            return false;
        } catch (InvalidPreferencesFormatException ipfe) {
            logger.warning("Error in the default preferences format: " + prefsPath);
            return false;
        } catch (NullPointerException npe) {
            // getResourceAsStream() returns an InputStream object even if
            // it doesn't find the file.  However, the object causes NPE
            // when used.
            logger.info("No default preferences file found: " + prefsPath);
            return false;
        }
        logger.info("Preferences have been initialized: " + prefsPath);
        return true;
    }
    
    private static String getPackageName(Class<?> c) {
        String name = c.getName();
        int i = name.lastIndexOf(".");
        if (i > 1) {
            name = name.substring(0, i);
        }
        return name;
    }
}
