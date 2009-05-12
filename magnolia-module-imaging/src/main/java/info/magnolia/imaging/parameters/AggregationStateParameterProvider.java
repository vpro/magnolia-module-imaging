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

import info.magnolia.cms.core.AggregationState;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.ParameterProvider;

/**
 * A ParameterProvider which provides the AggregationState as a parameter; TODO -currently not used nor having
 * a corresponding ParameterProviderFactory.
 * 
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class AggregationStateParameterProvider implements ParameterProvider<AggregationState> {

    public AggregationState getParameter() {
        return MgnlContext.getAggregationState();
    }

}
