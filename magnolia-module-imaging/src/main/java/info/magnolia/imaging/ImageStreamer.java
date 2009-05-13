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
package info.magnolia.imaging;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface ImageStreamer<P> {

    // TODO or ImageStreamer
    // TODO or ImageSender

    /**
     * Generates an image (if deemed necessary) with the given ImageGenerator and Parameters, streams it to
     * the given OutputStream.
     */
    void serveImage(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params, OutputStream out) throws ImagingException, IOException;

}
