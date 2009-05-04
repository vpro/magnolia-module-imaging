/**
 * This file Copyright (c) 2007-2009 Magnolia International
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
package info.magnolia.module.imaging.tools;

import junit.framework.TestCase;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageIOPluginsPageTest extends TestCase {
    public void testFilterDuplicatesAndReturnsLowercase() {
        final Collection<String> res = ImageIOPluginsPage.filter("png", "JPG", "PNG", "jpg", "fOo");
        assertEquals(3, res.size());
        final Iterator<String> it = res.iterator();
        assertEquals("foo", it.next());
        assertEquals("jpg", it.next());
        assertEquals("png", it.next());
        assertFalse(it.hasNext());
    }
}
