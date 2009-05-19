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
package info.magnolia.imaging.operations.text;

import java.awt.image.BufferedImage;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface TextRenderer {

    void renderText(final BufferedImage img, final String txt, final TextStyle style, final Alignment horizontalAlignment, final Alignment verticalAlignment, final int txtPositionX, final int txtPositionY);

}
