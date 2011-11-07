/**
 * This file Copyright (c) 2009-2011 Magnolia International
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
            } else if (w < targetWidth) {
                w = targetWidth * 2;
                if (w > targetWidth) {
                    w = targetWidth;
                }
            }

            if (h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            } else if (h < targetHeight) {
                h = targetHeight * 2;
                if (h > targetHeight) {
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
            if (ret != null) {
                // if we are generating many images, flush native resources w/o waiting for GC to do this for you
                ret.flush();
            }
            ret = tmp;
            firstStep = false;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
}
