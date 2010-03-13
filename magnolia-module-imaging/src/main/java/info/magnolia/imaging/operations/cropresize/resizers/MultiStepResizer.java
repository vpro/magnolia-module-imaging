/**
 * This file Copyright (c) 2009-2010 Magnolia International
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
package info.magnolia.imaging.operations.cropresize.resizers;

import info.magnolia.imaging.operations.cropresize.Coords;
import info.magnolia.imaging.operations.cropresize.Size;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * Highly inspired by http://today.java.net/lpt/a/362
 *
 * This method will use a multi-step scaling technique that provides higher quality than the usual
 * one-step technique (only useful in downscaling cases, where {@code targetWidth} or {@code targetHeight} is
 * smaller than the original dimensions, and generally only when the {@code BILINEAR} hint is specified).
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class MultiStepResizer extends BasicResizer {
    @Override
    public BufferedImage resize(BufferedImage src, Coords srcCoords, Size targetSize) {
        final int targetWidth = targetSize.getWidth();
        final int targetHeight = targetSize.getHeight();
        final int type = (src.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = src;
        int w, h;

        // Use multi-step technique: start with original size, then
        // scale down in multiple passes with drawImage()
        // until the target size is reached
        w = srcCoords.getWidth();
        h = srcCoords.getHeight();

        boolean firstStep = true;
        do {
            if (w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            final BufferedImage tmp = new BufferedImage(w, h, type);
            final Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, getInterpolationHint());
            if (firstStep) {
                g2.drawImage(ret, 0, 0, w, h, srcCoords.getX1(), srcCoords.getY1(), srcCoords.getX2(), srcCoords.getY2(), null);
            } else {
                g2.drawImage(ret, 0, 0, w, h, null);
            }
            g2.dispose();

            ret = tmp;
            firstStep = false;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
}
