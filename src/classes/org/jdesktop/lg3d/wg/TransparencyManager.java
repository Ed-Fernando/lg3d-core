/**
 * Project Looking Glass
 *
 * $RCSfile: TransparencyManager.java,v $
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
 * $Revision: 1.11 $
 * $Date: 2007-01-04 22:40:22 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.TransparencyAttributes;
import org.jdesktop.lg3d.sg.utils.traverser.NodeChangeProcessor;
import org.jdesktop.lg3d.sg.utils.traverser.TreeScan;

/**
 *
 * Manages Transparency for Component3D and AnimationGroup
 *
 * @author paulby
 */
class TransparencyManager {
    protected static final Logger logger = Logger.getLogger("lg.transparencymgr");
    
    // Attribute used if the user did not set a transparency attribute
    private TransparencyAttributes systemTransparency = null;
    private Appearance systemAppearance = null;
    private TransparencyRecord sysTransparencyRecord = null;
    
    private boolean isOpaque = true;
    private LinkedHashMap<Node,TransparencyRecord> transparencyList;
    
    private static int count = 0;
//    private static ArrayList unique = new ArrayList();
    
    /** Creates a new instance of TransparencyManager */
    public TransparencyManager() {
        transparencyList = new LinkedHashMap<Node,TransparencyRecord>();
    }
    
    /**
     * Modify the transparency of all objects managed by multiplying there
     * user defined transparency by value.
     *
     * For best performance when the value is 1f and we come accross a
     * system TransparencyAttribute we turn of transparency
     *
     * The value should be in the range [0.0, 1.0] with 0.0 being fully opaque 
     * and 1.0 fully transparent.
     */
    public void modifyTransparency(float value) {
        count=0;
        long start = System.currentTimeMillis();
        boolean origIsOpaque = isOpaque;
        
        internalModifyTransparency(value);
        
        if (logger.getLevel()!=null && logger.getLevel().intValue()<=Level.FINE.intValue()) {
            logger.fine("modifyTransparency took " +(System.currentTimeMillis()-start)+" ms.   value="+value+" isOpaque="+origIsOpaque+"   "+this);
            logger.finer("Nodes changed "+count);
        }
                
    }
    
    void internalModifyTransparency(float value) {
        boolean opaqueChange;

        if ((value==0f && !isOpaque) || (value!=0f && isOpaque))
            opaqueChange = true;
        else 
            opaqueChange = false;
        
        synchronized(transparencyList) {
            for(TransparencyRecord t : transparencyList.values()) {
                t.modifyTransparency(value, opaqueChange);  
            }
        }
        
        if (sysTransparencyRecord!=null)
            sysTransparencyRecord.modifyTransparency(value,opaqueChange);
        
        isOpaque = (value==0f);        
    }
    
    /**
     * Add a new Graph in which transparency should be managed.
     *
     * Traverses tree looking for shapes and gathering/setting transparency
     * attributes. The traversal stops at each component3D or AnimationGroup
     * found as these will have their own TransparencyManager and this manager
     * will call their manager to manipulate transparency in that subgraph.
     *
     * For shape3D's which don't have an appearance or TransparencyAttribute one
     * will be created and assigned.
     */
    void addGraph(Node graph) {
        if (graph==null)
            return;
        
        //logger.fine("TransparencyManager addGraph "+graph.getName());
        
        NodeChangeProcessor proc = new NodeChangeProcessor() {
            public boolean changeNode( org.jdesktop.lg3d.sg.Node node ) {
                
//                if (unique.contains(node))
//                    throw new RuntimeException("Duplicate Entry");
//                unique.add(node);
                if (node instanceof Shape3D) {   
                    //logger.finer("TM Adding Shape3D "+node.getName()+" "+node);
                    TransparencyAttributes attr;
                    Appearance app = ((Shape3D)node).getAppearance();
                    
                    if (app==null) {
                        if (systemAppearance==null)
                            systemAppearance = new Appearance();
                        app = systemAppearance;
                        ((Shape3D)node).setAppearance(app);
                    }
                    
                    if (!app.isLive()) {
                        app.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
                    }
                    attr = app.getTransparencyAttributes();
                    
                    if (attr==null) {
                        if (systemTransparency==null) {
                            systemTransparency = new TransparencyAttributes();
                            systemTransparency.setTransparencyMode(TransparencyAttributes.NONE);
                            systemTransparency.setCapability(TransparencyAttributes.ALLOW_MODE_WRITE);
                            systemTransparency.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
                            systemTransparency.setCapability(TransparencyAttributes.ALLOW_VALUE_READ);
                            sysTransparencyRecord=new TransparencyRecord((TransparencyAttributes)systemTransparency);
                        }
                        attr = systemTransparency;
                        app.setTransparencyAttributes(systemTransparency);
                        //logger.severe("Adding SystemTrans "+node.getName());
                        //transparencyList.put( node, new TransparencyRecord((TransparencyAttributes)attr));
                    } else if (app!=systemAppearance) {
                        transparencyList.put( node, new TransparencyRecord((TransparencyAttributes)attr));                    
                        if (!attr.isLive()) {
                            attr.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
                            attr.setCapability(TransparencyAttributes.ALLOW_VALUE_READ);
                        }
                    }
                    
                } else if (node instanceof AnimationGroup) {
                    //logger.finer("TM Adding AnimationGroup "+node.getName()+" "+node);
                    transparencyList.put( node, new TransparencyRecord((AnimationGroup)node));                    
                    ((AnimationGroup)node).createTransparencyManager();
                    return false;
                } else if (node instanceof Component3D) {
                    //logger.finer("TM Adding Component3D "+node.getName()+" "+node);
                    transparencyList.put( node, new TransparencyRecord((Component3D)node));
                    ((Component3D)node).createTransparencyManager();
                    return false;
                } else 
                    throw new RuntimeException("Unexpected class from findNode");
                
                return true;
            }
        };

        try {
            Class[] shapeClass = new Class[] {org.jdesktop.lg3d.sg.Shape3D.class,
                                              org.jdesktop.lg3d.wg.AnimationGroup.class,
                                              org.jdesktop.lg3d.wg.Component3D.class };

            synchronized(transparencyList) {
                TreeScan.findNode( graph, shapeClass, proc, false, true );
            }
        } catch(Exception e ) {
            logger.log( Level.WARNING, "Problems setting up TransparencyManager ", e);
        }
        
        //logger.fine("TransparencyList size "+transparencyList.size());
    }
    
