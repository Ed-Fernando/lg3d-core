/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyNativeWindowLookAndFeel.java,v $
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
 * $Revision: 1.42 $
 * $Date: 2007-04-11 20:13:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.decoration;

import java.util.Timer;
import java.util.TimerTask;
import javax.vecmath.Vector3f;
import javax.vecmath.Color4f;
import org.jdesktop.lg3d.scenemanager.utils.event.Component3DGestureMoveLeftEvent;
import org.jdesktop.lg3d.scenemanager.utils.event.Component3DGestureMoveRightEvent;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.Component3DToFrontEvent;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindow3D;
import org.jdesktop.lg3d.displayserver.nativewindow.NativePopup3D;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowLookAndFeel;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowObject;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowMonitor;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowControl;
import org.jdesktop.lg3d.displayserver.nativewindow.TiledNativeWindowImage;
import org.jdesktop.lg3d.displayserver.nativewindow.WindowShape;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.utils.transparency.TransparencyOrderedGroup;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.action.AppearanceChangeAction;
import org.jdesktop.lg3d.utils.action.ScaleActionBoolean;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.Component3DParkedEventAdapter;
import org.jdesktop.lg3d.utils.shape.GlassyPanel;
import org.jdesktop.lg3d.utils.shape.ImagePanel;
import org.jdesktop.lg3d.utils.shape.PickableRegion;
import org.jdesktop.lg3d.utils.shape.RectShadow;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.utils.shape.GlassyText2D;


public class GlassyNativeWindowLookAndFeel extends NativeWindowLookAndFeel {
    private static final float bodyHeader = 0.003f;
    private static final float bodyFooter = 0.0002f;
    private static final float bodyBorder = 0.001f;
    public static final float bodyDepth  = 0.006f;
    private static final float shadowN = 0.001f;
    private static final float shadowE = 0.002f;
    private static final float shadowS = 0.003f;
    private static final float shadowW = 0.001f;
    private static final float shadowI = 0.001f;
    
    private static final float buttonSize = bodyHeader + bodyBorder * 0.5f;
    private static final float buttonOnSize = buttonSize * 1.15f;
    private static final float topTitleHeight = buttonSize * 1.4f;
    private static final float spineTitleHeight = bodyDepth;
    private static final float resizeRegionSize = buttonSize * 0.75f;
    private /*static*/ final SimpleAppearance bodyApp
	= new SimpleAppearance(
	    0.6f, 1.0f, 0.6f,
	    SimpleAppearance.DISABLE_CULLING);

    private static final float layer0 = bodyDepth * -0.10f;
    private static final float layer1 = bodyDepth * -0.05f;
    private static final float layer2 = bodyDepth *  0.00f;
    private static final float layer3 = bodyDepth *  0.05f;
    private static final float layer4 = bodyDepth *  0.10f;
    
    private static final float backsideWinSizeRatio = 0.95f;
    
    private static final Timer flipperTimer = new Timer("GlassyL&F:FilpperTimer");
    
    private NativeWindow3D nativeWindow;
    private TiledNativeWindowImage appImage;
    private boolean decorated;
    private WindowShape windowShape;
    
    private Component3D rootCont;
    private Component3D popupRoot;
    private Component3D body;
    private NativeWindowObject bodyPanel;
    private Cursor3D nativeCursor;
    
    private Component3D minimizeButton;
    private Component3D maximizeButton;
    private Component3D closeButton;
    private Component3D resizeButton;
    private Component3D resizeSERegion;
    private Component3D dockLeftButton;
    private Component3D dockRightButton;
    private InvisibleButton resizeSRegion;
    private InvisibleButton moveRegion;
    
    private Title topTitle;
    private SpineTitle leftSpineTitle;
    private SpineTitle rightSpineTitle;
    
    private SimpleAppearance closeButtonOffAppearance;
    private SimpleAppearance closeButtonOnAppearance;
    private SimpleAppearance maximizeButtonOffAppearance;
    private SimpleAppearance maximizeButtonOnAppearance;
    private SimpleAppearance minimizeButtonOffAppearance;
    private SimpleAppearance minimizeButtonOnAppearance;
    private SimpleAppearance dockLeftButtonOffAppearance;
    private SimpleAppearance dockLeftButtonOnAppearance;
    private SimpleAppearance dockRightButtonOffAppearance;
    private SimpleAppearance dockRightButtonOnAppearance;
    private SimpleAppearance resizeButtonAppearance;
    
