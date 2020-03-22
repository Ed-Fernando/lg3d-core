/***************************************************************************
 *   Project Looking Glass                                                 *
 *   Incubator Project - 3D Start Menu                                     *
 *                                                                         *
 *   $RCSfile: PopupText.java,v $                                          *
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
 *   $Revision: 1.2 $                                                      *
 *   $Date: 2006-02-10 14:20:30 $                                          *
 ***************************************************************************/
package org.jdesktop.lg3d.scenemanager.utils.startmenu.util;

import javax.vecmath.Color4f;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.shape.GlassyText2D;
import org.jdesktop.lg3d.wg.Component3D;

/**
 * Simple text component for displaying a popup message.
 */
// TODO: This should be refactored to handle the popup functionality internally
public class PopupText extends Component3D {

    private GlassyText2D textShape;

    public PopupText(String text, int fontSize) {
        textShape= new GlassyText2D(text, 0.1f, 0.0075f, new Color4f(0.6f, 0.8f, 0.6f, 1.0f));
        addChild(textShape);
        setAnimation(new NaturalMotionAnimation(750));
        setVisible(false);
        setMouseEventEnabled(false);
    }

    /**
     * @param text The text to set.
     */
    public void setText(String text) {
        textShape.setText(text);
    }

}
