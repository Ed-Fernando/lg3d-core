/**
 * Project Looking Glass
 *
 * $RCSfile: TiledNativeWindowImage.java,v $
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
 * $Revision: 1.14 $
 * $Date: 2007-04-10 22:58:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import gnu.x11.Visual;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.Map;
import javax.media.j3d.Canvas3D;
import org.jdesktop.lg3d.sg.ImageComponent2D;
import org.jdesktop.lg3d.displayserver.fws.WindowContents;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;

// TODO: TEMPORARY: We need this only until the Yup bugs in Java3D 
// are fixed and we no longer need to switch back and forth between
// Yup and Ydown images. See the comment for the USE_YUP_IMAGES member
// of DisplayServerControl.
import org.jdesktop.lg3d.displayserver.DisplayServerControl;

/**
 * This object implements an image which consists of multiple smaller tiles.
 * The total dimensions of the entire image can be greater than the maximum
 * texture dimensions of the graphics device. The dimension of each tile is 
 * less than or equal to the maximum texture dimensions of the device.
 *
 * The image consists of a base tile, which can grow to the maximum device
 * texture dimensions and a set of expansion tiles, which provide for 
 * image growth on the right and bottom sides.
 *
 *
 * Here is an example of one such image. The grid locations
 * (described below) are location annotated for several of
 * the tiles.
 *
 *     +-----------------------------+----+----+----+
 *     |    . 	 .    .	   .    .    |0,6 |0,7 |0,8 |
 *     |    .	 .  Base   .    .    |    |    |    |
 *     |.............................+----+----+----+
 *     |    .	 .  Tile   .	.    |1,6 |1,7 |1,8 |
 *     |    .    .    .	   .    .    |    |    |    |
 *     |.............................+----+----+----+
 *     |    .    (0,0 to 2,5)   .    |... |... |... |
 *     |    .	 .    .    .	.    |    |    |    |
 *     +----+----+----+----+----+----+----+----+----+
 *     |3,0 |3,1 |... |    |    |    |    |    |    |
 *     |    |	 |    |    |    |    |    |    |    |
 *     +-----------------------------+----+----+----+
 *     |4,0 |4,1 |... |    |    |    |    |    |4,8 |
 *     |    |	 |    |    |    |    |    |    |    |
 *     +-----------------------------+----+----+----+
 *     
 * The width and height of all tiles are powers of two. The tiles are
 * not necessarily square. The expansion tiles all have the same
 * dimensions. 
 * 
 * The caller can use the default tile dimensions or provide their
 * own. Using smaller expansion tile dimensions provides for 
 * more efficient texture memory usage. However, care must be taken
 * to not use dimensions that are too small, otherwise there can
 * be waste due to texture memory minimum sizes. It is suggested
 * that the tile size be made a user configuration option.
 *
 * To access a particular tile, the caller supplies a (row, column)
 * grid location to getTile. This grid extends over the entire 
 * composite image, including the base tile. There is one grid 
 * location per expansion tile, and multiple grid locations which 
 * cover the base tile. If the caller supplies a grid location 
 * which covers the base tile, the base tile is returned. Otherwise 
 * the the expansion tile for that location is returned. 
 *
 * Note: the expansion width/height you specify must be an integral
 * factor of the device maximum texture width/height.
 */

public class TiledNativeWindowImage 
{
    
    public static final int DEFAULT_EXPANSION_TILE_WIDTH = 64;
    public static final int DEFAULT_EXPANSION_TILE_HEIGHT = 64;
    
    private static final RuntimeException TEX_MAX_DIMS_EXCEPTION = new RuntimeException(
			 "Cannot figure out maximum texture dimensions");

    private static final RuntimeException EXP_TILE_WIDTH_FACTOR_EXCEPTION = new RuntimeException(
			 "Expansion tile width is not an integral factor of device maximum texture width");

    private static final RuntimeException EXP_TILE_HEIGHT_FACTOR_EXCEPTION = new RuntimeException(
			 "Expansion tile height is not an integral factor of device maximum texture height");
    private static final int RGB5_565 = 2016; // green mask is 0x7e0 
    private static final int RGB5_555 = 992;
    
