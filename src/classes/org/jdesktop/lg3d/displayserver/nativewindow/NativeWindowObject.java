/**
 * Project Looking Glass
 *
 * $RCSfile: NativeWindowObject.java,v $
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
 * $Revision: 1.15 $
 * $Date: 2007-04-10 22:58:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import java.util.ArrayList;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;

import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;

import org.jdesktop.lg3d.displayserver.DisplayServerControl;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.sg.Group;
import org.jdesktop.lg3d.sg.ImageComponent2D;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.Switch;
import org.jdesktop.lg3d.sg.Texture;
import org.jdesktop.lg3d.sg.Texture2D;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.sg.TransformGroup;
import org.jdesktop.lg3d.sg.TransparencyAttributes;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;


// TODO: TEMPORARY: We need this only until the Yup bugs in Java3D 
// are fixed and we no longer need to switch back and forth between
// Yup and Ydown images. See the comment for the USE_YUP_IMAGES member
// of DisplayServerControl.

/**
 * A BranchGroup that contains all the shape3D necessary to
 * render a TiledNativeWindowImage.
 *
 * The current implementation uses a single Shape3D and Texture which
 * limits a windows size to the maximum texture size that the graphics
 * card can display. Current NVidia cards support 4k x 4k textures,
 * but ATI cards seem to only support 1k x 1k.
 *
 * A future implementation will use a grid of Shape3D's allowing windows
 * of any size to be viewed on any hardware. Prototype code for this is
 * already included.
 *
 * @author  paulby
 */
public class NativeWindowObject extends Group {
    /*
     * The set of Shapes grows automatically to accomodate window resize. When
     * the window reduces in size the shapes are disabled but are not removed
     * from the scene graph (the remove operation is somewhat heavy weight). A
     * watchdog is setup so that once the window size is fixed for a certain
     * period if fires and causes unused shapes to actually be removed from the
     * graph.
     */
    private TiledNativeWindowImage windowImage;
    private int currentRows;
    private int currentCols;
    
    // LG3D assumes the origin is in the geometric center of an object.
    // For performance reasons the geomtry for the tiled images has it's
    // origin in the top left, so center translates the set of
    // shapes so the origin appears at the geometric center
    private TransformGroup center;
    private NativePane bg;
    private Transform3D centerT3D;
    private Vector3f v3f;
    
    private ArrayList<GridCell> rowList = new ArrayList();
    private ArrayList<GridCell> colList = new ArrayList();
    
    // EXPEREMENTEL
    //private boolean removedPlane = false;
    
    private boolean renderingOrderLater;
    private float initTransparency;
    private int fuzzyEdgeWidth;
    
    /** Creates a new instance of TiledShape3Ds */
    public NativeWindowObject(TiledNativeWindowImage windowImage, 
            float initTransparency, boolean renderingOrderLater, int fuzzyEdgeWidth) 
    {
        this.windowImage = windowImage;
        this.initTransparency = initTransparency;
        this.renderingOrderLater = renderingOrderLater;
        this.fuzzyEdgeWidth = fuzzyEdgeWidth;
        
        bg = new NativePane();
        bg.setWid(windowImage.getWid());
        bg.setNativeWidth(windowImage.getWinPreferredWidth());
        bg.setNativeHeight(windowImage.getWinPreferredHeight());
        // TODO: this is a kludge
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        bg.setWidth(toolkit3d.widthNativeToPhysical(windowImage.getWinPreferredWidth()));
        bg.setHeight(toolkit3d.heightNativeToPhysical(windowImage.getWinPreferredHeight()));
        
        center = new TransformGroup();
        centerT3D = new Transform3D();
        v3f = new Vector3f();
        
        center.addChild(bg);
        
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        center.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        createShape3Ds();
        
        // Only requred for current sizeChanged implementation
        rowList.get(0).getFuzzyEdgePanel().setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        rowList.get(0).getFuzzyEdgePanel().getAppearance().setCapability(Appearance.ALLOW_TEXTURE_WRITE);
        
        this.addChild(center);
    }
    
    public boolean isRemwin () {
	return bg.isRemwin();
    }

    public void setIsRemwin (boolean isRemwin) {
	bg.setIsRemwin(isRemwin);
    }

