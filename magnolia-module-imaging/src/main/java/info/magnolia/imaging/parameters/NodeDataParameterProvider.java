/**
 * This file Copyright (c) 2009-2011 Magnolia International
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
import info.magnolia.imaging.ParameterProvider;


/**
 * @author pbracher
 * @version $Id$
 *
 */
public class NodeDataParameterProvider implements ParameterProvider<NodeData> {
    private final NodeData nd;

    public NodeDataParameterProvider(NodeData nd) {
        this.nd = nd;
    }

    public NodeData getParameter() {
        return nd;
    }

}