    private long wid;
    private int  winWidth;
    private int  winHeight;
    private int  winPreferredWidth;
    private int  winPreferredHeight;
    private int  expansionTileWidth;
    private int  expansionTileHeight;
    private int  imageFormat;
    private boolean overlap;
    
    // The window dimensions rounded up to the nearest power of two
    private int  baseWidth;
    private int  baseHeight;

    private int numGridCols;
    private int numGridRows;
    private int numBaseCols;
    private int numBaseRows;
    private int numExpansionCols;
    private int numExpansionRows;

    private int numRightCols;
    private int numRightRows;
    private int numBottomCols;
    private int numBottomRows;

    private int texMaxWidth;
    private int texMaxHeight;

    private Object userData;
    
    //  depth
    private static final int DEPTH16 = 16;
    private static final int DEPTH24 = 24;
    private int depth;
    private Visual visual = null;
    private int greenMask = -1;
    
    private Semaphore filledSemaphore = new Semaphore( 0, false );
    
    private Logger logger = Logger.getLogger("lg.sg");
    
    // The base tile
    private ImageComponent2D     baseTile;

    // Expansion tile arrays.
    // I (arbitrarily) store the lower right "corner" tiles with the bottom tiles.
    private ArrayList<ArrayList> rightTiles;
    private ArrayList<ArrayList> bottomTiles;
    
    // True if there are expansion tiles
    private boolean multiTile = false;
    private boolean endResize = true;
    
    /**
     * Creates a TiledNativeWindowImage which will be displayed in the given 
     * window system.
     *
     * expansionTileWidth will default to DEFAULT_EXPANSION_TILE_WIDTH.
     * expansionTileHeight will default to DEFAULT_EXPANSION_TILE_HEIGHT.
     * overlap will default to false.
     *
     * @param wid    The window ID of the window.
     * @param imageFormat The image format to use for creating the ImageComponent2Ds.
     * @param winWidth The width of the window (including the border, if supported
     * 		by the Foundation Window System).
     * @param winHeight The height of the window (including the border, if supported
     * 		by the Foundation Window System).
     */
    public TiledNativeWindowImage(FoundationWinSys fws, long wid, int imageFormat, 
				  int winWidth, int winHeight) 
	throws IllegalArgumentException
    {        
	this(fws, wid, imageFormat, winWidth, winHeight, false, 24,null);	
    }
    
    /**
     * Creates a TiledNativeWindowImage which will be displayed in the given canvas.
     *
     * expansionTileWidth will default to DEFAULT_EXPANSION_TILE_WIDTH.
     * expansionTileHeight will default to DEFAULT_EXPANSION_TILE_HEIGHT.
     * overlap will default to false.
     *
     * @param wid    The window ID of the window.
     * @param imageFormat The image format to use for creating the ImageComponent2Ds.
     * @param winWidth The width of the window (including the border, if supported
     * 		by the Foundation Window System).
     * @param winHeight The height of the window (including the border, if supported
     * 		by the Foundation Window System).
     */
    public TiledNativeWindowImage(FoundationWinSys fws, long wid, int imageFormat, 
				  int winWidth, int winHeight, int depth, Visual visual) 
	throws IllegalArgumentException
    {        
	this(fws, wid, imageFormat, winWidth, winHeight, false, depth, visual);	
    }

    /**
     * Creates a TiledNativeWindowImage which will be displayed in the given canvas.
     *
     * overlap will default to false.
     *
     * @param wid    The window ID of the window.
     * @param imageFormat The image format to use for creating the ImageComponent2Ds.
     * @param winWidth The width of the window (including the border, if supported
     * 		by the Foundation Window System).
     * @param winHeight The height of the window (including the border, if supported
     * 		by the Foundation Window System).
     * @param expansionTileWidth The width of the expansion tiles. This must be an 
     *            integral factor of the device maximum texture width.
     * @param expansionTileHeight The height of the expansion tiles. This must be an 
     *            integral factor of the device maximum texture height.
     */
    public TiledNativeWindowImage (FoundationWinSys fws, long wid, int imageFormat, int winWidth, 
				   int winHeight, int expansionTileWidth,  int expansionTileHeight, 
				   int depth, Visual visual) 
	throws IllegalArgumentException
    {
	this(fws, wid, imageFormat, winWidth, winHeight, expansionTileWidth, expansionTileHeight, 
	     false, depth, visual);
    }

