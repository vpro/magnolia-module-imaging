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
package info.magnolia.imaging.util;

import junit.framework.TestCase;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class PathSplitterTest extends TestCase {

    public void testEmptyStringYieldsNoResults() {
        assertEquals(0, new PathSplitter("").count());
        // unless we'd start checking the position against the elements array length and return null, this can't work:
        /*assertEquals("", new PathSplitter("").next());
        assertEquals("", new PathSplitter("").skipTo(0));
        assertEquals("", new PathSplitter("").remaining());*/
    }

    public void testNullsAreHandledGracefullyCauseWeAreThatNice() {
        assertEquals(0, new PathSplitter(null).count());
        // unless we'd start checking the position against the elements array length and return null, this can't work:
        /*assertEquals("", new PathSplitter(null).next());
        assertEquals("", new PathSplitter(null).skipTo(0));
        assertEquals("", new PathSplitter(null).remaining());*/
    }

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

    public void testEmptyElementsMatter() {
        final PathSplitter ps = new PathSplitter("/foo//bar/");
        assertEquals(3, ps.count());
        assertEquals("foo", ps.skipTo(0));
        assertEquals("", ps.skipTo(1));
        assertEquals("bar", ps.skipTo(2));
    }

    // although maybe this isn't desired ?
    public void testEmptyElementsBeforeTrailingSlashWorkToo() {
        final PathSplitter ps = new PathSplitter("/foo/bar//");
        assertEquals(3, ps.count());
        assertEquals("foo", ps.skipTo(0));
        assertEquals("bar", ps.skipTo(1));
        assertEquals("", ps.skipTo(2));
    }

    // although maybe this isn't desired ?
    public void testEmptyElementsAfterLeadingSlashWorkToo() {
        final PathSplitter ps = new PathSplitter("//foo/bar/");
        assertEquals(3, ps.count());
        assertEquals("", ps.skipTo(0));
        assertEquals("foo", ps.skipTo(1));
        assertEquals("bar", ps.skipTo(2));
    }

    public void testDocumentedExample() {
        final PathSplitter ps = new PathSplitter("/foo/bar/baz/a/b/c/d/e");
        assertEquals(8, ps.count());
        assertEquals("baz", ps.skipTo(2));
        assertEquals("a", ps.next());
        assertEquals("b/c/d/e", ps.remaining());
    }

    public void testRemainingCanBeCalledFromTheStartEvenIfThisSeemsQuiteUseless() {
        assertEquals("foo/bar/baz/a/b/c/d/e", new PathSplitter("/foo/bar/baz/a/b/c/d/e").remaining());
    }

    public void testRemainingCanBeCalledEvenIfWeAlreadyReachedTheLastElement() {
        final PathSplitter ps = new PathSplitter("/foo/bar");
        assertEquals("bar", ps.skipTo(1));
        assertEquals("", ps.remaining());
    }

    public void testExtensionIsTrimmedByDefault() {
        final PathSplitter ps = new PathSplitter("/foo/bar.html");
        assertEquals(2, ps.count());
        assertEquals("bar", ps.skipTo(1));
        assertEquals("", ps.remaining());
    }

    public void testExtensionCanBeKeptAndIsNotCountingAsAnElement() {
        final PathSplitter ps = new PathSplitter("/foo/bar.html", '/', false);
        assertEquals(2, ps.count());
        assertEquals("foo", ps.next());
        assertEquals("bar.html", ps.next());
        assertEquals("", ps.remaining());
    }

    /* TODO
    public void testThereIsCertainlyABugIfADotIsPresentBeforeTheLastSlash() {
        final PathSplitter ps = new PathSplitter("/foo.html/bar", '/', true);
        assertEquals(2, ps.count());
        assertEquals("foo.html", ps.next());
        assertEquals("bar", ps.next());
        assertEquals("", ps.remaining());
    }
    */

    public void testNextAndSkipCallsDontOverlapEachOther() {
        final PathSplitter ps = new PathSplitter("/foo/bar");
        assertEquals("foo", ps.next());
        assertEquals("bar", ps.skipTo(1));
        assertEquals("", ps.remaining());
    }

}
