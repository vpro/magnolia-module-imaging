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

import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.caching.CachingStrategy;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class StringBasedCachingStrategy implements CachingStrategy<String> {
    public String getCachePath(ImageGenerator<ParameterProvider<String>> generator, ParameterProvider<String> params) {
        final String p = params.getParameter();
        return "/" + generator.getName() + "/path-to/" + p.replace(' ', '-') + "/generated";
    }

    public boolean shouldRegenerate(NodeData cachedBinary, ParameterProvider<String> stringParameterProvider) {
        // Never regenerate ? Well. That's probably not right.
        // But we're in a test here.
        return false;
    }
}
