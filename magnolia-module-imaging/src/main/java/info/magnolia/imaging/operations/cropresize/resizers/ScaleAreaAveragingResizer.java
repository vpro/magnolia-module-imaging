/**
 * This file Copyright (c) 2009-2011 Magnolia International
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
package info.magnolia.imaging.operations.cropresize.resizers;

import info.magnolia.imaging.operations.cropresize.Coords;
import info.magnolia.imaging.operations.cropresize.Resizer;
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
 * @version $Id$
 */
public class ScaleAreaAveragingResizer implements Resizer {
    @Override
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
