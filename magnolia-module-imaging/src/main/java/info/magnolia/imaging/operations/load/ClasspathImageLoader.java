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
package info.magnolia.imaging.operations.load;

import info.magnolia.imaging.ParameterStrategy;

import java.net.URL;

/**
 * An ImageFilter which loads an image from the classpath.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ClasspathImageLoader<P extends ParameterStrategy<?>> extends AbstractURLImageLoader<P> {
    private String src;

    protected URL getAndValidateUrl() {
        final URL url = getClass().getResource(src);
        if (url == null) {
            throw new IllegalArgumentException("Can't find image at " + src);
        }
        return url;
    }

    public void setSrc(String src) {
        this.src = src;
    }

}
