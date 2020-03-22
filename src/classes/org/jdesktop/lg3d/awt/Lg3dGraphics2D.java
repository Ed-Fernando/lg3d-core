/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dGraphics2D.java,v $
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
 * $Date: 2007-03-08 09:33:47 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulby
 */
public class Lg3dGraphics2D extends java.awt.Graphics2D implements Lg3dBackBuffer.BufferResizeListener {
    // TODO Need to coallesce backbuffer repaints. Every draw operation should call
    // backBuffer.contentsChanged to mark the buffer as dirty. Repaints should
    // be triggered once the users paint method returns. At the moment every contentsChanged
    // call will redraw the buffer which is grossly inefficient.
    
    private Logger log = Logger.getLogger("lg.graphics2d");
    
    private Graphics2D graphics;
    private Lg3dBackBuffer backBuffer;
    private GraphicsConfiguration graphicsConfiguration = null;
    
    Lg3dGraphics2D(Lg3dBackBuffer backBuffer) {
        this(backBuffer,null);
    }
    
    Lg3dGraphics2D(Lg3dBackBuffer backBuffer, Graphics2D graphics) {
        this.backBuffer = backBuffer;
        if (graphics==null)
            this.graphics = backBuffer.createGraphics();
        else
            this.graphics = (Graphics2D)graphics.create();
        //log.warning("graphics2 "+graphics.hashCode());
    }

    public void addRenderingHints(java.util.Map<?, ?> map) {
        log.fine("addRenderingHints");
        graphics.addRenderingHints(map);
    }
    
    public void clearRect(int x, int y, int width, int height) {
        log.fine("clearRect");
        graphics.clearRect(x,y,width,height);
        backBuffer.contentsChanged();
    }

    public void clip(Shape shape) {
        log.fine("clip");
        graphics.clip(shape);
    }

