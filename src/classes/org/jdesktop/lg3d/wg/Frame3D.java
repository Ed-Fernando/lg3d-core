/**
 * Project Looking Glass
 *
 * $RCSfile: Frame3D.java,v $
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
 * $Revision: 1.19 $
 * $Date: 2007-04-10 22:58:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import java.util.Enumeration;
import java.util.HashMap;

import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;
import org.jdesktop.lg3d.wg.internal.wrapper.Frame3DWrapper;

/**
 * The top level contaier for an LG3D-aware application.p
 */
public class Frame3D extends Container3D {
    protected boolean enabled = false;
    private HashMap<Object,Object> property = new HashMap<Object,Object>();
    
    /**
     * Creates a new instance of Frame3D.
     */
    public Frame3D() {
	super(false);
        setThumbnail(Thumbnail.DEFAULT);
    }
    
    /**
     * Make the application active. 
     */
    protected void setEnabledInternal(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            AppConnectorPrivate.getAppConnector().addFrame3D(this);
	} else {
            AppConnectorPrivate.getAppConnector().removeFrame3D(this);
	}
    }

    /**
     * Returns true is active.
     */    
    public boolean isEnabledInternal() {
        return enabled;
    }
    
    /**
     * Enable/Disable the application.
     */
    public void setEnabled(boolean enabled) {
        changeEnabled(enabled, 0);
    }
    
    /**
     * Enable/Disable the application by applying the enable animation over
     * the default duration.
     */
    public void changeEnabled(boolean enabled) {
        assert(c3dAnimation instanceof Frame3DAnimation);
        changeEnabled(enabled, ((Frame3DAnimation)c3dAnimation).getDefaultEnabledDuration());  
    }
    /**
     * Enable/Disable the application by applying the enable animation over
     * the specified duration
     */
    public void changeEnabled(boolean enabled, int duration) {
        assert(c3dAnimation instanceof Frame3DAnimation);
        Frame3DAnimation f3da = (Frame3DAnimation)c3dAnimation;
        boolean prevEnabled = f3da.isFinalEnabled();
        f3da.changeEnabled(enabled, duration);
        // do container layout after making the change
        if (prevEnabled != enabled) {
            requestParentToRevalidate();
        }
    }
    
    /**
     * Returns true is active.
     */    
    public boolean isEnabled() {
        assert(c3dAnimation instanceof Frame3DAnimation);
        Frame3DAnimation f3da = (Frame3DAnimation)c3dAnimation;
        return f3da.isEnabled();
    }
    
    /**
     * Returns true is active.
     */    
    public boolean isFinalEnabled() {
        assert(c3dAnimation instanceof Frame3DAnimation);
        Frame3DAnimation f3da = (Frame3DAnimation)c3dAnimation;
        return f3da.isFinalEnabled();
    }
    
    /**
     * Sets a thumbnail representation of this application.
     */
    public synchronized void setThumbnail(Thumbnail thumbnail) {
        Thumbnail oldThumbnail = getThumbnail();
        if (oldThumbnail != null) {
            oldThumbnail.setBody(null);
        }
        if (thumbnail != null) {
            thumbnail.setBody(this);
        }
        // FIXME -- once we introduce ThubmnailWeapper class, we switch
        // to use it instead of Component3DWrapper.
        Component3DWrapper thumbnailWrapped 
            = (thumbnail == null)
                ?(null):((Component3DWrapper)thumbnail.getWrapped());
        ((Frame3DWrapper)wrapped).setThumbnail(thumbnailWrapped);
    }
    
    /**
     * Requests to the thumbnail representation of this application.
     */
    public Thumbnail getThumbnail() {
        Component3DWrapper thumbnailWrapped 
            = ((Frame3DWrapper)wrapped).getThumbnail();
        Thumbnail ret 
            = (thumbnailWrapped == null)
                ?(null):(Thumbnail)thumbnailWrapped.getUserData();
        return ret;
    }
    
    /**
     * Set the Frame3D animation, this controls the 'look and feel' of
     * position, rotation and scale changes for this component.
     *
     * If an animation is already present it will be disabled and the new
     * animation set in it's place. The previous animation is stopped dead 
     * and it's final values are NOT copied to the new animation.`
     *
     */
    public void setAnimation(Frame3DAnimation animation) {
        setAnimationInternal(animation, new Frame3DAnimationTarget());
        if (c3dAnimation == null) {
            animation.changeEnabled(false, 0);
        }
    }
    
    /**
     *@deprecated argument needs to be a Frame3DAnimation object.
     */
    public void setAnimation(Component3DAnimation animation) {
        if (!(animation instanceof Frame3DAnimation)) {
            throw new IllegalArgumentException("Argument needs to be a Frame3DAnimation object.");
        }
        setAnimation((Frame3DAnimation)animation);
    }
    
    /**
     * Implementation detail, do not call from user code.
     * May be removed in future.
     */
    protected void createWrapped() {
        wrapped = instantiate( "Frame3D" );
    }
    

    /**
     * Get already defined property of this Frame3D object.
     * @param key property name.
     * @return
     */
    public Object getProperty(Object key) {
        return property.get(key);
    }

    /**
     * Set property of this Frame3D object.
     * @param key
     * @param value
     */
    public void setProperty(Object key, Object value) {
	property.put(key, value);
    }
}
