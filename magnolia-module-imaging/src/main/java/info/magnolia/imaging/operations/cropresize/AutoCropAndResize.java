/**
 * This file Copyright (c) 2007-2010 Magnolia International
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

import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;

import java.awt.image.BufferedImage;

/**
 * A CropAndResize implementation which will resize to the configured targetWidth and targetHeight,
 * and crop if needed: while respecting the ratio of the target dimensions, it will crop the image
 * such that the largest possible portion of the image is kept. It will keep the central part of the
 * image and cut off the external parts. (i.e centered crop)
 *
 * If either targetWidth or targetHeight is <=0, the ratio of the source image will be preserved.
 * If both targetWidth and targetHeight are >0, both will be used, even if they don't match the ratio of the source image (thus cropping it).
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class AutoCropAndResize extends AbstractCropAndResize {
    private int targetWidth;
    private int targetHeight;

    protected Coords getCroopCoords(BufferedImage source, ParameterProvider params) throws ImagingException {
        final int sourceWidth = source.getWidth();
        final int sourceHeight = source.getHeight();

        int width, height;
        if (targetWidth <= 0 && targetHeight <= 0) {
            throw new ImagingException("Please specify either or both targetWidth and targetHeight");
        } else if (targetWidth <= 0 || targetHeight <= 0) {
            // either target width or height is set, not both, so let's respect the source ratio
            width = sourceWidth;
            height = sourceHeight;
        } else {
            // both target dimensions are specified, let's respect targetRatio
            final double targetRatio = (double) targetWidth / (double) targetHeight;
            // this is a rather dumb and crude way of determining which side of the source to use for the largest possible result
            // a seemingly smarter "algorithm" involved comparing the ratio of (sourceRatio/targetRatio) to 1. It ended up being less legible and comprehensible than this, so I gave in.
            width = (int) (sourceHeight * targetRatio);
            height = sourceHeight;
            if (width > sourceWidth) {
                width = sourceWidth;
                height = (int) (sourceWidth / targetRatio);
            }
        }

        final int x1 = (sourceWidth - width) / 2;
        final int x2 = sourceWidth - x1;
        final int y1 = (sourceHeight - height) / 2;
        final int y2 = sourceHeight - y1;
        return new Coords(x1, y1, x2, y2);
    }

    // TODO - this has been moved out of AbstractCropAndResize. Here is how it was originally documented:
    // If targetWidth and targetHeight are <=0, no resizing will happen. (ie cropping only)
    // If either targetWidth or targetHeight is <=0, the ratio of the cropped image will be preserved.
    // If both targetWidth and targetHeight are >0, both will be used, even if they don't match the ratio of the cropped image (thus cropping it).

    protected Size getEffectiveTargetSize(BufferedImage source, Coords cropCoords, ParameterProvider params) {
        return Size.conformToCropRatio(cropCoords, targetWidth, targetHeight);
    }

    public int getTargetWidth() {
        return targetWidth;
    }

    public void setTargetWidth(int targetWidth) {
        this.targetWidth = targetWidth;
    }

    public int getTargetHeight() {
        return targetHeight;
    }

    public void setTargetHeight(int targetHeight) {
        this.targetHeight = targetHeight;
    }
}
