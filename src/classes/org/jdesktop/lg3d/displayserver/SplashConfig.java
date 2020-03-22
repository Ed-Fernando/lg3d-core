/*
 * Project Looking Glass 
 *
 * SplashConfig.java
 *
 * Revision: 1.0 
 *
 * Created on 12 November 2004, 12:32
 */

/**
 *
 * @author  PhilD   (Phil Dowell)
 *
 */

package org.jdesktop.lg3d.displayserver;

import java.io.*;
import java.util.*;

public class SplashConfig implements Serializable {
    
    public boolean runSplash;
    public boolean showText;
    public String imageLocation;
    public String messageText;
    
    /** No arg constructor */
    public SplashConfig() {        
    }        
    
    public SplashConfig(String imgloc, String msgtxt, boolean runSplash, boolean showText) {
    }
           
    // Create the Getters and the Setters
    public boolean getRunSplash() {
        return runSplash;
    }
    
    public void setRunSplash(boolean a) {
        runSplash = a;
    }
    
    public boolean getShowText() {
        return showText;
    }
    
    public void setShowText(boolean a) {
        showText = a;
    }
    
    public String getImageLocation() {
        return imageLocation;
    }
    
    public void setImageLocation(String a) {
        imageLocation = a;
    }
    
    public String getMessageText() {
        return messageText;
    }
    
    public void setMessageText(String a) {
        messageText = a;
    }
    
    
}
