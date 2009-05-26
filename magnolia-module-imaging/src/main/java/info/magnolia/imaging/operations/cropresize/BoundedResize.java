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

import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;

import java.awt.image.BufferedImage;

/**
 * A CropAndResize implementation which will never crop, and resize to either
 * maxHeight or maxWidth while keeping the original image's ratio. 
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class BoundedResize extends AbstractCropAndResize {
    private int maxWidth;
    private int maxHeight;

    protected Coords getCroopCoords(BufferedImage source, ParameterProvider params) throws ImagingException {
        return new Coords(0, 0, source.getWidth(), source.getHeight());
    }

    protected Size getEffectiveTargetSize(BufferedImage source, Coords cropCoords, ParameterProvider params) {
        final int sourceWidth = source.getWidth();
        final int sourceHeight = source.getHeight();

        // Casting the first member of the division would be enough, just casting all of them for clarity. Adding parenthese would round the result before casting it.
        final double r = maxWidth < maxHeight ? (double) sourceWidth / (double) maxWidth : (double) sourceHeight / (double) maxHeight;
        final int effectiveWidth = (int) (sourceWidth / r);
        final int effectiveHeight = (int) (sourceHeight / r);

        return new Size(effectiveWidth, effectiveHeight);
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
