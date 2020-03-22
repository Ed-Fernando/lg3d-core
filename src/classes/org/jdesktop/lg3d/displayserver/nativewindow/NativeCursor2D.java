/**
 * Project Looking Glass
 *
 * $RCSfile$
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
 */
/** Copyright (c) 2004 Amir Bukhari
 *
 * Permission to use, copy, modify, distribute, and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice appear in all copies and that both that
 * copyright notice and this permission notice appear in supporting
 * documentation, and that the name of Amir Bukhari not be used in
 * advertising or publicity pertaining to distribution of the software without
 * specific, written prior permission.  Amir Bukhari makes no
 * representations about the suitability of this software for any purpose.  It
 * is provided "as is" without express or implied warranty.
 *
 * AMIR BUKHARI DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,
 * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS, IN NO
 * EVENT SHALL AMIR BUKHARI BE LIABLE FOR ANY SPECIAL, INDIRECT OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.displayserver.fws.CursorImageListener;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.BoundingBox;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.sg.GeometryArray;
import org.jdesktop.lg3d.sg.ImageComponent2D;
import org.jdesktop.lg3d.sg.IndexedQuadArray;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.Texture;
import org.jdesktop.lg3d.sg.Texture2D;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.sg.TransformGroup;
import org.jdesktop.lg3d.sg.TransparencyAttributes;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Toolkit3D;


/**
 * @author bukhari
 */
public class NativeCursor2D extends Cursor3D implements CursorImageListener {
    // make sure that the cursor gets rendered in front of native windows
    private static BoundingBox BOUNDS_TO_BRING_TO_FRONT
        = new BoundingBox(new Point3f(-0.0f, -0.0f, -0.0f), new Point3f(0.0f, 0.0f, 0.5f));
    
    private FoundationWinSys fws = FoundationWinSys.getFoundationWinSys();
    protected ImagePanel shape = null;
    protected Appearance app = null;
    private Component3D center = null;
    protected TransformGroup trGroup;
    protected Transform3D t3d = new Transform3D();
    protected TransparencyAttributes bodySharedTransAttrs;

    public NativeCursor2D() {
        this("NATIVE_CURSOR_2D");
    }

    protected NativeCursor2D (String cursorName) {
        super(cursorName);
	initCursorImageListener();
    }

    protected void initCursorImageListener () {
        fws.addCursorImageListener(this);
    }

    /*
     * update the cursor image.
     */
    public void imageChanged(ImageComponent2D image, int actualWidth,
            int actualHeight, int hotX, int hotY) {

        int hotXDistance = (image.getWidth()/2) - hotX;
        int hotYDistance = (image.getHeight()/2) - hotY;
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        if (shape == null) {
            //setVisible(true);
            bodySharedTransAttrs = new TransparencyAttributes(
                    TransparencyAttributes.BLENDED, 0.5f);
            bodySharedTransAttrs
                    .setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
            bodySharedTransAttrs.setTransparency(1.0f);

            app = new Appearance();

            Texture2D t2d = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                    image.getWidth(), image.getHeight());
            t2d.setImage(0, image);
            t2d.setCapability(Texture2D.ALLOW_IMAGE_READ);
            app.setTexture(t2d);
            app.setCapability(Appearance.ALLOW_TEXTURE_READ);
            app.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
            app.setTransparencyAttributes(bodySharedTransAttrs);
            
            shape = new ImagePanel(
                    toolkit3d.widthNativeToPhysical(image.getWidth()),
                    toolkit3d.heightNativeToPhysical(image.getHeight()), app);
            
            // REMINDER -- this is an workaround; to be revised
            shape.setBoundsAutoCompute(false);
            shape.setBounds(BOUNDS_TO_BRING_TO_FRONT);
            
            trGroup = new TransformGroup();
            trGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            trGroup.addChild(shape);

	    // There is a race condition here that we need to avoid.
	    // The problem is that the cursor may be in the process
	    // of being detached from the scene group (i.e. becoming 
	    // invisible) but the animation may not yet be finished.
	    // This seems to happen more in app mode than in session mode.
	    // If the branch NativeCursor2D is live in the scene graph
	    // Java3D will throw an exception  when the transform group 
	    // is added. But if we use a branch group in between everything
	    // will work.
            //addChild(trGroup);
            BranchGroup bg = new BranchGroup();
	    bg.addChild(trGroup);
            addChild(bg);
            
            t3d.set(
                new Vector3f(
                    toolkit3d.widthNativeToPhysical(hotXDistance),
                    toolkit3d.heightNativeToPhysical(-hotYDistance),
                    0.0f));
            trGroup.setTransform(t3d);
        } else {

            shape.setSize(
                    toolkit3d.widthNativeToPhysical(image.getWidth()),
                    toolkit3d.heightNativeToPhysical(image.getHeight()));
            Texture2D t2d = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                    image.getWidth(), image.getHeight());
            t2d.setImage(0, image);
            t2d.setCapability(Texture2D.ALLOW_IMAGE_READ);
            app.setTexture(t2d);
            t3d.set(
                new Vector3f(
                    toolkit3d.widthNativeToPhysical(hotXDistance),
                    toolkit3d.heightNativeToPhysical(-hotYDistance),
                    0.0f));
            trGroup.setTransform(t3d);
        }
    }
}

class ImagePanel extends Shape3D {

    private IndexedQuadArray geom = null;

    public ImagePanel(float width, float height, Appearance app) {
        
        setAppearance(app);
        geom = new IndexedQuadArray(4,
                GeometryArray.COORDINATES 
                | GeometryArray.TEXTURE_COORDINATE_2, 1,
                new int[] { 0 }, 4);
        geom.setCapability(IndexedQuadArray.ALLOW_COORDINATE_WRITE);
        geom.setCapability(IndexedQuadArray.ALLOW_COORDINATE_INDEX_WRITE);
        geom.setCapability(IndexedQuadArray.ALLOW_TEXCOORD_WRITE);
        geom.setCapability(IndexedQuadArray.ALLOW_TEXCOORD_INDEX_WRITE);
        setSize(width, height);
        setGeometry(geom);
        this.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        this.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        this.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
    }
    
    public void setSize(float width, float height) {
        float w = width / 2.0f;
        float h = height / 2.0f;

        float[] coords = { 
                -w, -h, 0.0f, 
                 w, -h, 0.0f, 
                 w,  h, 0.0f, 
                -w,  h, 0.0f, 
                };

        int[] indices = { 0, 1, 2, 3, };

        float[] texCoords = { 
                0.0f, 0.0f, 
                1.0f, 0.0f, 
                1.0f, 1.0f, 
                0.0f, 1.0f 
                };

        float[] normals = { 0.0f, 0.0f, 1.0f, };

        int[] normalIndices = { 0, 0, 0, 0, };

        int[] colorIndices = { 0, 1, 0, 1 };
        
        geom.setCoordinates(0, coords);
        geom.setCoordinateIndices(0, indices);
        geom.setTextureCoordinates(0, 0, texCoords);
        geom.setTextureCoordinateIndices(0, 0, indices);
    }
}

