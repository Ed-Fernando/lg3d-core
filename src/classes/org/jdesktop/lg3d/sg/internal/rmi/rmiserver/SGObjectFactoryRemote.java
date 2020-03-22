/**
 * Project Looking Glass
 *
 * $RCSfile: SGObjectFactoryRemote.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2005-04-14 23:04:21 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.vecmath.*;

import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Component3DRemote;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Frame3DRemote;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Container3DRemote;


/**
 *
 * @author  Paul
 */
public interface SGObjectFactoryRemote extends Remote {
        
    public AppearanceRemote newAppearance() throws java.rmi.RemoteException;
    
    public BranchGroupRemote newBranchGroup() throws java.rmi.RemoteException;

    public GroupRemote newGroup() throws java.rmi.RemoteException;
    
    public ImageComponent2DRemote newImageComponent2D( int format, int width, int height, boolean byReference, boolean yUp ) throws java.rmi.RemoteException;

    public ImageComponent2DRemote newImageComponent2D( int format, javax.media.jai.remote.SerializableRenderedImage image, boolean byReference, boolean yUp ) throws java.rmi.RemoteException;
    
    public RotationInterpolatorRemote newRotationInterpolator( AlphaRemote alpha, TransformGroupRemote target ) throws java.rmi.RemoteException;

    public Transform3DRemote newTransform3D() throws java.rmi.RemoteException;
          
    public TextureAttributesRemote newTextureAttributes( int textureMode,
                                                         Transform3DRemote transform,
                                                         Color4f textureBlendColor,
                                                         int perspCorrectionMode ) throws java.rmi.RemoteException;

    public Texture2DRemote newTexture2D( int mipMapMode,
                                         int format,
                                         int width,
                                         int height,
                                         int boundaryWidth) throws java.rmi.RemoteException;

    public QuadArrayRemote newQuadArray( int vertexCount, int vertexFormat ) throws java.rmi.RemoteException;

    public QuadArrayRemote newQuadArray( int vertexCount, int vertexFormat,
                                                    int texCoordSetCount,
                                                    int[] texCoordSetMap ) throws java.rmi.RemoteException;
    
    public BoundingSphereRemote newBoundingSphere() throws java.rmi.RemoteException;

    public BoundingBoxRemote newBoundingBox() throws java.rmi.RemoteException;
    
    public Frame3DRemote newFrame3D() throws java.rmi.RemoteException;
    
    public Component3DRemote newComponent3D() throws java.rmi.RemoteException;
    
    public Container3DRemote newContainer3D() throws java.rmi.RemoteException;

    public SceneGraphObjectRemote newInstance( Class c ) throws java.rmi.RemoteException;
    
    public SceneGraphObjectRemote newInstance( Class c, Class[] parameterTypes, Object[] parameterArgs ) throws java.rmi.RemoteException;

}
