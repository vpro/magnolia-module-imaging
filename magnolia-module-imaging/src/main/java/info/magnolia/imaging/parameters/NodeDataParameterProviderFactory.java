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
        final NodeData nodeData;
        // working around info.magnolia.cms.beans.config.URI2RepositoryMapping's adding of the binary's filename in links...
        // AggregatorFilter does this too
        final String subPath = path.substring(0, path.lastIndexOf('/'));
        if (hm.isNodeData(subPath)) {
            nodeData = hm.getNodeData(subPath);
        } else {
            nodeData = hm.getNodeData(path);
        }
        return new NodeDataParameterProvider(new SimpleEqualityNodeDataWrapper(nodeData));
    }

    public CachingStrategy<NodeData> getCachingStrategy() {
        return new NodeDataBasedCachingStrategy();
    }

}