    /**
     * Creates a TiledNativeWindowImage which will be displayed in the given canvas.
     *
     * expansionTileWidth will default to DEFAULT_EXPANSION_TILE_WIDTH.
     * expansionTileHeight will default to DEFAULT_EXPANSION_TILE_HEIGHT.
     *
     * @param wid    The window ID of the window.
     * @param imageFormat The image format to use for creating the ImageComponent2Ds.
     * @param winHeight The height of the window (including the border, if supported
     * 		by the Foundation Window System).
     * @param expansionTileWidth The width of the expansion tiles. This must be an 
     *            integral factor of the device maximum texture width.
     * @param overlap If true, specifies that a the tiles will overlap the adjacent tiles
     *            on all sides by one texel. If false, all tiles will be nonoverlapping.
     *            In the overlap case, the images should be loaded into textures that were
     *            created with image borders and the texture wrap mode can be (if desired)
     *            set to CLAMP. If the overlap case, the images should be loaded into textures 
     *            that were created with no image borders and it is recommended that the
     *            wrap mode be set to CLAMP_TO_EDGE.
     *
     * Note: TODO: overlap==true is not yet implemented and this parameter is currently ignored.
     */
    public TiledNativeWindowImage (FoundationWinSys fws, long wid, int imageFormat, 
				   int winWidth, int winHeight, 
				  boolean overlap, int depth, Visual visual) 
	throws IllegalArgumentException
    {
        this(fws, wid, imageFormat, winWidth, winHeight, DEFAULT_EXPANSION_TILE_WIDTH, 
	     DEFAULT_EXPANSION_TILE_HEIGHT, overlap, depth, visual);
    }
    
    /**
     * Creates a TiledNativeWindowImage which will be displayed in the given canvas.
     *
     * @param wid    The window ID of the window.
     * @param imageFormat The image format to use for creating the ImageComponent2Ds.
     * @param winHeight The height of the window (including the border, if supported
     * 		by the Foundation Window System).
     * @param expansionTileWidth The width of the expansion tiles. This must be an 
     *            integral factor of the device maximum texture width.
     * @param expansionTileWidth The width of the expansion tiles. This must be an 
     *            integral factor of the device maximum texture width.
     * @param expansionTileHeight The height of the expansion tiles. This must be an 
     *            integral factor of the device maximum texture height.
     * @param overlap If true, specifies that a the tiles will overlap the adjacent tiles
     *            on all sides by one texel. If false, all tiles will be nonoverlapping.
     *            In the overlap case, the images should be loaded into textures that were
     *            created with image borders and the texture wrap mode can be (if desired)
     *            set to CLAMP. If the overlap case, the images should be loaded into textures 
     *            that were created with no image borders and it is recommended that the
     *            wrap mode be set to CLAMP_TO_EDGE.
     *
     * Note: TODO: overlap==true is not yet implemented and this parameter is currently ignored.
     */
    public TiledNativeWindowImage (FoundationWinSys fws, long wid, int imageFormat, 
				   int winWidth, int winHeight, 
				   int expansionTileWidth, int expansionTileHeight, 
				   boolean overlap, int depth, Visual visual) 
	throws IllegalArgumentException
    {
	if (winWidth <= 0 && winHeight <= 0) {
	    throw new IllegalArgumentException("Width (" + winWidth + ") and height (" + 
					       winHeight + ") cannot be <= 0");
	}

        this.wid = wid;
        this.imageFormat = imageFormat;
        this.winWidth = winWidth;
        this.winHeight = winHeight;
	this.expansionTileWidth = expansionTileWidth;
	this.expansionTileHeight = expansionTileHeight;
        this.overlap = overlap;
        this.depth   = depth;
        this.visual = visual;
        this.winPreferredWidth = winWidth;
	this.winPreferredHeight = winHeight;
        if(visual != null) {
            this.greenMask = visual.green_mask();
        }
	determineTexMaxDims(fws);

	// TODO: for now, just toss all old tiles and create new ones
	sizeChanged(winWidth, winHeight, true);
    }
    
