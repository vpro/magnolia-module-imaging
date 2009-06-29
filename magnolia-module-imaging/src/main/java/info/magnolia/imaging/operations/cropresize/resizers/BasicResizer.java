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
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class BasicResizer implements Resizer {
    /**
     * Internal map mapping a name to a corresponding RenderingHints.VALUE_INTERPOLATION_* instance.
     */
    private static final Map<String, Object> INTERPOLATION_HINTS = new HashMap<String, Object>() {{
        put("nearest_neighbor", RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        put("bilinear", RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        put("bicubic", RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }};

    private String interpolation = "bilinear";

    public BufferedImage resize(BufferedImage src, Coords srcCoords, Size targetSize) {
        final ColorModel cm = src.getColorModel();
        final WritableRaster raster = cm.createCompatibleWritableRaster(targetSize.getWidth(), targetSize.getHeight());
        final BufferedImage dst = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
        final Graphics2D g = dst.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, getInterpolationHint());

        g.drawImage(src, 0, 0, targetSize.getWidth(), targetSize.getHeight(), srcCoords.getX1(), srcCoords.getY1(), srcCoords.getX2(), srcCoords.getY2(), null);
        g.dispose();
        return dst;
    }

    protected Object getInterpolationHint() {
        return INTERPOLATION_HINTS.get(interpolation);
    }

    public String getInterpolation() {
        return interpolation;
    }

    /**
     * One of "nearest_neighbor", "bilinear" or "bicubic".
     * @see #INTERPOLATION_HINTS
     * TODO : make this an enum, when content2bean supports it.
     */
    public void setInterpolation(String interpolation) {
        this.interpolation = interpolation;
    }
}