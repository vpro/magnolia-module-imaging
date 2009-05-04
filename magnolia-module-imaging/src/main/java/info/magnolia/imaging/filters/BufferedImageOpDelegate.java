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
package info.magnolia.imaging.filters;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * An implementation of ImageFilter which delegates to a java.awt.image.BufferedImageOp.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public abstract class BufferedImageOpDelegate<P> implements ImageFilter<P> {

    public BufferedImage apply(BufferedImage source, P filterParams) {
        return getDelegate().filter(source, null);
    }

    protected abstract BufferedImageOp getDelegate();
}