    /**
     * Returns the window ID of the window
     */
    public long getWid() { return wid; }

    /**
     * Returns the width of the window
     * NOTE: this should be use by FWS (fws package).
     */
    public int getWinWidth() { return winWidth;}

    /**
     * Returns the height of the window
     * NOTE: this should be use by FWS (fws package).
     */
    public int getWinHeight() { return winHeight; }
    
    /**
     * Returns the width of the window.
     * NOTE: this should be use by 3D Part (nativewindow package).
     */
    public int getWinPreferredWidth() { return winPreferredWidth;}

    /**
     * Returns the height of the window.
     * NOTE: this should be use by 3D Part (nativewindow package).
     */
    public int getWinPreferredHeight() { return winPreferredHeight; }

    /**
     * Returns the current width of the base tile
     */
    public int getBaseTileWidth () { return baseWidth; }

    /**
     * Returns the current height of the base tile
     */
    public int getBaseTileHeight () { return baseHeight; }

    /**
     * Returns the width of the of the expansion tiles
     */
    public int getExpansionTileWidth() { return expansionTileWidth; }

    /**
     * Returns the height of the of the expansion tiles
     */
    public int getExpansionTileHeight() { return expansionTileHeight; }

    /**
     * Returns the current number of total grid rows (including the
     * base tile.
     */
    public int getNumGridRows() { return numGridRows; }

    /**
     * Returns the current number of total grid cols (including the
     * base tile.
     */
    public int getNumGridCols() { return numGridCols; }

    /**
     * Returns the current number of total grid rows occupied by
     * the base tile.
     */
    public int getNumBaseRows() { return numBaseRows; }

    /**
     * Returns the current number of total base cols (including the
     * base tile.
     */
    public int getNumBaseCols() { return numBaseCols; }

    /**
     * Returns the current number of expansion rows
     */
    public int getNumExpansionRows() { return numExpansionRows; }

    /**
     * Returns the current number of expansion columns
     */
    public int getNumExpansionCols() { return numExpansionCols; }
    
    /**
     * Returns the specified tile. 
     * 
     * Note: in order to facilitate fast traversal, this method does
     * not ensure that the specified row and column are within the grid.
     *
     * Note: the returned ImageComponent2D contains a userData2 property 
     * for use in FWS. To get to the java 3d IC2D within FWS use
     *
     * lgIC2D = getTile( a, b );
     * j3dIC2D = (javax.media.j3d.ImageComponent2D)lgIC2D.getWrapped();
     *
     * and to get from j3dIC2D back to the lg object
     *
     * lgIC2D = (ImageComponent2D)j3dIC2D.getUserData()
     */
   public ImageComponent2D getTile (int row, int col) {

	if (!multiTile ||
	    (row < numBaseRows && col < numBaseCols)) {
	    return baseTile;
	}

	// Is the desired tile in the right set or the bottom set?
	if (row >= numBaseRows) {
	    // It's in the bottom array
	    return getTileFromTileArray(bottomTiles, row - numBaseRows, col);
	} else {
	    // It's in the right array
	    return getTileFromTileArray(bottomTiles, row, col - numBaseCols);
	}
    }
    
    private ImageComponent2D getTileFromTileArray (ArrayList tileArray, 
						   int row, int col) {
	ArrayList<ImageComponent2D> cols = (ArrayList<ImageComponent2D>) tileArray.get(row);
	ImageComponent2D tile = (ImageComponent2D) cols.get(col);
	return tile;
    }

