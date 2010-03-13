/**
 * This file Copyright (c) 2009-2010 Magnolia International
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
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;

import javax.jcr.RepositoryException;


/**
 * @author pbracher
 * @version $Id$
 *
 */
public class FromContent extends AbstractFromContent<Content> {

    private String propertyName = "binary";

    protected NodeData getNodeData(ParameterProvider<Content> param) throws ImagingException {
        try {
            final Content node = param.getParameter();
            if (!node.hasNodeData(propertyName)) {
                throw new ImagingException("There is no '" + propertyName + "' property at " + node.getHandle());
            }
            return node.getNodeData(propertyName);
        } catch (RepositoryException e) {
            throw new RuntimeException(e); // TODO - MAGNOLIA-2746
        }
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

}
