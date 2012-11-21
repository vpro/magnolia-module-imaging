/**
 * This file Copyright (c) 2009-2012 Magnolia International
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
package info.magnolia.imaging;

import info.magnolia.imaging.util.ImageUtil;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * An ImageStreamer implementation which always generates the image.
 * It also knows about some of the javax.imageio quirks, so other
 * implementations should consider subclassing or wrapping it.
 *
 * @param <P> type of ParameterProvider's parameter
 *
 * @version $Id$
 */
public class DefaultImageStreamer<P> implements ImageStreamer<P> {

    @Override
    public void serveImage(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params, OutputStream out) throws ImagingException, IOException {
        final BufferedImage img = generator.generate(params);

        OutputFormat outputFormat = generator.getOutputFormat(params);

        // if source is transparent and format doesn't support transparency, we have to do some handy work
        final BufferedImage fixedImg = ImageUtil.flattenTransparentImageForOpaqueFormat(img, outputFormat);

        write(fixedImg, out, outputFormat);
    }

    protected void write(final BufferedImage img, final OutputStream out, final OutputFormat outputFormat) throws IOException {
        // this is factored out of ImageIO.write() to allow customization of the ImageWriteParam
        final ImageOutputStream imgOut = ImageIO.createImageOutputStream(out);
        final ImageTypeSpecifier imgType = ImageTypeSpecifier.createFromRenderedImage(img);
        final Iterator<ImageWriter> iter = ImageIO.getImageWriters(imgType, outputFormat.getFormatName());
        if (!iter.hasNext()) {
            throw new IllegalStateException("Can't find ImageWriter for " + outputFormat.getFormatName());
        }
        final ImageWriter imageWriter = iter.next();
        final ImageWriteParam params = imageWriter.getDefaultWriteParam();

        outputFormat.applyTo(params);
        imageWriter.setOutput(imgOut);

        imageWriter.write(null, new IIOImage(img, null, null), params);
        imageWriter.dispose();
        imgOut.flush();
        imgOut.close();
    }

}
