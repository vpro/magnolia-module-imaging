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
 * A CropAndResize implementation which uses coordinates as selected by a enduser.
 * TODO
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class SelectedCropAndResize extends AbstractCropAndResize {
    protected Coords getCroopCoords(BufferedImage source, ParameterProvider params) throws ImagingException {
        throw new IllegalStateException("not implemented yet !");
    }

    protected Size getEffectiveTargetSize(BufferedImage source, Coords cropCoords, ParameterProvider params) {
        throw new IllegalStateException("not implemented yet !");
    }
}
