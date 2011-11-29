/**
 * This file Copyright (c) 2009-2011 Magnolia International
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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.core.NonExistingNodeData;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.cms.util.NodeDataWrapper;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.AbstractRepositoryTestCase;
import info.magnolia.imaging.ParameterProvider;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Ignore;
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

        req = createStrictMock(HttpServletRequest.class);
    }

    @Test
    public void testWrapsNodeDataSuchThatEqualsAndHashCodeAreImplementedBasedOnPathAndHierarchyManagerName() throws RepositoryException {
        final HierarchyManager hm = MgnlContext.getHierarchyManager("website");
        assertNotSame(srcProp, hm.getNodeData("/some/node/chalala"));

        expect(req.getPathInfo()).andReturn("/generator/website/some/node/chalala").times(2);
        replay(req);
        final NodeDataParameterProviderFactory factory = new NodeDataParameterProviderFactory();
        final ParameterProvider<NodeData> param1 = factory.newParameterProviderFor(req);
        final ParameterProvider<NodeData> param2 = factory.newParameterProviderFor(req);
        assertNotSame("2 calls to ParameterProviderFactory should return 2 *different instances*.",
                param1.getParameter(), param2.getParameter());
        assertEquals("2 calls to ParameterProviderFactory should return 2 *equivalent instances*.",
                param1.getParameter(), param2.getParameter());
        verify(req);
    }

    @Test
    public void testReturnsWrappedNonExistingNodeDataIfPropertyDoesNotExist() throws Exception {
        expect(req.getPathInfo()).andReturn("/generator/website/some/node/booyah");
        replay(req);
        final NodeDataParameterProviderFactory factory = new NodeDataParameterProviderFactory();
        final ParameterProvider<NodeData> pp = factory.newParameterProviderFor(req);
        final NodeData data = pp.getParameter();
        assertTrue(((NodeDataWrapper) data).getWrappedNodeData() instanceof NonExistingNodeData);
        verify(req);
    }

    /** TODO this currently ends up in a miserable NPE - see MAGNOLIA-2745 */
    @Ignore
    @Test
    public void testYieldsAProperExceptionIfGeneratorNameIsOmmitted() throws Exception {
        expect(req.getPathInfo()).andReturn("/website/some/node/chalala");
        replay(req);
        final NodeDataParameterProviderFactory factory = new NodeDataParameterProviderFactory();
        try {
            final ParameterProvider<NodeData> pp = factory.newParameterProviderFor(req);
            final NodeData data = pp.getParameter();
        } catch (Throwable t) {
            t.printStackTrace();
            // since "website" would be the generator name:
            assertEquals("No repository mapped to 'some'", t.getMessage());
        }
        verify(req);
    }

    @Test
    public void testExistingPropertyJustWorks() throws RepositoryException {
        expect(req.getPathInfo()).andReturn("/generator/website/some/node/chalala");
        replay(req);
        final NodeDataParameterProviderFactory factory = new NodeDataParameterProviderFactory();
        final ParameterProvider<NodeData> pp = factory.newParameterProviderFor(req);
        final NodeData data = pp.getParameter();
        verify(req);
    }
}
