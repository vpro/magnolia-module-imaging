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

import info.magnolia.content2bean.Content2BeanException;
import info.magnolia.content2bean.impl.Content2BeanTransformerImpl;
import info.magnolia.imaging.util.ColorConverter;

import java.awt.Color;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class TextStyleTransformer extends Content2BeanTransformerImpl {
    @Override
    public Object convertPropertyValue(Class propertyType, Object value) throws Content2BeanException {
        if (Color.class.equals(propertyType)) {
            if (!(value instanceof String)) {
                throw new Content2BeanException("Can only transform String values to java.awt.Color instances.");
            }
            return ColorConverter.toColor((String) value);
        }
        return super.convertPropertyValue(propertyType, value);
    }
}
