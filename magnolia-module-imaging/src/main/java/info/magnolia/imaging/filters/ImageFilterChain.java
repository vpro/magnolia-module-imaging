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
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of ImageFilter which delegates to a list of other ImageFilters.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageFilterChain<P> implements ImageFilter<P> {
    private final List<ImageFilter<P>> filters;

    public ImageFilterChain() {
        this.filters = new ArrayList<ImageFilter<P>>();
    }

    public List<ImageFilter<P>> getFilters() {
        return filters;
    }

    public void addFilter(ImageFilter<P> filter) {
        filters.add(filter);
    }

    public BufferedImage apply(BufferedImage source, P filterParams) {
        BufferedImage result = source;
        for (ImageFilter<P> filter : filters) {
            result = filter.apply(result, filterParams);
        }
        return result;
    }

}
