/**
 * Project Looking Glass
 *
 * $RCSfile: J3dI18N.java,v $
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

import java.io.*;
import java.util.*;


class J3dI18N {
    static String getString(String key) {
	String s;
	try {
	    s = (String) ResourceBundle.getBundle("org.jdesktop.lg3d.sg.ExceptionStrings").getString(key);
	}
	catch (MissingResourceException e) {
	    System.err.println("J3dI18N: Error looking up: " + key);
	    s = key;
	}
	return s;
    }
}
