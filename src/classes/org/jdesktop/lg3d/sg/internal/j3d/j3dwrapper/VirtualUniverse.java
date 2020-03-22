/**
 * Project Looking Glass
 *
 * $RCSfile: VirtualUniverse.java,v $
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
 * $Date: 2004-06-23 18:50:46 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import java.util.Vector;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

/**
 * A VirtualUniverse object is the top-level container for all scene
 * graphs.  A virtual universe consists of a set of Locale objects,
 * each of which has a high-resolution position within the virtual
 * universe.  An application or applet may have more than one
 * VirtualUniverse objects, but many applications will need only one.
 * Virtual universes are separate entities in that no node object may
 * exist in more than one virtual universe at any one time. Likewise,
 * the objects in one virtual universe are not visible in, nor do they
 * interact with objects in, any other virtual universe.
 * <p>
 * A VirtualUniverse object defines methods to enumerate its Locale
 * objects and to remove them from the virtual universe.
 *
 * @see Locale
 */

public class VirtualUniverse extends Object {
    // Implementation version string is obtained from the
    // ImplementationVersion class which is dynamically created
    // during the build process. 
    // See $J3D/build/bin/share/MakeImplementationVersionClass.java
    // for more details.

    // Implementation vendor: should be changed as appropriate by implementor
    private static final String J3D_VENDOR = "Sun Microsystems, Inc.";

    // Specification version (major version)
    private static final String J3D_SPECIFICATION_VERSION = "1.3";

    // Specification vendor (this should never change)
    private static final String J3D_SPECIFICATION_VENDOR = "Sun Microsystems, Inc.";

    javax.media.j3d.VirtualUniverse wrapped;

    /**
     * Constructs a new VirtualUniverse.
     */
    public VirtualUniverse() {
	wrapped = new javax.media.j3d.VirtualUniverse();
    }
    
    /**
     * Removes a Locale and its associates branch graphs from this
     * universe.  All branch graphs within the specified Locale are
     * detached, regardless of whether their ALLOW_DETACH capability
     * bits are set.  The Locale is then marked as being dead: no
     * branch graphs may subsequently be attached.
     *
     * @param locale the Locale to be removed.
     *
     * @exception IllegalArgumentException if the specified Locale is not
     * attached to this VirtualUniverse.
     *
     * @since Java 3D 1.2
     */
    public void removeLocale(Locale locale) {
	wrapped.removeLocale( locale.wrapped );
    }


    /**
     * Removes all Locales and their associates branch graphs from
     * this universe.  All branch graphs within each Locale are
     * detached, regardless of whether their ALLOW_DETACH capability
     * bits are set.  Each Locale is then marked as being dead: no
     * branch graphs may subsequently be attached.  This method
     * should be called by applications and applets to allow
     * Java 3D to cleanup its resources.
     *
     * @since Java 3D 1.2
     */
    public void removeAllLocales() {
	wrapped.removeAllLocales();
    }


    /**
     * Returns the enumeration object of all locales in this virtual universe.
     * @return the enumeration object 
     */
    public Enumeration getAllLocales() {
	throw new RuntimeException( "Not Implemented" );
    }

    /**
     * Returns the number of locales.
     * @return the count of locales
     */
    public int numLocales() {
	return wrapped.numLocales();
    }

