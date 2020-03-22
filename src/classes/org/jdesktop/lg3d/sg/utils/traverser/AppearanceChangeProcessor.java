/**
 * Project Looking Glass
 *
 * $RCSfile: AppearanceChangeProcessor.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2005-04-14 23:04:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.utils.traverser;

import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.Appearance;

/**
 * Abstract class for changing the parameters of Appearance Node Components.
 * Subclasses implement changeAppearance to make the actual updates
 *
 * @author  Paul Byrne
 * @version $Id: AppearanceChangeProcessor.java,v 1.3 2005-04-14 23:04:23 paulby Exp $
 */
public abstract class AppearanceChangeProcessor implements ProcessNodeInterface {
    
    /**
     * Called by TreeScan. node must be an instance of Shape3D
     */
    public boolean processNode(org.jdesktop.lg3d.sg.Node node) {
        Appearance app = ((Shape3D)node).getAppearance();
        changeAppearance( (Shape3D)node, app );
        return true;
    }
    
    public abstract void changeAppearance( org.jdesktop.lg3d.sg.Shape3D shape,
            org.jdesktop.lg3d.sg.Appearance app );
}
