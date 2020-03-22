/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyTextTextureGenerator.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-06-29 23:10:32 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import org.jdesktop.lg3d.sg.ImageComponent;
import org.jdesktop.lg3d.sg.ImageComponent2D;
import org.jdesktop.lg3d.sg.Texture;
import org.jdesktop.lg3d.sg.Texture2D;


class GlassyTextTextureGenerator {
    private static final Font textFont = new Font("Serif", Font.PLAIN, 22);
    private static final Color text2DColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);
    private static final int widthMargin = 1;
    private static final int heightMargin = 2;
    private static final FontMetrics fontMetrics;
    
    private Texture t2d;
    private float width;
    private float widthRatio;
    private float heightRatio;
    
    static {
	BufferedImage bi 
	    = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	Graphics2D g2d = (Graphics2D)bi.getGraphics();
	g2d.setFont(textFont);
	fontMetrics = g2d.getFontMetrics();
	g2d.dispose();
    }
    
    public GlassyTextTextureGenerator(String text, float height, float widthScale, 
            int xShift, int yShift) 
    {
        t2d = createTexture(text, height, widthScale, xShift, yShift);
    }
    
    public Texture getTexture() {
        return t2d;
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getWidthRatio() {
        return widthRatio;
    }
    
    public float getHeightRatio() {
        return heightRatio;
    }
    
    private Texture createTexture(String text, float height, float widthScale, 
            int xShift, int yShift) 
    {
	BufferedImage bi = createTextureImage(text, height, widthScale, xShift, yShift);
	ImageComponent2D ic2d 
	    = new ImageComponent2D(ImageComponent.FORMAT_RGBA, bi, true, true);
	Texture2D t2d 
	    = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, 
//		ic2d.getWidth(), ic2d.getHeight());
                bi.getWidth(), bi.getHeight());
	t2d.setMinFilter(Texture.BASE_LEVEL_LINEAR);
	t2d.setMagFilter(Texture.BASE_LEVEL_LINEAR);
	t2d.setImage(0, ic2d);

	return t2d;
    }
    
    private BufferedImage createTextureImage(String text, float height, 
            float widthScale, int xShift, int yShift) 
    {
	if (text == null) {
	    text = ""; // in order to simplify the special case, text==null
	}

	int textWidth = fontMetrics.stringWidth(text) + widthMargin * 2;
	int textHeight = fontMetrics.getHeight() + heightMargin * 2;
	int descent = fontMetrics.getDescent();

	int biWidth = getRoundUptoPow2(textWidth);
	int biHeight = getRoundUptoPow2(textHeight);

	widthRatio = (float)textWidth / (float)biWidth;
	heightRatio = (float)textHeight / (float)biHeight;
        
        width = height * (biWidth * widthRatio) 
                / (biHeight * heightRatio) * widthScale;
        
	int x = widthMargin;
	int y = biHeight - heightMargin - descent;

	BufferedImage bi 
	    = new BufferedImage(biWidth, biHeight, BufferedImage.TYPE_INT_ARGB);
	Graphics2D g2d = (Graphics2D)bi.getGraphics();

	AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC);
	g2d.setComposite(ac);
	g2d.setRenderingHints(
	    new RenderingHints(
		RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
                
	////g2d.fillRect(0, 0, biWidth, biHeight);
	g2d.setFont(textFont);
	g2d.setColor(Color.WHITE);
	g2d.drawString(text, x + xShift, y + yShift);
	g2d.setColor(Color.BLACK);
	g2d.drawString(text, x - xShift, y - yShift);
        g2d.setColor(text2DColor);
	////g2d.setColor(Color.RED);
	g2d.drawString(text, x, y);
	g2d.dispose();

	return bi;
    }
    
    private int getRoundUptoPow2(int n) {
	if (n <= 1) {
	    return 1;
	}

	int pow = 2;
	for ( ; pow < n; pow *= 2);

	return pow;
    }
}

