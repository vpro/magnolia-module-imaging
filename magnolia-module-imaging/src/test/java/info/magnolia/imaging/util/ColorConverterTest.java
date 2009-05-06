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
package info.magnolia.imaging.util;

import junit.framework.TestCase;

import java.awt.Color;

/**
 * TODO : accept formats such as "255, 0, 0" ?
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ColorConverterTest extends TestCase {
    public void testCanDecodeHexa() {
        assertEquals(Color.white, ColorConverter.toColor("#ffffff"));
        assertEquals(Color.black, ColorConverter.toColor("#000000"));
        assertEquals(Color.red, ColorConverter.toColor("#ff0000"));
        assertEquals(new Color(128, 64, 23), ColorConverter.toColor("#804017"));
    }

//    public void testCanDecodeHexaWithAlphaChannel() {
//        assertEquals(new Color(128, 64, 23, 35), ColorConverter.toColor("#80401723"));
//    }

    public void testCanDecodeNames() {
        assertEquals(Color.white, ColorConverter.toColor("white"));
        assertEquals(Color.black, ColorConverter.toColor("Black"));
        assertEquals(Color.red, ColorConverter.toColor("RED"));
        assertEquals(Color.yellow, ColorConverter.toColor("yelLoW"));
    }

    public void testFailsOnUnknownName() {
        try {
            ColorConverter.toColor("foo");
            fail("should have failed");
        } catch (IllegalArgumentException e) {
            assertEquals("Can't decode color: foo. Please provide either an #ffffff hexadecimal value or a known named color.", e.getMessage());
        }
    }

//    public void testCanEncodeToHexa() {
//        assertEquals("#0000ff", ColorConverter.toString(Color.blue));
//    }
}
