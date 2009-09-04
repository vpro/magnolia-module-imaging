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

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.util.ContentWrapper;
import info.magnolia.context.Context;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.ParameterProvider;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ContentParameterProviderFactoryTest extends TestCase {
    protected void tearDown() throws Exception {
        MgnlContext.setInstance(null);
        super.tearDown();
    }

    public void testExtensionIsIgnored() throws Exception {
        validPathsTests("/myGenerator/dummyWS/foo/bar/baz.jpg", "dummyWS", "/foo/bar/baz");
    }

    public void testExtensionIsIgnoredAlsoForTopLevelNode() throws Exception {
        validPathsTests("/myGenerator/dummyWS/foo.jpg", "dummyWS", "/foo");
    }

    public void testDeepNodeIsRetrieved() throws Exception {
        validPathsTests("/myGenerator/dummyWS/foo/bar/baz", "dummyWS", "/foo/bar/baz");
    }

    public void testNodeAtFirstLevelIsRetrievedToo() throws Exception {
        validPathsTests("/myGenerator/dummyWS/foo", "dummyWS", "/foo");
    }

    public void testCanGetTheRootNodeIfNeededButWhoWantsThis() throws Exception {
        validPathsTests("/myGenerator/dummyWS", "dummyWS", "/");
    }

    private void validPathsTests(final String pathInfo, final String expectedWorkspaceName, final String expectedNodePath) throws RepositoryException {
        final Context ctx = createStrictMock(Context.class);
        final HierarchyManager hm = createStrictMock(HierarchyManager.class);
        final Content mockNode = createStrictMock(Content.class);
        final HttpServletRequest req = createStrictMock(HttpServletRequest.class);
        MgnlContext.setInstance(ctx);

        expect(req.getPathInfo()).andReturn(pathInfo);
        expect(ctx.getHierarchyManager(expectedWorkspaceName)).andReturn(hm);
        expect(hm.getContent(expectedNodePath)).andReturn(mockNode);
        expect(mockNode.getHandle()).andReturn("/does/not/matter"); // see SimpleEqualityContentWrapper

        replay(ctx, hm, mockNode, req);
        final ContentParameterProviderFactory f = new ContentParameterProviderFactory();
        final ParameterProvider<Content> pp = f.newParameterProviderFor(req);
        final Content result = pp.getParameter();
        // we're only checking if the ParameterProvider indeed returns our mock, just
        // so we can ensure it wasn't tempered with (i.e that the ParameterProvider did not call any unexpected method on it)
        // now that we know its wrapped, we also check that, and assertEquals against the unwrapped instance
        assertTrue(result instanceof SimpleEqualityContentWrapper);
        assertEquals(mockNode, ((ContentWrapper)result).getWrappedContent());
        verify(ctx, hm, mockNode, req);
    }

    public void testFailsIfNoPathInfo() throws Exception {
        failureTestForIncompletePaths(null, "Can't determine node, no pathInfo is available for uri /chalala");
    }

    public void testFailsIfWorkspaceNorNodePath() throws Exception {
        failureTestForIncompletePaths("/dummy", "Can't determine node from pathInfo: /dummy");
    }

    private void failureTestForIncompletePaths(final String pathInfo, final String expectedExceptionMsg) {
        final HttpServletRequest req = createStrictMock(HttpServletRequest.class);
        expect(req.getPathInfo()).andReturn(pathInfo);
        expect(req.getRequestURI()).andReturn("/chalala" + (pathInfo == null ? "" : pathInfo)).anyTimes();

        replay(req);
        final ContentParameterProviderFactory f = new ContentParameterProviderFactory();
        try {
            f.newParameterProviderFor(req);
            fail("should have failed");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedExceptionMsg, e.getMessage());
        }
        verify(req);
    }
}
