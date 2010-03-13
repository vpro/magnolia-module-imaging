/**
 * This file Copyright (c) 2010-2010 Magnolia International
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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * A simple abstraction to allow loading BufferedImages using other mechanisms than the default ImageIO.read().
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $) 
 */
public interface ImageDecoder {
    BufferedImage read(InputStream in) throws IOException, ImagingException;
}
