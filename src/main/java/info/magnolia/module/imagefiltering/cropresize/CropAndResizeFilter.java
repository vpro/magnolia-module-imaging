/**
 * This file Copyright (c) 2003-2009 Magnolia International
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
