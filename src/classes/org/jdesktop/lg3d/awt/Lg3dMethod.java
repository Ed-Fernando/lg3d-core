/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dMethod.java,v $
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
 * $Date: 2006-08-14 23:13:26 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * We need to call some private/package access methods in java.awt.* classes
 * This is a wrapper to do that...
 *
 * TODO: The setAccessible stuff might break with webstart/JNLP...
 */
public class Lg3dMethod {

    protected static final Logger logger = Logger.getLogger("lg.awt.peer");

    private Class clazz;
    private Class[] parameterTypes;
    private String methodName;
    private Method method;
    private boolean accessible;

    public Lg3dMethod(Class clazz, String methodName, Class... parameterTypes) {
	this.clazz = clazz;
	this.methodName = methodName;
	this.parameterTypes = parameterTypes;
    }

    private boolean init() {
	if (method == null) {
	    try {
		method = clazz.getDeclaredMethod(methodName, parameterTypes);

	    } catch (Exception e) {
		logger.severe("Unable to find method [" + methodName +
					"] in class [" + clazz.getName() + "]");
		return false;
	    }

	    // Disable accessibility checks
	    java.security.AccessController.doPrivileged
		(new java.security.PrivilegedAction() {
			public Object run() {
			    method.setAccessible(true);
			    return null;
			}
		    });

	    accessible = method.isAccessible();
	}

	return accessible;
    }

    public Object invoke(Object obj, Object... args) {
	if (init()) {
	    try {
		return method.invoke(obj, args);

	    } catch (InvocationTargetException ite) {
		logger.severe("Exception from invoked method [" + methodName +
					"] on class [" + clazz.getName() + "]");
		ite.printStackTrace();

	    } catch (Exception e) {
		logger.severe("Unable to invoke method [" + methodName +
					"] on class [" + clazz.getName() + "]");
		e.printStackTrace();
	    }
	}
        
        return null;
    }
}
