/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dClass.java,v $
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
 * $Date: 2006-08-14 23:13:27 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.util.logging.Logger;

/**
 * We need to do some instanceOf checks on private/package access classes...
 *
 * TODO: This stuff might break with webstart/JNLP...
 */
public class Lg3dClass {

    protected static final Logger logger = Logger.getLogger("lg.awt.peer");

    private Class clazz;
    private Class[] declaredClasses;
    private boolean accessible;

    public Lg3dClass(Class clazz) {
	this.clazz = clazz;
    }

    public boolean isInstance(Object obj, String innerClass) {
	if (declaredClasses == null) {
	    try {
		declaredClasses = clazz.getDeclaredClasses();

	    } catch (Exception e) {
		logger.severe("Unable to get declared classes in class [" + clazz.getName() + "]");
		return false;
	    }
	}

	String innerClassName = clazz.getName() + "$" + innerClass;

	for (Class c : declaredClasses) {
	    if (c.getName().equals(innerClassName))
		return c.isInstance(obj);
	}

	logger.severe("Unable to find inner class [" + innerClass + "] in class [" + clazz.getName() + "]");
	return false;
    }
}