    /**
     * Wait for FWS to notify us that all of the tiles of this
     * object have been filled with valid image data.
     *
     * A warning message is logged every 2 seconds until the method
     * returns.
     */
    public void waitFilled() throws InterruptedException {
        if (org.jdesktop.lg3d.displayserver.fws.FoundationWinSys.getFoundationWinSys() 
                   instanceof org.jdesktop.lg3d.displayserver.fws.awt.WinSysAWT) {
            logger.warning("Wait Filled disabled because WinSysAWT being used");
            return;
        }
        while(!filledSemaphore.tryAcquire( 2, TimeUnit.SECONDS ))
            logger.warning("Waiting for TiledNativeWindowImage to be filled, wid = " + wid);
    }
    
    /**
     * Notify any waiters that all of the tiles of this object
     * have been filled with valid image data. This method is
     * only to be used by FWS.
     */
    private void notifyFilled() {
        filledSemaphore.release();
    }
    
    /**
     * Calls the loader.loadTile method for all the tiles in the image that
     * don't have valid contents. Note: this method is only to be used by FWS. 
     * 
     * @param loader The tile loader.
     * @param winContents An image which is the contents of the window.
     */
    public void fillTiles (TiledNativeWindowImageLoader loader, WindowContents winContents) {
	fillTilesEntireImage(loader, winContents);
	notifyFilled();
    }

    /**
     * Calls the loader.loadTile method for the tiles in the image which are 
     * covered by the given rectangle (in window relative coordinates. 
     * Note: this method is only to be used by FWS. 
     * 
     * @param loader The tile loader.
     * @param x  The x origin of the rectangle.
     * @param y  The y origin of the rectangle.
     * @param width  The width of the rectangle.
     * @param height The height of the rectangle.
     * @param winContents An image which is the contents of the window.
     */
    public void fillTiles (TiledNativeWindowImageLoader loader, int x, int y, 
			   int width, int height, WindowContents winContents) {

	// Single tile case
	if (!multiTile) {
	    // Load the base tile up to the window dimensions
	    loader.loadTile(baseTile, x, y, width, height, winContents);
	    return;
	}

	// TODO: currently, we don't support subregion loading for the multi-tile
	// case. So just load all of the tiles.
	fillTilesEntireImage(loader, winContents);
	notifyFilled();
    }


    // Calls the loader.loadTile method for the tiles in the image, to load
    // the tiles with contents from the window.

    private void fillTilesEntireImage (TiledNativeWindowImageLoader loader, 
				      WindowContents winContents) {
	int srcX, srcY;
	int maxWidth, maxHeight;

	// Single tile case
	if (!multiTile) {
	    // Load the base tile up to the window dimensions
	    loader.loadTile(baseTile, 0, 0, winWidth, winHeight, winContents);
	    return;
	}

	// Load the entire base tile 
	int loadBaseWidth  = Math.min(winWidth, baseWidth);
	int loadBaseHeight = Math.min(winHeight, baseHeight);
	loader.loadTile(baseTile, 0, 0, loadBaseWidth, loadBaseHeight, winContents);

	// Are there tiles on the right? If so, load them
	if (numRightCols > 0) {
	    srcX      = baseWidth + 1;
	    srcY      = 0;
	    maxWidth  = winWidth - loadBaseWidth;
	    maxHeight = loadBaseHeight;
	    fillTileArray(loader, rightTiles, numRightRows, numRightCols, srcX, srcY, 
			  maxWidth, maxHeight, winContents);
	}

	// Are there tiles on the bottom? If so, load them
	if (numBottomRows > 0) {
	    srcX      = 0;
	    srcY      = baseHeight + 1;
	    maxWidth  = winWidth;
	    maxHeight = winHeight - loadBaseHeight;
	    fillTileArray(loader, bottomTiles, numBottomRows, numBottomCols, srcX, srcY, 
			  maxWidth, maxHeight, winContents);
	}
    }

