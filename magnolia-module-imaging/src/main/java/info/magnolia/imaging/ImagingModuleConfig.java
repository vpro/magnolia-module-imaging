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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImagingModuleConfig<P extends ParameterStrategy<?>> {
    private final Map<String, ImageOperation<P>> operations = new LinkedHashMap<String, ImageOperation<P>>();

    public void addOperation(String name, ImageOperation<P> operation) {
        operations.put(name, operation);
    }

    public Map<String, ImageOperation<P>> getOperations() {
        return operations;
    }

}
