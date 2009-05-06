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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public abstract class AbstractURLImageLoader<P extends ParameterProvider<?>> implements ImageOperation<P> {
    public BufferedImage apply(BufferedImage source, P filterParams) {
        try {
            final URL url = getAndValidateUrl();
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    protected abstract URL getAndValidateUrl();
}
