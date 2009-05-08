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

import org.apache.commons.lang.StringUtils;

/**
 * Splits a String representation path where elements are separated by '/'.
 * Leading and trailing separators are ignored. Empty elements are not
 * ("/a//c/" as three elements, "a", "" and "c")
 *
 * This keeps an internal iterator, such that for an input of "/foo/bar/baz/a/b/c/d/e",
 * following sequence of calls:
 * <ul>
 * <li>skipTo(2) : return "baz"
 * <li>next() : return "a"
 * <li>remaining() : "b/c/d/e"
 * </ul>
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class PathSplitter {
    private static final String[] EMPTY = new String[0];

    private final char separator;
    //private final String source;
    private final String[] pathElements;
    private int position;

    /**
     * Splits with the '/' character (NOT the system-specific file separator) and trims extensions.
     */
    public PathSplitter(String source) {
        this(source, true);
    }

    /**
     * Splits with the '/' character (NOT the system-specific file separator).
     */
    public PathSplitter(String source, boolean trimExtension) {
        this(source, '/', trimExtension);
    }

    public PathSplitter(String source, char separator, boolean trimExtension) {
        this.separator = separator;
        final String sep = String.valueOf(separator);
        if (source == null) {
            source = "";
        }
        if (source.startsWith(sep)) {
            source = source.substring(1);
        }
        if (source.endsWith(sep)) {
            source = source.substring(0, source.length() - 1);
        }
        // trim extension
        final int dot = source.lastIndexOf('.');
        if (trimExtension && dot >= 0) {
            source = source.substring(0, dot);
        }

        // this.source = source;
        this.pathElements = source.length() == 0 ? EMPTY : source.split(sep, -1); // include trailing empty matches
        this.position = -1;
    }

    /**
     * Returns the number of path elements in the source string.
     */
    public int count() {
        return pathElements.length;
    }

    /**
     * Returns the next path element from the internal iterator.
     */
    public String next() {
        return pathElements[++position];
    }

    /**
     * Returns the element at the given 0-based index.
     * @throws ArrayIndexOutOfBoundsException
     */
    public String skipTo(int index) {
        position = index;
        return pathElements[position];
    }

    /**
     * Returns all the elements lefts after the last call to at() or next(),
     * *without* a leading separator. This returns an empty string if there
     * are not remaining elements.
     * Calls to next(), skipTo() or remaining() after a call to remaining will
     * lead to unexpected results.
     */
    public String remaining() {
        position++;
        return StringUtils.join(pathElements, separator, position, pathElements.length);
    }
}
