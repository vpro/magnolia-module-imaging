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

import info.magnolia.imaging.filters.ImageFilter;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class Config {
    private final Map<String, ImageFilter<?>> filters = new LinkedHashMap<String, ImageFilter<?>>();

    public void addFilter(String name, ImageFilter<?> filter) {
        filters.put(name, filter);
    }

    public Map<String, ImageFilter<?>> getFilters() {
        return filters;
    }

}
