/**
 * Project Looking Glass
 *
 * $RCSfile: lg3dtoolkit.java,v $
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
 * $Date: 2007-03-22 08:38:19 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.toolkit;
/**
 *
 * @author paulby
 */
public interface lg3dtoolkit {
    public ClassLoader getLg3dClassLoader();
    public boolean enableLg3d(ClassLoader classLoader);
}
