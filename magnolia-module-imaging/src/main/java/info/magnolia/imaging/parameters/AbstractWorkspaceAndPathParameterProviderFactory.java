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
package info.magnolia.imaging.parameters;

import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.ParameterProviderFactory;
import info.magnolia.imaging.util.PathSplitter;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;


/**
 * @author pbracher
 * @version $Id$
 *
 */
public abstract class AbstractWorkspaceAndPathParameterProviderFactory<PT> implements ParameterProviderFactory<HttpServletRequest, PT> {

    public ParameterProvider<PT> newParameterProviderFor(HttpServletRequest req) {
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
        final String path = "/" + pathSplitter.remaining();

        try {
            final HierarchyManager hm = MgnlContext.getHierarchyManager(workspaceName);
            return newParameterProviderForPath(hm, path);
        } catch (RepositoryException e) {
            throw new RuntimeException("Can't load source from " + path + " from workspace " + workspaceName, e); // TODO
        }
    }

    protected abstract ParameterProvider<PT> newParameterProviderForPath(final HierarchyManager hm, final String path) throws RepositoryException;

}
