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
package info.magnolia.imaging.filters.load;

import info.magnolia.imaging.filters.ImageFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * An ImageFilter which loads an image from the classpath.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ResourceImage implements ImageFilter {
    private String src;

    public BufferedImage apply(BufferedImage source, Object filterParams) {
        try {
            final InputStream input = getClass().getResourceAsStream(src);
            if (input == null) {
                throw new IllegalArgumentException("Can't find image at " + src);
            }
            return ImageIO.read(input);
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    public void setSrc(String src) {
        this.src = src;
    }

}
