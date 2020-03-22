/**
 * Project Looking Glass
 *
 * $RCSfile: AppKitUniverseFactory.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2007-05-23 18:48:49 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.appkit;

import java.awt.GraphicsConfiguration;
import javax.media.j3d.Canvas3D;
import com.sun.j3d.utils.universe.ConfiguredUniverse;
import java.net.URL;
import java.util.logging.Logger;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import org.jdesktop.lg3d.displayserver.UniverseFactory;

/**
 *
 * @author paulby
 */

public class AppKitUniverseFactory extends UniverseFactory {

    private Logger logger = Logger.getLogger("lg.displayserver");
    private Canvas3D c3d=null;

    public ConfiguredUniverse createUniverse(URL configURL) { 
        GraphicsConfiguration config =
           ConfiguredUniverse.getPreferredConfiguration();

        c3d = new Canvas3D(config);
        ConfiguredUniverse ret =  new com.sun.j3d.utils.universe.ConfiguredUniverse(c3d);

        View view = ret.getViewer().getView();
        view.setScreenScalePolicy(View.SCALE_EXPLICIT);
        view.setCoexistenceCenteringEnable(true);
        view.setWindowEyepointPolicy(View.RELATIVE_TO_WINDOW);
        view.setWindowMovementPolicy(View.PHYSICAL_WORLD);
        view.setWindowResizePolicy(View.VIRTUAL_WORLD);
        view.setFrontClipDistance(0.01f);
        view.setBackClipDistance(10f);

        ViewPlatform vp = ret.getViewingPlatform().getViewPlatform();
        vp.setViewAttachPolicy(View.NOMINAL_SCREEN);

        c3d.getScreen3D().setPhysicalScreenHeight(0.360f);
        c3d.getScreen3D().setPhysicalScreenWidth(0.288f);

        ret.getViewingPlatform().setNominalViewingTransform();

        if (listener!=null)
            listener.universeCreated(new Canvas3D[] {c3d});
        else
            throw new RuntimeException("No Canvas3D listener registered");

        return ret;
    }


}

