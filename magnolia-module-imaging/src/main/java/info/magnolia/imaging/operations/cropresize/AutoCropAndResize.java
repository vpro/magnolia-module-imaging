/**
 * This file Copyright (c) 2007-2009 Magnolia International
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
 * A CropAndResize implementation which will resize to the configured targetWidth and targetSize,
 * and crop if needed: while respecting the ratio of the target dimensions, it will crop the image
 * such that the largest possible portion of the image is kept. It will keep the central part of the
 * image and cut off the external parts. (i.e centered crop)
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

        final int width, height;
        if (targetWidth <= 0 && targetHeight <= 0) {
            throw new ImagingException("Please specify either or both targetWidth and targetHeight");
        } else if (targetWidth <= 0 || targetHeight <= 0) {
            // either target width or height is set, not both, so let's respect the source ratio
            width = sourceWidth;
            height = sourceHeight;
        } else {
            // both target dimensions are specified, let's respect targetRatio
            double targetRatio = (double) targetWidth / (double) targetHeight;
            // use the largest possible source - if targetRatio is <1, the target image is vertical
            if (targetRatio < 1) {
                width = (int) (sourceHeight * targetRatio);
                height = sourceHeight;
            } else if (targetRatio > 1) {
                width = sourceWidth;
                height = (int) (sourceWidth / targetRatio);
            } else {
                // yay it's a square ! let's use the small dimension of the source, which will give us the largest possible square from the source
                width = (sourceWidth > sourceHeight ? sourceHeight : sourceWidth);
                height = width;
            }
        }

        final int x1 = (sourceWidth - width) / 2;
        final int x2 = sourceWidth - x1;
        final int y1 = (sourceHeight - height) / 2;
        final int y2 = sourceHeight - y1;
        return new Coords(x1, y1, x2, y2);
    }

    protected Size getEffectiveTargetSize(BufferedImage source, Coords cropCoords, ParameterProvider params) {
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
