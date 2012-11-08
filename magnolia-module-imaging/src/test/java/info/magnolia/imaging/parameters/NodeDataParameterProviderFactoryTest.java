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
package info.magnolia.imaging.parameters;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.core.NonExistingNodeData;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.cms.util.NodeDataWrapper;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.AbstractRepositoryTestCase;
import info.magnolia.imaging.ParameterProvider;

import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 */
public class NodeDataParameterProviderFactoryTest extends AbstractRepositoryTestCase {
    private NodeData srcProp;
    private HttpServletRequest req;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        final HierarchyManager srcHM = MgnlContext.getHierarchyManager("website");
        final Content src = ContentUtil.createPath(srcHM, "/some/node");
        srcProp = src.createNodeData("chalala", "tralala");
        srcHM.save();

        req = mock(HttpServletRequest.class);
    }

    @Test
    public void testWrapsNodeDataSuchThatEqualsAndHashCodeAreImplementedBasedOnPathAndHierarchyManagerName() throws RepositoryException {
        final HierarchyManager hm = MgnlContext.getHierarchyManager("website");
        assertNotSame(srcProp, hm.getNodeData("/some/node/chalala"));

        when(req.getPathInfo()).thenReturn("/generator/website/some/node/chalala");
        final NodeDataParameterProviderFactory factory = new NodeDataParameterProviderFactory();
        final ParameterProvider<NodeData> param1 = factory.newParameterProviderFor(req);
        final ParameterProvider<NodeData> param2 = factory.newParameterProviderFor(req);
        assertNotSame("2 calls to ParameterProviderFactory should return 2 *different instances*.",
                param1.getParameter(), param2.getParameter());
        assertEquals("2 calls to ParameterProviderFactory should return 2 *equivalent instances*.",
                param1.getParameter(), param2.getParameter());
    }

    @Test
    public void testReturnsWrappedNonExistingNodeDataIfPropertyDoesNotExist() throws Exception {
        when(req.getPathInfo()).thenReturn("/generator/website/some/node/booyah");
        final NodeDataParameterProviderFactory factory = new NodeDataParameterProviderFactory();
        final ParameterProvider<NodeData> pp = factory.newParameterProviderFor(req);
        final NodeData data = pp.getParameter();
        assertTrue(((NodeDataWrapper) data).getWrappedNodeData() instanceof NonExistingNodeData);
    }

    @Test
    public void testYieldsAProperExceptionIfGeneratorNameIsOmmitted() throws Exception {
        when(req.getPathInfo()).thenReturn("/website/some/node/chalala");
        final NodeDataParameterProviderFactory factory = new NodeDataParameterProviderFactory();
        try {
            factory.newParameterProviderFor(req);
        } catch (Throwable t) {
            assertTrue(t.getCause() instanceof NoSuchWorkspaceException);
            assertTrue(t.getMessage().contains("some"));
        }
    }

    @Test
    public void testExistingPropertyJustWorks() throws RepositoryException {
        when(req.getPathInfo()).thenReturn("/generator/website/some/node/chalala");
        final NodeDataParameterProviderFactory factory = new NodeDataParameterProviderFactory();
        final ParameterProvider<NodeData> pp = factory.newParameterProviderFor(req);
        final NodeData data = pp.getParameter();
    }
}
