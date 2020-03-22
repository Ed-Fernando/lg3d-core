/**
 * Project Looking Glass
 *
 * $RCSfile: InputEvent3D.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2007-07-18 16:34:25 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;

import java.awt.event.InputEvent;
import org.jdesktop.lg3d.displayserver.fws.awt.PickEngineAWT;


/**
 * The abstract and super class for all the input events.
 */
public abstract class InputEvent3D extends LgEvent {
    public static final int AUGMENTED_MODIFIERS_F = PickEngineAWT.AUGMENTED_MODIFIERS_F;
    public static final int AUGMENTED_MODIFIERS_R = PickEngineAWT.AUGMENTED_MODIFIERS_R;

    public enum ModifierId {
        ALT, ALT_GRAPH, CTRL, META, SHIFT, BUTTON1, BUTTON2, BUTTON3,
    };
    
    private static final ModifierConvTable[] modifierConvTable = {
        new ModifierConvTable(InputEvent.ALT_DOWN_MASK,     ModifierId.ALT),
        new ModifierConvTable(InputEvent.ALT_GRAPH_DOWN_MASK, ModifierId.ALT_GRAPH), 
        new ModifierConvTable(InputEvent.CTRL_DOWN_MASK,    ModifierId.CTRL), 
        new ModifierConvTable(InputEvent.META_DOWN_MASK,    ModifierId.META), 
        new ModifierConvTable(InputEvent.SHIFT_DOWN_MASK,   ModifierId.SHIFT), 
        new ModifierConvTable(InputEvent.BUTTON1_DOWN_MASK, ModifierId.BUTTON1), 
        new ModifierConvTable(InputEvent.BUTTON2_DOWN_MASK, ModifierId.BUTTON2), 
        new ModifierConvTable(InputEvent.BUTTON3_DOWN_MASK, ModifierId.BUTTON3), 
    };     
    
    private int augmentedModifiers;

    abstract InputEvent getAwtEvent();
    
    /** 
     * Returns the time stamp of when this event occurred
     * @return time stamp for event
     */
    public long getWhen() {
        return getAwtEvent().getWhen();
    }
    
    /**
     * Returns a set of the extended modifiers this event. Extended modifiers 
     * represent the state of all modal keys, such as ALT, CTRL, META, 
     * and the mouse buttons just after the event occurred.
     * 
     * @param ret  an array to fill in the resutl.  If it is too small,
     *             a new array with enough size will be allocated.
     * @return an array of ModifierId objects that represents the modifiers
     *         status.
     * @see java.awt.event.InputEvent
     */
    public ModifierId[] getModifiersEx(ModifierId[] ret) {
        if (ret == null) {
            throw new IllegalArgumentException("the argument cannot be null");
        }
        
        int modifiers = getAwtEvent().getModifiersEx();
        int numBits = 0;
        int tmpMod = modifiers;
        
        while (tmpMod > 0) {
            numBits += (tmpMod & 0x1);
            tmpMod /= 2;
        }
        
        if (numBits > ret.length) {
            ret = new ModifierId[numBits];
        }
        
        int idx = 0;
        for (ModifierConvTable entry: modifierConvTable) {
            if ((modifiers & entry.awt) > 0) {
                assert(idx < ret.length);
                ret[idx] = entry.lg3d;
                idx++;
            }
        }
        // null-out the rest
        for ( ; idx < ret.length; idx++) {
            ret[idx] = null; 
        }
        
        return ret;
    }
    
    private static class ModifierConvTable {
        int awt;
        ModifierId lg3d;
        
        ModifierConvTable(int awt, ModifierId lg3d) {
            this.awt = awt;
            this.lg3d = lg3d;
        }
    }

    public void setAugmentedModifiers (int augmentedModifiers) {
	this.augmentedModifiers = augmentedModifiers;
    }

    public int getAugmentedModifiers () {
	return augmentedModifiers;
    }
}
