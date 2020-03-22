/**
 * Project Looking Glass
 *
 * $RCSfile: SGObjectFactoryImpl.java,v $
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
 * $Date: 2005-04-14 23:04:21 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.RemoteObject;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.logging.Level;
import java.lang.reflect.Constructor;

import javax.vecmath.*;

import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Frame3DRemote;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Container3DRemote;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Frame3D;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Component3D;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Container3D;

/**
 *
 * @author  Paul
 */
public class SGObjectFactoryImpl extends UnicastRemoteObject implements SGObjectFactoryRemote {
    
    private HashMap remoteToLocal = new HashMap();
    private HashMap boundsToLocal = new HashMap();
    private java.util.logging.Logger logger = java.util.logging.Logger.getLogger("lg.sg");
    
    private static SGObjectFactoryImpl factoryImpl = null;
    
    SGObjectFactoryImpl() throws java.rmi.RemoteException {
    }
    
    public static SGObjectFactoryRemote getSGObjectFactory() {
        if (factoryImpl==null)
            try {
                factoryImpl = new SGObjectFactoryImpl();
            } catch( java.rmi.RemoteException rex ) {
                throw new RuntimeException(rex);
            }
        return factoryImpl;
    }
    
    public static SGObjectFactoryImpl getFactoryImpl() {
        return factoryImpl;
    }
    
    public BranchGroupRemote newBranchGroup() throws java.rmi.RemoteException {
        logger.finer("Creating RMI BranchGroup");
        try {
            BranchGroup bg = new BranchGroup();
            remoteToLocal.put( RemoteObject.toStub( bg ), bg );
            return bg;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    
    public QuadArrayRemote newQuadArray(int vertexCount, int vertexFormat) throws java.rmi.RemoteException {
        try {
            QuadArray sgo = new QuadArray( vertexCount, vertexFormat );
            remoteToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    
    public QuadArrayRemote newQuadArray(int vertexCount, int vertexFormat, int texCoordSetCount, int[] texCoordSetMap) throws java.rmi.RemoteException {
        try {
            QuadArray sgo = new QuadArray( vertexCount, vertexFormat, texCoordSetCount, texCoordSetMap );
            remoteToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }

    public Transform3DRemote newTransform3D() throws java.rmi.RemoteException {
        try {
            Transform3D sgo = new Transform3D();
            remoteToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    /**
     * Return the local object that maps to the supplied remote stub.
     *
     * This should only be called by rmi implementation classes
     */
    public SceneGraphObject getLocal( SceneGraphObjectRemote remote ) {
        return (SceneGraphObject)remoteToLocal.get(remote);
    }
    
    public Transform3D getLocal( Transform3DRemote remote ) {
        return (Transform3D)remoteToLocal.get(remote);
    }
    
    public Bounds getLocal( BoundsRemote remote ) {
        return (Bounds)boundsToLocal.get( remote );
    }
    
    public BoundingSphereRemote newBoundingSphere() throws java.rmi.RemoteException {
        try {
            BoundingSphere sgo = new BoundingSphere();
            boundsToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }

    public BoundingBoxRemote newBoundingBox() throws java.rmi.RemoteException {
        try {
            BoundingBox sgo = new BoundingBox();
            boundsToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    
    public RotationInterpolatorRemote newRotationInterpolator(AlphaRemote alpha, TransformGroupRemote target) throws java.rmi.RemoteException {
        try {
            RotationInterpolator sgo = new RotationInterpolator( alpha, target );
            remoteToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    
    public GroupRemote newGroup() throws java.rmi.RemoteException {
        try {
            Group sgo = new Group();
            remoteToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    
    public ImageComponent2DRemote newImageComponent2D(int format, int width, int height, boolean byReference, boolean yUp) throws java.rmi.RemoteException {
        try {
            ImageComponent2D sgo = new ImageComponent2D( format, width, height, byReference, yUp );
            remoteToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public ImageComponent2DRemote newImageComponent2D(int format, javax.media.jai.remote.SerializableRenderedImage image, boolean byReference, boolean yUp) throws java.rmi.RemoteException {
        try {
            ImageComponent2D sgo = new ImageComponent2D( format, image, byReference, yUp );
            remoteToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public TextureAttributesRemote newTextureAttributes(int textureMode, Transform3DRemote transform, Color4f textureBlendColor, int perspCorrectionMode) throws java.rmi.RemoteException {
        try {
            TextureAttributes sgo = new TextureAttributes( textureMode, transform, textureBlendColor, perspCorrectionMode );
            remoteToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public Texture2DRemote newTexture2D(int mipMapMode, int format, int width, int height, int boundaryWidth) throws java.rmi.RemoteException {
        try {
            Texture2D sgo = new Texture2D( mipMapMode, format, width, height, boundaryWidth);
            remoteToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public AppearanceRemote newAppearance() throws java.rmi.RemoteException {
        try {
            Appearance sgo = new Appearance();
            remoteToLocal.put( RemoteObject.toStub( sgo ), sgo );
            return sgo;
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public Frame3DRemote newFrame3D() throws java.rmi.RemoteException {
        try {
            Frame3D bg = new Frame3D();
            remoteToLocal.put( RemoteObject.toStub( bg ), bg );
            return bg;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    
    public org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Component3DRemote newComponent3D() throws java.rmi.RemoteException {
        try {
            Component3D bg = new Component3D();
            remoteToLocal.put( RemoteObject.toStub( bg ), bg );
            return bg;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    
    public Container3DRemote newContainer3D() throws java.rmi.RemoteException {
        try {
            Container3D bg = new Container3D();
            remoteToLocal.put( RemoteObject.toStub( bg ), bg );
            return bg;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    
    public SceneGraphObjectRemote newInstance(Class c) throws java.rmi.RemoteException {
        try {
            SceneGraphObject bg = (SceneGraphObject)c.newInstance();
            remoteToLocal.put( RemoteObject.toStub( bg ), bg );
            return bg;
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        } catch( Exception ex ) {
            throw new RuntimeException( ex );
        }
            
    }
    
    public SceneGraphObjectRemote newInstance(Class cl, Class[] parameterTypes, Object[] parameterArgs) throws java.rmi.RemoteException {
        Constructor constructor=null;
        try {
            constructor = cl.getConstructor( parameterTypes );
            
            logger.finer("Instantiating "+cl);
            
            SceneGraphObjectRemote sgo = (SceneGraphObjectRemote)constructor.newInstance(parameterArgs);
            remoteToLocal.put( RemoteObject.toStub( sgo ), sgo );
            //sgo.setUserData( this );
            
            return sgo;
        } catch( IllegalArgumentException iae ) {
            logger.severe("Parameter types");
            for(int i=0; i<parameterArgs.length; i++)
                logger.severe( i+"  "+parameterArgs[i].getClass().getName() );
            
            logger.severe("\nConstructor expecting");
            if (constructor!=null) {
                Class[] expect = constructor.getParameterTypes();
                for(int i=0; i<expect.length; i++)
                    logger.severe( i+"  "+expect[i] +"  "+expect[i].isAssignableFrom(parameterArgs[i].getClass() ));
            } else
                logger.severe("Constructor null");
                
            throw new RuntimeException( iae );
        } catch( Exception ie ) {
            logger.log(Level.SEVERE,"Failed to instantiate "+cl.getName(),ie);
            throw new RuntimeException(ie);
        }
    }
 }
