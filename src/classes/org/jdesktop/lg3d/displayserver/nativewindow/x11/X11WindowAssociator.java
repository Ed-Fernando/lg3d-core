/**
 * Project Looking Glass
 *
 * $RCSfile: X11WindowAssociator.java,v $
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
 * $Revision: 1.12 $
 * $Date: 2007-04-10 23:25:10 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.nativewindow.x11;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.logging.Logger;

import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindow3D;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.prefs.LgPreferencesHelper;


/**
 *
 */
final class X11WindowAssociator {
    private static final Logger logger = Logger.getLogger("lg.x11");
    
    private ArrayList<WindowAssociationRuleEntry> 
        windowAssociationRule = new ArrayList<WindowAssociationRuleEntry>();
    
    private X11Client focusedWindow = null;
    
    X11WindowAssociator() {
        // keep track of the X window touched last
        LgEventConnector.getLgEventConnector().addListener(NativeWindow3D.class,
            new MouseEnteredEventAdapter(
                new ActionBoolean() {
                    public void performAction(LgEventSource source, boolean enter) {
                        if (enter) {
                            NativeWindow3D nw = (NativeWindow3D)source;
                            X11Client xc = (X11Client)nw.getNativeWindowControl();
                            focusedWindow = xc;
                        } else
                            focusedWindow = null;
                    }
                }
            ));
        loadDefaultRules();
    }
    
    private void loadDefaultRules() {
        logger.info("Loadinging window association rules...");
        Preferences prefs = LgPreferencesHelper.userNodeForPackage(getClass()).node("win-assoc");
        
        String[] rules = null;
        try {
            rules = prefs.childrenNames();
        } catch (BackingStoreException bse) {
            logger.warning("Failed to obtain window association configuration: " + bse);
            return;
        }
        if (rules.length == 0) {
            logger.info("No window association rule provided");
            return;
        }
        
        for (String rule : rules) {
            logger.info("Loading window association rule: " + rule);
            Preferences ruleRrefs = prefs.node(rule);

            String parentResCls  = ruleRrefs.get("parent-res-cls", null);
            String parentResName = ruleRrefs.get("parent-res-name", null);
            String parentResTitlePattern = ruleRrefs.get("parent-title-pattern", null);
            
            String childResCls   = ruleRrefs.get("child-res-cls", null);
            String childResName  = ruleRrefs.get("child-res-name", null);
            String childResTitlePattern = ruleRrefs.get("child-title-pattern", null);
            
            if (parentResCls == null || parentResName == null || parentResTitlePattern == null
                    || childResCls == null || childResName == null || childResTitlePattern == null) 
            {
                logger.warning("Rule for window association rule is broken: " + rule);
            } else {
                addRule(parentResCls, parentResName, parentResTitlePattern,
                        childResCls, childResName, childResTitlePattern);
            }
        }
    }
    
    void addRule(X11Client targetWindow, 
            String subWinResCls, String subWinResName, String subWinTitlePattern) 
    {
        windowAssociationRule.add(
            new WindowAssociationRuleEntry(
                targetWindow, subWinResCls, subWinResName, subWinTitlePattern));
    }
    
    void addRule(String targetWinResCls, String targetWinResName, String targetWinTitlePattern, 
            String subWinResCls, String subWinResName, String subWinTitlePattern) 
    {
        windowAssociationRule.add(
            new WindowAssociationRuleEntry(
                targetWinResCls, targetWinResName, targetWinTitlePattern, 
                subWinResCls, subWinResName, subWinTitlePattern));
    }
    
    X11Client getAssociatedWindow(X11Client x11Client) {
        if (focusedWindow == null) {
            return null;
        }
        for (WindowAssociationRuleEntry entry : windowAssociationRule) {
            X11Client ret = entry.getTargetWindow(x11Client, focusedWindow);
            if (ret != null) {
                if (entry.isOneTime()) {
                    windowAssociationRule.remove(entry);
                }
                return ret;
            }
        }
        return null;
    }

    /**
     * Remove all the rules associated with this x11Client
     */
    void removeAllRules(X11Client x11Client) {
        if (x11Client==null)
            return;
        for(WindowAssociationRuleEntry rule : windowAssociationRule) {
            if (rule.getTargetWindow()==x11Client)
                windowAssociationRule.remove(rule);
        }
        
        if (focusedWindow==x11Client)
            focusedWindow = null;
    }
    
    private static class WindowAssociationRuleEntry {
        private X11Client targetWindow;
        private String subWinResCls;
        private String subWinResName;
        private Pattern targetWinTitlePattern;
        private String targetWinResCls;
        private String targetWinResName;
        private Pattern subWinTitlePattern;
        
        WindowAssociationRuleEntry(X11Client targetWindow, 
                String subWinResCls, String subWinResName, String subWinTitlePattern) 
        {
            this.targetWindow = targetWindow;
            
            this.subWinResCls = subWinResCls;
            this.subWinResName = subWinResName;
            if (subWinTitlePattern != null) {
                try {
                    this.subWinTitlePattern = Pattern.compile(subWinTitlePattern);
                } catch (Exception ex) {
                    logger.warning("Error found in pattern: " + ex);
                }
            }
        }
        
        WindowAssociationRuleEntry(
                String targetWinResCls, String targetWinResName, String targetWinTitlePattern, 
                String subWinResCls, String subWinResName, String subWinTitlePattern) 
        {
            this.targetWinResCls = targetWinResCls;
            this.targetWinResName = targetWinResName;
            if (targetWinTitlePattern != null) {
                try {
                    this.targetWinTitlePattern = Pattern.compile(targetWinTitlePattern);
                } catch (Exception ex) {
                    logger.warning("Error found in pattern: " + ex);
                }
            }
            
            this.subWinResCls = subWinResCls;
            this.subWinResName = subWinResName;
            if (subWinTitlePattern != null) {
                try {
                    this.subWinTitlePattern = Pattern.compile(subWinTitlePattern);
                } catch (Exception ex) {
                    logger.warning("Error found in pattern: " + ex);
                }
            }
        }
        
        X11Client getTargetWindow(X11Client subWinCandidate, X11Client focusedWindow) {
            String cls = null, name = null;
            String title = subWinCandidate.getName();
            
	    if (subWinCandidate.classHint != null) {
		cls = subWinCandidate.classHint.res_class();
		name = subWinCandidate.classHint.res_name();
	    }

            if (subWinResCls != null && !subWinResCls.equals(cls)) {
                return null;
            }
            if (subWinResName != null && !subWinResName.equals(name)) {
                return null;
            }
            if (subWinTitlePattern != null) {
                Matcher sm = subWinTitlePattern.matcher(title);
                if (!sm.matches()) {
                    return null;
                }
            }
            
            if (targetWindow != null) {
                return targetWindow;
            }
            
            String fCls = focusedWindow.classHint.res_class();
            String fName = focusedWindow.classHint.res_name();
            String fTitle = focusedWindow.getName();
            
            if (targetWinResCls != null && !targetWinResCls.equals(fCls)) {
                return null;
            }
            if (targetWinResName != null && !targetWinResName.equals(fName)) {
                return null;
            }
            if (targetWinTitlePattern != null) {
                Matcher sm = targetWinTitlePattern.matcher(fTitle);
                if (!sm.matches()) {
                    return null;
                }
            }
            return focusedWindow;
        }
        
        boolean isOneTime() {
            return (targetWindow != null);
        }
        
        X11Client getTargetWindow() {
            return targetWindow;
        }
    }
}