    /**
     * Assumes only the base cell is used
     */
    public void sizeChanged() {
        GridCell base = rowList.get(0);
// EXPEREMENTEL
//        if (!windowImage.isEndResize()) {
//            base.setEnabled(false);
//            removedPlane = true;
//            base.getFuzzyEdgePanel().getAppearance().setTexture(null);
//            return;
//        } else if (removedPlane) {
//            base.setEnabled(true);
//            removedPlane = false;
//        }
        ImageComponent2D ic2d = windowImage.getTile(0, 0);
        logger.fine("sizeChanged Using image " + ic2d);
        Texture2D t2d = null;
        if (windowImage.isEndResize()) {
            t2d = new Texture2D(Texture.BASE_LEVEL, Texture.RGB,
                    ic2d.getWidth(), ic2d.getHeight());
                    t2d.setMinFilter(Texture.BASE_LEVEL_LINEAR);
                    t2d.setMagFilter(Texture.BASE_LEVEL_LINEAR);
                    t2d.setImage(0, ic2d);
                    t2d.setCapability(Texture2D.ALLOW_IMAGE_READ);
        }
        
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
	int width = windowImage.getWinPreferredWidth();
	int height = windowImage.getWinPreferredHeight();
        base.setSize(
            toolkit3d.widthNativeToPhysical(width),
            toolkit3d.heightNativeToPhysical(height));
        if (windowImage.isEndResize()) {
            base.getFuzzyEdgePanel().getAppearance().setTexture(t2d);
        }
        
        v3f.x = -toolkit3d.widthNativeToPhysical(width)/2f;
        v3f.y = toolkit3d.heightNativeToPhysical(height)/2f;
        centerT3D.set(v3f, 1.0f);
        center.setTransform(centerT3D);
        
        bg.setNativeWidth(width);
        bg.setNativeHeight(height);
        // TODO: this is a kludge
        bg.setWidth(toolkit3d.widthNativeToPhysical(width));
        bg.setHeight(toolkit3d.heightNativeToPhysical(height));

	// Fix for 214. See comment on FoundationWinSys.damageEntireWindow.
	FoundationWinSys.getFoundationWinSys().damageWindowRectangle(windowImage.getWid(),
								     0, 0, width, height);
    }
    
    /**
     * Need TiledNativeWindowImage to return the same ImageComponents for each
     * cell after they have been created. This means that the base tile size
     * is fixed at creation time. We need to experiment to find the optimum
     * solution, should we recreate the base texture to allow the base image
     * to resize (on every size change, or perhaps when the number of
     * extended cells grows to a certain number) or should tbe base size
     * be fixed at creation time ?
     */
    public void sizeChangedExperimental() {
        logger.fine("Window Size " + windowImage.getWinPreferredWidth() + " " + 
        	windowImage.getWinPreferredHeight());
        //rowList.clear();
        //colList.clear();
        //this.removeAllChildren();
        //this.addChild(createShape3Ds());
        
        int newRowCount = windowImage.getNumGridRows()-windowImage.getNumBaseRows();
        int rowChange = newRowCount-rowList.size();
        if (rowChange > 0) {
            addRows(rowChange);
        }
        if (newRowCount > currentRows) {
            // Enable rows
            for(int row = currentRows; row < newRowCount; row++)
                enableRow(row, true);
        }
        
        int heightDiff = (windowImage.getBaseTileHeight()+newRowCount*windowImage.getExpansionTileHeight()) - windowImage.getWinHeight();
        
        // TODO if currentRows==1 && currentCols==1 set Base tile width & height in a single call
        
        assert( heightDiff >= 0 );
        logger.fine("Height Diff "+heightDiff+"  "+currentRows );
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        if (currentRows == 1) {
            setRowHeight( currentRows-1, toolkit3d.heightNativeToPhysical(windowImage.getBaseTileHeight()-heightDiff) );
        } else {
            setRowHeight( currentRows-1, toolkit3d.heightNativeToPhysical(windowImage.getExpansionTileHeight()-heightDiff) );
        }
        
        int newColCount = 0;
        int widthDiff = (windowImage.getBaseTileWidth()+newColCount*windowImage.getExpansionTileWidth()) - 
        	windowImage.getWinPreferredWidth();
        logger.fine("Width Diff "+widthDiff+"  "+currentCols );
        if (currentCols == 1) {
            setColWidth( currentCols-1, toolkit3d.widthNativeToPhysical(windowImage.getBaseTileWidth()-widthDiff) );
        } else {
            setColWidth( currentCols-1, toolkit3d.widthNativeToPhysical(windowImage.getExpansionTileWidth()-widthDiff) );
        }
        //enableRow( 2, false );
        //enableCol( 1, false );
        
        //setRowHeight( 1, NativeWindowLookAndFeel.heightNativeToPhysical(windowImage.getTileHeight()/2) );
        
        v3f.x = -toolkit3d.widthNativeToPhysical(windowImage.getWinPreferredWidth())/2f;
        v3f.y = toolkit3d.heightNativeToPhysical(windowImage.getWinPreferredHeight())/2f;
        centerT3D.set( v3f, 1.0f );
        center.setTransform(centerT3D);
    }
    
