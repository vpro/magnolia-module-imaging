/**
 * This file Copyright (c) 2009-2010 Magnolia International
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
 * @author pbracher
 * @version $Id$
 *
 */
public abstract class AbstractContentBasedCachingStrategy<P> implements CachingStrategy<P> {

    public String getCachePath(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> parameterProvider) {
        final P param = parameterProvider.getParameter();
        return "/" + generator.getName() + "/" + getWorkspaceName(param) + getPathOf(param);
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
            final Calendar srcLastMod = getContent(parameterProvider.getParameter()).getMetaData().getModificationDate();

            System.out.println(Thread.currentThread().getName() + ":: cache:" + cacheLastMod);
            System.out.println(Thread.currentThread().getName() + ":: src  :" + srcLastMod);
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
