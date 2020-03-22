/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DGroupMigrationAction.java,v $
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
 * An action class that moves all the component
 * in one Container3D to another.
 */
public class Component3DGroupMigrationAction 
    implements ActionNoArg, ActionBoolean 
{
    private Container3D[] srcCont;
    private Container3D distCont;

    public Component3DGroupMigrationAction(Container3D srcCont, 
        Container3D distCont)
    {
	this(new Container3D[] {srcCont}, distCont);
    }
    
    public Component3DGroupMigrationAction(Container3D[] srcCont, 
        Container3D distCont) 
    {
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
    
    private int numChildrenInSrcConts() {
        int num = 0;
        for (Container3D c : srcCont) {
            num += c.numChildren();
        }
        return num;
    }
    
    private void migrate() {
        for (int j = srcCont.length - 1; j >= 0 ; j--) {
            Container3D c = srcCont[j];
            // FIXME -- possible race condition
            int num = c.numChildren();
            for (int i = 0; i < num; i++) {
                // REMINDER -- always use 0 for the index since we are removing
                // the 0th child in this loop
                Component3D comp = (Component3D)c.getChild(0);
                c.removeChild(0);
                distCont.addChild(comp);
            }
        }
    }
    
    private void migrateReverse() {
        assert(srcCont.length > 0);
        // FIXME -- possible race condition
        int num = distCont.numChildren();
        for (int i = 0; i < num; i++) {
            // REMINDER -- always use 0 for the index since we are removing
            // the 0th child in this loop
            Component3D comp = (Component3D)distCont.getChild(0);
            distCont.removeChild(0);
            srcCont[0].insertChild(comp, i);
        }
    }
   
    public void performAction(LgEventSource source, boolean state) {
        if (state) {
            migrate();
        } else {
            migrateReverse();
        }
    }
    
    public void performAction(LgEventSource source) {
        if (numChildrenInSrcConts() > 0) {
            migrate();
        } else {
            migrateReverse();
        }
    }
}
