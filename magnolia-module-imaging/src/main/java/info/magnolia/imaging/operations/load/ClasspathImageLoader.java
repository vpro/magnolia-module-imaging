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
package info.magnolia.imaging.operations.load;

import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;

import java.awt.Color;
import java.net.URL;

/**
 * An ImageFilter which loads an image from the classpath.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ClasspathImageLoader<P extends ParameterProvider<?>> extends AbstractURLImageLoader<P> {
    private String src;

    public ClasspathImageLoader() {
    }

    public ClasspathImageLoader(String src) {
        this(null, src);
    }

    public ClasspathImageLoader(Color backgroundColor, String src) {
        super(backgroundColor);
        this.src = src;
    }

    protected URL getAndValidateUrl(P filterParams) throws ImagingException {
        final URL url = getClass().getResource(src);
        if (url == null) {
            throw new ImagingException("Can't find image at " + src);
        }
        return url;
    }

    public void setSrc(String src) {
        this.src = src;
    }

}
