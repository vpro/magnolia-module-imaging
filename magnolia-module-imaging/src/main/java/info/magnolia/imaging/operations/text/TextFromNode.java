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
package info.magnolia.imaging.operations.text;

import info.magnolia.cms.core.Content;
import info.magnolia.imaging.parameters.ContentParameterProvider;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class TextFromNode extends AbstractTextOverlay<ContentParameterProvider> {
    private String propertyName = "text";

    protected String getText(ContentParameterProvider filterParams) {
        final Content node = filterParams.getParameter();
        return node.getNodeData(propertyName).getString();
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
