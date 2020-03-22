/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: DisplayResource.java,v $
 *   
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the
 *   Free Software Foundation, Inc.,
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *   
 *   Author: Colin M. Bullock
 *   cmbullock@gmail.com
 *   
 *   $Revision: 1.2 $
 *   $Date: 2006-04-19 17:23:25 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.data;

import java.io.Serializable;
import java.net.URL;

/**
 * This encapsulates the various types of resources
 * (icons, tapps, etc.) that can be displayed to
 * represent a menu component.
 */
public class DisplayResource implements Serializable {
    
    /**
     * The defined types of display resources.
     * <ul>
     * <li><b>DEFAULT</b>: The default representation (model implementation dependent)
     * <li><b>ICON</b>: An icon stored in a file or other resource pointed to by the URL
     * <li><b>TAPP</b>: A sub-class of <code>Tapp</code> named by the URL
     * </ul>
     * @see org.jdesktop.lg3d.wg.Tapp
     */
    public static enum Type { DEFAULT, ICON, TAPP };
    
    /** The URL describing the resource */
    private URL url;
    
    /** The name of the file of the resource */
//    private String filename;
    
    private String classname;
    
    /** The type of the resource */
    private Type type;
    
    /**
     * Default constructor
     */
    public DisplayResource() {
        type= Type.DEFAULT;
    }
    
    /**
     * Create a new display resource with the given type
     * and URL descriptor
     * @param url The URL of the resource
     */
    public DisplayResource(URL url) {
        this.url= url;
        this.classname = null;
        this.type= Type.ICON;
    }

    /**
     * Create a new display resource with the given type
     * and URL descriptor
     * @param url The URL of the resource
     */
    public DisplayResource(String classname) {
        this.url= null;
        this.classname = classname;
        this.type= Type.ICON;
    }
    
    
    /**
     * Get the type of the resource
     * @return The resource type
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the type of the resource
     * @param type The resource type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Get the URL of the resource
     * @return The resource URL
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Set the URL of the resource
     * @param url The resource URL
     */
    public void setUrl(URL url) {
        this.url = url;
    }
    
//    /**
//     * Get the relative path of the resource
//     * @return The resource path
//     */
//    public String getFilename() {
//        return filename;
//    }
//
//    /**
//     * Set the filename of the resource
//     * @param url The resource filename
//     */
//    public void setFilename(String filename) {
//        this.filename = filename;
//    }    
    /**
     * Set the classname for Tapp types
     * @param classname the name of the class
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }
    
    /**
     * Get the classname for Tapp display resources
     * @return The classname
     */
    public String getClassname() {
        return this.classname;
    }

}
