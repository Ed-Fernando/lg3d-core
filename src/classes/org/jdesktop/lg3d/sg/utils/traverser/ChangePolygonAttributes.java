/**
 * Project Looking Glass
 *
 * $RCSfile: ChangePolygonAttributes.java,v $
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

import org.jdesktop.lg3d.sg.PolygonAttributes;
import org.jdesktop.lg3d.sg.Appearance;

/** 
 *
 * @author  paulby
 * @version 
 */
public class ChangePolygonAttributes extends Object {

  /**
    * Traverse the graph setting the polygon mode in all Polygon Attributes
    * object that exist
    */
  public static void setPolygonMode( org.jdesktop.lg3d.sg.Node treeRoot,
                                     final int polygonMode ) {
    setPolygonMode( treeRoot, polygonMode, false );
  }                     
                 
  /**
    * Traverse the graph setting the polygon mode in all PolygonAttributes.
    * If createAttribute is false on PolygonAttribute objects that already
    * exist are modified.
    * If craeteAttribute is true, PolygonAttributes and Appearance objects will
    * be created as necessary
    */
  public static void setPolygonMode( org.jdesktop.lg3d.sg.Node treeRoot,
                                     final int polygonMode, 
                                     final boolean createAttribute ) {
  
    AppearanceChangeProcessor processor = new AppearanceChangeProcessor() {
      public void changeAppearance( org.jdesktop.lg3d.sg.Shape3D shape,
                                    org.jdesktop.lg3d.sg.Appearance app ) {
        if (app==null)
          if (createAttribute) {
            app = new Appearance();
            shape.setAppearance( app );
          } else 
            return;
        PolygonAttributes poly = app.getPolygonAttributes();
        if (poly!=null)
          poly.setPolygonMode( polygonMode );
        else if (createAttribute) {
          poly = new PolygonAttributes();
          poly.setPolygonMode( polygonMode );
          app.setPolygonAttributes( poly );
        }
      }
    };
    
    scanTree( treeRoot, processor );
  }
  
  /**
    * Traverse the graph setting the cullFace in all Polygon Attributes
    * object that exist
    */
  public static void setCullFace( org.jdesktop.lg3d.sg.Node treeRoot,
                                     final int cullFace ) {

    setCullFace( treeRoot, cullFace, false );                                  
  }
                                    
  /**
    * Traverse the graph setting the cullFace in all PolygonAttributes.
    * If createAttribute is false on PolygonAttribute objects that already
    * exist are modified.
    * If craeteAttribute is true, PolygonAttributes and Appearance objects will
    * be created as necessary
    */
  public static void setCullFace( org.jdesktop.lg3d.sg.Node treeRoot,
                                     final int cullFace,
                                     final boolean createAttribute ) {
  
    AppearanceChangeProcessor processor = new AppearanceChangeProcessor() {
      public void changeAppearance( org.jdesktop.lg3d.sg.Shape3D shape,
                                    org.jdesktop.lg3d.sg.Appearance app ) {
        if (app==null)
          if (createAttribute) {
            app = new Appearance();
            shape.setAppearance( app );
          } else 
            return;
        PolygonAttributes poly = app.getPolygonAttributes();
        if (poly!=null)
          poly.setCullFace( cullFace );
        else if (createAttribute) {
          poly = new PolygonAttributes();
          poly.setCullFace( cullFace );
          app.setPolygonAttributes( poly );
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
      System.out.println( "ERROR ChangePolygonAttributes, SceneGraph contains"+
                          " Live or compiled nodes, without correct capabilities");
    }
  }
  
}
