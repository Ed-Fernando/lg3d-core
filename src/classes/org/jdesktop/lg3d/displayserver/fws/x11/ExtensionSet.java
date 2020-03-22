/**
 * Project Looking Glass
 *
 * $RCSfile: ExtensionSet.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2006-03-07 00:16:58 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import gnu.x11.Display;
import gnu.x11.extension.NotFoundException;

// The collection of all X11 extensions used by FWS X11

public class ExtensionSet {

    public X11XfixesExt xfixesExtForCep;
    public X11LgeExt    lgeExtForCep;
    public X11XtestExt  xtestExtForCep;

    public X11XfixesExt xfixesExtForDev;
    public X11CompositeExt compExtForDev;
    public X11LgeExt    lgeExtForDev;
    public X11XtestExt  xtestExtForDev;

    public X11DamageExt damageExtForDam;
    public X11XfixesExt xfixesExtForDam;
    public X11LgeExt    lgeExtForDam;   
    public X11ShmExt    shmExtForDam;
    public X11XtestExt  xtestExtForDam;

    public X11XfixesExt xfixesExtForCieb;
    public X11LgeExt    lgeExtForCieb; 
    public X11XtestExt  xtestExtForCieb;

    public ExtensionSet (Display cepDpy, Display devDpy, Display damDpy, 
			 Display ciebDpy) 
	throws NotFoundException 
    {
	// Initialize extensions used by CookedEventPoller
	xfixesExtForCep = new X11XfixesExt(cepDpy);
	lgeExtForCep = new X11LgeExt(cepDpy);
	xtestExtForCep = new X11XtestExt(cepDpy);

        // Initialize extensions used by WinSysX11
	xfixesExtForDev = new X11XfixesExt(devDpy);
	compExtForDev = new X11CompositeExt(devDpy);
	lgeExtForDev = new X11LgeExt(devDpy);
	xtestExtForDev = new X11XtestExt(devDpy);

	// Initialize extensions used by the DamageEventBroker
	damageExtForDam = new X11DamageExt(damDpy);
	xfixesExtForDam = new X11XfixesExt(damDpy);
	lgeExtForDam = new X11LgeExt(damDpy);
	shmExtForDam = new X11ShmExt(damDpy);
	xtestExtForDam = new X11XtestExt(damDpy);

	// Initialize extensions used by CursorImageEventBroker
	xfixesExtForCieb = new X11XfixesExt(ciebDpy);
	lgeExtForCieb = new X11LgeExt(ciebDpy);
	xtestExtForCieb = new X11XtestExt(ciebDpy);
    }
}








