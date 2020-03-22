/**
 * Project Looking Glass
 *
 * $RCSfile: ChangeMaterial.java,v $
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

import org.jdesktop.lg3d.sg.Material;

/** 
 * Make changes to the Material properties for ALL Materials in the
 * scene graph provided
 *
 * @author  paulby
 * @version 1.4, 01/18/02
 */
public class ChangeMaterial extends Object {

  /**
    * Set the LightingEnable state in every Material in the SceneGraph
    * treeRoot
    * @param treeRoot, root of Scene Graph
    * @param state, Lighting state
    */
  public static void setLightingEnable( org.jdesktop.lg3d.sg.Node treeRoot,
                                     final boolean state ) {
  
    AppearanceChangeProcessor processor = new AppearanceChangeProcessor() {
      public void changeAppearance( org.jdesktop.lg3d.sg.Shape3D shape,
                                    org.jdesktop.lg3d.sg.Appearance app ) {
        if (app==null) return;
        Material mat = app.getMaterial();
        if (mat!=null)
          mat.setLightingEnable( state );
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
      System.out.println( "ERROR ChangeMaterial, SceneGraph contains Live or compiled nodes");
    }
  }
  
}
