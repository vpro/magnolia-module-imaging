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
package info.magnolia.imaging.operations.load;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ParameterProvider;


/**
 * @author pbracher
 * @version $Id$
 *
 */
public class FromNode extends AbstractFromContent<Content> {

    private String propertyName = "binary";

    protected NodeData getNodeData(ParameterProvider<Content> filterParams) {
        final Content node = filterParams.getParameter();
        return node.getNodeData(propertyName);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

}
