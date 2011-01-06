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

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.AbstractRepositoryTestCase;
import info.magnolia.imaging.ParameterProvider;
import static org.easymock.EasyMock.*;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class NodeDataParameterProviderFactoryTest extends AbstractRepositoryTestCase {
    private NodeData srcProp;
    private HttpServletRequest req;

    protected void setUp() throws Exception {
        super.setUp();
        final HierarchyManager srcHM = MgnlContext.getHierarchyManager("website");
        final Content src = ContentUtil.createPath(srcHM, "/some/node");
        srcProp = src.createNodeData("chalala", "tralala");
        srcHM.save();

        req = createStrictMock(HttpServletRequest.class);
    }

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

    public void testYieldsAProperExceptionIfPropertyDoesNotExist() throws Exception {
        expect(req.getPathInfo()).andReturn("/generator/website/some/node/booyah");
        replay(req);
        final NodeDataParameterProviderFactory factory = new NodeDataParameterProviderFactory();
        try {
            final ParameterProvider<NodeData> pp = factory.newParameterProviderFor(req);
            final NodeData data = pp.getParameter();
            fail();
        } catch (RuntimeException t) {
            // TODO - fix type of exception
            assertEquals("Can't load source from /some/node/booyah from workspace website", t.getMessage());
        }
        verify(req);
    }

    /** TODO this currently ends up in a miserable NPE - see MAGNOLIA-2745
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
     }*/

    public void testExistingPropertyJustWorks() throws RepositoryException {
        expect(req.getPathInfo()).andReturn("/generator/website/some/node/chalala");
        replay(req);
        final NodeDataParameterProviderFactory factory = new NodeDataParameterProviderFactory();
        final ParameterProvider<NodeData> pp = factory.newParameterProviderFor(req);
        final NodeData data = pp.getParameter();
        verify(req);
    }
}
