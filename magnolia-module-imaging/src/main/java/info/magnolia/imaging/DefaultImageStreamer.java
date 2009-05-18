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
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class DefaultImageStreamer<P> implements ImageStreamer<P> {

    public void serveImage(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params, OutputStream out) throws ImagingException, IOException {
        final BufferedImage img = generator.generate(params);

        // if source is transparent and format doesn't support transparency, we have to do some handy work
        final BufferedImage fixedImg = ImageUtil.flattenTransparentImageForOpaqueFormat(img, generator.getOutputFormat());

        write(fixedImg, out, generator.getOutputFormat());
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
