/**
 * Project Looking Glass
 *
 * $RCSfile: ScreenCaptureBehavior.java,v $
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
 * $Date: 2006-09-14 19:39:29 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOnBehaviorPost;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Raster;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsContext3D;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import javax.imageio.ImageIO;

import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEvent;

/**
 * A class to capture the Looking Glass screen and write it to a png
 * file. Can also capture a continous stream of screens for later
 * processing into a movie.
 *
 * All canvas3D's must be the same size.
 *
 * Main resources are only allocated when an image is captured so 
 * it's cheap to have this object in the scene graph.
 *
 * TODO make readRaster etc a WeakReference so they can be gc'ed if 
 * they are not used again.
 *
 * @author  paulby
 */
public class ScreenCaptureBehavior extends javax.media.j3d.Behavior implements LgEventListener {
    
    private WakeupOnElapsedFrames elapsedFrame;
    private WakeupOnBehaviorPost post;
    private Raster readRaster;
    private GraphicsContext3D[] gc;
    private ImageComponent2D readImageComponent;
    private int imageNo=0;
    private int imageCount;
    private int rasterWidth=0;
    private int rasterHeight=0;
    
    private String format="png";
    private String dir;
    
    private static final Logger logger = Logger.getLogger("lg.displayserver");
    
    /** Creates a new instance of ScreenCapture */
    public ScreenCaptureBehavior( Canvas3D[] canvas3ds ) {
        gc = new GraphicsContext3D[canvas3ds.length];
        for(int i=0; i<gc.length; i++)
            gc[i] = canvas3ds[i].getGraphicsContext3D();
        
        AppConnectorPrivate.getAppConnector().addListener( this, ScreenCaptureEvent.class );
    }
    
    private void setup() {
        rasterWidth = gc[0].getCanvas3D().getWidth();
        rasterHeight = gc[0].getCanvas3D().getHeight(); 
        
        readImageComponent = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, 
                                                  rasterWidth, 
                                                  rasterHeight);
                                                                                
        readRaster = new Raster(new Point3f(0.0f,0.0f,0.0f),
                                       Raster.RASTER_COLOR, 0, 0, rasterWidth,
                                       rasterHeight, readImageComponent, null);
                                                                                
        
    }
    
    /**
     * Java3D method to initialize behavior
     */
    public void initialize() {
        setSchedulingBounds( new BoundingSphere( new Point3d(), Double.POSITIVE_INFINITY));
        
        // Passive wakeup
        elapsedFrame = new WakeupOnElapsedFrames(0, false);
        post = new WakeupOnBehaviorPost(this, 0);
        wakeupOn( post );
    }
    
    public void processStimulus( java.util.Enumeration e ) {
        if (readRaster==null || 
                rasterWidth!=gc[0].getCanvas3D().getWidth() || 
                rasterHeight!=gc[0].getCanvas3D().getHeight())
            setup();
        
        try {
            for(int i=0; i<gc.length; i++) {
                File file;
                file = new File(dir+"/lgscreen-"+i+"-"+imageNo+"."+format);
                gc[i].readRaster( readRaster );
                ImageIO.write( readImageComponent.getRenderedImage(), format, file );
                imageNo++;
            }
        } catch(Exception ex) {
            logger.log( Level.SEVERE, "Error writing image "+ex,ex);
        }
        
        if (imageNo<imageCount)
            wakeupOn(elapsedFrame);
        else
            wakeupOn(post);
    }
    
    public void processEvent( LgEvent evt ) {
        
        if (!(evt instanceof ScreenCaptureEvent)) {
            logger.warning("Unexpected event received "+evt.getClass().getName() );
            return;
        }
        
        ScreenCaptureEvent sce = (ScreenCaptureEvent)evt;
        
        format = sce.getFormat();
        dir = sce.getDirectory();
        
        switch( sce.getCaptureMode() ) {
            case SINGLE :
                imageCount = imageNo;
                break;
            case START :
                imageCount = Integer.MAX_VALUE;
                break;
            case STOP :
                imageCount = imageNo;
                break;
            case START_COUNT :
                imageCount = imageNo + sce.getFrameCount();
                break;
            default :
                logger.warning("Unrecognised Capture Mode "+sce.getCaptureMode() );
        }
        
        postId(0);
    }
    
    public Class<LgEvent>[] getTargetEventClasses() {
        return null; // FIXME
    }
}
