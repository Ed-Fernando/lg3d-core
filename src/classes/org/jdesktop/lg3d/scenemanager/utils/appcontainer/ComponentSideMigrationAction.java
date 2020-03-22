/**
 * Project Looking Glass
 *
 * $RCSfile: ComponentSideMigrationAction.java,v $
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
 * $Date: 2005-04-14 23:04:08 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.appcontainer;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;


public class ComponentSideMigrationAction implements ActionNoArg {
    protected Container3D mainCont;
    protected Container3D leftCont;
    protected Container3D rightCont;
    
    public ComponentSideMigrationAction(Container3D mainCont, 
        Container3D leftCont, Container3D rightCont) 
    {
	this.mainCont = mainCont;
        this.leftCont = leftCont;
        this.rightCont = rightCont;
    }
    
    protected void migrate(Component3D comp, Container3D srcCont, 
        Container3D distCont) 
    {
        int idx = srcCont.indexOfChild(comp);
        if (idx >= 0) {
            Component3D target = (Component3D)srcCont.getChild(idx);
            distCont.moveTo(target);
        }
        
    }
    
    public void performAction(LgEventSource source) {
        if (!(source instanceof Component3D)) {
            return;
        }
        Component3D comp = (Component3D)source;
        Vector3f tmpV3f = new Vector3f();
        float contWidth = mainCont.getPreferredSize(tmpV3f).x;
        float compWidth = comp.getPreferredSize(tmpV3f).x;
        Vector3f compLoc = comp.getTranslation(new Vector3f());
        float compX = compLoc.x;
        
        if (compX - compWidth * 0.5 <= contWidth * -0.5f) {
            migrate(comp, mainCont, leftCont);
        } else if (compX + compWidth * 0.5 >= contWidth * 0.5f) {
            migrate(comp, mainCont, rightCont);
        }
    }
}