    // Given a loader and a tile array, use the loader to load the valid pixels from the
    // tile array's tiles. numRows and numCols are the number of rows and columns in the
    // tile array. srcStartX and srcStartY are origin of the (0,0) tile in the array 
    // (in window relative coordinates). maxWidth and maxHeight are the maximum number of 
    // texels in the X and Y directions to load. And image handle is a handle to a
    // foundation window system image.

    private void fillTileArray (TiledNativeWindowImageLoader loader, ArrayList<ArrayList> tileArray, 
				int numRows, int numCols, int srcStartX, int srcStartY, 
				int maxWidth, int maxHeight, WindowContents winContents) {
	int srcY = srcStartY;

	for (int row = 0; row < numRows && maxWidth > 0; row++) {
	    ArrayList<ImageComponent2D> cols = (ArrayList<ImageComponent2D>) tileArray.get(row);
	    int srcX = srcStartX;

	    for (int col = 0; col < numCols && maxHeight > 0; col++) {
		ImageComponent2D tile = (ImageComponent2D) cols.get(col);
		int widthToLoad  = Math.min(maxWidth, expansionTileWidth);
		int heightToLoad = Math.min(maxHeight, expansionTileHeight);
		
		loader.loadTile(tile, srcX, srcY, widthToLoad, heightToLoad, winContents);

                maxWidth  -= widthToLoad;
		maxHeight -= heightToLoad;
		srcX += expansionTileWidth;
	    }

	    srcY += expansionTileHeight;
	}
    }

    /**
     * Set the user data.
     */
    public void setUserData(Object userData) {
        this.userData = userData;
    }
    
    /**
     * Returns the user data.
     */
    public Object getUserData() {
        return userData;
    }
    
    /**
     * Used by the Foundation Window System to indicate that the 
     * dimensions of the window have changed. The size of the base
     * tile and the number of expansion tiles are increased or 
     * decreased to fit this size
     */
    public void sizeChanged(int winWidth, int winHeight, boolean endOfResize) 
        throws IllegalArgumentException
    {
	if (winWidth <= 0 && winHeight <= 0) {
	    throw new IllegalArgumentException("Width (" + winWidth + ") and height (" + 
					       winHeight + ") cannot be <= 0");
	}

	/* these will be used allways by creating the 3D object */ 
	this.winPreferredWidth = winWidth;
	this.winPreferredHeight = winHeight;
	
	/** these will be used by FWS */
	if (endOfResize) {
            this.winWidth = winWidth;
            this.winHeight = winHeight;
	}
	// TODO: for now, just toss all old tiles and create new ones
        
        logger.fine("Size Changed "+winWidth+" "+winHeight);

	// Figure out what the grid looks like
	calcGrid();
	
	endResize = endOfResize;
	if (endOfResize) {
	    // Create the tiles
	    createBaseAndExpansionTiles();
	}
    }    
    
    public boolean isEndResize() {
	return endResize;
    }
        
    private void dummyColorImage( ImageComponent2D tile, int row, int col ) {
        int tileWidth, tileHeight;
        
        if (col==0 && row==0) {
            tileWidth = baseWidth;
            tileHeight = baseHeight;
        } else {
            tileWidth = expansionTileWidth;
            tileHeight = expansionTileHeight;
        }
        
        java.awt.image.BufferedImage buf = new java.awt.image.BufferedImage( 
                                                    tileWidth, 
                                                    tileHeight, 
                                                    BufferedImage.TYPE_INT_ARGB );
        if (col%2==0)
            row+=1;
        
        java.awt.Graphics g = buf.getGraphics();
        
        if (row%2==0)
            g.setColor( java.awt.Color.red );
        else
            g.setColor( java.awt.Color.blue );
        
        g.fillRect(0,0, tileWidth, tileHeight);
        tile.set( buf );
    }
      
