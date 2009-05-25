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
package info.magnolia.imaging.operations.load;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.AbstractRepositoryTestCase;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.ParameterProviderFactory;
import info.magnolia.imaging.caching.CachingStrategy;
import info.magnolia.imaging.caching.ContentBasedCachingStrategy;
import info.magnolia.imaging.parameters.ContentParameterProvider;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class FromContentTest extends AbstractRepositoryTestCase {
    private Content src;

    protected void setUp() throws Exception {
        super.setUp();
        final HierarchyManager srcHM = MgnlContext.getHierarchyManager("website");
        src = ContentUtil.createPath(srcHM, "/some/node");
        src.createNodeData("chalala", "tralala");
        srcHM.save();
    }

    public void testNonBinaryPropertyYieldsAProperException() throws Exception {
        final ParameterProviderFactory<Object, Content> ppf = new TestParameterProviderFactory(src);

        final FromContent op = new FromContent();
        op.setPropertyName("chalala");

        try {
            op.apply(null, ppf.newParameterProviderFor(null));
            fail();
        } catch (ImagingException e) {
            assertEquals("Nodedata /some/node/chalala doesn't exist or is not of type binary.", e.getMessage());
        }
    }

    public void testUnexistingPropertyYieldsAProperException() throws Exception {
        final ParameterProviderFactory<Object, Content> ppf = new TestParameterProviderFactory(src);

        final FromContent op = new FromContent();
        op.setPropertyName("foobar");

        try {
            op.apply(null, ppf.newParameterProviderFor(null));
            fail();
        } catch (ImagingException e) {
            assertEquals("There is no property named foobar at /some/node", e.getMessage());
        }
    }

    private class TestParameterProviderFactory implements ParameterProviderFactory<Object, Content> {
        private final Content src;

        public TestParameterProviderFactory(final Content src) {
            this.src = src;
        }

        public ParameterProvider<Content> newParameterProviderFor(Object environment) {
            return new ContentParameterProvider(src);
        }

        public CachingStrategy getCachingStrategy() {
            return new ContentBasedCachingStrategy();
        }
    }
}
