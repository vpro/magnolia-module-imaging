/**
 * This file Copyright (c) 2003-2009 Magnolia International
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
package info.magnolia.imaging.filters.text;

import java.awt.*;


/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 *          TODO : copied from org.mgnl.magnolia.texticons.utils
 */
public class TextStyle {
    public static final double SHEARING_DIV_FACTOR = 20D;
    public static final double CHARSPACING_DIV_FACTOR = 10D;

    private String fontName;
    private int fontStyle = Font.PLAIN;
    private Color color;
    private Color bgColor;
    private double charSpacing;
    private int fontSize;
    private int shearingValue;

    public double calculateShearingValue() {
        return (-1.0D * (new Double(shearingValue)).doubleValue()) / SHEARING_DIV_FACTOR;
    }

//    public Font deriveFont() {
      //  return this.font.deriveFont(this.fontStyle, (float) this.fontSize);
    public Font getFont() {
        return new Font(fontName, fontStyle, fontSize);
    }

    // ---generated getters and setters
    public double getCharSpacing() {
        return charSpacing;
    }

    public void setCharSpacing(double charSpacing) {
        this.charSpacing = charSpacing;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String  fontName) {
        this.fontName = fontName;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getShearingValue() {
        return shearingValue;
    }

    public void setShearingValue(int shearingValue) {
        this.shearingValue = shearingValue;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }
}