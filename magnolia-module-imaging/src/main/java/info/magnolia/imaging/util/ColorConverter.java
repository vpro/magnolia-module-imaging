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
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
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
