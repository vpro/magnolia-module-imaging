/**
 * This file Copyright (c) 2009-2012 Magnolia International
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
package info.magnolia.imaging.operations.load;


import static org.junit.Assert.*;

import org.junit.Test;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.AbstractRepositoryTestCase;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.ParameterProviderFactory;
import info.magnolia.imaging.caching.CachingStrategy;
import info.magnolia.imaging.caching.ContentBasedCachingStrategy;
import info.magnolia.imaging.parameters.NodeDataParameterProvider;

/**
 * @version $Id$
 */
public class FromNodeDataTest extends AbstractRepositoryTestCase {

    @Test
    public void testNonBinaryPropertyYieldsAProperException() throws Exception {
        final HierarchyManager srcHM = MgnlContext.getHierarchyManager("website");
        final Content src = ContentUtil.createPath(srcHM, "/some/node");
        final NodeData srcProp = src.createNodeData("chalala", "tralala");
        srcHM.save();

        final ParameterProviderFactory<Object, NodeData> ppf = new ParameterProviderFactory<Object, NodeData>() {
            @Override
            public ParameterProvider<NodeData> newParameterProviderFor(Object environment) {
                return new NodeDataParameterProvider(srcProp);
            }

            @Override
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
