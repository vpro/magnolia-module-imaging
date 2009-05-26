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
package info.magnolia.imaging.operations.cropresize.resizers;

import info.magnolia.imaging.operations.cropresize.Resizer;
import info.magnolia.imaging.operations.cropresize.Coords;
import info.magnolia.imaging.operations.cropresize.Size;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * This implementation uses the discouraged java.awt.Image#getScaledInstance method,
 * which has rather poor performance, but unmatched quality.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ScaleAreaAveragingResizer implements Resizer {
    public BufferedImage resize(BufferedImage src, Coords srcCoords, Size targetSize) {

        final BufferedImage cropped = src.getSubimage(srcCoords.getX1(), srcCoords.getY1(), srcCoords.getWidth(), srcCoords.getHeight());
        final Image img = cropped.getScaledInstance(targetSize.getWidth(), targetSize.getHeight(), Image.SCALE_AREA_AVERAGING);

        final ColorModel cm = src.getColorModel();
        final WritableRaster raster = cm.createCompatibleWritableRaster(targetSize.getWidth(), targetSize.getHeight());
        final BufferedImage dst = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
        final Graphics2D g = dst.createGraphics();
        g.drawImage(img, null, null);
        g.dispose();
        return dst;
    }
}