    private Vector3f tmpV3f = new Vector3f();
    private boolean beingFlipped = false;
    private Component3D backsideWindow = null;
    private StickyNote stickyNote = null;
    
    private Component3D panelComp;    
    
	/// TODO: need to preserve even if the new constructor is removed!
    private boolean isWonderland = true;

/*
    public void initialize (NativeWindow3D nativeWindow, TiledNativeWindowImage appImage, 
	                    boolean decorated, WindowShape shape, Cursor3D cursor) {

	isWonderland = false;

        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
	float width = toolkit3d.widthNativeToPhysical(appImage.getWinWidth());
	float height = toolkit3d.heightNativeToPhysical(appImage.getWinHeight());

	initialize(nativeWindow, appImage, decorated, shape, cursor, width, height);
    }

    public void initialize (NativeWindow3D nativeWindow, TiledNativeWindowImage appImage, 
	                    boolean decorated, WindowShape shape, Cursor3D cursor,
			    float width, float height) 
    {
        this.nativeWindow = nativeWindow;
        this.appImage = appImage;
        this.decorated = decorated;
        this.windowShape = shape;
        
        setupComponents(
            width, height, 
            nativeWindow.getNativeWindowControl().getName(),
	    cursor);
    }
*/

    public void initialize (NativeWindow3D nativeWindow, TiledNativeWindowImage appImage, 
	                    boolean decorated, WindowShape shape, Cursor3D cursor) {

        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
	float width = toolkit3d.widthNativeToPhysical(appImage.getWinWidth());
	float height = toolkit3d.heightNativeToPhysical(appImage.getWinHeight());

        this.nativeWindow = nativeWindow;
        this.appImage = appImage;
        this.decorated = decorated;
        this.windowShape = shape;
        
        setupComponents(
            width, height, 
            nativeWindow.getNativeWindowControl().getName(),
	    cursor);
    }

