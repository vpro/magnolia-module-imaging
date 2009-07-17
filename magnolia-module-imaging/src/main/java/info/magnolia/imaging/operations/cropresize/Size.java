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
package info.magnolia.imaging.operations.cropresize;

import java.io.Serializable;

/**
 * A simple immutable bean holding two integers reprensenting a Size, width and height.
 * Similar to java.awt.Dimension, without all the toolkit and environment
 * dependencies.
 * Provides factory methods to calculate sizes based on ratios, source dimensions, crop coordinates.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
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
        final double sourceRatio = (double) sourceWidth / (double) sourceHeight;

        final int tentativeWidth = maxWidth;
        final int tentativeHeight = (int) (tentativeWidth / sourceRatio);

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
