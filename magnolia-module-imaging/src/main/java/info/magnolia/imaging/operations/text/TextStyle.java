/**
 * This file Copyright (c) 2007-2011 Magnolia International
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

import java.awt.Color;
import java.awt.Font;

/**
 * A simple bean holding font-related configuration. 
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
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
