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

import info.magnolia.imaging.filters.ImageFilter;

import java.awt.*;
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
//        final ColorModel cm = src.getColorModel();
//        final WritableRaster raster = cm.createCompatibleWritableRaster(src.getWidth(), src.getHeight());
//        final BufferedImage dest = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);

        renderText(src.createGraphics(), "Hello world", 10, src.getHeight() - 10);

        return src;
    }

    // roughly copied from texticons-BackgroundImageRenderer
    protected void renderText(Graphics2D g, final String txt, final int txtPositionX, final int txtPositionY) {

//        final TextStyle style = new TextStyle();
//        style.setFont(new Font("Arial", 1, 20));
//        style.setColor(Color.RED);
        // shear(g, style.getShearingValue());

//        g.setFont(textStyle.getFont());
//        g.setColor(textStyle.getColor());
        
//        FontMetrics fontmetrics = g.getFontMetrics();
//        int lineHeight = fontmetrics.getHeight();
        // int shearingDelta = new Double(lineHeight * style.getShearingValue()).intValue();
        // int newX = new Double(txtPositionX - shearingDelta - style.getShearingValue() * txtPositionY).intValue();
        drawString(g, txt, txtPositionX, txtPositionY);

        // undo transformation (pseudo-italic)
        // shear(g, -1 * style.getShearingValue());


    }

    protected synchronized void drawString(final Graphics2D graphics2d, String text, int txtPositionX, int txtPositionY) {
        float f = 0.0F;
        final Font font = textStyle.getFont();
        graphics2d.setFont(font);
        graphics2d.setColor(textStyle.getColor());
        FontMetrics fontmetrics = graphics2d.getFontMetrics();
        float f1 = (float) ((double) textStyle.getCharSpacing() / TextStyle.CHARSPACING_DIV_FACTOR);
        FontRenderContext fontrendercontext = new FontRenderContext(null, false, false);
        GlyphVector glyphvector = font.createGlyphVector(fontrendercontext, text);
        for (int k = 0; k < text.length(); k++) {
            char c = text.charAt(k);
            int l = fontmetrics.charWidth(c);
            Point2D point2d = glyphvector.getGlyphPosition(k);
            point2d.setLocation(f, point2d.getY());
            glyphvector.setGlyphPosition(k, point2d);
            f += (new Float((float) l + f1)).floatValue();
        }
        graphics2d.drawGlyphVector(glyphvector, (float) txtPositionX, (float)txtPositionY + textStyle.getFontSize());
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
    }
}
