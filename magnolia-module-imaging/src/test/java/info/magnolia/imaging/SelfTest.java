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
package info.magnolia.imaging;

import info.magnolia.cms.beans.runtime.FileProperties;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.cms.util.NodeDataUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.caching.CachingStrategy;
import info.magnolia.imaging.caching.ContentBasedCachingStrategy;
import info.magnolia.imaging.parameters.ContentParameterProvider;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.jcr.PropertyType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @version $Id$
 */
public class SelfTest extends AbstractRepositoryTestCase {

    @Test
    public void testCanGetBinaryStreamOutOfTheSamePropertyInstanceTwice() throws Exception {
        final HierarchyManager hm = MgnlContext.getHierarchyManager("website");
        final Content content = ContentUtil.createPath(hm, "/foo/bar");
        final NodeData prop = NodeDataUtil.getOrCreate(content, "test", PropertyType.BINARY);

        final ByteArrayInputStream tempIn = new ByteArrayInputStream("HELLO".getBytes());
        prop.setValue(tempIn);
        prop.setAttribute(FileProperties.PROPERTY_CONTENTTYPE, "text/plain");
        prop.setAttribute(FileProperties.PROPERTY_LASTMODIFIED, Calendar.getInstance());
        hm.save();

        // -------
        final NodeData p = hm.getNodeData("/foo/bar/test");
        final InputStream in1 = p.getStream();
        final InputStream in2 = p.getStream();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in1, out);
        IOUtils.copy(in2, out); // outputs "HELLO" a 2nd time
        IOUtils.copy(in1, out); // outputs nothing, the whole stream has already been read
        assertEquals("HELLOHELLO", out.toString());
    }

    @Test
    public void testNodeParameterProviderHandlesTimestampsProperly() throws Exception {
        final HierarchyManager hm = MgnlContext.getHierarchyManager("website");

        final Content src = ContentUtil.createPath(hm, "/cha/lala", true);
        src.getMetaData().setCreationDate();
        src.getMetaData().setModificationDate();
        System.out.println("src.getMetaData().getModificationDate() = " + src.getMetaData().getModificationDate());
        TimeUnit.MILLISECONDS.sleep(3);

        // fake cache 3ms later
        final Content cachedNode = ContentUtil.createPath(hm, "/foo/bar", true);
        cachedNode.getMetaData().setCreationDate();
        cachedNode.getMetaData().setModificationDate();
        final NodeData cachedBinaryProp = NodeDataUtil.getOrCreate(cachedNode, "test", PropertyType.BINARY);

        final ByteArrayInputStream tempIn = new ByteArrayInputStream("HELLO".getBytes());
        cachedBinaryProp.setValue(tempIn);
        cachedBinaryProp.setAttribute(FileProperties.PROPERTY_CONTENTTYPE, "text/plain");
        cachedBinaryProp.setAttribute(FileProperties.PROPERTY_LASTMODIFIED, Calendar.getInstance());
        hm.save();

        final ContentParameterProvider pp = new ContentParameterProvider(src);
        final CachingStrategy<Content> cachingStrategy = new ContentBasedCachingStrategy();
        assertFalse(cachingStrategy.shouldRegenerate(cachedBinaryProp, pp));

        TimeUnit.MILLISECONDS.sleep(25);
        // fake updating the src node
        src.getMetaData().setModificationDate();
        assertTrue(cachingStrategy.shouldRegenerate(cachedBinaryProp, pp));
    }

}
