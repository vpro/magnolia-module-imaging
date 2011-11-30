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

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.AbstractRepositoryTestCase;
import info.magnolia.imaging.ParameterProvider;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
/**
 * @version $Id$
 */
public class ContentParameterProviderFactoryRepositoryTest extends AbstractRepositoryTestCase {

    @Test
    public void testWrapsContentSuchThatEqualsAndHashCodeAreImplementedBasedOnPathAndHierarchyManagerName() throws RepositoryException {
        final HierarchyManager hm = MgnlContext.getHierarchyManager("website");
        ContentUtil.createPath(hm, "/some/node");
        assertNotNull(hm.getContent("/some/node"));
        assertNotSame(hm.getContent("/some/node"), hm.getContent("/some/node"));
        assertFalse(hm.getContent("/some/node").equals(hm.getContent("/some/node")));

        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getPathInfo()).thenReturn("/generator/website/some/node");
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
