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
package info.magnolia.module.imaging.cropresize;

import info.magnolia.cms.core.Content;
import info.magnolia.imaging.filters.ImageFilter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropAndResizeFilter implements ImageFilter<CropperInfo> {

    /**
     * Dialog parameters configuring this filter:
     * <ul>
     * <li>targetWidth</li>
     * <li>targetHeight</li>
     * </ul>
     *
     * If targetWidth and targetHeight are <=0, no resizing will happen. (ie the CropperInfo will be used to determine targetWidth and targetHeight)
     * If targetWidth or targetHeight is <=0, the other side will be sized proportionally, using the given CropperInfo.
     * If both targetWidth and targetHeight are >0, we will use them even if they don't match the original ratio.
     */
    public BufferedImage apply(BufferedImage source, CropperInfo filterParams, Content dialogControlConfigNode) {
        // TODO get these from dialogControlConfigNode if not specified in subnode ??
        final Content config = dialogControlConfigNode.getChildByName(filterParams.getConfigName());
        final int targetWidth = (int) config.getNodeData("targetWidth").getLong();
        final int targetHeight = (int) config.getNodeData("targetHeight").getLong();

        return resize(source, filterParams.getCoords(), targetWidth, targetHeight);
    }

    protected BufferedImage resize(BufferedImage src, Coords cropCoords, int targetWidth, int targetHeight) {
        if (targetWidth <= 0 && targetHeight <= 0) {
            targetWidth = cropCoords.getWidth();
            targetHeight = cropCoords.getHeight();
        } else if (targetWidth <= 0) {
            double ratio = (double) targetHeight / (double) cropCoords.getHeight();
            targetWidth = (int) (cropCoords.getWidth() * ratio);
        } else if (targetHeight <= 0) {
            double ratio = (double) targetWidth / (double) cropCoords.getWidth();
            targetHeight = (int) (cropCoords.getHeight() * ratio);
        }

        final ColorModel cm = src.getColorModel();
        final WritableRaster raster = cm.createCompatibleWritableRaster(targetWidth, targetHeight);
        final BufferedImage dst = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
        Graphics2D g = dst.createGraphics();
        g.drawImage(src, 0, 0, targetWidth, targetHeight, cropCoords.getX1(), cropCoords.getY1(), cropCoords.getX2(), cropCoords.getY2(), null);
        g.dispose();

        return dst;
    }
}
