/*
 * SwichablePseudo3DIcon.java
 *
 * Created on October 4, 2005, 10:50 PM
 *
 */

package org.jdesktop.lg3d.utils.component;

import java.net.URL;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.utils.action.AppearanceChangeAction;
import org.jdesktop.lg3d.utils.action.ScaleActionBoolean;
import org.jdesktop.lg3d.utils.action.TranslateActionBoolean;
import org.jdesktop.lg3d.utils.actionadapter.ActionMulticaster;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.shape.ImagePanel;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.utils.action.*;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.wg.Component3DAnimation;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
/**
 *
 * @author Radek Kierner
 */
public class SwichablePseudo3DIcon  extends Component3D {
    private static final float DEFAULT_SIZE = 0.01f; // 1cm
    private static final int DEFAULT_ANIM_DURATION = 150;
    private URL downImage,upImage;
    private Vector3f mouseEnteredTrans;
    private final  Component3D bodyComp;
    private final Component3D shadowComp;
    /** Creates a new instance of SwichablePseudo3DIcon */
    
    public SwichablePseudo3DIcon(URL downImage ,URL upImage) {
	this.downImage = downImage;
	this.upImage = upImage;
	
	if (this.downImage == null) {
	    throw new IllegalArgumentException("argument downImage cannot be null");
	}
	if (this.upImage == null) {
	    throw new IllegalArgumentException("argument upImage cannot be null");
	}
	
	float size = DEFAULT_SIZE;
	mouseEnteredTrans = new Vector3f(0.0f, size * 0.15f, 0.0f);
	setPreferredSize(new Vector3f(size, size, size));
	bodyComp = new Component3D();
	NaturalMotionAnimation anim = new NaturalMotionAnimation(DEFAULT_ANIM_DURATION);
	NaturalMotionAnimation anim2 = new NaturalMotionAnimation(DEFAULT_ANIM_DURATION);
	bodyComp.setAnimation(anim2);
	ImagePanel body = new ImagePanel(size, size);
	
	SimpleAppearance bodyApp
		= new SimpleAppearance(
		1.0f, 1.0f, 1.0f, 0.95f,
		SimpleAppearance.ENABLE_TEXTURE);
	try {
	    bodyApp.setTexture(downImage);
	} catch (Exception e) {
	    throw new RuntimeException("failed to initialize texture: " + e);
	}
	
	SimpleAppearance bodyAppOn
		= new SimpleAppearance(
		1.0f, 1.0f, 1.0f, 0.95f,
		SimpleAppearance.ENABLE_TEXTURE);
	try {
	    bodyAppOn.setTexture(upImage);
	} catch (Exception e) {
	    throw new RuntimeException("failed to initialize texture: " + e);
	}
	
	body.setAppearance(bodyApp);
	bodyComp.addChild(body);
	bodyComp.setTranslation(0.0f, 0.0f, size * 0.6f);
	addChild(bodyComp);
	
	shadowComp = new Component3D();
	shadowComp.setAnimation(anim);
	ImagePanel shadow = new ImagePanel(size, size);
	SimpleAppearance shadowApp
		= new SimpleAppearance(
		0.0f, 0.0f, 0.0f, 0.3f,
		SimpleAppearance.ENABLE_TEXTURE
		| SimpleAppearance.NO_GLOSS);
	
	SimpleAppearance shadowAppOn
		= new SimpleAppearance(
		0.0f, 0.0f, 0.0f, 0.3f,
		SimpleAppearance.ENABLE_TEXTURE
		| SimpleAppearance.NO_GLOSS);
	
	shadowApp.setTexture(bodyApp.getTexture());
	shadowAppOn.setTexture(bodyAppOn.getTexture());
	
	shadow.setAppearance(shadowApp);
	
	shadowComp.addChild(shadow);
	shadowComp.setRotationAxis(1.0f, 0.0f, 0.0f);
	shadowComp.setRotationAngle((float)Math.toRadians(-90));
	shadowComp.setTranslation(0.0f, size * -0.5f, 0.0f);
	addChild(shadowComp);
	
	setMouseEventPropagatable(true);
	
	Action[] enteredActions = {
	    new TranslateActionBoolean(bodyComp, mouseEnteredTrans),
		    new TwoScaleActionBoolean(bodyComp, 1.6f, 1.3f, DEFAULT_ANIM_DURATION),
		    new TwoScaleActionBoolean(shadowComp, 1.6f, 1.3f ,DEFAULT_ANIM_DURATION),
		    new AppearanceChangeAction(body,bodyAppOn),
		    new AppearanceChangeAction(shadow,shadowAppOn)
	};
	addListener(
		new MouseEnteredEventAdapter(
		new ActionMulticaster(enteredActions)));
	addListener(
		new MousePressedEventAdapter(
		new ScaleActionBoolean(bodyComp, 1.05f, 100)));
    }
    
    private  class TwoScaleActionBoolean implements ActionBoolean {
	private static final int USE_DEFAULT_DURATION = -1;
	private Component3D target;
	private float baseScale;
	private float baseScale2;
	private float bigOnScale;
	private float smallOnScale;
	private int duration = USE_DEFAULT_DURATION;
	private Component3DAnimation anim;
	private boolean animDone= false;
	private boolean state;
	
	public TwoScaleActionBoolean(Component3D target, float bigOnScale,float smallOnScale) {
	    if (target == null) {
		new IllegalArgumentException("target cannot be null");
	    }
	    if (bigOnScale <= smallOnScale) {
		new IllegalArgumentException("bigOnScale cannot be smaller then smallOnScale");
	    }
	    
	    this.target = target;
	    this.bigOnScale = bigOnScale;
	    this.smallOnScale = smallOnScale;
	    this.baseScale = Float.NaN;
	    baseScale2 = target.getScale();
	    anim = target.getAnimation();
	    anim.setAnimationFinishedEvent(SwichAnim.class);
	    
	    if (anim == null) {
		new IllegalArgumentException("target Object Animation null");
	    }
	    LgEventConnector.getLgEventConnector().addListener(
		    LgEventSource.ALL_SOURCES,
		    new LgEventListener() {
		public void processEvent(final LgEvent event) {
		    SwichAnim sa = (SwichAnim)event;
		    setSmallScale();
		}
		public Class<LgEvent>[] getTargetEventClasses() {
		    return new Class[] {SwichAnim.class};
		}
	    });
	}
	
	public TwoScaleActionBoolean(Component3D target, float bigOnScale,float smallOnScale, int duration) {
	    this(target, bigOnScale,smallOnScale);
	    if (duration < 0) {
		new IllegalArgumentException("duration cannot be negative");
	    }
	    this.duration = duration;
	}
        
	private void setSmallScale(){
	    if(!animDone && state) {
                if (duration == USE_DEFAULT_DURATION) {
                    target.changeScale(smallOnScale);
                } else {
                    target.changeScale(smallOnScale, duration);
                }
	    }
	    animDone = true;
	}
	
	public void performAction(LgEventSource source, boolean state) {
	    this.state = state;
	    float newScale = Float.NaN;
	    if (state) {
		newScale = bigOnScale;
		baseScale = target.getFinalScale();
		animDone= false;
	    } else {
		if (Float.isNaN(baseScale)) {
		    return;
		}
		newScale = baseScale2;
	    }
	    if (duration == USE_DEFAULT_DURATION) {
		target.changeScale(newScale);
	    } else {
		target.changeScale(newScale, duration);
	    }
	}
    }
    
    public static class SwichAnim extends LgEvent {
	public SwichAnim(){}
	//tag class
    }
}
