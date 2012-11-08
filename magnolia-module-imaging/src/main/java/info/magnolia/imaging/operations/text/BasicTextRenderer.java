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
package info.magnolia.imaging.operations.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Basic implementation of a TextRenderer.
 *
 * @version $Id$
 */
public class BasicTextRenderer implements TextRenderer {
    /** for debugging purposes. */
    private boolean drawBoundingBox;

    // TODO this currently does not allow specifying text/character spacing

    @Override
    public void renderText(BufferedImage img, String txt, TextStyle style, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, int txtPositionX, int txtPositionY) {
        final Graphics2D g = img.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(style.getColor());
        final Font font = style.getFont();
        final FontRenderContext fontRenderContext = g.getFontRenderContext();
        final GlyphVector glyph = font.createGlyphVector(fontRenderContext, txt);
        final Rectangle2D bounds = glyph.getVisualBounds();
        final float textWidth = (float) bounds.getWidth();
        final float textHeight = (float) bounds.getHeight();

        // Could use ascent and descent for more precise positioning.
        // As well as TextLayout, and add support for multi-line, etc.
        // This is left as an exercise to the reader.
        final float x = horizontalAlignment.getPositionFor(textWidth, img.getWidth(), txtPositionX);
        final float y = verticalAlignment.getPositionFor(textHeight, img.getHeight(), txtPositionY);
        g.drawGlyphVector(glyph, x, y);

        if (drawBoundingBox) {
            g.setColor(Color.YELLOW);
            g.draw(new Rectangle((int) x, (int) (y - textHeight), (int) textWidth, (int) textHeight));
        }

        g.dispose();
    }

    public boolean isDrawBoundingBox() {
        return drawBoundingBox;
    }

    /**
     * Enable this to draw a yellow box around the generated text, can be helpful to help positioning.
     */
    public void setDrawBoundingBox(boolean drawBoundingBox) {
        this.drawBoundingBox = drawBoundingBox;
    }
}
