/**
 * This file Copyright (c) 2009-2011 Magnolia International
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

import java.awt.image.BufferedImage;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface Resizer {

    BufferedImage resize(BufferedImage src, Coords srcCoords, Size targetSize);

}
