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

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An {@link ImageDecoder} implementation which uses {@link com.sun.image.codec.jpeg.JPEGImageDecoder}
 * (which might not be present on all systems), and if failing, falls back on {@link info.magnolia.imaging.operations.load.DefaultImageIOImageDecoder}.
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
