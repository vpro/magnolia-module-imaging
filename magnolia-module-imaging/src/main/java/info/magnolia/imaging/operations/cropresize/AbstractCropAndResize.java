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
import info.magnolia.imaging.operations.ImageOperation;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * Subclasses of this determine which portion of the source image is taken into account.
 *
 * If targetWidth and targetHeight are <=0, no resizing will happen. (ie cropping only)
 * If either targetWidth or targetHeight is <=0, the ratio of the cropped image will be preserved.
 * If both targetWidth and targetHeight are >0, both will be used, even if they don't match the ratio of the cropped image.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public abstract class AbstractCropAndResize implements ImageOperation {
    private ResizeTechnique resizeTechnique = new BasicResizeTechnique();
    private int targetWidth;
    private int targetHeight;

    public BufferedImage apply(BufferedImage source, ParameterProvider params) throws ImagingException {
        final Coords coords = getCroopCoords(source, targetWidth, targetHeight);
        return resize(source, coords);
    }

    /**
     * Determines the coordinates of the cropping to apply on the source image.
     * If no cropping needs to happen, return new Coords(0, 0, source.getWidth(), source.getHeight()). 
     */
    protected abstract Coords getCroopCoords(BufferedImage source, int targetWidth, int targetHeight) throws ImagingException;

    protected BufferedImage resize(BufferedImage src, Coords cropCoords) {
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

        return resize(src, cropCoords, effectiveTargetWidth, effectiveTargetHeight);
    }

    protected BufferedImage resize(BufferedImage src, Coords srcCoords, int targetWidth, int targetHeight) {
        return getResizeTechnique().resize(src, srcCoords, targetWidth, targetHeight);
    }

    public ResizeTechnique getResizeTechnique() {
        return resizeTechnique;
    }

    public void setResizeTechnique(ResizeTechnique resizeTechnique) {
        this.resizeTechnique = resizeTechnique;
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