    private void dummyGridImage( ImageComponent2D tile, int row, int col ) {
        int tileWidth, tileHeight;
        
        if (col==0 && row==0) {
            tileWidth = baseWidth;
            tileHeight = baseHeight;
        } else {
            tileWidth = expansionTileWidth;
            tileHeight = expansionTileHeight;
        }
        
        java.awt.image.BufferedImage buf = new java.awt.image.BufferedImage( 
                                                    tileWidth, 
                                                    tileHeight, 
                                                    BufferedImage.TYPE_INT_ARGB );
        if (col%2==0)
            row+=1;
        
        java.awt.Graphics g = buf.getGraphics();
        if (row%2==0)
            g.setColor( java.awt.Color.red );
        else
            g.setColor( java.awt.Color.blue );
        
        for(int i=0; i<tileWidth; i+=8) {
            g.drawLine( i,0, i,tileHeight );
            g.drawLine( 0,i, tileWidth,i );
        }
        tile.set( buf );
    }

    private void dummyGridImage3( ImageComponent2D tile, int row, int col ) {
        int tileWidth, tileHeight;
        
        if (col==0 && row==0) {
            tileWidth = baseWidth;
            tileHeight = baseHeight;
        } else {
            tileWidth = expansionTileWidth;
            tileHeight = expansionTileHeight;
        }
        
        java.awt.image.BufferedImage buf = new java.awt.image.BufferedImage( 
                                                    tileWidth, 
                                                    tileHeight, 
                                                    BufferedImage.TYPE_INT_ARGB );
        if (col%2==0)
            row+=1;
        
        java.awt.Graphics g = buf.getGraphics();
        if (row%2==0)
            g.setColor( java.awt.Color.orange );
        else
            g.setColor( java.awt.Color.yellow );
        
        for(int i=0; i<tileWidth; i+=8) {
            g.drawLine( i,0, i,tileHeight );
            g.drawLine( 0,i, tileWidth,i );
        }
        tile.set( buf );
    }

    private void determineTexMaxDims (FoundationWinSys fws) {
	Canvas3D canvas = fws.getCanvas(0);
	// Get max texture dimensions from canvas
	Map canvasProps = canvas.queryProperties();
	if (canvasProps == null) {
	    throw TEX_MAX_DIMS_EXCEPTION;
	}
	texMaxWidth  = ((Integer)canvasProps.get("textureWidthMax")).intValue();
	texMaxHeight = ((Integer)canvasProps.get("textureHeightMax")).intValue();
        
//        logger.warning("Max Texture Size Clamped for testing");
//        texMaxWidth = 128;
//        texMaxHeight = 128;
        
	//prn("texMaxWidth  = " + texMaxWidth);
	//prn("texMaxHeight = " + texMaxHeight);

	// Check constructor arguments
        float numColsInTexMax = (float)texMaxWidth / (float)expansionTileWidth;
	float numRowsInTexMax = (float)texMaxHeight / (float)expansionTileHeight;
	if (numColsInTexMax - Math.floor(numColsInTexMax) > 0) {
	    throw EXP_TILE_WIDTH_FACTOR_EXCEPTION;
	}
	if (numRowsInTexMax - Math.floor(numRowsInTexMax) > 0) {
	    throw EXP_TILE_HEIGHT_FACTOR_EXCEPTION;
	}
    }

    private void calcGrid () {
	int expansionWidth, expansionHeight;

	// Base tile dimenions must be rounded up to nearest power of two
	baseWidth  = getPowerOfTwoUpperBound(winPreferredWidth);
	baseHeight = getPowerOfTwoUpperBound(winPreferredHeight);

	// Do we need expansion tiles?
 	// TODO: for now, never use expansion tiles
	//multiTile = baseWidth > texMaxWidth || baseHeight > texMaxHeight;

	if (multiTile) {

	    // How much extra space do we need on right and bottom?
	    expansionWidth  = winWidth - baseWidth;
	    expansionHeight = winHeight - baseHeight;
            if (expansionWidth<0)
                expansionWidth=0;
            if (expansionHeight<0)
                expansionHeight=0;
	    numExpansionCols = Math.round(expansionWidth/expansionTileWidth);
	    numExpansionRows = Math.round(expansionHeight/expansionTileHeight);
            
	    // How many grid cells are in the base tile (which is full)
	    numBaseCols = Math.round(baseWidth/expansionTileWidth);
	    numBaseRows = Math.round(baseHeight/expansionTileHeight);

	} else {
	    numExpansionCols = 0;
	    numExpansionRows = 0;
	    numBaseCols = Math.round(winWidth/expansionTileWidth);
	    numBaseRows = Math.round(winHeight/expansionTileHeight);
	}

	// What is the total number of grid cells?
	numGridCols = numBaseCols + numExpansionCols;
	numGridRows = numBaseRows + numExpansionRows;
    }

