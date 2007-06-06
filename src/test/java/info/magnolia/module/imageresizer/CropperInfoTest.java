/**
 *
 * Magnolia and its source-code is licensed under the LGPL.
 * You may copy, adapt, and redistribute this file for commercial or non-commercial use.
 * When copying, adapting, or redistributing this document in keeping with the guidelines above,
 * you are required to provide proper attribution to obinary.
 * If you reproduce or distribute the document without making any substantive modifications to its content,
 * please use the following attribution line:
 *
 * Copyright 1993-2006 obinary Ltd. (http://www.obinary.com) All rights reserved.
 *
 */
package info.magnolia.module.imageresizer;

import junit.framework.TestCase;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropperInfoTest extends TestCase {
    public void testCalculatesWidthAndHeightProperly() {
        final CropperInfo.Coords ci = new CropperInfo.Coords();
        ci.setX1(10);
        ci.setX2(20);
        ci.setY1(30);
        ci.setY2(50);

        assertEquals(10, ci.getWidth());
        assertEquals(20, ci.getHeight());
    }

    public void testThrowsExceptionWhenCalculatingWidthAndHeightWithInvalidCoordinates() {
        final CropperInfo.Coords ci = new CropperInfo.Coords();
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
