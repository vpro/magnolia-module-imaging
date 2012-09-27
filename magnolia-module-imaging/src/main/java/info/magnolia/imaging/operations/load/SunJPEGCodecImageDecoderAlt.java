/**
 * This file Copyright (c) 2010-2011 Magnolia International
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

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
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
 * @version $Id$
 */
public class SunJPEGCodecImageDecoderAlt implements ImageDecoder {

    @Override
    public BufferedImage read(final InputStream in) throws IOException, ImagingException {
        final BufferedInputStream buff = ImageUtil.newBufferedInputStream(in);
        // Haven't observed ImageIO's preliminary usage of the stream (before we do the actual loading)
        // going further than 8. (As opposed to JPEGImageDecoder who went as far as 60k before throwing
        // an ImageFormatException.
        buff.mark(100);

        final ImageInputStream iis = ImageIO.createImageInputStream(buff);
        final Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        if (readers.hasNext()) {
            final ImageReader reader = readers.next();
            try {
                final String formatName = reader.getFormatName().toLowerCase();
                if (formatName.contains("jpeg") || formatName.contains("jpg")) {
                    buff.reset();

                    final JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(buff);
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
