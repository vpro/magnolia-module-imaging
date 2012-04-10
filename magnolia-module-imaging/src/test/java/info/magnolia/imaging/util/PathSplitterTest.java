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
package info.magnolia.imaging.util;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
/**
 *
 * @version $Id$
 */
public class PathSplitterTest {

    @Test
    public void testEmptyStringYieldsNoResults() {
        assertEquals(0, new PathSplitter("").count());
        // unless we'd start checking the position against the elements array length and return null, this can't work:
        /*assertEquals("", new PathSplitter("").next());
        assertEquals("", new PathSplitter("").skipTo(0));
        assertEquals("", new PathSplitter("").remaining());*/
    }

    @Test
    public void testNullsAreHandledGracefullyCauseWeAreThatNice() {
        assertEquals(0, new PathSplitter(null).count());
        // unless we'd start checking the position against the elements array length and return null, this can't work:
        /*assertEquals("", new PathSplitter(null).next());
        assertEquals("", new PathSplitter(null).skipTo(0));
        assertEquals("", new PathSplitter(null).remaining());*/
    }

    @Test
    public void testLeadingAndTralingslahesAreIgnored() {
        PathSplitter ps = new PathSplitter("/foo/bar");
        assertEquals(2, ps.count());
        assertEquals("foo", ps.next());
        assertEquals("bar", ps.next());

        ps = new PathSplitter("/foo/bar/");
        assertEquals(2, ps.count());
        assertEquals("foo", ps.next());
        assertEquals("bar", ps.next());

        ps = new PathSplitter("foo/bar/");
        assertEquals(2, ps.count());
        assertEquals("foo", ps.next());
        assertEquals("bar", ps.next());
    }

    @Test
    public void testEmptyElementsMatter() {
        final PathSplitter ps = new PathSplitter("/foo//bar/");
        assertEquals(3, ps.count());
        assertEquals("foo", ps.skipTo(0));
        assertEquals("", ps.skipTo(1));
        assertEquals("bar", ps.skipTo(2));
    }

    // although maybe this isn't desired ?
    @Test
    public void testEmptyElementsBeforeTrailingSlashWorkToo() {
        final PathSplitter ps = new PathSplitter("/foo/bar//");
        assertEquals(3, ps.count());
        assertEquals("foo", ps.skipTo(0));
        assertEquals("bar", ps.skipTo(1));
        assertEquals("", ps.skipTo(2));
    }

    // although maybe this isn't desired ?
    @Test
    public void testEmptyElementsAfterLeadingSlashWorkToo() {
        final PathSplitter ps = new PathSplitter("//foo/bar/");
        assertEquals(3, ps.count());
        assertEquals("", ps.skipTo(0));
        assertEquals("foo", ps.skipTo(1));
        assertEquals("bar", ps.skipTo(2));
    }

    @Test
    public void testDocumentedExample() {
        final PathSplitter ps = new PathSplitter("/foo/bar/baz/a/b/c/d/e");
        assertEquals(8, ps.count());
        assertEquals("baz", ps.skipTo(2));
        assertEquals("a", ps.next());
        assertEquals("b/c/d/e", ps.remaining());
    }

    @Test
    public void testRemainingCanBeCalledFromTheStartEvenIfThisSeemsQuiteUseless() {
        assertEquals("foo/bar/baz/a/b/c/d/e", new PathSplitter("/foo/bar/baz/a/b/c/d/e").remaining());
    }

    @Test
    public void testRemainingCanBeCalledEvenIfWeAlreadyReachedTheLastElement() {
        final PathSplitter ps = new PathSplitter("/foo/bar");
        assertEquals("bar", ps.skipTo(1));
        assertEquals("", ps.remaining());
    }

    @Test
    public void testExtensionIsTrimmedByDefault() {
        final PathSplitter ps = new PathSplitter("/foo/bar.html");
        assertEquals(2, ps.count());
        assertEquals("bar", ps.skipTo(1));
        assertEquals("", ps.remaining());
    }

    @Test
    public void testExtensionCanBeKeptAndIsNotCountingAsAnElement() {
        final PathSplitter ps = new PathSplitter("/foo/bar.html", '/', false);
        assertEquals(2, ps.count());
        assertEquals("foo", ps.next());
        assertEquals("bar.html", ps.next());
        assertEquals("", ps.remaining());
    }

    @Ignore("reactivate when solving MGNLIMG-95")
    @Test
    public void testDotsInPathNotAtTheEndAreNotTrimmed() {
        final PathSplitter ps = new PathSplitter("/foo.something/bar", '/', true);
        assertEquals(2, ps.count());
        assertEquals("foo.something", ps.next());
        assertEquals("bar", ps.next());
        assertEquals("", ps.remaining());
    }

    @Test
    public void testNextAndSkipCallsDontOverlapEachOther() {
        final PathSplitter ps = new PathSplitter("/foo/bar");
        assertEquals("foo", ps.next());
        assertEquals("bar", ps.skipTo(1));
        assertEquals("", ps.remaining());
    }

}
