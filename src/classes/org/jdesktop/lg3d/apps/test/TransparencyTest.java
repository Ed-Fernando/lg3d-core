/*
 * TransparencyTest.java
 *
 * Created on 01 March 2005, 17:03
 */

package org.jdesktop.lg3d.apps.test;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.sg.TransformGroup;
import org.jdesktop.lg3d.sg.TransparencyAttributes;
import org.jdesktop.lg3d.utils.action.ActionInt;
import org.jdesktop.lg3d.utils.animation.TransparencyAnimationFloat;
import org.jdesktop.lg3d.utils.eventadapter.MouseWheelEventAdapter;
import org.jdesktop.lg3d.utils.shape.Box;
import org.jdesktop.lg3d.utils.shape.ColorCube;
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.wg.AnimationGroup;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 *
 * @author paulby
 */
public class TransparencyTest extends HelloUniverseApp {
    
    private TransparencyAnimationFloat transparencyAnimation;
    
    private enum Shape { SPHERE, COLOR_CUBE, BOX };
    
    /**
     * Give subclasses the abilitity to add more components to the root
     * container
     */
    protected void addComponents( Container3D container ) {   
        transparencyAnimation = new TransparencyAnimationFloat(0.0f, 200);
        Component3D top = new Component3D();
        container.addChild(top);
        AnimationGroup ag = new AnimationGroup();
        top.addChild(ag);
        
        top.addListener(
            new MouseWheelEventAdapter(new TransparencyAction()));
        
        ag.addChild(createShape(new Vector3f(0.01f,0f,0f), Shape.SPHERE));
        top.addChild(createShape(new Vector3f(0.01f, 0f, -0.02f), Shape.COLOR_CUBE));
        
        ag.addChild(createShape(new Vector3f(0.02f, 0f,0f), Shape.SPHERE));
        
        AnimationGroup ag2 = new AnimationGroup();
        ag.addChild(ag2);
        
        ag2.addChild(createShape(new Vector3f(0.04f,0f,0f), Shape.BOX));
        
        // Must add the animation as the last operation otherwise the
        // transparencyMangers don't scan the graph completely.
        ag.setAnimation(transparencyAnimation);
    }
    
    private TransformGroup createShape(Vector3f pos, Shape shape) {
        Transform3D t3d = new Transform3D();
        t3d.set(1f,pos);
        TransformGroup tg = new TransformGroup(t3d);
        
        switch(shape) {
            case SPHERE:
                tg.addChild(new Sphere(0.01f));
                break;
            case COLOR_CUBE:
                tg.addChild(new ColorCube(0.01f));
                break;
            case BOX:
                Appearance app = new Appearance();
                TransparencyAttributes ta = new TransparencyAttributes();
                ta.setTransparency(0.5f);
                ta.setTransparencyMode(TransparencyAttributes.BLENDED);
                app.setTransparencyAttributes(ta);
                tg.addChild(new Box(0.01f,0.01f,0.01f,app));
                break;
        }
        return tg;
    }

    public static void main(String args[]) {
        new TransparencyTest();
    }
    
    class TransparencyAction implements ActionInt {
        private float transparency = 0f;
        
        public void performAction(LgEventSource source, int value) {
            transparency += value*0.05f;
            
            transparencyAnimation.performAction(source, transparency);
        }
        
    }
}
