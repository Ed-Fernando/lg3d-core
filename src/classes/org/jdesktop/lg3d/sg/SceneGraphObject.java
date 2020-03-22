/**
 * Project Looking Glass
 *
 * $RCSfile: SceneGraphObject.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2007-04-10 22:58:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.lg3d.sg.internal.wrapper.SceneGraphObjectWrapper;

/**
 * SceneGraphObject is a common superclass for
 * all scene graph component objects.  This includes Node,
 * Geometry, Appearance, etc.
 */
public abstract class SceneGraphObject {
   // Any global flags? (e.g., execution cullable, collideable)

    protected SceneGraphObjectWrapper wrapped=null;
    
    //Used by RMI sg implementation
    //private static HashMap remoteToLocal = new HashMap();
    
    private Object userData;
    protected Logger logger = Logger.getLogger("lg.sg");

    /**
     * Constructs a SceneGraphObject with default parameters.  The default
     * values are as follows:
     * <ul>
     * capability bits : clear (all bits)<br>
     * isLive : false<br>
     * isCompiled : false<br>
     * user data : null<br>
     * </ul>
     */
    public SceneGraphObject() {
        createWrapped();
    }
    
    protected abstract void createWrapped();
  
    /**
     * Retrieves the specified capability bit.  Note that only one capability
     * bit may be retrieved per method invocation--capability bits cannot
     * be ORed together.
     * @param bit the bit whose value is returned
     * @return true if the bit is set, false if the bit is clear
     */
    public final boolean getCapability(int bit) {
	return wrapped.getCapability( bit );
    }

    /**
     * Sets the specified capability bit.  Note that only one capability bit
     * may be set per method invocation--capability bits cannot be ORed
     * together.
     * @param bit the bit to set
     * @exception RestrictedAccessException if this object is part of live
     * or compiled scene graph
     */
    public final void setCapability(int bit) {
	wrapped.setCapability( bit );
    }

    /**
     * Clear the specified capability bit.  Note that only one capability bit
     * may be cleared per method invocation--capability bits cannot be ORed
     * together.
     * @param bit the bit to clear
     * @exception RestrictedAccessException if this object is part of live
     * or compiled scene graph
     */
    public final void clearCapability(int bit) {
        wrapped.clearCapability( bit );
    }

    /**
     * Returns a flag indicating whether the node is part of a live
     * scene graph.
     * @return true if node is part of a live scene graph, else false
     */
    public final boolean isLive() {
	return wrapped.isLive();
    }

    /**
     * Sets the userData field associated with this scene graph object.
     * The userData field is a reference to an arbitrary object
     * and may be used to store any user-specific data associated
     * with this scene graph object--it is not used by the Java 3D API.
     * If this object is cloned, the userData field is copied
     * to the newly cloned object.
     * @param userData a reference to the new userData field
     */
    public void setUserData(Object userData) {
	this.userData = userData;
    }

    /**
     * Retrieves the userData field from this scene graph object.
     * @return the current userData field
     */
    public Object getUserData() {
	return userData;
    }
    
    /**
     * Set the name of this node. This data has no runtime use and
     * is just available to allow tools and users to navigate the scene
     * graph
     */
    public void setName(String name) {
        wrapped.setName(name);
    }
    
    public String getName() {
        return wrapped.getName();
    }
    
    /**
     * Instantiate a concrete scenegraph object wrapper given the fully qualified 
     * class name or just the classname in which case the package name will
     * be prepended
     */
    protected SceneGraphObjectWrapper instantiate( String classname ) {
        if (!classname.startsWith("org.")) {
            if (!(this instanceof org.jdesktop.lg3d.wg.Component3D)) {
                classname = SceneGraphSetup.getWrapperPackage() + classname;
            } else {
                classname = SceneGraphSetup.getWidgetWrapperPackage() + classname;
            }
        }
        try {
            logger.finer("Instantiating "+classname);
            SceneGraphObjectWrapper sgo = (SceneGraphObjectWrapper)Class.forName(classname).newInstance();
            sgo.setUserData( this );
            
            return sgo;
        } catch( Exception ie ) {
            logger.log(Level.SEVERE,"Failed to instantiate "+classname,ie);
            throw new RuntimeException(ie);
        }
    }
    
    protected SceneGraphObjectWrapper instantiate( String classname, Class[] parameterTypes, Object[] parameterArgs ) {
        Constructor constructor=null;
        try {
            Class cl = Class.forName( classname );
            constructor = cl.getConstructor( parameterTypes );
            
            logger.finer("Instantiating "+cl);
            
            SceneGraphObjectWrapper sgo = (SceneGraphObjectWrapper)constructor.newInstance(parameterArgs);
            sgo.setUserData( this );
            
            return sgo;
        } catch( IllegalArgumentException iae ) {
            logger.severe("Parameter types");
            for(int i=0; i<parameterArgs.length; i++)
                logger.severe( i+"  "+parameterArgs[i] );
            
            logger.severe("\nConstructor expecting");
            if (constructor!=null) {
                Class[] expect = constructor.getParameterTypes();
                for(int i=0; i<expect.length; i++)
                    logger.severe( i+"  "+expect[i] +"  "+expect[i].isAssignableFrom(parameterArgs[i].getClass() ));
            } else
                logger.severe("Constructor null");
                
            throw new RuntimeException( iae );
        } catch( Exception ie ) {
            logger.log(Level.SEVERE,"Failed to instantiate "+classname,ie);
            logger.severe("Parameter args and types");
            for(int i=0; i<parameterArgs.length; i++)
                logger.severe( i+"  "+parameterArgs[i]+"  "+parameterTypes[i].getName() );
            throw new RuntimeException(ie);
        }
    }
    
    /**
     * Converts the supplied array into an Array we can use for reflection
     */
    java.lang.reflect.Array toIntArray( int[] in ) {
        java.lang.reflect.Array ret = (java.lang.reflect.Array)java.lang.reflect.Array.newInstance( Integer.TYPE, in.length );
        for(int i=0; i<in.length; i++)
            ret.setInt( ret, i, in[i]);
        
        return ret;
    }
    
    /**
     * Not intended for public use. Users should NEVER call this.
     */
    public SceneGraphObjectWrapper getWrapped() {
        return wrapped;
    }

    /**
     * Not intended for public use. Users should NEVER call this.
     */
    public Object getWrappedWrapped () {
        return wrapped.getWrapped();
    }
}

