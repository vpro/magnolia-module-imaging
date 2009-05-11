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

import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.operations.ImageOperation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Just generates an empty BufferedImage canvas, fills it with the specified color if any.
 * Use this if you don't use an image background.
 *
 * TODO -- maybe merge this into ImageGenerator. At least provide a simpler way for non-transparent images...
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class Blank implements ImageOperation {
    private int type = BufferedImage.TYPE_INT_ARGB_PRE;
    private Color color;
    private int width;
    private int height;

    public BufferedImage apply(BufferedImage source, ParameterProvider params) throws ImagingException {
        final BufferedImage img = new BufferedImage(width, height, type);

        if (color != null) {
            final Graphics2D g = img.createGraphics();
            g.setColor(new Color(1f, 1f, 1f, 1f));
            g.fill(new Rectangle(0, 0, width, height));
        }

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
