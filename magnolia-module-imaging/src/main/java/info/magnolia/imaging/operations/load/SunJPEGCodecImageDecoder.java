/**
 * This file Copyright (c) 2010 Magnolia International
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

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An {@link ImageDecoder} implementation which uses {@link com.sun.image.codec.jpeg.JPEGImageDecoder}
 * (which might not be present on all systems), and if failing, falls back on {@link info.magnolia.imaging.operations.load.DefaultImageIOImageDecoder}
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $) 
 */
public class SunJPEGCodecImageDecoder implements ImageDecoder {
    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SunJPEGCodecImageDecoder.class);

    private final ImageDecoder fallback = new DefaultImageIOImageDecoder();

    public BufferedImage read(final InputStream in) throws IOException, ImagingException {
        final BufferedInputStream buff = ImageUtil.newBufferedInputStream(in);
        // Observed JPEGImageDecoder going as far as 60k in the stream before throwing an ImageFormatException.
        // JPEGImageDecoder seems to re-mark this buffer much further once it starts loading.
        buff.mark(1000);
        try {
            final JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(buff);
            return decoder.decodeAsBufferedImage();
        } catch (ImageFormatException e) {
            log.debug("Could not load image using com.sun.image.codec.jpeg.JPEGImageDecoder: {}. Current buffer state: {}", e.getMessage(), buff);
            buff.reset();
            return fallback.read(buff);
        }
    }

}
