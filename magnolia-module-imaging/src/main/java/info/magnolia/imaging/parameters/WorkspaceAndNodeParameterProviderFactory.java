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
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.ParameterProviderFactory;
import info.magnolia.imaging.caching.CachingStrategy;
import info.magnolia.imaging.caching.ContentBasedCachingStrategy;
import info.magnolia.imaging.util.PathSplitter;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

/**
 * This is a ParameterProviderFactory which determines the workspace and node to use based on the
 * request uri. (the using HttpServletRequest.getPathInfo(), to be accurate)
 * It assumes that the first path element is the name of the ImageGenerator in use; that the second is the name
 * of the workspace where the node we want is, and that the rest is the path to the node in question.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class WorkspaceAndNodeParameterProviderFactory implements ParameterProviderFactory<HttpServletRequest, Content> {

    public NodeParameterProvider newParameterProviderFor(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            throw new IllegalArgumentException("Can't determine node, no pathInfo is available for uri " + req.getRequestURI());
        }

        // TODO - here we assume that the first path element is the ImageGenerator name, which is in fact something that is currently the business of the ImagingServlet.
        // Can we do better ?
        final PathSplitter pathSplitter = new PathSplitter(pathInfo, true);
        if (pathSplitter.count() < 2) {
            throw new IllegalArgumentException("Can't determine node from pathInfo: " + pathInfo);

        }
        final String workspaceName = pathSplitter.skipTo(1);
        final String nodePath = "/" + pathSplitter.remaining();

        try {
            final HierarchyManager hm = MgnlContext.getHierarchyManager(workspaceName);
            return new NodeParameterProvider(hm.getContent(nodePath));
        } catch (RepositoryException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    public CachingStrategy<Content> getCachingStrategy() {
        return new ContentBasedCachingStrategy();
    }

}
