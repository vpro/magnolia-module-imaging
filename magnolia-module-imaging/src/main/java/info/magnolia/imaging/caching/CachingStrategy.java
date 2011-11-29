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
package info.magnolia.imaging.caching;

import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ImageGenerator;
import info.magnolia.imaging.ParameterProvider;

/**
 * Defines a CachingStrategy.
 *
 * @param <P> type of ParameterProvider's parameter
 *
 * @version $Id$
 */
public interface CachingStrategy<P> {

    /**
     * Returns a unique path pertinent to the given generator and parameter to use for storing
     * the generated image.
     */
    String getCachePath(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> parameterProvider);


    /**
     * Given the currently cached generated image, determine if CachingImageStreamer should
     * regenerate the image. Implementations will most likely simply compare the timestamp
     * with their parameter if applicable, or use some other mechanism.
     */
    boolean shouldRegenerate(NodeData cachedBinary, ParameterProvider<P> parameterProvider);

}
