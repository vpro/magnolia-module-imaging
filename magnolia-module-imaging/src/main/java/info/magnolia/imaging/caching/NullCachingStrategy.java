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
 * A CachingStrategy implementation that will ... never cache !
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class NullCachingStrategy<P> implements CachingStrategy<P> {
    public String getCachePath(ImageGenerator<ParameterProvider<P>> parameterProviderImageGenerator, ParameterProvider<P> parameterProvider) {
        return null;
    }

    public boolean shouldRegenerate(NodeData cachedBinary, ParameterProvider<P> parameterProvider) {
        return true;
    }
}
