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

import java.util.Calendar;

import javax.jcr.RepositoryException;


/**
 * @author pbracher
 * @version $Id$
 *
 */
public abstract class AbstractContentBasedCachingStrategy<P> implements CachingStrategy<P> {

    public String getCachePath(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params) {
        return "/" + generator.getName() + "/" + getContent(params).getHierarchyManager().getName() + getPath(params);
    }

    /**
     * The default implementation simply delegates this decision to ParameterProvider.
     * @return true if the image should be regenerated.
     */
    public boolean shouldRegenerate(NodeData cachedBinary, ParameterProvider<P> parameterProvider) {
        try {
            // this is assuming the cached node's metadata was updated, not just the binary
            final Calendar cacheLastMod = cachedBinary.getParent().getMetaData().getModificationDate();
    
            // this is assuming our parameter's mgnl:metaData was updated when updating any of its properties/binaries
            final Calendar srcLastMod = getContent(parameterProvider).getMetaData().getModificationDate();
    
            return cacheLastMod.before(srcLastMod);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    abstract protected Content getContent(ParameterProvider<P> params);

    abstract protected String getPath(ParameterProvider<P> params);
}
