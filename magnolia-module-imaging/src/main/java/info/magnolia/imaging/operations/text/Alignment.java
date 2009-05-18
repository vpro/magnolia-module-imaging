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
public enum Alignment {
    left {
        float getPositionFor(double content, double container, double delta) {
            return (float) delta;
        }},
    top {
        float getPositionFor(double content, double container, double delta) {
            return (float) content + (float) delta;
        }},
    /** delta is ignored for center. */
    center {
        float getPositionFor(double content, double container, double delta) {
            return (float) (container / 2) - (float) (content / 2);
        }},
    right {
        float getPositionFor(double content, double container, double delta) {
            return (float) (container - content - delta);
        }},
    bottom {
        float getPositionFor(double content, double container, double delta) {
            return (float) (container - delta);
        }};

    abstract float getPositionFor(double content, double container, double delta);
}
