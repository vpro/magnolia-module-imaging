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

import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.caching.CachingStrategy;
import info.magnolia.imaging.caching.NodeDataBasedCachingStrategy;

import javax.jcr.RepositoryException;


/**
 * @author pbracher
 * @version $Id$
 *
 */
public class NodeDataParameterProviderFactory extends AbstractWorkspaceAndPathParameterProviderFactory<NodeData> {

    protected ParameterProvider<NodeData> newParameterProviderForPath(final HierarchyManager hm, String path) throws RepositoryException {
        return new NodeDataParameterProvider(hm.getNodeData(path));
    }

    public CachingStrategy<NodeData> getCachingStrategy() {
        return new NodeDataBasedCachingStrategy();
    }

}