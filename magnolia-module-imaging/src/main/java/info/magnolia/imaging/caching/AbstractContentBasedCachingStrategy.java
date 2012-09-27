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

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ImageGenerator;
import info.magnolia.imaging.ParameterProvider;

import javax.jcr.RepositoryException;
import java.util.Calendar;


/**
 * Superclass for Content based CachingStrategies.
 *
 * @param <P> type of ParameterProvider's parameter
 *
 * @version $Id$
 */
public abstract class AbstractContentBasedCachingStrategy<P> implements CachingStrategy<P> {

    @Override
    public String getCachePath(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> parameterProvider) {
        final P param = parameterProvider.getParameter();
        return "/" + generator.getName() + "/" + getWorkspaceName(param) + getPathOf(param);
    }

    /**
     * The default implementation simply delegates this decision to ParameterProvider.
     * @return true if the image should be regenerated.
     */
    @Override
    public boolean shouldRegenerate(NodeData cachedBinary, ParameterProvider<P> parameterProvider) {
        try {
            // this is assuming the cached node's metadata was updated, not just the binary
            final Calendar cacheLastMod = cachedBinary.getParent().getMetaData().getModificationDate();

            // this is assuming our parameter's mgnl:metaData was updated when updating any of its properties/binaries
            final Calendar srcLastMod = getContent(parameterProvider.getParameter()).getMetaData().getModificationDate();

            //we should always check for the other one anyway since getModificationDate() _can_ return null
            if (srcLastMod == null ) {
                // no srcLastMod update, means likely also no source at all ...
                return false;
            } else if (cacheLastMod == null) {
                // no cacheLstMod update, means that the copy is stale or not created properly
                return true;
            } else {
                return cacheLastMod.before(srcLastMod);
            }
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    abstract protected String getWorkspaceName(P param);

    abstract protected Content getContent(P param);

    abstract protected String getPathOf(P param);
}
