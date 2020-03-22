/**
 * Project Looking Glass
 *
 * $RCSfile: UniverseFactoryInterface.java,v $
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
 * $Date: 2005-09-29 18:59:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

import com.sun.j3d.utils.universe.ConfiguredUniverse;
import java.net.URL;

/**
 *
 * @author paulby
 */
public interface UniverseFactoryInterface {
    
    public ConfiguredUniverse createUniverse(URL configURL);
}
