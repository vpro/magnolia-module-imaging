/**
 * This file Copyright (c) 2007-2009 Magnolia International
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
package info.magnolia.module.imaging.cropresize;

import junit.framework.TestCase;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CoordsTest extends TestCase {
    public void testCalculatesWidthAndHeightProperly() {
        final Coords ci = new Coords();
        ci.setX1(10);
        ci.setX2(20);
        ci.setY1(30);
        ci.setY2(50);

        assertEquals(10, ci.getWidth());
        assertEquals(20, ci.getHeight());
    }

    public void testThrowsExceptionWhenCalculatingWidthAndHeightWithInvalidCoordinates() {
        final Coords ci = new Coords();
        ci.setX1(10);
        ci.setX2(10);
        ci.setY1(30);
        ci.setY2(20);

        try {
            ci.getWidth();
            fail("should have failed");
        } catch (IllegalStateException e) {
            assertEquals("Invalid coordinates, negative width: x1 = 10, x2 = 10", e.getMessage());
        }
        try {
            ci.getHeight();
            fail("should have failed");
        } catch (IllegalStateException e) {
            assertEquals("Invalid coordinates, negative height: y1 = 30, y2 = 20", e.getMessage());
        }
    }

}
