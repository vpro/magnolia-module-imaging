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
package info.magnolia.imaging;

import java.awt.image.BufferedImage;

/**
 * The entry point for generating images.
 *
 * @see info.magnolia.imaging.operations.ImageOperationChain
 * @see info.magnolia.imaging.operations.ImageOperation
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface ImageGenerator<P extends ParameterProvider<?>> {

    BufferedImage generate(P params) throws ImagingException;

    ParameterProviderFactory getParameterProviderFactory();

    OutputFormat getOutputFormat(P params);

    String getName();
}
