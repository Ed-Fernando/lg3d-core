/*
 * StickyNote.java
 *
 * Created on June 13, 2005, 8:57 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.lg3d.scenemanager.utils.decoration;

import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.util.prefs.Preferences;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.lg3d.wg.SwingNode;
import org.jdesktop.lg3d.utils.prefs.LgPreferencesHelper;

/**
 * Implementation of the yellow-sticky-on-the-backside feature.
 * This class provides StickyNote implementation as a SwingNode.
 * GlassynativeWindowLookAndFeel is responsible to put this SwingNode
 * on the backside of a window of X native app.
 */
public class StickyNote extends SwingNode {
    protected static final Logger logger = Logger.getLogger("lg.scenemanager");
    
    private static final Font font = new Font("Serif", Font.BOLD, 14);
    private static final Color bodyFgColor = Color.BLACK;
    private static final Color bodyBgColor = new Color(1.0f, 1.0f, 0.75f);
    private static final Font topBarFont = new Font("Serif", Font.BOLD, 18);
    private static final Color topBarFgColor = Color.WHITE;
    private static final Color topBarBgColor = new Color(0.5f, 0.5f, 0.7f);
    private static final float borderMargin = 0.1f;
    
    private static Preferences prefs 
            = LgPreferencesHelper.userNodeForPackage(StickyNote.class);
    private String name;
    private int width;
    private int height;
    private JPanel panel;
    private JTextArea textArea;
    private JTextField textField;
    
    public StickyNote() {
        //
    }
    
    public void initialize(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        
        panel.setEnabled(true);
        // set the title
        textField.setText("Notes - " + name);
        
        // load the content from the user preferences
        String content = prefs.get(name, "");
        textArea.append(content);
        logger.info("Loaded sticky notes for: " + name);
        
        int w = (int)(width * (1.0f - borderMargin));
        int h = (int)(height * (1.0f - borderMargin));
        panel.setPreferredSize(new Dimension(w, h));
        panel.setEnabled(true);
        
        setPanel(panel);
    }
    
    public void setEnabled(boolean enabled) {
        if (enabled) {
            enable();
        } else {
            disable();
        }
    }
    
    private void enable() {
        if (panel != null) {
            assert(textField != null);
            return;
        }
        
        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setSize((int)(width * 0.9f), (int)(height * 0.9f));
        panel.setLayout(new BorderLayout());
        
        assert(textField == null);
        // initialize the title bar
        textField = new JTextField();
        textField.setFont(topBarFont);
        textField.setForeground(topBarFgColor);
        textField.setBackground(topBarBgColor);
        textField.setMargin(new Insets(8, 8, 8, 8));
        textField.setEditable(false);
        panel.add(textField, BorderLayout.NORTH);
        
        // initialize the content area
        textArea = new JTextArea("");
        textArea.setFont(font);
        textArea.setForeground(bodyFgColor);
        textArea.setBackground(bodyBgColor);
        textArea.setMargin(new Insets(8, 8, 8, 8));
        panel.add(textArea, BorderLayout.CENTER);
    }
    
    private void disable() {
        // save the content into the user preferences
        String content = textArea.getText();
        prefs.put(name, content);
        logger.info("Saved sticky notes for: " + name);
        
        // TODO -- how to clearnly release the resource?
        // setPanel(null); // this causes NullPointerException in Container.addImpl()
        panel.setEnabled(false);
        textArea.setText(null); // clear the content
//        panel = null;
//        textArea = null;
    }
}
