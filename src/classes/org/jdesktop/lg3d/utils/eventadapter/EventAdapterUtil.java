/**
 * Project Looking Glass
 *
 * $RCSfile: EventAdapterUtil.java,v $
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
 * $Date: 2006-03-08 02:27:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventadapter;

import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;


/**
 * An interface that all the EventAdapters extend.
 */
class EventAdapterUtil {
    public static boolean modifierMatches(ModifierId[] modifierSet, 
            ModifierId target, boolean ignoreButtons) 
    {
        if (target == null) {
            for (ModifierId m : modifierSet) {
                if (ignoreButtons && isMouseButton(m)) {
                    continue;
                }
                if (m != null) {
                    return false;
                }
            }
            // no match, the condition matched for null
            return true;
        } else {
            for (ModifierId m : modifierSet) {
                if (ignoreButtons && isMouseButton(m)) {
                    continue;
                }
                if (m == target) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private static boolean isMouseButton(ModifierId modifier) {
        return (modifier == ModifierId.BUTTON1
                || modifier == ModifierId.BUTTON2
                || modifier == ModifierId.BUTTON3);
    }
    
    public static void throughIAEIfNull(String name, Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException("the argument " + name + " cannot be null");
        }
    }
}
