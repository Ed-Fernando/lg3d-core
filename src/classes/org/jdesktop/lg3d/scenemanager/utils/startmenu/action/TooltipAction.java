/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: TooltipAction.java,v $
 *   
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the
 *   Free Software Foundation, Inc.,
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *   
 *   Author: Colin M. Bullock
 *   cmbullock@gmail.com
 *   
 *   $Revision: 1.1 $
 *   $Date: 2005-12-02 17:06:28 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.action;

import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.util.PopupText;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

public class TooltipAction implements ActionBoolean {
    
    private PopupText text;
    
    private Component3D comp;
    
    private float offset;
    
    public TooltipAction(PopupText text, Component3D comp, float offset) {
        this.text= text;
        this.comp= comp;
        this.offset= offset;
    }
    
    public void performAction(LgEventSource source, boolean state) {
        if(state) {
            Vector3f pos= comp.getFinalTranslation(new Vector3f());
            text.setTranslation(pos.x + offset, pos.y + offset, pos.z + 0.01f);
            text.changeVisible(true, 500);
        } else {
            text.changeVisible(false, 500);
        }
    }
    
}