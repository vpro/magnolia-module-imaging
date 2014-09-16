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
package info.magnolia.imaging.operations.cropresize;

import java.io.Serializable;

/**
 * A simple immutable bean holding two integers reprensenting a Size, width and height.
 * Similar to java.awt.Dimension, without all the toolkit and environment
 * dependencies.
 * Provides factory methods to calculate sizes based on ratios, source dimensions, crop coordinates.
 *
 * @version $Id$
 */
public class Size implements Serializable {
    private final int width;
    private final int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static Size conformToCropRatio(final Coords cropCoords, final int targetWidth, final int targetHeight) {
        final int effectiveTargetWidth, effectiveTargetHeight;
        if (targetWidth <= 0 && targetHeight <= 0) {
            effectiveTargetWidth = cropCoords.getWidth();
            effectiveTargetHeight = cropCoords.getHeight();
        } else if (targetWidth <= 0) {
            double ratio = (double) targetHeight / (double) cropCoords.getHeight();
            effectiveTargetWidth = (int) (cropCoords.getWidth() * ratio);
            effectiveTargetHeight = targetHeight;
        } else if (targetHeight <= 0) {
            double ratio = (double) targetWidth / (double) cropCoords.getWidth();
            effectiveTargetHeight = (int) (cropCoords.getHeight() * ratio);
            effectiveTargetWidth = targetWidth;
        } else {
            effectiveTargetWidth = targetWidth;
            effectiveTargetHeight = targetHeight;
        }

        return new Size(effectiveTargetWidth, effectiveTargetHeight);
    }

    public static Size maxSizeComplyingWithSourceRatio(final int sourceWidth, final int sourceHeight, final int maxWidth, final int maxHeight) {
        return maxSizeComplyingWithSourceRatio(sourceWidth, sourceHeight, maxWidth, maxHeight, true);
    }

    public static Size maxSizeComplyingWithSourceRatio(final int sourceWidth, final int sourceHeight, final int maxWidth, final int maxHeight, final boolean expand) {
        final double sourceRatio = (double) sourceWidth / (double) sourceHeight;

        final int tentativeWidth = maxWidth;
        final int tentativeHeight = (int) (tentativeWidth / sourceRatio);
        if (!expand && sourceWidth <= maxWidth && sourceHeight <= maxHeight) {
            return new Size(sourceWidth, sourceHeight);
        }
        if (tentativeHeight <= maxHeight) {
            // first attempt was good enough
            return new Size(tentativeWidth, tentativeHeight);
        } else {
            // nope, we need to resize the other way
            final int effectiveHeight = maxHeight;
            final int effectiveWidth = (int) (effectiveHeight * sourceRatio);
            return new Size(effectiveWidth, effectiveHeight);
        }
    }
}
