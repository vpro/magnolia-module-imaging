/**
 * This file Copyright (c) 2009-2011 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
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
 * @param <P> type of ParameterProvider
 *
 * @version $Id$
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

    @Override
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
