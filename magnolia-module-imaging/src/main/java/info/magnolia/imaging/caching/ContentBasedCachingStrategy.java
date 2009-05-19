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
package info.magnolia.imaging.caching;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ImageGenerator;
import info.magnolia.imaging.ParameterProvider;

import javax.jcr.RepositoryException;
import java.util.Calendar;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ContentBasedCachingStrategy implements CachingStrategy<Content> {

    // TODO -- this is assuming that a generated node's path is retrievable from the params.
    // TODO -- since there might be querying involved, it might be more efficient and clear if this method would instead
    // TODO -- return the node instance directly
    public String getCachePath(ImageGenerator<ParameterProvider<Content>> generator, ParameterProvider<Content> params) {
        final Content p = params.getParameter();
        return "/" + generator.getName() + "/" + p.getHierarchyManager().getName() + p.getHandle();
    }

    /**
     * The default implementation simply delegates this decision to ParameterProvider.
     * @return true if the image should be regenerated.
     */
    public boolean shouldRegenerate(NodeData cachedBinary, ParameterProvider<Content> parameterProvider) {
        try {
            // this is assuming the cached node's metadata was updated, not just the binary
            final Calendar cacheLastMod = cachedBinary.getParent().getMetaData().getModificationDate();

            // this is assuming our parameter's mgnl:metaData was updated when updating any of its properties/binaries
            final Calendar srcLastMod = parameterProvider.getParameter().getMetaData().getModificationDate();

            return cacheLastMod.before(srcLastMod);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

}
