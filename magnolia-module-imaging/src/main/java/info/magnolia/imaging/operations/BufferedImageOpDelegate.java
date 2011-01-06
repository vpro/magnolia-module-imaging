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
package info.magnolia.imaging.operations;

import info.magnolia.imaging.ParameterProvider;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * An implementation of ImageFilter which delegates to a java.awt.image.BufferedImageOp.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class BufferedImageOpDelegate<P extends ParameterProvider<?>> implements ImageOperation<P> {
    private BufferedImageOp delegate;
    // TODO -- some content2bean magic to automatically wrap BufferedImageOp into ImageOperation ?
    // -- or vice-versa ...

    public BufferedImage apply(BufferedImage source, P filterParams) {
        return getDelegate().filter(source, null);
    }

    public BufferedImageOp getDelegate() {
        return delegate;
    }

    public void setDelegate(BufferedImageOp delegate) {
        this.delegate = delegate;
    }
}
