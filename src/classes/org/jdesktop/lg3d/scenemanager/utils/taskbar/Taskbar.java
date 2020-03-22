/**
 * Project Looking Glass
 *
 * $RCSfile: Taskbar.java,v $
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
 * $Revision: 1.10 $
 * $Date: 2006-08-07 19:02:50 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.taskbar;

import java.util.logging.Logger;

import org.jdesktop.lg3d.scenemanager.utils.appcontainer.AppContainer;
import org.jdesktop.lg3d.scenemanager.utils.event.Frame3DAddedEvent;
import org.jdesktop.lg3d.scenemanager.utils.event.Frame3DRemovedEvent;
import org.jdesktop.lg3d.scenemanager.utils.plugin.SceneManagerPlugin;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Thumbnail;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventConnector;


public abstract class Taskbar extends Container3D implements SceneManagerPlugin {
    protected static final Logger logger = Logger.getLogger("lg.scenemanager");
    
    public abstract void addThumbnail(Component3D thumbnail);
    public abstract void removeThumbnail(Component3D thumbnail);
    
    public void addTaskbarItem(Component3D item) {
        addTaskbarItem(item, -1);
    }
    
    public abstract void addTaskbarItem(Component3D item, int index);
    public abstract void removeTaskbarItem(Component3D item);
    
    public void initialize() {
        // Listen for new frames and add their thumbnails
        LgEventConnector.getLgEventConnector().addListener(AppContainer.class,
                new LgEventListener() {
                    public void processEvent(LgEvent evt) {
                        Frame3DAddedEvent event= (Frame3DAddedEvent)evt;
                        Frame3D frame= event.getFrame3D();
                        Component3D tn = frame.getThumbnail();
                        if (tn != null && tn != Thumbnail.DEFAULT) {
                            tn.setScale(0.0f);
                            addThumbnail(tn);
                        }
                    }
                    public Class[] getTargetEventClasses() {
                        return new Class[]{ Frame3DAddedEvent.class };
                    }
                });

        // Listen for removed frames and remove their thumbnails
        LgEventConnector.getLgEventConnector().addListener(AppContainer.class,
                new LgEventListener() {
                    public void processEvent(LgEvent evt) {
                        Frame3DRemovedEvent event= (Frame3DRemovedEvent)evt;
                        Frame3D frame= event.getFrame3D();
                        Component3D tn = frame.getThumbnail();
                        if (tn != null) {
                            removeThumbnail(tn);
                        }
                    }
                    public Class[] getTargetEventClasses() {
                        return new Class[]{ Frame3DRemovedEvent.class };
                    }
                });
    }
    
    public void destroy() {
        // No cleanup to be done
    }
    
    public Component3D getPluginRoot() {
        return this;
    }
    
    public boolean isRemovable() {
        return false;
    }

}

