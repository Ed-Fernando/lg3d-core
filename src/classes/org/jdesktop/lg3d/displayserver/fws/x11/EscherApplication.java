/**
 * Project Looking Glass
 *
 * $RCSfile: EscherApplication.java,v $
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
 * $Date: 2005-01-20 22:05:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import gnu.x11.Display;
import gnu.x11.Application;

class EscherApplication extends Application {

    public EscherApplication (String[] argStrings) {
	this(argStrings, null);
    }

    public EscherApplication (String[] argStrings, String clientThreadName) {
	super(argStrings, clientThreadName);
    }

    public Display getDisplay () { return display; }
}

