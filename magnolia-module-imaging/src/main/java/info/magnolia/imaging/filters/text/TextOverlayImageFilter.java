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
package info.magnolia.imaging.filters.text;

import info.magnolia.imaging.ColorConverter;
import info.magnolia.imaging.filters.ImageFilter;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class TextOverlayImageFilter implements ImageFilter<TextStyle> {
    private TextStyle textStyle;

    public BufferedImage apply(BufferedImage src, TextStyle filterParams) {
        renderText(src.createGraphics(), "Hello world", 10, 10);
        return src;
    }

    // roughly copied from texticons-BackgroundImageRenderer
    protected void renderText(final Graphics2D g, final String txt, final int txtPositionX, final int txtPositionY) {
        // shear(g, style.getShearingValue());
//        FontMetrics fontMetrics = g.getFontMetrics();
//        int lineHeight = fontMetrics.getHeight();
        // int shearingDelta = new Double(lineHeight * style.getShearingValue()).intValue();
        // int newX = new Double(txtPositionX - shearingDelta - style.getShearingValue() * txtPositionY).intValue();

        // undo transformation (pseudo-italic)
        // shear(g, -1 * style.getShearingValue());

        float f = 0.0F;
        final Font font = textStyle.getFont();
        g.setFont(font);
        g.setColor(textStyle.getColor());

        final FontMetrics fontMetrics = g.getFontMetrics();
        final FontRenderContext fontRenderContext = new FontRenderContext(null, false, false);
        final GlyphVector glyphvector = font.createGlyphVector(fontRenderContext, txt);

        float f1 = (float) ((double) textStyle.getCharSpacing() / TextStyle.CHARSPACING_DIV_FACTOR);
        for (int k = 0; k < txt.length(); k++) {
            final char c = txt.charAt(k);
            final int l = fontMetrics.charWidth(c);
            final Point2D point2d = glyphvector.getGlyphPosition(k);
            point2d.setLocation(f, point2d.getY());
            glyphvector.setGlyphPosition(k, point2d);
            f += (new Float((float) l + f1)).floatValue();
        }
        g.drawGlyphVector(glyphvector, (float) txtPositionX, (float) txtPositionY + textStyle.getFontSize());
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
    }
}
