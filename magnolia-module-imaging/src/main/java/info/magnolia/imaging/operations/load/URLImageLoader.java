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

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Loads an image off an arbitrary URL. So, yes, this can load images off the internet, and yes, it could be
 * painfully slow.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class URLImageLoader<P extends ParameterProvider<?>> extends AbstractURLImageLoader<P> {
    private String url;

    @Override
    protected URL getAndValidateUrl(P filterParams) throws ImagingException {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new ImagingException("Can't load image from url " + e.getMessage());
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
