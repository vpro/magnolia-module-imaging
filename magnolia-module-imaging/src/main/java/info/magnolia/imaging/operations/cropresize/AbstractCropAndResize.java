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
package info.magnolia.imaging.operations.cropresize;

import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.operations.ImageOperation;
import info.magnolia.imaging.operations.cropresize.resizers.BasicResizer;

import java.awt.image.BufferedImage;

/**
 * Subclasses of this determine which portion of the source image is taken into account,
 * and what final dimensions the image needs to be resized to.
 *
 * @param <P> type of ParameterProvider
 *
 * @version $Id$
 */
public abstract class AbstractCropAndResize<P extends ParameterProvider<?>> implements ImageOperation<P> {
    private Resizer resizer = new BasicResizer();

    @Override
    public BufferedImage apply(BufferedImage source, P params) throws ImagingException {
        final Coords coords = getCroopCoords(source, params);
        final Size effectiveTargetSize = getEffectiveTargetSize(source, coords, params);
        return resize(source, coords, effectiveTargetSize);
    }

    /**
     * Determines the coordinates of the cropping to apply on the source image.
     * If no cropping needs to happen, return new Coords(0, 0, source.getWidth(), source.getHeight()).
     */
    protected abstract Coords getCroopCoords(BufferedImage source, P params) throws ImagingException;

    /**
     * Determines the actual size for the resized image based on the source image, the crop coordinates
     * calculated by {@link #getCroopCoords} and the given ParameterProvider.
     */
    protected abstract Size getEffectiveTargetSize(BufferedImage source, Coords cropCoords, P params);

    protected BufferedImage resize(BufferedImage src, Coords srcCoords, Size targetSize) {
        return getResizer().resize(src, srcCoords, targetSize);
    }

    public Resizer getResizer() {
        return resizer;
    }

    public void setResizer(Resizer resizer) {
        this.resizer = resizer;
    }

}
