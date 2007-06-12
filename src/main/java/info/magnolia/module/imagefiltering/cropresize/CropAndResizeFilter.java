/**
 *
 * Magnolia and its source-code is licensed under the LGPL.
 * You may copy, adapt, and redistribute this file for commercial or non-commercial use.
 * When copying, adapting, or redistributing this document in keeping with the guidelines above,
 * you are required to provide proper attribution to obinary.
 * If you reproduce or distribute the document without making any substantive modifications to its content,
 * please use the following attribution line:
 *
 * Copyright 1993-2006 obinary Ltd. (http://www.obinary.com) All rights reserved.
 *
 */
package info.magnolia.module.imagefiltering.cropresize;

import info.magnolia.cms.core.Content;
import info.magnolia.module.imagefiltering.ImageFilter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropAndResizeFilter implements ImageFilter {

    public Class getParameterType() {
        return CropperInfo.class;
    }

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
    public BufferedImage apply(BufferedImage source, Object filterParams, Content dialogControlConfigNode) {
        final CropperInfo cropperInfo = (CropperInfo) filterParams;

        // TODO get these from dialogControlConfigNode if not specified in subnode ??
        final Content config = dialogControlConfigNode.getChildByName(cropperInfo.getConfigName());
        final int targetWidth = (int) config.getNodeData("targetWidth").getLong();
        final int targetHeight = (int) config.getNodeData("targetHeight").getLong();

        return resize(source, cropperInfo.getCoords(), targetWidth, targetHeight);
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