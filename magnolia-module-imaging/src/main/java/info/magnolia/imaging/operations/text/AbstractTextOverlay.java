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

import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.operations.ImageOperation;

import java.awt.image.BufferedImage;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public abstract class AbstractTextOverlay<P extends ParameterProvider<?>> implements ImageOperation<P> {
    private TextRenderer textRenderer = new BasicTextRenderer();
    private TextStyle textStyle;
    private HorizontalAlignment horizontalAlign = HorizontalAlignment.left;
    private VerticalAlignment verticalAlign = VerticalAlignment.bottom;
    private int x;
    private int y;

    public BufferedImage apply(BufferedImage source, P filterParams) {
        final String txt = getText(filterParams);

        getTextRenderer().renderText(source, txt, textStyle, horizontalAlign, verticalAlign, x, y);
        return source;
    }

    protected abstract String getText(P params);

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public void setTextRenderer(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    public HorizontalAlignment getHorizontalAlign() {
        return horizontalAlign;
    }

    public void setHorizontalAlign(HorizontalAlignment horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
    }

    public VerticalAlignment getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(VerticalAlignment verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