    public void clipRect(int x, int y, int width, int height) {
        log.fine("clipRect");
        graphics.clipRect(x,y,width,height);
    }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        log.fine("copyArea");
        graphics.copyArea(x,y,width,height, dx, dy);
        backBuffer.contentsChanged();
    }

    public Graphics create() {
        Graphics tmp = new Lg3dGraphics2D(backBuffer, graphics);
        log.fine("Create "+tmp);
        return tmp;
    }

    public void dispose() {
        log.fine("dispose "+this);
        graphics.dispose();
        
        // This will correctly cause a null pointer exception if this
        // graphics device is used after being disposed
        graphics = null;            
    }

    public void draw(Shape shape) {
        log.fine("draw");
        graphics.draw(shape);
        backBuffer.contentsChanged();
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int endAngle) {
        log.fine("drawArc");
        graphics.drawArc(x,y, width, height, startAngle, endAngle);
        backBuffer.contentsChanged();
    }

    public void drawGlyphVector(java.awt.font.GlyphVector glyphVector, float x, float y) {
        log.fine("drawGlyphVector");
        graphics.drawGlyphVector(glyphVector,x,y);
        backBuffer.contentsChanged();
    }

    public boolean drawImage(Image image, java.awt.geom.AffineTransform affineTransform, java.awt.image.ImageObserver imageObserver) {
        log.fine("drawImage");
        boolean ret = graphics.drawImage(image,affineTransform,imageObserver);
        backBuffer.contentsChanged();
        return ret;
    }

    public boolean drawImage(Image image, int x, int y, java.awt.image.ImageObserver imageObserver) {
        log.fine("drawImage2 "+x+","+y+"   "+image.getWidth(null)+" "+image.getHeight(null));
        boolean ret = graphics.drawImage(image,x,y,imageObserver);
        backBuffer.contentsChanged();
        return ret;
    }

    public void drawImage(java.awt.image.BufferedImage bufferedImage, java.awt.image.BufferedImageOp bufferedImageOp, int x, int y) {
        log.fine("drawImage3");
        graphics.drawImage(bufferedImage,bufferedImageOp,x,y);
        backBuffer.contentsChanged();
    }

    public boolean drawImage(Image image, int x, int y, Color color, java.awt.image.ImageObserver imageObserver) {
        log.fine("drawImage4");
        boolean ret = graphics.drawImage(image, x, y, color, imageObserver);
        backBuffer.contentsChanged();
        return ret;
    }

    public boolean drawImage(Image image, int x, int y, int width, int height, java.awt.image.ImageObserver imageObserver) {
        log.fine("drawImage5");
        boolean ret = graphics.drawImage(image, x, y, width, height, imageObserver);
        backBuffer.contentsChanged();
        return ret;
    }

    public boolean drawImage(Image image, int x, int y, int width, int height, Color color, java.awt.image.ImageObserver imageObserver) {
        log.fine("drawImage6");
        boolean ret = graphics.drawImage(image, x, y, width, height, color, imageObserver);
        backBuffer.contentsChanged();
        return ret;
    }

    public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.image.ImageObserver imageObserver) {
        log.fine("drawImage7");
        boolean ret = graphics.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, imageObserver);
        backBuffer.contentsChanged();
        return ret;
    }

    public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgColor, java.awt.image.ImageObserver observer) {
        log.fine("drawImage8");
        boolean ret = graphics.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgColor, observer);
        backBuffer.contentsChanged();
        return ret;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        log.fine("drawLine");
        graphics.drawLine(x1,y1,x2,y2);
        backBuffer.contentsChanged();
    }

    public void drawOval(int x, int y, int width, int height) {
        log.fine("drawOval");
        graphics.drawOval(x,y,width,height);
        backBuffer.contentsChanged();
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        log.fine("drawPolygon");
        graphics.drawPolygon(xPoints, yPoints, nPoints);
        backBuffer.contentsChanged();
    }

    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        log.fine("drawPolyline");
        graphics.drawPolyline(xPoints, yPoints, nPoints);
        backBuffer.contentsChanged();
    }

    public void drawRenderableImage(java.awt.image.renderable.RenderableImage img, java.awt.geom.AffineTransform xform) {
        log.fine("drawRenderableImage");
        graphics.drawRenderableImage(img, xform);
        backBuffer.contentsChanged();
    }

    public void drawRenderedImage(java.awt.image.RenderedImage img, java.awt.geom.AffineTransform xform) {
        log.fine("drawRenderableImage");
        graphics.drawRenderedImage(img, xform);
        backBuffer.contentsChanged();
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        log.fine("drawRoundRect");
        graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
        backBuffer.contentsChanged();
    }

    public void drawString(String str, float x, float y) {
        log.fine("drawString");
        graphics.drawString(str,x,y);
        backBuffer.contentsChanged();
    }

    public void drawString(String str, int x, int y) {
        log.fine("drawString 2");
        graphics.drawString(str,x,y);
        backBuffer.contentsChanged();
    }

    public void drawString(java.text.AttributedCharacterIterator attributedCharacterIterator, float x, float y) {
        log.fine("drawString 3");
        graphics.drawString(attributedCharacterIterator,x,y);
        backBuffer.contentsChanged();
    }

    public void drawString(java.text.AttributedCharacterIterator attributedCharacterIterator, int x, int y) {
        log.fine("drawString 4");
        graphics.drawString(attributedCharacterIterator,x,y);
        backBuffer.contentsChanged();
    }

    public void fill(Shape shape) {
        log.fine("fill");
        graphics.fill(shape);
        backBuffer.contentsChanged();
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        log.fine("fillArc");
        graphics.fillArc(x, y, width, height, startAngle, arcAngle);
        backBuffer.contentsChanged();
    }

    public void fillOval(int param, int param1, int param2, int param3) {
        log.fine("fillOval");
        graphics.fillOval(param, param1, param2, param3);
        backBuffer.contentsChanged();
    }

    public void fillPolygon(int[] values, int[] values1, int param) {
        log.fine("fillPolygon");
        graphics.fillPolygon(values, values1, param);
        backBuffer.contentsChanged();
    }

    public void fillRect(int x, int y, int width, int height) {
        log.fine("filleRect");
        graphics.fillRect(x,y,width,height);
        backBuffer.contentsChanged();
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        log.fine("fillRoundRect");
        graphics.fillRoundRect(x,y,width,height, arcWidth, arcHeight);
        backBuffer.contentsChanged();
    }

    public Color getBackground() {
        log.fine("getBackground");
        return graphics.getBackground();
    }

    public Shape getClip() {
        log.fine("getClip");
        return graphics.getClip();
    }

    public Rectangle getClipBounds() {
        log.fine("getClipBounds");
        return graphics.getClipBounds();
    }

    public Color getColor() {
        log.fine("getColor");
        return graphics.getColor();
    }

    public Composite getComposite() {
        log.fine("getComposite");
        return graphics.getComposite();
    }

    public GraphicsConfiguration getDeviceConfiguration() {
        log.fine("getDeviceConfiguration");
	/*
        if (graphicsConfiguration==null)
            graphicsConfiguration = new Lg3dGraphicsConfiguration(graphics.getDeviceConfiguration());
        return graphicsConfiguration;
	    */
	return graphics.getDeviceConfiguration();
    }

    public Font getFont() {
        log.fine("getFont");
        return graphics.getFont();
    }

    public FontMetrics getFontMetrics(Font font) {
        log.fine("getFontMetrics");
        return graphics.getFontMetrics(font);
    }

    public java.awt.font.FontRenderContext getFontRenderContext() {
        log.fine("getFontRenderContext");
        return graphics.getFontRenderContext();
    }

    public Paint getPaint() {
        log.fine("getPaint");
        return graphics.getPaint();
    }

    public Object getRenderingHint(RenderingHints.Key key) {
        log.fine("getRenderingHint");
        return graphics.getRenderingHint(key);
    }

    public RenderingHints getRenderingHints() {
        log.fine("getRenderingHints");
        return graphics.getRenderingHints();
    }

    public Stroke getStroke() {
        log.fine("getStroke");
        return graphics.getStroke();
    }

    public java.awt.geom.AffineTransform getTransform() {
        log.fine("getTransform");
        return graphics.getTransform();
    }

    public boolean hit(Rectangle rectangle, Shape shape, boolean onStroke) {
        log.fine("hit");
        return graphics.hit(rectangle,shape, onStroke);
    }

    public void rotate(double theta) {
        log.fine("rotate");
        graphics.rotate(theta);
    }

    public void rotate(double theta, double x, double y) {
        log.fine("rotate");
        graphics.rotate(theta,x,y);
    }

    public void scale(double sx, double sy) {
        log.fine("scale");
        graphics.scale(sx,sy);
    }

    public void setBackground(Color color) {
        log.fine("setBackground");
        graphics.setBackground(color);
    }

    public void setClip(Shape shape) {
        log.fine("setClip");
        graphics.setClip(shape);
    }

    public void setClip(int x, int y, int width, int height) {
        log.fine("setClip");
        graphics.setClip(x,y,width,height);
    }

    public void setColor(Color color) {
        log.fine("setColor");
        graphics.setColor(color);
    }

    public void setComposite(Composite composite) {
        log.fine("setComposite");
        graphics.setComposite(composite);
    }

    public void setFont(Font font) {
        log.fine("setFont");
        graphics.setFont(font);
    }

    public void setPaint(Paint paint) {
        log.fine("setPaint");
        graphics.setPaint(paint);
    }

    public void setPaintMode() {
        log.fine("setPaintMode");
        graphics.setPaintMode();
    }

    public void setRenderingHint(RenderingHints.Key key, Object obj) {
        log.fine("setRenderingHint");
        graphics.setRenderingHint(key,obj);
    }

    public void setRenderingHints(java.util.Map<?, ?> map) {
        log.fine("setRenderingHints");
        graphics.setRenderingHints(map);
    }

    public void setStroke(Stroke stroke) {
        log.fine("setStroke");
        graphics.setStroke(stroke);
    }

    public void setTransform(java.awt.geom.AffineTransform affineTransform) {
        log.fine("setTransform");
        graphics.transform(affineTransform);
    }

    public void setXORMode(Color color) {
        log.fine("setXORMode");
        graphics.setXORMode(color);
    }

    public void shear(double shx, double shy) {
        log.fine("shear");
        graphics.shear(shx,shy);
    }

    public void transform(java.awt.geom.AffineTransform affineTransform) {
        log.fine("transform");
        graphics.transform(affineTransform);
    }

    public void translate(double x, double y) {
        log.fine("translate");
        graphics.translate(x,y);
    }

    public void translate(int x, int y) {
        log.fine("translate");
        graphics.translate(x,y);
    }
    
    public String toString() {
        return "Lg3dGraphics2D@"+hashCode();
    }
    
    public void bufferResized(Lg3dBackBuffer buffer) {
        log.fine("Buffer resized *********************************************");
        graphics = backBuffer.createGraphics();
    }    
}
