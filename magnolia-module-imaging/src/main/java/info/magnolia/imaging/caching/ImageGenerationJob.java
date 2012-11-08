/**
 * This file Copyright (c) 2009-2012 Magnolia International
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
package info.magnolia.imaging.caching;

import info.magnolia.imaging.ImageGenerator;
import info.magnolia.imaging.ParameterProvider;

/**
 * Wrapper around an ImageGenerator and a ParameterProvider used as a key for current running jobs in
 * CachingImageStreamer.
 *
 * The IMPORTANT bit here is that we implemented equals() and hashCode() to compare
 * the actual parameter and not the ParameterProvider instance: we're assuming that whatever parameter
 * is used provides an appropriate equals/hashCode() implementation.
 *
 * @param <P> type of ParameterProvider's parameter
 *
 * @version $Id$
 */
class ImageGenerationJob<P> {
    private final ImageGenerator<ParameterProvider<P>> generator;
    private final ParameterProvider<P> params;

    ImageGenerationJob(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params) {
        this.generator = generator;
        this.params = params;
    }

    public ImageGenerator<ParameterProvider<P>> getGenerator() {
        return generator;
    }

    public ParameterProvider<P> getParams() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ImageGenerationJob<ParameterProvider<P>> that = (ImageGenerationJob<ParameterProvider<P>>) o;

        if (!generator.equals(that.generator)) return false;
        if (!params.getParameter().equals(that.params.getParameter())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = generator.hashCode();
        result = 31 * result + params.getParameter().hashCode();
        return result;
    }

}
