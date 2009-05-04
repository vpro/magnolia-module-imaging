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

import info.magnolia.content2bean.TransformationState;
import info.magnolia.content2bean.TypeDescriptor;
import info.magnolia.content2bean.impl.Content2BeanTransformerImpl;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ColorContent2BeanTransformer extends Content2BeanTransformerImpl {
    protected TypeDescriptor onResolveType(TransformationState state, TypeDescriptor resolvedType) {
        return super.onResolveType(state, resolvedType);
    }
}
