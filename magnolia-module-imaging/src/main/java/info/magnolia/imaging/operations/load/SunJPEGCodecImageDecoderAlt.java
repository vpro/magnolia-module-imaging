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

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * An {@link ImageDecoder} implementation which uses {@link com.sun.image.codec.jpeg.JPEGImageDecoder}
 * (which might not be present on all systems), if it can determine the input is a JPEG image. If it isn't,
 * it uses ImageIO to load the image.
 *
 * TODO - Need to analyze differences in behaviour and performance between this and
 * {@link info.magnolia.imaging.operations.load.SunJPEGCodecImageDecoder}.
 *
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class SunJPEGCodecImageDecoderAlt implements ImageDecoder {

    public BufferedImage read(InputStream in) throws IOException, ImagingException {
        if (in.markSupported()) {
            // Haven't observed ImageIO's preliminary usage of the stream (before we do the actual loading)
            // going further than 8. (As opposed to JPEGImageDecoder who went as far as 60k before throwing
            // an ImageFormatException.
            in.mark(100);
        }
        final ImageInputStream iis = ImageIO.createImageInputStream(in);
        final Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        if (readers.hasNext()) {
            final ImageReader reader = readers.next();
            try {
                final String formatName = reader.getFormatName().toLowerCase();
                if (formatName.contains("jpeg") || formatName.contains("jpg")) {
                    if (in.markSupported()) {
                        in.reset();
                    }
                    final JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
                    return decoder.decodeAsBufferedImage();
                } else {
                    final ImageReadParam param = reader.getDefaultReadParam();
                    reader.setInput(iis, true, true);
                    return reader.read(0, param);
                }
            } catch (ImageFormatException e) {
                throw new ImagingException("Could not load image using com.sun.image.codec.jpeg.JPEGImageDecoder: " + e.getMessage(), e);
            } finally {
                reader.dispose();
                iis.close();
            }
        }
        return null;
    }

}