/**
 * This file Copyright (c) 2009-2012 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.imaging.util;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

/**
 * TODO : accept formats such as "255, 0, 0" ?
 *
 * @version $Id$
 */
public class ColorConverterTest {

    @Test
    public void testCanDecodeHexa() {
        assertEquals(Color.white, ColorConverter.toColor("#ffffff"));
        assertEquals(Color.black, ColorConverter.toColor("#000000"));
        assertEquals(Color.red, ColorConverter.toColor("#ff0000"));
        assertEquals(new Color(128, 64, 23), ColorConverter.toColor("#804017"));
    }

    @Test
    public void testCanDecodeNames() {
        assertEquals(Color.white, ColorConverter.toColor("white"));
        assertEquals(Color.black, ColorConverter.toColor("Black"));
        assertEquals(Color.red, ColorConverter.toColor("RED"));
        assertEquals(Color.yellow, ColorConverter.toColor("yelLoW"));
    }

    @Test
    public void testFailsOnUnknownName() {
        try {
            ColorConverter.toColor("foo");
            fail("should have failed");
        } catch (IllegalArgumentException e) {
            assertEquals("Can't decode color: foo. Please provide either an #ffffff hexadecimal value or a known named color.", e.getMessage());
        }
    }

}
