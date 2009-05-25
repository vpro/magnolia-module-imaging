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

import javax.jcr.RepositoryException;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ParameterProvider;


/**
 * @author pbracher
 * @version $Id$
 *
 */
public class NodeDataBasedCachingStrategy extends AbstractContentBasedCachingStrategy<NodeData> {

    @Override
    protected Content getContent(ParameterProvider<NodeData> params) {
        final NodeData nd = params.getParameter();
        try {
            return nd.getParent();
        }
        catch (RepositoryException e) {
            throw new IllegalStateException("Can't access parent of " + nd.getHandle());
        }
    }

    @Override
    protected String getPath(ParameterProvider<NodeData> params) {
        return params.getParameter().getHandle();
    }
    

}
