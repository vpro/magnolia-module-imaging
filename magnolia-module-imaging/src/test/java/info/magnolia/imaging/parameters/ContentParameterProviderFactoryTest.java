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

import static org.mockito.Mockito.*;
import info.magnolia.cms.core.Content;
import static org.junit.Assert.*;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.util.ContentWrapper;
import info.magnolia.context.Context;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.ParameterProvider;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Test;

/**
 * @version $Id$
 */
public class ContentParameterProviderFactoryTest {
    @After
    public void tearDown() throws Exception {
        MgnlContext.setInstance(null);
    }

    @Test
    public void testExtensionIsIgnored() throws Exception {
        validPathsTests("/myGenerator/dummyWS/foo/bar/baz.jpg", "dummyWS", "/foo/bar/baz");
    }

    @Test
    public void testExtensionIsIgnoredAlsoForTopLevelNode() throws Exception {
        validPathsTests("/myGenerator/dummyWS/foo.jpg", "dummyWS", "/foo");
    }

    @Test
    public void testDeepNodeIsRetrieved() throws Exception {
        validPathsTests("/myGenerator/dummyWS/foo/bar/baz", "dummyWS", "/foo/bar/baz");
    }

    @Test
    public void testNodeAtFirstLevelIsRetrievedToo() throws Exception {
        validPathsTests("/myGenerator/dummyWS/foo", "dummyWS", "/foo");
    }

    @Test
    public void testCanGetTheRootNodeIfNeededButWhoWantsThis() throws Exception {
        validPathsTests("/myGenerator/dummyWS", "dummyWS", "/");
    }

    private void validPathsTests(final String pathInfo, final String expectedWorkspaceName, final String expectedNodePath) throws RepositoryException {
        final Context ctx = mock(Context.class);
        final HierarchyManager hm = mock(HierarchyManager.class);
        final Content mockNode = mock(Content.class);
        final HttpServletRequest req = mock(HttpServletRequest.class);
        MgnlContext.setInstance(ctx);

        when(req.getPathInfo()).thenReturn(pathInfo);
        when(ctx.getHierarchyManager(expectedWorkspaceName)).thenReturn(hm);
        when(hm.getContent(expectedNodePath)).thenReturn(mockNode);
        when(mockNode.getHandle()).thenReturn("/does/not/matter"); // see SimpleEqualityContentWrapper

        final ContentParameterProviderFactory f = new ContentParameterProviderFactory();
        final ParameterProvider<Content> pp = f.newParameterProviderFor(req);
        final Content result = pp.getParameter();
        // we're only checking if the ParameterProvider indeed returns our mock, just
        // so we can ensure it wasn't tempered with (i.e that the ParameterProvider did not call any unexpected method on it)
        // now that we know its wrapped, we also check that, and assertEquals against the unwrapped instance
        assertTrue(result instanceof SimpleEqualityContentWrapper);
        assertEquals(mockNode, ((ContentWrapper)result).getWrappedContent());
    }

    @Test
    public void testFailsIfNoPathInfo() throws Exception {
        failureTestForIncompletePaths(null, "Can't determine node, no pathInfo is available for uri /chalala");
    }

    @Test
    public void testFailsIfWorkspaceNorNodePath() throws Exception {
        failureTestForIncompletePaths("/dummy", "Can't determine node from pathInfo: /dummy");
    }

    private void failureTestForIncompletePaths(final String pathInfo, final String expectedExceptionMsg) {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getPathInfo()).thenReturn(pathInfo);
        when(req.getRequestURI()).thenReturn("/chalala" + (pathInfo == null ? "" : pathInfo));

        final ContentParameterProviderFactory f = new ContentParameterProviderFactory();
        try {
            f.newParameterProviderFor(req);
            fail("should have failed");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedExceptionMsg, e.getMessage());
        }
    }
}
