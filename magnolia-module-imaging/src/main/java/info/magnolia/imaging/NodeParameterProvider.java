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
package info.magnolia.imaging;

import info.magnolia.cms.core.Content;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class NodeParameterProvider implements ParameterProvider<Content> {
    private final Content node;

    // TODO subclass or other impl that uses the AggCtx

    // TODO -- if we have specific constructors, either our simple factory isn't extensible,
    // or we need a factory per strategy
    // or the ctor of the strategy acts as factory 
    // -- how do we determine/decide which strategy to use ?
    // --> the ImagingFilter (root of chain)
    public NodeParameterProvider(Content node) {
        this.node = node;
    }

    public Content getParameter() {
        return node;
//        return MgnlContext.getAggregationState().getCurrentContent();
    }
}
