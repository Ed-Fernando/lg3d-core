/**
 * Project Looking Glass
 *
 * $RCSfile: CursorImage.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-08-25 00:31:48 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.awt.image.BufferedImage;

// Holds an XFixesCursorImage structure

class CursorImage 
{
    int actualWidth;
    int actualHeight;
    int roundedWidth;
    int roundedHeight;
    int hotX;
    int hotY;
    long cursorSerial;
    BufferedImage image;
}

