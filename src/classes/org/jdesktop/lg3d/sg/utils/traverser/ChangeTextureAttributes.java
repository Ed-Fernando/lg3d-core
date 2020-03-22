/**
 * Project Looking Glass
 *
 * $RCSfile: ChangeTextureAttributes.java,v $
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
 * $Date: 2004-06-23 18:51:53 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.utils.traverser;

import org.jdesktop.lg3d.sg.Texture;
import org.jdesktop.lg3d.sg.TextureUnitState;
import org.jdesktop.lg3d.sg.Appearance;

/**
 *
 * @author  Paul Byrne
 * @version $Id: ChangeTextureAttributes.java,v 1.2 2004-06-23 18:51:53 paulby Exp $
 */
public class ChangeTextureAttributes extends Object {
    
    /**
     * Traverse the graph setting the Texture Enable flag in all Texture
     * objects that exist, including those in TextureUnitState's
     */
    public static void setTextureEnable( org.jdesktop.lg3d.sg.Node treeRoot,
                                         final boolean enabled ) {
        
        AppearanceChangeProcessor processor = new AppearanceChangeProcessor() {
            public void changeAppearance( org.jdesktop.lg3d.sg.Shape3D shape,
            org.jdesktop.lg3d.sg.Appearance app ) {
                if (app==null) return;
                Texture texture = app.getTexture();
                if (texture!=null)
                    texture.setEnable( enabled );
                if (app.getTextureUnitState()!=null) {
                    TextureUnitState[] states = app.getTextureUnitState();
                    for(int i=0; i<states.length; i++)
                        if (states[i].getTexture()!=null)
                            states[i].getTexture().setEnable( enabled );
                }
            }
            
        };
        
        scanTree( treeRoot, processor );
    }
    
    private static void scanTree( org.jdesktop.lg3d.sg.Node treeRoot,
    AppearanceChangeProcessor processor ) {
        try {
            Class shapeClass = Class.forName("javax.media.j3d.Shape3D");
            
            TreeScan.findNode( treeRoot, shapeClass, processor, false, true );
        } catch(Exception e ) {
            e.printStackTrace();
            System.out.println( "ERROR ChangeTextureAttributes, SceneGraph contains"+
            " Live or compiled nodes, without correct capabilities");
        }
    }
    
}
