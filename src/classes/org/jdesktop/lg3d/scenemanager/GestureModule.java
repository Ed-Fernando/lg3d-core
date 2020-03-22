/**
 * Project Looking Glass
 *
 * $RCSfile: GestureModule.java,v $
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
 * $Date: 2005-01-20 22:05:33 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager;

import org.jdesktop.lg3d.sg.BranchGroup;

/**
 * Interface for the Gesture system.
 * @author  paulby
 */
public interface GestureModule {
    
    /**
     * Set the group to which gesture module should
     * attach any scene graph objects. This should be the same
     * as the root for the cursor module.
     * @param root the branch group to attach the gesture module to
     */
    public void setModuleRoot( BranchGroup root );

}
