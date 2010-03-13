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
package info.magnolia.imaging;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class StringParameterProvider implements ParameterProvider<String> {
    private final String string;

    public StringParameterProvider(final String string) {
        this.string = string;
    }

    public String getParameter() {
        return string;
    }
}
