/**
 * This file Copyright (c) 2009-2011 Magnolia International
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

import info.magnolia.imaging.ParameterProvider;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class FixedText<P extends ParameterProvider<?>> extends AbstractTextOverlay<P> {
    private String text;

    protected String getText(P params) {
        // TODO : i18n
        return getText();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
