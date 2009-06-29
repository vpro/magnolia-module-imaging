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
package info.magnolia.imaging.parameters;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.caching.CachingStrategy;
import info.magnolia.imaging.caching.ContentBasedCachingStrategy;

import javax.jcr.RepositoryException;

/**
 * This is a ParameterProviderFactory which determines the workspace and node to use based on the
 * request uri. (the using HttpServletRequest.getPathInfo(), to be accurate)
 * It assumes that the first path element is the name of the ImageGenerator in use; that the second is the name
 * of the workspace where the node we want is, and that the rest is the path to the node in question.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ContentParameterProviderFactory extends AbstractWorkspaceAndPathParameterProviderFactory<Content> {

    protected ParameterProvider<Content> newParameterProviderForPath(final HierarchyManager hm, final String path) throws RepositoryException {
        final Content node = hm.getContent(path);
        return new ContentParameterProvider(new SimpleEqualityContentWrapper(node));
    }

    public CachingStrategy<Content> getCachingStrategy() {
        return new ContentBasedCachingStrategy();
    }

}
