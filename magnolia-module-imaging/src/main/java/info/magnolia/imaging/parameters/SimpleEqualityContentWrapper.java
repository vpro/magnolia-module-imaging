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
package info.magnolia.imaging.parameters;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.util.ContentWrapper;

/**
 * A ContentWrapper which simply defines equals() and hashCode() methods
 * based on the node's path and HierarchyManager name.
 */
public class SimpleEqualityContentWrapper extends ContentWrapper {
    public SimpleEqualityContentWrapper(Content node) {
        super(node);
    }

    public boolean equals(Object o) {
        return isSame((Content) o);
    }

    private boolean isSame(Content other) {
        return getHierarchyManager().getName().equals(other.getHierarchyManager().getName())
                && getHandle().equals(other.getHandle());
    }

    public int hashCode() {
        return (31 * getHandle().hashCode()) + getHierarchyManager().getName().hashCode();
    }
}
