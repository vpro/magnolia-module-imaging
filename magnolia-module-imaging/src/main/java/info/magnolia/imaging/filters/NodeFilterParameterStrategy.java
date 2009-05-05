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
package info.magnolia.imaging.filters;

import info.magnolia.cms.core.Content;
import info.magnolia.context.MgnlContext;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class NodeFilterParameterStrategy implements FilterParameterStrategy<Content> {
    public Content getParameter() {
        return MgnlContext.getAggregationState().getCurrentContent();
    }
}
