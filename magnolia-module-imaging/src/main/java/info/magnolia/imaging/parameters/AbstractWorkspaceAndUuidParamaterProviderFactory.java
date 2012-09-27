/**
 * This file Copyright (c) 2010-2012 Magnolia International
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
package info.magnolia.imaging.parameters;

import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.ParameterProviderFactory;
import info.magnolia.imaging.util.PathSplitter;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

/**
 * AbstractWorkspaceAndUuidParamaterProviderFactory. This class extracts workspace and uuid from the path.
 * Everything after the uuid is ignored so that one can pass e.g. a properly named filename to the image.
 * @param <PT> type of ParameterProvider's parameter
 */
public abstract class AbstractWorkspaceAndUuidParamaterProviderFactory<PT>  implements ParameterProviderFactory<HttpServletRequest, PT> {
    @Override
    public ParameterProvider<PT> newParameterProviderFor(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            throw new IllegalArgumentException("Can't determine node, no pathInfo is available for uri " + request.getRequestURI());
        }

        // TODO - here we assume that the first path element is the ImageGenerator name, which is in fact something that is currently the business of the ImagingServlet.
        // Can we do better ?
        final PathSplitter pathSplitter = new PathSplitter(pathInfo, true);
        if (pathSplitter.count() < 2) {
            throw new IllegalArgumentException("Can't determine node from pathInfo: " + pathInfo);

        }
        final String workspaceName = pathSplitter.skipTo(1);
        final String uuid = pathSplitter.skipTo(2);

        try {
            final Session session = MgnlContext.getJCRSession(workspaceName);
            return newParameterProviderForUuid(session, uuid);
        } catch (RepositoryException e) {
            throw new RuntimeException("Can't load source from " + uuid + " from workspace " + workspaceName, e); // TODO
        }
    }

    abstract protected ParameterProvider<PT> newParameterProviderForUuid(Session session, String uuid) throws RepositoryException;

}
