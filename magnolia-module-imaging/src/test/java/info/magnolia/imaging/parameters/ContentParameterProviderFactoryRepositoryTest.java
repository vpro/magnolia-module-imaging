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
package info.magnolia.imaging.parameters;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
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
public class ContentParameterProviderFactoryRepositoryTest extends AbstractRepositoryTestCase {
    public void testWrapsContentSuchThatEqualsAndHashCodeAreImplementedBasedOnPathAndHierarchyManagerName() throws RepositoryException {
        final HierarchyManager hm = MgnlContext.getHierarchyManager("website");
        ContentUtil.createPath(hm, "/some/node");
        assertNotNull(hm.getContent("/some/node"));
        assertNotSame(hm.getContent("/some/node"), hm.getContent("/some/node"));
        assertFalse(hm.getContent("/some/node").equals(hm.getContent("/some/node")));

        final HttpServletRequest req = createStrictMock(HttpServletRequest.class);
        expect(req.getPathInfo()).andReturn("/generator/website/some/node").times(2);
        replay(req);
        final ContentParameterProviderFactory factory = new ContentParameterProviderFactory();
        final ParameterProvider<Content> param1 = factory.newParameterProviderFor(req);
        final ParameterProvider<Content> param2 = factory.newParameterProviderFor(req);
        assertNotSame("2 calls to ParameterProviderFactory should return 2 *different instances*.",
                param1.getParameter(), param2.getParameter());
        assertEquals("2 calls to ParameterProviderFactory should return 2 *equivalent instances*.",
                param1.getParameter(), param2.getParameter());
        verify(req);
    }
}
