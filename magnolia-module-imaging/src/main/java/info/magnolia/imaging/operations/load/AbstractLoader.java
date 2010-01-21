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

import javax.imageio.ImageIO;
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
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public abstract class AbstractLoader<P extends ParameterProvider<?>> implements ImageOperation<P> {
    private Color backgroundColor;

    protected AbstractLoader() {
    }

    protected AbstractLoader(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public BufferedImage apply(BufferedImage source, P filterParams) throws ImagingException {
        if (source != null) {
            throw new ImagingException("This operation currently does not support overlaying images");
        }
        final BufferedImage loaded = loadSource(filterParams);
        final BufferedImage img = new BufferedImage(loaded.getWidth(), loaded.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        final Graphics2D g = img.createGraphics();

        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fill(new Rectangle(0, 0, img.getWidth(), img.getHeight()));
        }
        g.drawImage(loaded, null, 0, 0);

        g.dispose();
        return img;
    }

    protected abstract BufferedImage loadSource(P filterParams) throws ImagingException;

    protected BufferedImage doReadAndClose(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IOException("No input");
        }
        try {
            return ImageIO.read(inputStream);
        } finally {
            inputStream.close();
        }
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
