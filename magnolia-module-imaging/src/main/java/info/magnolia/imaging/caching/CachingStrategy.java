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
package info.magnolia.imaging.caching;

import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ImageGenerator;
import info.magnolia.imaging.ParameterProvider;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface CachingStrategy<P> {

    /**
     * Returns a unique path pertinent to the given generator and parameter to use for storing
     * the generated image.
     */
    String getCachePath(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params);


    /**
     * Given the currently cached generated image, determine if CachingImageStreamer should
     * regenerate the image. Implementations will most likely simply compare the timestamp
     * with their parameter if applicable, or use some other mechanism.
     */
    boolean shouldRegenerate(NodeData cachedBinary, ParameterProvider<P> parameterProvider);

}
