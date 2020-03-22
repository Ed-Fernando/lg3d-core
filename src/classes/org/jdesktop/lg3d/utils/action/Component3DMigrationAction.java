/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DMigrationAction.java,v $
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
 * $Date: 2005-04-14 23:04:26 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.action;

import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 * An action class that moves the event source component from
 * one Container3D to another.
 */
public class Component3DMigrationAction implements ActionNoArg, ActionBoolean {
    private Container3D[] srcCont;
    private Container3D distCont;

    public Component3DMigrationAction(Container3D srcCont, Container3D distCont) {
	this(new Container3D[] {srcCont}, distCont);
    }
    
    public Component3DMigrationAction(Container3D[] srcCont, Container3D distCont) {
        if (srcCont == null || distCont == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        if (srcCont.length <= 0) {
            throw new IllegalArgumentException(
                "at least one Container3D required as source container(s)");
        }
        for (Container3D c : srcCont) {
            if (c == null) {
                throw new IllegalArgumentException("argument cannot be null");
            }
        }
	this.srcCont = srcCont;
        this.distCont = distCont;
    }
    
    private void migrate(Component3D comp, boolean dir) {
        assert(srcCont.length >= 1);
        Container3D dist = (dir)?(distCont):(srcCont[0]);
        Component3D target = null;
        if (dir) {
            for (Container3D c : srcCont) {
                int idx = c.indexOfChild(comp);
                if (idx >= 0) {
                    target = (Component3D)c.getChild(idx);
                    break;
                }
            }
        } else {
            int idx = distCont.indexOfChild(comp);
            if (idx >= 0) {
                target = (Component3D)dist.getChild(idx);
            }
        }
        if (target != null) {
            dist.moveTo(target);
        }
    }
   
    public void performAction(LgEventSource source, boolean state) {
        if (!(source instanceof Component3D)) {
            return;
        }
        migrate((Component3D)source, state);
    }
    
    public void performAction(LgEventSource source) {
        if (!(source instanceof Component3D)) {
            return;
        }
        migrate((Component3D)source, true);
    }
}
