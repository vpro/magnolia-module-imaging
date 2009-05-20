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
package info.magnolia.imaging.util;

import info.magnolia.imaging.OutputFormat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageUtil {
    /**
     * @see info.magnolia.imaging.util.ImageUtilTest#testJpegOddity()
     */
    public static BufferedImage flattenTransparentImageForOpaqueFormat(final BufferedImage img, OutputFormat outputFormat) {
        // this is not entirely sufficient; for instance, a gif with no transparent pixels loaded through ImageIO.read() will not be considered opaque by the following.
        final boolean isOpaque = img.getTransparency() == Transparency.OPAQUE;

        if (!isOpaque && !outputFormat.supportsTransparency()) {
            final WritableRaster raster = img.getRaster();
            final WritableRaster newRaster = raster.createWritableChild(0, 0, img.getWidth(), img.getHeight(), 0, 0, new int[]{0, 1, 2});

            // create a ColorModel that represents the one of the ARGB except the alpha channel
            final DirectColorModel cm = (DirectColorModel) img.getColorModel();
            final DirectColorModel newCM = new DirectColorModel(cm.getPixelSize(), cm.getRedMask(), cm.getGreenMask(), cm.getBlueMask());

            // now create the new buffer that we'll use to write the image
            return new BufferedImage(newCM, newRaster, false, null);
        } else {
            return img;
        }
    }

    // this is much slower than flattenTransparentImageForOpaqueFormat; left here for comparisons and tests.
    static BufferedImage fillTransparentPixels(final BufferedImage image, Color fillColor) {
        int w = image.getWidth();
        int h = image.getHeight();
        final BufferedImage image2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = image2.createGraphics();
        g.setColor(fillColor);
        g.fillRect(0, 0, w, h);
        g.drawRenderedImage(image, null);
        g.dispose();
        return image2;
    }
}
