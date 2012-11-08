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
package info.magnolia.imaging.util;

import info.magnolia.imaging.OutputFormat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Keeps utility functions for imaging.
 *
 * @version $Id$
 */
public class ImageUtil {
    /**
     * @see info.magnolia.imaging.util.ImageUtilTest#testJpegOddity()
     */
    public static BufferedImage flattenTransparentImageForOpaqueFormat(final BufferedImage img, OutputFormat outputFormat) {
        if (img == null || outputFormat == null) {
            throw new IllegalArgumentException("No image or output format passed: [img:" + img + ", outputFormat:" + outputFormat + "]");
        }
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

    public static BufferedInputStream newBufferedInputStream(InputStream in) {
        return new BufferedInputStream(in) {
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("BufferedInputStream");
                sb.append("{pos='").append(pos).append('\'');
                sb.append(", count='").append(count).append('\'');
                sb.append(", markpos='").append(markpos).append('\'');
                sb.append(", marklimit='").append(marklimit).append('\'');
                sb.append(", buf.length='").append(buf.length).append('\'');
                sb.append(", wrapping ").append(in.toString());
                sb.append('}');
                return sb.toString();
            }
        };
    }
}