    private void enableRow(int row, boolean enable) {
        assert(row < rowList.size());
        GridCell cell = rowList.get(row);
        while(cell != null) {
            cell.setEnabled(enable);
            cell = cell.nextInRow;
        }
        
        // TODO When enabling check size is tileHeight and reset if not
    }
    
    private void enableCol(int col, boolean enable) {
        assert(col < colList.size());
        GridCell cell = colList.get(col);
        while(cell != null) {
            cell.setEnabled(enable);
            cell = cell.nextInCol;
        }
        
        // TODO when enabling check size is tileWidth and reset if not
    }
    
    private void setRowHeight(int row, float height) {
        assert(row < rowList.size());
        GridCell cell = rowList.get(row);
        while(cell != null) {
            cell.setHeight(height);
            cell = cell.nextInRow;
        }
    }
    
    private void setColWidth(int col, float width) {
        assert(col < colList.size());
        GridCell cell = colList.get(col);
        while(cell != null) {
            cell.setWidth(width);
            cell = cell.nextInCol;
        }
        
        // TODO when enabling check size is tileWidth and reset if not
    }
    
    private void addRows( int numRows ) {
        GridCell previousInRow;
        GridCell previousInCol;
        
        logger.fine("Adding rows "+numRows);
        
        previousInCol = rowList.get(rowList.size() - 1);
        for(int row = rowList.size(); row < rowList.size() + numRows; row++) {
            previousInRow = null;
            for(int col = 0; col < colList.size(); col++) {
                GridCell cell = createCell( row, col, previousInRow, previousInCol );
                previousInRow = cell;
                if (col == 0) {
                    rowList.add(cell);
                }
                if (row == 0) {
                    colList.add(cell);
                }
                if (previousInCol != null) {
                    previousInCol = previousInCol.nextInRow;
                }
            }
            previousInCol = rowList.get(row);
        }
        currentRows = rowList.size();
    }
    
    private void createShape3Ds() {
        GridCell previousInRow;
        GridCell previousInCol;
        
        //rowList.ensureCapacity(windowImage.getNumRows());
        //colList.ensureCapacity(windowImage.getNumCols());
        
        rowList.clear();
        colList.clear();
        
        // TODO Base cell should be a different size !
        // Expansion cells never refer to the base cell
        previousInRow = previousInCol = null;
        GridCell baseCell = createCell( 0,0, null, null );
        rowList.add(baseCell);
        colList.add(baseCell);
        
        previousInCol = null;
        for(int row=0; row<windowImage.getNumExpansionRows(); row++) {
            previousInRow = null;
            for(int col=0; col<windowImage.getNumExpansionCols(); col++) {
                GridCell cell = createCell( row,
                col,
                previousInRow,
                previousInCol );
                
                previousInRow = cell;
                if (col==0)
                    rowList.add(cell);
                if (row==0)
                    colList.add(cell);
                if (previousInCol!=null)
                    previousInCol = previousInCol.nextInRow;
            }
            previousInCol = rowList.get(row);
        }
        
        // Add 1 for the Base tile
        currentRows = windowImage.getNumExpansionRows()+1;
        currentCols = windowImage.getNumExpansionCols()+1;
    }
    
