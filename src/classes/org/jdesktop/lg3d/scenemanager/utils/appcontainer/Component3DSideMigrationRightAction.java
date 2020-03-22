/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DSideMigrationRightAction.java,v $
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
 * $Date: 2005-04-14 23:04:07 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.appcontainer;

import org.jdesktop.lg3d.scenemanager.utils.appcontainer.ComponentSideMigrationAction;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;


public class Component3DSideMigrationRightAction extends ComponentSideMigrationAction {

    public Component3DSideMigrationRightAction(Container3D mainCont, 
        Container3D rightCont) {
	super(mainCont, null, rightCont);
    }
        
    public void performAction(LgEventSource source) {
        if (!(source instanceof Component3D)) {
            return;
        }
        
        Component3D comp = (Component3D)source;
        
        migrate(comp, mainCont, rightCont);
    }
}
