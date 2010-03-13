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

import javax.jcr.RepositoryException;


/**
 * @author pbracher
 * @version $Id$
 *
 */
public class NodeDataBasedCachingStrategy extends AbstractContentBasedCachingStrategy<NodeData> {
    @Override
    protected String getWorkspaceName(NodeData param) {
        return param.getHierarchyManager().getName();
    }

    @Override
    protected Content getContent(NodeData param) {
        try {
            return param.getParent();
        } catch (RepositoryException e) {
            throw new IllegalStateException("Can't access parent of " + param.getHandle());
        }
    }

    @Override
    protected String getPathOf(NodeData param) {
        return param.getHandle();
    }


}
