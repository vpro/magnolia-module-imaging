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
import info.magnolia.objectfactory.Components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * An abstract ImageOperation used to load images. It takes charge of converting the loaded image
 * to the appropriate type. (ImageIO.read() will return a different image type depending on the
 * source format). Overhead is negligible.
 * 
 * @param <P> type of ParameterProvider's parameter
 * @version $Id$
 */
public abstract class AbstractLoader<P extends ParameterProvider<?>> implements ImageOperation<P> {
    private ImageDecoder imageDecoder = Components.getComponentProvider().newInstance(ImageDecoder.class);
    private Color backgroundColor;

    protected AbstractLoader() {
    }

    protected AbstractLoader(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public BufferedImage apply(BufferedImage source, P filterParams) throws ImagingException {
        if (source != null) {
            throw new ImagingException("This operation currently does not support overlaying images");
        }
        final BufferedImage loaded = loadSource(filterParams);
        if (loaded == null) {
            throw new ImagingException("Could not load image for " + filterParams);
        }

        int imageType = loaded.getType(); // set the output image type to the source image type

        if (imageType == BufferedImage.TYPE_CUSTOM) { // if the source image type is not set...
            if (loaded.getAlphaRaster() != null) { // with alpha channel
                imageType = BufferedImage.TYPE_INT_ARGB_PRE;
            } else { // without alpha channel
                imageType = BufferedImage.TYPE_INT_RGB;
            }
        }
        final BufferedImage img = new BufferedImage(loaded.getWidth(), loaded.getHeight(), imageType);

        final Graphics2D g = img.createGraphics();

        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fill(new Rectangle(0, 0, img.getWidth(), img.getHeight()));
        }
        g.drawImage(loaded, null, 0, 0);
        // TODO would this make any difference ? g.drawRenderedImage(loaded, null);

        g.dispose();
        return img;
    }

    protected abstract BufferedImage loadSource(P filterParams) throws ImagingException;

    protected BufferedImage doReadAndClose(InputStream inputStream) throws IOException, ImagingException {
        if (inputStream == null) {
            throw new IOException("No input");
        }
        try {
            return imageDecoder.read(inputStream);
        } finally {
            inputStream.close();
        }
    }

    public ImageDecoder getImageDecoder() {
        return imageDecoder;
    }

    public void setImageDecoder(ImageDecoder imageDecoder) {
        this.imageDecoder = imageDecoder;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
