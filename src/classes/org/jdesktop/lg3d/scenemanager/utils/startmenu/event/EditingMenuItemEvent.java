/***************************************************************************
 *   Project Looking Glass                                                 *
 *   Incubator Project - 3D Start Menu                                     *
 *                                                                         *
 *   $RCSfile: EditingMenuItemEvent.java,v $                                                             *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   Author: Colin M. Bullock                                              *
 *   cmbullock@gmail.com                                                   *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 *                                                                         *
 *   $Revision: 1.1 $                                                            *
 *   $Date: 2005-12-02 17:06:33 $                                                                *
 ***************************************************************************/
package org.jdesktop.lg3d.scenemanager.utils.startmenu.event;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuItem;
import org.jdesktop.lg3d.wg.event.LgEvent;

public class EditingMenuItemEvent extends LgEvent {
    
    private MenuItem item;
    
    public EditingMenuItemEvent(MenuItem item) {
        this.item= item;
    }
    
    public MenuItem getItem() {
        return item;
    }

}
