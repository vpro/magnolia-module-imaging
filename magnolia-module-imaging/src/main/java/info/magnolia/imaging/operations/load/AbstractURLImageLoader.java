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

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public abstract class AbstractURLImageLoader<P extends ParameterProvider<?>> extends AbstractLoader<P> {
    protected AbstractURLImageLoader() {
        super();
    }

    protected AbstractURLImageLoader(Color backgroundColor) {
        super(backgroundColor);
    }

    protected BufferedImage loadSource(P filterParams) throws ImagingException {
        try {
            final URL url = getAndValidateUrl(filterParams);
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new ImagingException("Can't load image: " + e.getMessage(), e);
        }
    }

    /**
     * @throws ImagingException where the message should be the invalid URL.
     */
    protected abstract URL getAndValidateUrl(P filterParams) throws ImagingException;
}