    private void setupComponents(float width, float height, String title,
				 Cursor3D cursor) {
        System.err.println("GNWLAF: wh = " + width + " " + height);
        panelComp = new Component3D();
        bodyPanel = new NativeWindowObject(appImage, 1.0f, false, 4);
	panelComp.addChild(bodyPanel);

	nativeCursor = cursor;
// TODO:DJ :HACK        panelComp.setCursor(nativeCursor);
        panelComp.setMouseEventPropagatable(true);
	decorated = nativeWindow.getNativeWindowControl().isDecorated();
	boolean isResizable = nativeWindow.getNativeWindowControl().isResizable();	
	boolean isMaximizable = nativeWindow.getNativeWindowControl().isMaximizable();
        boolean isNormalWin = nativeWindow.getNativeWindowControl().isNormalWindow();
        boolean isDockable = nativeWindow.getNativeWindowControl().isDockable();
        if (!decorated) {
            return;
        }
        
	body = new Component3D();
	GlassyPanel bodyDecoration 
	    = new GlassyPanel(
		0.1f, 0.1f,
		bodyDepth, bodyDepth * 0.1f,
		bodyApp);
	body.addChild(bodyDecoration);
        RectShadow bodyShadow
	    = new RectShadow(
		0.1f, 0.1f,
		shadowN, shadowE, shadowS, shadowW, shadowI,
		-bodyDepth,
		0.3f);
	body.addChild(bodyShadow);
	body.setTranslation(0.0f, (bodyHeader - bodyFooter) * 0.5f, layer0);
// TODO:DJ :HACK  body.setCursor(Cursor3D.MEDIUM_CURSOR);
        
        setupButtonAppearances();
        
	closeButton 
	    = new Button(buttonSize, closeButtonOffAppearance,
		buttonOnSize, closeButtonOnAppearance);
	closeButton.setCursor(Cursor3D.SMALL_CURSOR);
        closeButton.addListener(
            new MouseClickedEventAdapter(
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        //nativeWindow.getNativeWindowControl().destroy();
                        nativeWindow.changeEnabled(false);
                    }
                }));
        
        if ((isNormalWin) || (isMaximizable)) {
	    maximizeButton =
	        new Button(buttonSize, maximizeButtonOffAppearance,
		    buttonOnSize, maximizeButtonOnAppearance);
	    maximizeButton.setCursor(Cursor3D.SMALL_CURSOR);
	    maximizeButton.addListener(
	    	new MouseClickedEventAdapter(
	    	    new ActionNoArg() {
	    	    	public void performAction(LgEventSource source) {
                            if (nativeWindow.getNativeWindowControl().isMaximized()) {
                                nativeWindow.getNativeWindowControl().setMaximized(false);
                            } else {
                                nativeWindow.getNativeWindowControl().setMaximized(true);
                            }
	    	    	}
	    	    }));
        }

        if ((isNormalWin) || (isDockable)) {
	    dockLeftButton =
	        new Button(buttonSize/2f, buttonSize, dockLeftButtonOffAppearance,
		    buttonOnSize/buttonSize, dockLeftButtonOnAppearance);
	    dockLeftButton.setCursor(Cursor3D.SMALL_CURSOR);
	    dockLeftButton.addListener(
	    	new MouseClickedEventAdapter(
	    	    new ActionNoArg() {
	    	    	public void performAction(LgEventSource source) {
                            nativeWindow.postEvent( 
                                    new Component3DGestureMoveLeftEvent());
	    	    	}
	    	    }));
	    dockRightButton =
	        new Button(buttonSize/2f, buttonSize, dockRightButtonOffAppearance,
		    buttonOnSize/buttonSize, dockRightButtonOnAppearance);
	    dockRightButton.setCursor(Cursor3D.SMALL_CURSOR);
	    dockRightButton.addListener(
	    	new MouseClickedEventAdapter(
	    	    new ActionNoArg() {
	    	    	public void performAction(LgEventSource source) {
                            nativeWindow.postEvent( 
                                    new Component3DGestureMoveRightEvent());
	    	    	}
	    	    }));
        }

        if (isNormalWin || (isResizable)) {
	    minimizeButton = new Button(buttonSize,
		    minimizeButtonOffAppearance, buttonOnSize,
		    minimizeButtonOnAppearance);
	    minimizeButton.setCursor(Cursor3D.SMALL_CURSOR);
	    minimizeButton.addListener(new MouseClickedEventAdapter(
		    new ActionNoArg() {
			public void performAction(LgEventSource source) {
			    nativeWindow.getNativeWindowControl().setVisible(
				    false);
			}
		    }));

	    resizeButton = new Button(buttonSize, resizeButtonAppearance);
            
	    resizeButton.setPickable(false);

	    resizeSERegion = new InvisibleButton(buttonSize * 1.2f,
		    buttonSize * 1.2f);
	    resizeSERegion = new Component3D();
	    PickableRegion pr1 = new PickableRegion(buttonSize * 1.2f * 0.4f,
		    buttonSize * 1.8f, buttonSize * 1.2f * 0.25f, 0.0f, 0.0f);
	    PickableRegion pr2 = new PickableRegion(buttonSize * 1.8f,
		    buttonSize * 1.2f * 0.4f, 0.0f, buttonSize * 1.2f * -0.25f,
		    0.0f);
	    resizeSERegion.addChild(pr1);
	    resizeSERegion.addChild(pr2);
	    resizeSERegion.setCursor(Cursor3D.SE_RESIZE_CURSOR);
	    resizeSERegion.addListener(new NativeWindow3DSEResizer(nativeWindow));

	    resizeSRegion = new InvisibleButton(0, 0);
	    resizeSRegion.setCursor(Cursor3D.S_RESIZE_CURSOR);
	    resizeSRegion.addListener(new NativeWindow3DSResizer(nativeWindow));
	}
        
	moveRegion = new InvisibleButton(0, 0);
        moveRegion.setMouseEventPropagatable(true);

	topTitle = new Title(title, 0, topTitleHeight);
        topTitle.setPickable(false);
        
	rightSpineTitle = new SpineTitle(title, 0, spineTitleHeight);
        rightSpineTitle.setRotationAxis(0.0f, 1.0f, 0.0f);
	rightSpineTitle.setRotationAngle((float)Math.toRadians(90));
        rightSpineTitle.setPickable(false);

	leftSpineTitle = new SpineTitle(title, 0, spineTitleHeight);
	leftSpineTitle.setRotationAxis(0.0f, 1.0f, 0.0f);
	leftSpineTitle.setRotationAngle((float)Math.toRadians(-90));
        leftSpineTitle.setPickable(false);
        
        if (isNormalWin && !isRemwin()) {
	    nativeWindow.setThumbnail(new GlassyNativeWindowThumbnail(
		    nativeWindow, appImage));
	} else {
	    nativeWindow.setThumbnail(null);
	}
        
        rootCont = new Component3D();
        rootCont.setRotationAxis(0.0f, 1.0f, 0.0f);
        rootCont.setTranslation(0.0f, 0.0f, bodyDepth * 0.5f);
        rootCont.setAnimation(new NaturalMotionAnimation(500));
        
        TransparencyOrderedGroup tog = new TransparencyOrderedGroup();
        tog.addChild(body);
        tog.addChild(closeButton);
        if (maximizeButton != null) {
            tog.addChild(maximizeButton);
        }
        if (minimizeButton != null) {
            tog.addChild(minimizeButton);
        }
        if (isDockable) {
            tog.addChild(dockRightButton);
            tog.addChild(dockLeftButton);
        }
        if (resizeButton != null) {
            tog.addChild(resizeButton);
            rootCont.addChild(resizeSERegion);
            rootCont.addChild(resizeSRegion);
        }
        tog.addChild(topTitle);
        tog.addChild(panelComp);
        popupRoot = new Component3D();
        popupRoot.setTranslation(0.0f, 0.0f, 0.001f);
        tog.addChild(popupRoot);
        rootCont.addChild(moveRegion);
        rootCont.addChild(rightSpineTitle);
        rootCont.addChild(leftSpineTitle);
        
        rootCont.addChild(tog);
        addChild(rootCont);
        
        nativeWindow.addListener(
            new MouseClickedEventAdapter(
                MouseEvent3D.ButtonId.BUTTON3,
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        if (backsideWindow != null) {
                            processFlipRequest(null);
                        } else if (!beingFlipped) {
                            if (stickyNote == null) {
                                stickyNote = createStickyNote();
                            }
                            initializeStickNote(stickyNote);
                            processFlipRequest(stickyNote);
                        } else {
                            // being flipped but not yet fully flipped - ignore
                        }
                    }
                }));
            
        nativeWindow.addListener(
            new Component3DParkedEventAdapter(
                new ActionBoolean() {
                    public void performAction(LgEventSource source, boolean parked) {
                        if (parked) {
                            processFlipRequest(null, 500); // unflip fast if flipped
                        }
                    }
                }));
    }
    
    public Cursor3D getNativeCursor () {
	return nativeCursor;
    }

    public Component3D getNativeWindowBodyComponent () {
	return panelComp;
    }

    public SimpleAppearance getDecorationAppeareance () {
	return bodyApp;
    }

    public boolean isRemwin () {
	return 	bodyPanel.isRemwin();
    }

    public void setIsRemwin (boolean isRemwin) {
	bodyPanel.setIsRemwin(isRemwin);
    }

    private void setupButtonAppearances() {
	closeButtonOffAppearance 
	    = new ButtonAppearance(
                "resources/images/button/window-close.png", false);
	closeButtonOnAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-close.png", true);
	maximizeButtonOffAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-maximize.png", false);
	maximizeButtonOnAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-maximize.png", true);
	minimizeButtonOffAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-minimize.png", false);
	minimizeButtonOnAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-minimize.png", true);
	dockLeftButtonOffAppearance
	    = new ButtonAppearance(
                "resources/images/button/dock-left.png", false);
	dockLeftButtonOnAppearance
	    = new ButtonAppearance(
                "resources/images/button/dock-left.png", true);
	dockRightButtonOffAppearance
	    = new ButtonAppearance(
                "resources/images/button/dock-right.png", false);
	dockRightButtonOnAppearance
	    = new ButtonAppearance(
                "resources/images/button/dock-right.png", true);
	resizeButtonAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-resize.png", false);

    }
    
    /* For Debug 
    private void printStack () {
	try {
	    throw new RuntimeException();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    */

    private void setSize(float width, float height) {
	System.err.println("GNWLAF: setSize: wh = " + width + " " + height);
	//printStack();

	float edgeWidth = bodyBorder * 2;
	float edgeHeight = bodyHeader * 2 + bodyBorder * 2; 
	tmpV3f.set(width + edgeWidth, height + edgeHeight, bodyDepth);
        nativeWindow.setPreferredSize(tmpV3f);

        if (nativeWindow.getNativeWindowControl().isNormalWindow() && !isRemwin()) {
            GlassyNativeWindowThumbnail gnwt 
                    = (GlassyNativeWindowThumbnail)nativeWindow.getThumbnail();
            gnwt.setSize(width, height);
        }
        
	float buttonY = (height + buttonSize) * 0.5f;
	float buttonX = (width - buttonSize - bodyBorder * 0.5f) * 0.5f;

	bodyPanel.sizeChanged();

	// these variable need to be readed again, because client may request
	// to change window type and thus those value may change.
	decorated = nativeWindow.getNativeWindowControl().isDecorated();
	boolean isResizable = nativeWindow.getNativeWindowControl().isResizable();	
	boolean isMaximizable = nativeWindow.getNativeWindowControl().isMaximizable();
	boolean isNormalWin = nativeWindow.getNativeWindowControl().isNormalWindow();
	boolean isDockable = nativeWindow.getNativeWindowControl().isDockable();
	
        if (!decorated) {
            return;
        }
	
	closeButton.setTranslation(buttonX, buttonY, layer1);

	if ((isNormalWin) || (isMaximizable)) {
	    buttonX -= buttonSize * 1.1f;
	    maximizeButton.setTranslation(buttonX, buttonY, layer1);
	}
	if (isNormalWin) {
	    buttonX -= buttonSize * 1.1f;
	    minimizeButton.setTranslation(buttonX, buttonY, layer1);
	}  
        if (isNormalWin && isDockable) {
            buttonX -= buttonSize * 1.1f;
            dockRightButton.setTranslation(buttonX, buttonY, layer1);
            buttonX -= buttonSize/1.5f * 1.1f;
            dockLeftButton.setTranslation(buttonX, buttonY, layer1);
        }
	if ((isNormalWin) || (isResizable)) {
	    buttonY = height * -0.5f + buttonSize * 0.25f;
	    buttonX = width * 0.5f - buttonSize * 0.3f;
	    resizeButton.setTranslation(buttonX, buttonY, layer1);

	    buttonY = (-height - bodyFooter + resizeRegionSize) * 0.5f
		    - bodyBorder;
	    buttonX = (width - resizeRegionSize) * 0.5f + bodyBorder;
	    resizeSERegion.setTranslation(buttonX, buttonY, layer4);

	    buttonY = (height + bodyFooter + bodyBorder) * -0.5f;
	    buttonX = 0.0f;
	    resizeSRegion.setTranslation(buttonX, buttonY, layer3);
	}
	buttonY = (height + bodyHeader + bodyBorder) * 0.5f;
	buttonX = buttonSize * -6.6f * 0.5f;
	moveRegion.setTranslation(buttonX, buttonY, layer3);

	buttonY = (height - bodyBorder) * 0.5f;
	buttonX = width * -0.5f + bodyBorder;
	topTitle.setTranslation(buttonX, buttonY, layer1);
        
	leftSpineTitle.setTranslation(
            width * -0.5f - bodyBorder - spineTitleHeight * 0.01f,
	    height * 0.5f + bodyHeader * 0.5f,
	    bodyDepth * -1.1f);
	rightSpineTitle.setTranslation(
            width * 0.5f + bodyBorder + spineTitleHeight * 0.01f,
	    height * 0.5f + bodyHeader * 0.5f,
	    bodyDepth * -0.1f);

	moveRegion.setSize(
	    width - buttonSize * 3.3f,
	    bodyHeader + bodyBorder);
	
	if ((isNormalWin) || (isResizable)) {	    
    	   resizeSRegion.setSize(width + bodyBorder * 2 - resizeRegionSize
		    * 2.8f, bodyFooter + bodyBorder);
    	}

	width = width + bodyBorder * 2;
	height = height + bodyHeader + bodyFooter + bodyBorder * 2;
//	bodyDecoration.setSize(width, height);
//	bodyShadow.setSize(width, height);
        body.setScale(width * 10, height * 10, 1.0f);
        
	float topTitleWidth = width - buttonSize * 5f;
	topTitle.setWidth(topTitleWidth);

	float spineTitleWidth = (height - bodyHeader * 2) * 0.95f;
	rightSpineTitle.setWidth(spineTitleWidth);
	leftSpineTitle.setWidth(spineTitleWidth);
    }
    
    private static class ButtonAppearance extends SimpleAppearance {
	private ButtonAppearance(String filename, boolean on) {
	    super(0.0f, 0.0f, 0.0f, 0.0f,
		SimpleAppearance.DISABLE_CULLING
		| SimpleAppearance.ENABLE_TEXTURE);

	    if (on) {
		setColor(1.0f, 0.6f, 0.6f, 0.8f);
	    } else {
		setColor(0.6f, 1.0f, 0.6f, 0.6f);
	    }
            try {
                setTexture(this.getClass().getClassLoader().getResource(filename));
            } catch (Exception e) {
                throw new RuntimeException(
                    "failed to initilaze window button: " + e);
            }
	}
    }

    private static class Button extends Component3D {
	private Button(float size, Appearance app) {
	    this(size, app, size, app);
	}

	private Button(float sizeOff, Appearance appOff,
	    float sizeOn, Appearance appOn) {
            this(sizeOff, sizeOff, appOff, sizeOn/sizeOff, appOn);
        }
        
	private Button(float widthOff, float heightOff, Appearance appOff,
	    float scaleOn, Appearance appOn)
	{
	    Shape3D shape = new ImagePanel(widthOff, heightOff);
	    shape.setAppearance(appOff);
	    addChild(shape);
	    if (appOff != appOn) {
                addListener(
                    new MouseEnteredEventAdapter(
                        new AppearanceChangeAction(shape, appOn)));
	    }
	    if (scaleOn!=1f) {
                addListener(
                    new MouseEnteredEventAdapter(
                        new ScaleActionBoolean(this, scaleOn, 100)));
	    }
	}
    }

    private static class InvisibleButton extends Component3D {
	private PickableRegion region;

	private InvisibleButton(float width, float height) {
	    region = new PickableRegion(width, height);
	    addChild(region);
	}

	private void setSize(float width, float height) {
	    region.setSize(width, height);
	}
    }

    private static class Title extends Component3D {
	private GlassyText2D panel;

	private Title(String title, float maxWidth, float height) {
            panel = new GlassyText2D(
                    title, maxWidth, height, 
                    new Color4f(0.6f, 1.0f, 0.6f, 0.6f), 
                    GlassyText2D.LightDirection.BOTTOM_RIGHT,
                    GlassyText2D.Alignment.LEFT,
                    1.2f, false);
            addChild(panel);
	}

	private void setWidth(float maxWidth) {
	    panel.setWidth(maxWidth);
	}
        
        public void setText(String title) {
            panel.setText(title);
        }
    }

    private static class SpineTitle extends Component3D {
	private GlassyText2D panel;
        
	private SpineTitle(String title, float maxWidth, float height) {
            panel = new GlassyText2D(
                    title, maxWidth, height, 
                    new Color4f(0.6f, 1.0f, 0.6f, 0.6f), 
                    GlassyText2D.LightDirection.TOP_RIGHT,
                    GlassyText2D.Alignment.LEFT,
                    1.8f, true);
            addChild(panel);
        }

	private void setWidth(float maxWidth) {
	    panel.setWidth(maxWidth);
	}
        
        public void setText(String title) {
            panel.setText(title);
        }
    }
    
    public void setName(String name) {
        super.setName(name);
        topTitle.setText(name);
        leftSpineTitle.setText(name);
        rightSpineTitle.setText(name);
    }
    
    public boolean isWindowAssociatable(NativeWindowControl nwc) {
//        // If the backside window is larget than the front one,
//        // just give up for now.
//        int bwW = nwc.getWidth();
//        int bwH = nwc.getHeight();
//        int w = nativeWindow.getNativeWindowControl().getWidth();
//        int h = nativeWindow.getNativeWindowControl().getHeight();
//        if (bwW > w || bwH > h) {
//            logger.info("The window specified for the backsize is bigger than "
//                    + "the front one: " + nwc.getName() + " Displaying it as a "
//                    + "normal window..");
//            return false;
//        }
        
        return (backsideWindow == null) && !beingFlipped;
    }
    
    public void associateWindow(NativeWindowMonitor nativeWinMonitor) {
        // FIXME -- this works, but a bit too wild
        NativePopup3D np3d = (NativePopup3D)nativeWinMonitor;
        
        // scale the associated window to fit to this window
        int bwW = np3d.getNativeWindowControl().getWidth();
        int bwH = np3d.getNativeWindowControl().getHeight();
        int w = nativeWindow.getNativeWindowControl().getWidth();
        int h = nativeWindow.getNativeWindowControl().getHeight();
        
        if (bwW > w * backsideWinSizeRatio || bwH > h * backsideWinSizeRatio) {
            float sx = (w * backsideWinSizeRatio) / bwW;
            float sy = (h * backsideWinSizeRatio) / bwH;
            np3d.setScale((sx < sy)?(sx):(sy));
        }
        
        np3d.setTranslation(0.0f, 0.0f, bodyDepth * -1.1f);
        np3d.setRotationAxis(0.0f, 1.0f, 0.0f);
        np3d.setRotationAngle((float)(Math.PI));
        processFlipRequest(np3d);
    }
    
    public void dissociateWindow(NativeWindowMonitor nativeWinMonitor) {
        processFlipRequest(null);
    }
    
    public void associatePopup(Component3D popup) {
        popupRoot.addChild(popup);
    }
    
    /**
     * The size of the native window image has changed so resize the
     * geometry to accomodate
     */
    public void sizeChangedNative(int nativeWidth, int nativeHeight) {
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        setSize(
            toolkit3d.widthNativeToPhysical(nativeWidth),
            toolkit3d.heightNativeToPhysical(nativeHeight));
    }
    
    /**
     * Called by NativeWindow3D when the 3D size is changed
     */
    public void sizeChanged3D(float width, float height) {
//        appImage.sizeChanged(
//            heightPhysicalToNative(width), widthPhysicalToNative(height));
        
        setSize(width, height);     
    }
    
    private void processFlipRequest(Component3D bWindow) {
        processFlipRequest(bWindow, (bWindow == null)?(750):(1000));
    }
    
    private void processFlipRequest(Component3D bWindow, int duration) {
        if (backsideWindow != null && bWindow != null) {
            // flip requested while flipped -- unflip
            bWindow.detach();
            if (bWindow instanceof NativePopup3D) {
                ((NativePopup3D)bWindow).getNativeWindowControl().destroy();
            } else if (bWindow instanceof StickyNote) {
                ((StickyNote)bWindow).setEnabled(false);
            } else {
                throw new InternalError("Invalid backside window: " + backsideWindow);
            }
            bWindow = null;
        }
        
        if (backsideWindow == null) {
            // not flipped yet -- flip
            if (bWindow == null) {
                // unflip requested while unflipped -- ignore
                return;
            }
            backsideWindow = bWindow;
            rootCont.addChild(backsideWindow);
            beingFlipped = true;
            rootCont.changeRotationAngle((float)Math.PI, duration);
            nativeWindow.postEvent(new Component3DToFrontEvent());
            
            flipperTimer.schedule(
                new TimerTask() {
                    public void run() {
                        beingFlipped = false;
                    }
                }, 1000);
        } else {
            assert(bWindow == null);
            
            beingFlipped = true;
            rootCont.setRotationAngle((float)-Math.PI);
            rootCont.changeRotationAngle(0f, duration);
            
            flipperTimer.schedule(
                new TimerTask() {
                    public void run() {
                        beingFlipped = false;
                        if (backsideWindow == null) {
                            return;
                        }
                        backsideWindow.detach();
                        if (backsideWindow instanceof NativePopup3D) {
                            ((NativePopup3D)backsideWindow).getNativeWindowControl().destroy();
                        } else if (backsideWindow instanceof StickyNote) {
                            ((StickyNote)backsideWindow).setEnabled(false);
                        } else {
                            throw new InternalError("Invalid backside window: " + backsideWindow);
                        }
                        backsideWindow = null;
                    }
                }, duration);
        }
    }
    
    private StickyNote createStickyNote() {
        StickyNote sn = new StickyNote();
        sn.setTranslation(0.0f, 0.0f, bodyDepth * -1.1f);
        sn.setRotationAxis(0.0f, 1.0f, 0.0f);
        sn.setRotationAngle((float)Math.PI);
        sn.addListener(
            new MouseClickedEventAdapter(
                MouseEvent3D.ButtonId.BUTTON3,
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        if (backsideWindow != null) {
                            processFlipRequest(null);
                        }
                    }
                }));
        sn.setEnabled(true);
        return sn;
    }
    
    private void initializeStickNote(StickyNote sn) {
        NativeWindowControl winCon = nativeWindow.getNativeWindowControl();
        String title = winCon.getName();
        int w = winCon.getWidth();
        int h = winCon.getHeight();
        sn.initialize(title, w, h);
    }
}

