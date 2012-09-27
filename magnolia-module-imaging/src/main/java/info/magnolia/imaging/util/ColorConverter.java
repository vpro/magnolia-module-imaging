/**
 * This file Copyright (c) 2009-2011 Magnolia International
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

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.TreeMap;

/**
 * A class that is able to convert java.lang.String instances to java.awt.Color instances.
 * It recognizes multiple String representations, such as named colors (using the java.awt.Color constants),
 * hexadecimal, TODO -- "r:100,g:100,b:100,a:80", "100, 100, 100, 80", ...
 *
 * TODO - and vice-versa.
 *
 * @version $Id$
 */
public class ColorConverter {
    private static final Map<String, Color> namedColors = reflectivelyGetNamedColors();

    public static Color toColor(String s) {
        if (namedColors.containsKey(s)) {
            return namedColors.get(s);
        } else if (s.startsWith("#")) {
            return Color.decode(s);
        } else {
            throw new IllegalArgumentException("Can't decode color: " + s + ". Please provide either an #ffffff hexadecimal value or a known named color.");
        }
    }

    /**
     * Using reflection, gather all fields of java.awt.Color that are
     * static, public, final and of type java.awt.Color, and put them
     * in them in a case-insensitive Map.
     * Since we've already loaded the java.awt.Color, there is virtually
     * no hit on performance or memory usage, except for this very small
     * map.
     * Yes, this is a bit ugly, since we're relying on how those constants
     * are declared ... but it is not very likely to change, is it ?
     */
    private static Map<String, Color> reflectivelyGetNamedColors() {
        try {
            final Map<String, Color> namedColors = new TreeMap<String, Color>(String.CASE_INSENSITIVE_ORDER);
            final Field[] fields = Color.class.getFields();
            for (final Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)) {
                    if (Color.class.equals(field.getType())) {
                        namedColors.put(field.getName(), (Color) field.get(null));
                    }
                }
            }
            return namedColors;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't access field values of java.awt.Color, is this system too secure?", e);
        }
    }
}