    /**
     * Returns a read-only Map object containing key-value pairs that
     * define various global properties for Java 3D.  All of the keys
     * are String objects.  The values are key-specific, but most will
     * be String objects.
     *
     * <p>
     * The set of global Java 3D properties always includes values for
     * the following keys:
     *
     * <p>
     * <ul>
     * <table BORDER=1 CELLSPACING=1 CELLPADDING=1>
     * <tr>
     * <td><b>Key (String)</b></td>
     * <td><b>Value Type</b></td>
     * </tr>
     * <tr>
     * <td><code>j3d.version</code></td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td><code>j3d.vendor</code></td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td><code>j3d.specification.version</code></td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td><code>j3d.specification.vendor</code></td>
     * <td>String</td>
     * </tr>
     * <tr>
     * <td><code>j3d.renderer</code></td>
     * <td>String</td>
     * </tr>
     * </table>
     * </ul>
     *
     * <p>
     * The descriptions of the values returned for each key are as follows:
     *
     * <p>
     * <ul>
     *
     * <li>
     * <code>j3d.version</code>
     * <ul>
     * A String that defines the Java 3D implementation version.
     * The portion of the implementation version string before the first
     * space must adhere to one of the the following three formats
     * (anything after the first space is an optional free-form addendum
     * to the version):
     * <ul>
     * <i>x</i>.<i>y</i>.<i>z</i><br>
     * <i>x</i>.<i>y</i>.<i>z</i>_<i>p</i><br>
     * <i>x</i>.<i>y</i>.<i>z</i>-<i>ssss</i><br>
     * </ul>
     * where:
     * <ul>
     * <i>x</i> is the major version number<br>
     * <i>y</i> is the minor version number<br>
     * <i>z</i> is the sub-minor version number<br>
     * <i>p</i> is the patch revision number <br>
     * <i>ssss</i> is a string, identifying a non-release build
     * (e.g., beta1, build47, rc1, etc.).  It may only
     * contain letters, numbers, periods, dashes, or
     * underscores.
     * </ul>
     * </ul>
     * </li>
     * <p>
     *
     * <li>
     * <code>j3d.vendor</code>
     * <ul>
     * String that specifies the Java 3D implementation vendor.
     * </ul>
     * </li>
     * <p>
     *
     * <li>
     * <code>j3d.specification.version</code>
     * <ul>
     * A String that defines the Java 3D specification version.
     * This string must be of the following form:
     * <ul>
     * <i>x</i>.<i>y</i>
     * </ul>
     * where:
     * <ul>
     * <i>x</i> is the major version number<br>
     * <i>y</i> is the minor version number<br>
     * </ul>
     * No other characters are allowed in the specification version string.
     * </ul>
     * </li>
     * <p>
     *
     * <li>
     * <code>j3d.specification.vendor</code>
     * <ul>
     * String that specifies the Java 3D specification vendor.
     * </ul>
     * </li>
     * <p>
     *
     * <li>
     * <code>j3d.renderer</code>
     * <ul>
     * String that specifies the Java 3D rendering library.  This could
     * be one of: "OpenGL" or "DirectX".
     * </ul>
     * </li>
     * <p>
     *
     * </ul>
     *
     * @return the global Java 3D properties
     *
     * @since Java 3D 1.3
     */
    public static final Map getProperties() {
        throw new RuntimeException("Not implemented");
//	if (properties == null) {
//	    // Create lists of keys and values
//	    ArrayList keys = new ArrayList();
//	    ArrayList values = new ArrayList();
//
//            // Implementation version string is obtained from the
//            // ImplementationVersion class which is dynamically created
//            // during the build process. 
//            // See $J3D/build/bin/share/MakeImplementationVersionClass.java
//            // for more details.
//	    keys.add("j3d.version");
//	    values.add(ImplementationVersion.getJ3dVersion());
//
//	    keys.add("j3d.vendor");
//	    values.add(J3D_VENDOR);
//
//	    keys.add("j3d.specification.version");
//	    values.add(J3D_SPECIFICATION_VERSION);
//
//	    keys.add("j3d.specification.vendor");
//	    values.add(J3D_SPECIFICATION_VENDOR);
//
//	    keys.add("j3d.renderer");
//	    values.add(mc.isD3D() ? "DirectX" : "OpenGL");
//
//	    // Now Create read-only properties object
//	    properties =
//		new J3dQueryProps((String[]) keys.toArray(new String[0]),
//				  values.toArray());
//	}
//  	return properties;
    }

}


