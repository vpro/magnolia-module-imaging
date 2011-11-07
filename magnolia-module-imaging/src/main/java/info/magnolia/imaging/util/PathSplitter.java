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
