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

import info.magnolia.imaging.ParameterProvider;


/**
 * @author pbracher
 * @version $Id$
 *
 */
public interface WorkspaceAndPathBasedParameterProvider<P> extends ParameterProvider<P> {
    
    public String getWorkspaceName();
    
    public String getPath();
}
