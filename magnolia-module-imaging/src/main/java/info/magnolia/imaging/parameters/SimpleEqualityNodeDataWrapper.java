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

import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.NodeDataWrapper;

/**
 * A NodeDataWrapper which simply defines equals() and hashCode() methods
 * based on the property's path and HierarchyManager name.
 */
public class SimpleEqualityNodeDataWrapper extends NodeDataWrapper {
    public SimpleEqualityNodeDataWrapper(NodeData nodeData) {
        super(nodeData);
    }

    public boolean equals(Object o) {
        return isSame((NodeData) o);
    }

    private boolean isSame(NodeData other) {
        return getHierarchyManager().getName().equals(other.getHierarchyManager().getName())
                && getHandle().equals(other.getHandle());
    }

    public int hashCode() {
        return (31 * getHandle().hashCode()) + getHierarchyManager().getName().hashCode();
    }
}
