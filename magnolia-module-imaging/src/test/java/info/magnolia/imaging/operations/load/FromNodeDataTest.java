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
package info.magnolia.imaging.operations.load;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.AbstractRepositoryTestCase;
import info.magnolia.imaging.OutputFormat;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.ParameterProviderFactory;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.caching.CachingStrategy;
import info.magnolia.imaging.caching.ContentBasedCachingStrategy;
import info.magnolia.imaging.parameters.NodeDataParameterProvider;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class FromNodeDataTest extends AbstractRepositoryTestCase {
    public void testNonBinaryPropertyYieldsAProperException() throws Exception {
        final HierarchyManager srcHM = MgnlContext.getHierarchyManager("website");
        final Content src = ContentUtil.createPath(srcHM, "/some/node");
        final NodeData srcProp = src.createNodeData("chalala", "tralala");
        srcHM.save();

        final ParameterProviderFactory<Object, NodeData> ppf = new ParameterProviderFactory<Object, NodeData>() {
            public ParameterProvider<NodeData> newParameterProviderFor(Object environment) {
                return new NodeDataParameterProvider(srcProp);
            }

            public CachingStrategy getCachingStrategy() {
                return new ContentBasedCachingStrategy();
            }
        };

        final FromNodeData op = new FromNodeData();
        try {
            op.apply(null, ppf.newParameterProviderFor(null));
            fail();
        } catch (ImagingException e) {
            assertEquals("Nodedata /some/node/chalala doesn't exist or is not of type binary.", e.getMessage());
        }
    }
}
