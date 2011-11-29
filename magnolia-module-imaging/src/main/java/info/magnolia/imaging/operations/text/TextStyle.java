/**
 * This file Copyright (c) 2007-2011 Magnolia International
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
package info.magnolia.imaging.operations.text;

import java.awt.Color;
import java.awt.Font;

/**
 * A simple bean holding font-related configuration.
 *
 * @version $Id$
 */
public class TextStyle {
    public static final double SHEARING_DIV_FACTOR = 20D;
    public static final double CHARSPACING_DIV_FACTOR = 10D;

    private String fontName;
    private int fontStyle = Font.PLAIN;
    private int fontSize;
    private Color color;
    private Color backgroundColor;
    private double charSpacing;
    private int shearingValue;

    public double calculateShearingValue() {
        return (-1.0D * (new Double(shearingValue)).doubleValue()) / SHEARING_DIV_FACTOR;
    }

    public float calculateCharSpacing() {
        return (float) (getCharSpacing() / TextStyle.CHARSPACING_DIV_FACTOR);
    }

    //    public Font deriveFont() {
    //  return this.font.deriveFont(this.fontStyle, (float) this.fontSize);

    public Font getFont() {
        return new Font(fontName, fontStyle, fontSize);
    }

    // ---generated getters and setters

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public double getCharSpacing() {
        return charSpacing;
    }

    public void setCharSpacing(double charSpacing) {
        this.charSpacing = charSpacing;
    }

    public int getShearingValue() {
        return shearingValue;
    }

    public void setShearingValue(int shearingValue) {
        this.shearingValue = shearingValue;
    }
}
