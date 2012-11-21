/**
 * This file Copyright (c) 2009-2012 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
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
 * @param <P> type of the ParameterProvider
 *
 * @version $Id$
 */
public class Blank<P extends ParameterProvider<?>> implements ImageOperation<P> {
    private static final int DEFAULT_TYPE = BufferedImage.TYPE_INT_ARGB_PRE;
    private static final int DEFAULT_SIZE = 200;
    private int type;
    private Color backgroundColor;
    private int width;
    private int height;

    public Blank() {
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    public Blank(int width, int height) {
        this(null, width, height);
    }

    public Blank(Color backgroundColor, int width, int height) {
        this(DEFAULT_TYPE, backgroundColor, width, height);
    }

    public Blank(int type, Color backgroundColor, int width, int height) {
        this.type = type;
        this.backgroundColor = backgroundColor;
        this.width = width;
        this.height = height;
    }

    @Override
    public BufferedImage apply(BufferedImage source, P params) throws ImagingException {
        if (source != null) {
            throw new ImagingException("This operation currently does not support overlaying images");
        }

        final BufferedImage img = new BufferedImage(width, height, type);

        if (backgroundColor != null) {
            final Graphics2D g = img.createGraphics();
            g.setColor(backgroundColor);
            g.fill(new Rectangle(0, 0, width, height));
            g.dispose();
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

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Background color for the new canvas. Default to white.
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
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
