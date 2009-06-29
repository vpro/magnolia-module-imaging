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
package info.magnolia.imaging.operations.text;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public enum HorizontalAlignment implements Alignment {
    left {
        public float getPositionFor(double content, double container, double delta) {
            return (float) delta;
        }},
    /** delta is ignored for center. */
    center {
        public float getPositionFor(double content, double container, double delta) {
            return (float) (container / 2) - (float) (content / 2);
        }},

    right {
        public float getPositionFor(double content, double container, double delta) {
            return (float) (container - content - delta);
        }}
}