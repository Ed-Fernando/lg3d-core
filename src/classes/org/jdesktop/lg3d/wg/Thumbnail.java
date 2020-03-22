/**
 * Project Looking Glass
 *
 * $RCSfile: Thumbnail.java,v $
 *
 * Copyright (c) 2005-2006, Sun Microsystems, Inc., All Rights Reserved
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
 * $Date: 2006-08-14 23:13:23 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.wg;

/**
 */
public class Thumbnail extends Component3D {
    public static final Thumbnail DEFAULT = new Thumbnail();
    private Frame3D body;
    
    /** Creates a new instance of Thumbnail */
    public Thumbnail() {
    }
    
    /*
     * FIXME -- this method is not remote-execution ready.
     */
    void setBody(Frame3D body) {
        if (this == DEFAULT) {
            return;
        }
        if (this.body != null) {
            throw new IllegalStateException(
                "this thumbnail has been associated with another Frame3D");
        }
        this.body = body;
    }
    
    public Frame3D getBody() {
        return body;
    }
}