    GridCell createCell(int row, int col, GridCell previousInRow, 
        GridCell previousInCol) 
    {
        int tileWidth;
        int tileHeight;
        if (row == 0 && col == 0) {
            tileHeight = windowImage.getWinPreferredHeight();
            tileWidth = windowImage.getWinPreferredWidth();
            tileHeight = windowImage.getBaseTileHeight();
            tileWidth = windowImage.getBaseTileWidth();
        } else {
            tileHeight = windowImage.getExpansionTileHeight();
            tileWidth = windowImage.getExpansionTileWidth();
        }
        
        Appearance app 
            = new SimpleAppearance(
                1.0f, 1.0f, 1.0f, initTransparency,
                SimpleAppearance.ENABLE_TEXTURE 
                    | ((renderingOrderLater)?(0):(SimpleAppearance.DISABLE_CULLING)));
        
        ImageComponent2D ic2d = windowImage.getTile(row, col);
        logger.fine("Using image "+ic2d);
        Texture2D t2d
            = new Texture2D(Texture.BASE_LEVEL, Texture.RGB,
                ic2d.getWidth(), ic2d.getHeight());
                t2d.setMinFilter(Texture.BASE_LEVEL_LINEAR);
                t2d.setMagFilter(Texture.BASE_LEVEL_LINEAR);
                t2d.setImage(0, ic2d);
        app.setTexture(t2d);
        
        t2d.setCapability(Texture2D.ALLOW_IMAGE_READ);
        
        Transform3D t3d = new Transform3D();
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        t3d.set(
            new Vector3f(
                col * toolkit3d.heightNativeToPhysical(tileHeight),
                row * toolkit3d.widthNativeToPhysical(tileWidth),
                0f),
            1f);
        
        float pWidth = toolkit3d.widthNativeToPhysical(tileWidth);
        float pHeight = toolkit3d.heightNativeToPhysical(tileHeight);
        float pEdge = toolkit3d.widthNativeToPhysical(fuzzyEdgeWidth);
        
        NativeWindowFuzzyEdgePanel fep = new NativeWindowFuzzyEdgePanel(
            pWidth,
            pHeight,
            pEdge, pEdge, pEdge, pEdge,
            app, 
            NativeWindowFuzzyEdgePanel.OriginType.TOP_LEFT, 
            DisplayServerControl.USE_YUP_IMAGES,
            true);
        
        GridCell cell = new GridCell(
            t3d,
            previousInRow,
            previousInCol,
            fep);
        bg.addChild(cell);
        return cell;
    }
    
    class GridCell extends BranchGroup {
        GridCell nextInRow = null;
        GridCell nextInCol = null;
        TransformGroup cellCenter;
        NativeWindowFuzzyEdgePanel fep;
        Switch cellSwitch;
        
        GridCell(
            Transform3D center,
            GridCell previousInRow,
            GridCell previousInCol,
            NativeWindowFuzzyEdgePanel fep) 
        {
            this.fep = fep;
            this.setCapability(BranchGroup.ALLOW_DETACH);
            if (previousInRow != null) {
                previousInRow.nextInRow = this;
            }
            if (previousInCol != null) {
                previousInCol.nextInCol = this;
            }
            cellCenter = new TransformGroup(center);
            cellCenter.addChild(fep);
            cellSwitch = new Switch();
            cellSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
            cellSwitch.addChild(cellCenter);
            cellSwitch.setWhichChild(Switch.CHILD_ALL);
            this.addChild(cellSwitch);
        }
        
        void setEnabled(boolean enable) {
            if (enable) {
                cellSwitch.setWhichChild(Switch.CHILD_ALL);
            } else {
                cellSwitch.setWhichChild(Switch.CHILD_NONE);
            }
        }
        
        void setHeight(float height) {
            fep.setHeight(height,
                (float)windowImage.getBaseTileHeight() / (float)windowImage.getWinPreferredHeight());
        }
        
        void setWidth(float width) {
            fep.setWidth(width,
                (float)windowImage.getBaseTileWidth() / (float)windowImage.getWinPreferredWidth());
        }
        
        void setSize(float width, float height) {
            if (windowImage.isEndResize()) {
                fep.setSize(width, height,
                (float)windowImage.getBaseTileWidth() / (float)windowImage.getWinPreferredWidth(),
                (float)windowImage.getBaseTileHeight() / (float)windowImage.getWinPreferredHeight());
            } else {
        	fep.setSize(width, height,
                        0,
                        0);
            }
        }
        
        NativeWindowFuzzyEdgePanel getFuzzyEdgePanel() {
            return fep;
        }
    }
}