    private void createBaseAndExpansionTiles () {

	// Create a new base tile and toss old one (if there was any)
	// TODO: eventually, let the user specify the image types 
        BufferedImage bi = null;
	if(depth == DEPTH16) {
	    if(greenMask == RGB5_565) {
	        bi = new TileBufferedImage(baseWidth, baseHeight, BufferedImage.TYPE_USHORT_565_RGB);
	    }else if(greenMask == RGB5_555) {
	        bi = new TileBufferedImage(baseWidth, baseHeight, BufferedImage.TYPE_USHORT_555_RGB);
	    }
	}else if (depth == DEPTH24) {	    
	    bi = new TileBufferedImage(baseWidth, baseHeight, BufferedImage.TYPE_INT_ARGB);
	}
	
//	if (baseTile != null) {
//	    baseTile.getRenderedImage().copyData(bi.getRaster());
//	}
	baseTile = new ImageComponent2D(imageFormat, bi, true, DisplayServerControl.USE_YUP_IMAGES);

        baseTile.setCapability( ImageComponent2D.ALLOW_IMAGE_WRITE );
        baseTile.setCapability( ImageComponent2D.ALLOW_IMAGE_READ );
        baseTile.setCapability( ImageComponent2D.ALLOW_SIZE_READ );
        
	if (!multiTile) return;

	// Figure out how big the right and botoom arrays are.
	// We (arbitrarily) put the lower righthand corner tiles
	// in the bottom array.
	numBottomCols = numGridCols;
	numBottomRows = numExpansionRows;
	numRightCols  = numExpansionCols;
	numRightRows  = numBaseRows;
        
        logger.fine("numBottomCols "+numGridCols);
        logger.fine("numBottomRows "+numExpansionRows);
        logger.fine("numRightCols "+numExpansionCols);
        logger.fine("numRightRows "+numBaseRows);
        
	// Create both tile arrays, toss old ones
	rightTiles  = createTileArray(numRightCols, numRightRows);
	bottomTiles = createTileArray(numBottomCols, numBottomRows);
    }

    private ArrayList<ArrayList> createTileArray (int numRows, int numCols) {
	ArrayList<ArrayList> rows;

        rows = new ArrayList(numRows);
        for (int row = 0; row < numRows; row++) {
            ArrayList<ImageComponent2D> cols = new ArrayList(numCols);
            rows.add(row, cols);
            for (int col = 0; col < numCols; col++) {
                BufferedImage bi = null;
        	if(depth == DEPTH16) {
        	    bi = new BufferedImage(baseWidth, baseHeight, BufferedImage.TYPE_USHORT_565_RGB);
        	}else if (depth == DEPTH24) {
        	    bi = new BufferedImage(baseWidth, baseHeight, BufferedImage.TYPE_INT_ARGB);
        	}
		ImageComponent2D tile = new ImageComponent2D(imageFormat, 
							     bi, true, DisplayServerControl.USE_YUP_IMAGES);
                cols.add(col, tile);
            }
        }
	
	return rows;
    }

    private static int getPowerOfTwoUpperBound(int value) {
        
        if (value < 1)
            return value;
        
        int powerValue = 1;
        
        for (;;) {
            powerValue *= 2;
            if (value < powerValue) {
                return powerValue;
            }
        }
    }

    private static void prn (String str) { System.err.println(str); }

    /**
     * Cleanup resources, the window has been destroyed
     */
    void destroy() {
        baseTile.setUserData2(null);
        baseTile = null;
        
        bottomTiles = null;
        rightTiles = null;
    }
}

