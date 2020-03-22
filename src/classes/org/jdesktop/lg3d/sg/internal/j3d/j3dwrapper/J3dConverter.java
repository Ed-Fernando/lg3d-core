/**
 * Project Looking Glass
 *
 * $RCSfile: J3dConverter.java,v $
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
 * $Date: 2004-06-23 18:50:38 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

/**
 * A utility class for converting Java3D scene graph objects to
 * Looking Glass objects.
 *
 * This class is an implementation detail and should NOT be used by users. This
 * class may be removed in a future version
 */
public class J3dConverter {
    
    
    //    public static IndexedTriangleArray convert( javax.media.j3d.IndexedTriangleArray orig ) {
    //        IndexedTriangleArray ret = new IndexedTriangleArray();
    //        ret.wrapped = orig;
    //
    //        return ret;
    //    }
    //
    //    public static IndexedTriangleFanArray convert( javax.media.j3d.IndexedTriangleFanArray orig ) {
    //        IndexedTriangleFanArray ret = new IndexedTriangleFanArray();
    //        ret.wrapped = orig;
    //
    //        return ret;
    //    }
    //
    //    public static IndexedTriangleStripArray convert( javax.media.j3d.IndexedTriangleStripArray orig ) {
    //        IndexedTriangleStripArray ret = new IndexedTriangleStripArray();
    //        ret.wrapped = orig;
    //
    //        return ret;
    //    }
    
    /**
     * Given a Java3D SceneGraphObject return it's looking glass representation,
     * if the LG representation does not exist it will be created
     */
    public static SceneGraphObject fromJ3d( javax.media.j3d.SceneGraphObject orig ) {
        if (orig==null)
            return null;
        
        if (orig.getUserData()!=null &&
            orig.getUserData() instanceof SceneGraphObject)
            return (SceneGraphObject)orig.getUserData();
        
        String origClassName = orig.getClass().getName();
        SceneGraphObject ret=null;
        
        String newClassName = origClassName.substring( origClassName.lastIndexOf(".")+1 );
        try {
            Class newClass = Class.forName( "org.jdesktop.lg3d.sg."+newClassName );
            ret = (SceneGraphObject)newClass.newInstance();
            ret.wrapped = orig;
            orig.setUserData( ret );
        } catch (Exception e ) {
            e.printStackTrace();
            return null;
        }
        
        return ret;
    }
    
    public static javax.media.j3d.SceneGraphObject toJ3d( SceneGraphObject orig ) {
        if (orig==null)
            return null;
        return orig.wrapped;
    }
}
