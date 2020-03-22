/**
 * Project Looking Glass
 *
 * $RCSfile: PickShape.java,v $
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
 * $Date: 2004-06-23 18:50:28 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;
import javax.vecmath.Point4d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.lang.reflect.Constructor;

import org.jdesktop.lg3d.sg.internal.wrapper.PickShapeWrapper;

/**
 * An abstract class for describing a pick shape that can be used with
 * the BranchGroup and Locale picking methods.
 *
 * @see BranchGroup#pickAll
 * @see Locale#pickAll
 */
public abstract class PickShape {


    PickShapeWrapper wrapped;
    
    /**
     * Instantiate a concrete scenegraph object wrapper given the fully qualified 
     * class name
     */
    protected PickShapeWrapper instantiate( String classname ) {
        try {
            PickShapeWrapper sgo = (PickShapeWrapper)Class.forName(classname).newInstance();
            //sgo.setUserData( this );
            
            return sgo;
        } catch( Exception ie ) {
            throw new RuntimeException(ie);
        }
    }
    
    protected PickShapeWrapper instantiate( String classname, Class[] parameterTypes, Object[] parameterArgs ) {
        Constructor constructor=null;
        try {
            Class cl = Class.forName( classname );
            constructor = cl.getConstructor( parameterTypes );
            
            
            PickShapeWrapper sgo = (PickShapeWrapper)constructor.newInstance(parameterArgs);
            //sgo.setUserData( this );
            
            return sgo;
        } catch( IllegalArgumentException iae ) {
            System.err.println("Parameter types");
            for(int i=0; i<parameterArgs.length; i++)
                System.err.println( i+"  "+parameterArgs[i].getClass().getName() );
            
            System.err.println("\nConstructor expecting");
            if (constructor!=null) {
                Class[] expect = constructor.getParameterTypes();
                for(int i=0; i<expect.length; i++)
                    System.err.println( i+"  "+expect[i] +"  "+expect[i].isAssignableFrom(parameterArgs[i].getClass() ));
            } else
                System.err.println("Constructor null");
                
            throw new RuntimeException( iae );
        } catch( Exception ie ) {
            throw new RuntimeException(ie);
        }
    }
    
    /**
     * Constructs a PickShape object.
     */
    public PickShape() {
    }


}

