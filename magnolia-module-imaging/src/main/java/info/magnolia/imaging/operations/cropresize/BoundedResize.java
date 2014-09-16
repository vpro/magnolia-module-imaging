/**
 * This file Copyright (c) 2009-2012 Magnolia International
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
package info.magnolia.imaging.operations.cropresize;

import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;

import java.awt.image.BufferedImage;

/**
 * A CropAndResize implementation which will never crop, and resize to either
 * maxHeight or maxWidth while keeping the original image's ratio.
 *
 * @version $Id$
 */
public class BoundedResize extends AbstractCropAndResize {
    private int maxWidth;
    private int maxHeight;
    private boolean expand = true;

    @Override
    protected Coords getCroopCoords(BufferedImage source, ParameterProvider params) throws ImagingException {
        return new Coords(0, 0, source.getWidth(), source.getHeight());
    }

    @Override
    protected Size getEffectiveTargetSize(BufferedImage source, Coords cropCoords, ParameterProvider params) {
        return Size.maxSizeComplyingWithSourceRatio(source.getWidth(), source.getHeight(), maxWidth, maxHeight, expand);
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

    public boolean getExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}
