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

import info.magnolia.imaging.filters.FilterParameterStrategy;
import info.magnolia.imaging.filters.ImageFilter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class Config<P extends FilterParameterStrategy<?>> {
    private final Map<String, ImageFilter<P>> filters = new LinkedHashMap<String, ImageFilter<P>>();

    public void addFilter(String name, ImageFilter<P> filter) {
        filters.put(name, filter);
    }

    public Map<String, ImageFilter<P>> getFilters() {
        return filters;
    }

}
