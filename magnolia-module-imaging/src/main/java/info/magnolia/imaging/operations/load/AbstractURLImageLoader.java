/**
 * This file Copyright (c) 2009-2011 Magnolia International
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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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
        final URL url = getAndValidateUrl(filterParams);
        try {
            final InputStream inputStream = url.openStream();
            return doReadAndClose(inputStream);
        } catch (IOException e) {
            throw new ImagingException("Can't load image from " + url + ": " + e.getMessage(), e);
        }
    }

    /**
     * @throws ImagingException where the message should be the invalid URL.
     */
    protected abstract URL getAndValidateUrl(P filterParams) throws ImagingException;
}
