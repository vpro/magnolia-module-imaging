/**
 * This file Copyright (c) 2009 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This program and the accompanying materials are made
 * available under the terms of the Magnolia Network Agreement
 * which accompanies this distribution, and is available at
 * http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.imaging.operations.load;

import info.magnolia.imaging.operations.ImageOperation;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.ImagingException;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Just generates an empty BufferedImage canvas, fills it with specified color.
 * Use this if you don't use an image background.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class Blank implements ImageOperation {
    private int type = BufferedImage.TYPE_INT_ARGB_PRE;
    private Color color = Color.white;
    private int width;
    private int height;

    public BufferedImage apply(BufferedImage source, ParameterProvider params) throws ImagingException {
        final BufferedImage img = new BufferedImage(width, height, type);
//        final Graphics2D g = img.createGraphics();
//        g.setBackground(color);
        //g.setc
//        g.drawRect(0, 0, width, height);
        return img;
    }

    public int getType() {
        return type;
    }

    /**
     * One of BufferedImage.TYPE_*; defaults to TYPE_INT_ARGB_PRE.
     * @see java.awt.image.BufferedImage#TYPE_INT_ARGB_PRE
     */
    public void setType(int type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Background color for the new canvas. Default to white.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
