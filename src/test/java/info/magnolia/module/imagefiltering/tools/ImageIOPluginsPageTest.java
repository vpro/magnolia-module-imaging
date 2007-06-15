/**
 *
 * Magnolia and its source-code is licensed under the LGPL.
 * You may copy, adapt, and redistribute this file for commercial or non-commercial use.
 * When copying, adapting, or redistributing this document in keeping with the guidelines above,
 * you are required to provide proper attribution to obinary.
 * If you reproduce or distribute the document without making any substantive modifications to its content,
 * please use the following attribution line:
 *
 * Copyright 1993-2006 obinary Ltd. (http://www.obinary.com) All rights reserved.
 *
 */
package info.magnolia.module.imagefiltering.tools;

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
         final String[] s = new String[]{
                "png", "JPG", "PNG", "jpg", "fOo"
        };
        final Collection res = ImageIOPluginsPage.filter(s);
        assertEquals(3, res.size());
        final Iterator it = res.iterator();
        assertEquals("foo", it.next());
        assertEquals("jpg", it.next());
        assertEquals("png", it.next());
        assertFalse(it.hasNext());
    }
}