    void removeGraph(Node graph) {
        //logger.fine("TransparencyManager removeGraph "+graph);
        NodeChangeProcessor proc = new NodeChangeProcessor() {
            public boolean changeNode( org.jdesktop.lg3d.sg.Node node ) {
                if (node instanceof Shape3D)
                    transparencyList.remove(node);
                else if (node instanceof AnimationGroup) {
                    transparencyList.remove(node);
                    ((AnimationGroup)node).removeTransparencyManager();
                    return false;
                } else if (node instanceof Component3D) {
                    transparencyList.remove(node);
                    ((Component3D)node).removeTransparencyManager();
                    return false;
                }
                
                return true;
            }
        };

        try {
            Class[] shapeClass = new Class[] {org.jdesktop.lg3d.sg.Shape3D.class,
                                              org.jdesktop.lg3d.wg.AnimationGroup.class,
                                              org.jdesktop.lg3d.wg.Component3D.class };

            TreeScan.findNode( graph, shapeClass, proc, false, true );
        } catch(Exception e ) {
            logger.log( Level.SEVERE, "Problems removing graph", e);
        }        
    } 
    
    void clear() {
        synchronized(transparencyList) {
            transparencyList.clear();
        }
    }
    
    class TransparencyRecord {
        
        private static final int TRANSPARENCY_ATTR = 1;
        private static final int ANIMATION_GROUP = 2;
        private static final int COMPONENT_3D = 3;
        private int recordType;
        private Object obj;
        private float userTransparency;
        
        public TransparencyRecord(TransparencyAttributes attr) {
            recordType = TRANSPARENCY_ATTR;
            obj = attr;
            userTransparency = attr.getTransparency();
        }
        
        public TransparencyRecord(AnimationGroup animationGroup) {
            recordType = ANIMATION_GROUP;
            obj = animationGroup;
        }
        
        public TransparencyRecord(Component3D c3d) {
            recordType = COMPONENT_3D;
            obj = c3d;
        }
        
        public void modifyTransparency(float value,boolean opaqueChange) {
            //logger.fine("modifyTransparency "+recordType+" "+userTransparency+" "+value+" "+opaqueChange);
            count++;
            switch(recordType) {
                case TRANSPARENCY_ATTR :
                    //logger.finer("TR modify TransparencyAttribute");
                    TransparencyAttributes attr = (TransparencyAttributes)obj;
                    if (userTransparency == 0f) {
                        attr.setTransparency(value);
                    } else {
                        attr.setTransparency(userTransparency + (1-userTransparency)*value);
                    }
                    if (opaqueChange && attr == systemTransparency) {
                        //logger.fine("Changing opacity");
                        if (!isOpaque) {
                            attr.setTransparencyMode(TransparencyAttributes.NONE);
                        } else {
                            attr.setTransparencyMode(TransparencyAttributes.BLENDED);
                        }
                    }
                    break;
                case ANIMATION_GROUP :
                    //logger.finer("TR modify AnimationGroup");
                    TransparencyManager tm = ((AnimationGroup)obj).getTransparencyManager();
                    if (tm != null) {
                        tm.internalModifyTransparency(value);
                    }
                    break;
                case COMPONENT_3D :
                    //logger.finer("TR modify Component3D");
                    tm = ((Component3D)obj).getTransparencyManager();
                    if (tm != null) {
                        tm.internalModifyTransparency(value);
                    }
                    break;
            }
        }
    }
    
}
