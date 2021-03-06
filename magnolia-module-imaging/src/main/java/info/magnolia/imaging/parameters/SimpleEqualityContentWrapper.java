/**
 * This file Copyright (c) 2009-2012 Magnolia International
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

import info.magnolia.cms.core.Content;
import info.magnolia.cms.util.ContentWrapper;

/**
 * A ContentWrapper which simply defines equals() and hashCode() methods
 * based on the node's path and HierarchyManager name.
 * It also caches the handle of the node, so that equals() and hashCode()
 * can be used even if the node's JCR session was terminated.
 *
 * @version $Id$
 */
public class SimpleEqualityContentWrapper extends ContentWrapper {
    private final String handle;

    public SimpleEqualityContentWrapper(Content node) {
        super(node);
        this.handle = node.getHandle();
    }

    @Override
    public String getHandle() {
        return handle;
    }

    @Override
    public boolean equals(Object o) {
        return isSame((Content) o);
    }

    private boolean isSame(Content other) {
        return getHierarchyManager().getName().equals(other.getHierarchyManager().getName())
                && getHandle().equals(other.getHandle());
    }

    @Override
    public int hashCode() {
        return (31 * getHandle().hashCode()) + getHierarchyManager().getName().hashCode();
    }
}
