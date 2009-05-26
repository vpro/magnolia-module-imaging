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
package info.magnolia.imaging.operations.cropresize;

import info.magnolia.imaging.AbstractImagingTest;

import java.awt.image.BufferedImage;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class BoundedResizeTest extends AbstractImagingTest {
    public void testJustResizeIfTargetRatioIsEquivalentToSourceRatio() throws Exception {
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getHorizontalTestImage(), 400, 300, 400, 300);
    }

    public void testKeepsOriginalRatioAndCompliesToSmallestMaximumDimension() throws Exception {
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getHorizontalTestImage(), 2500, 300, 400, 300);
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getHorizontalTestImage(), 200, 3000, 200, 150);
    }

    public void testKeepsOriginalRatioAndCompliesToSmallestMaximumDimensionForVerticalSourceToo() throws Exception {
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getVerticalTestImage(), 2500, 400, 300, 400);
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getVerticalTestImage(), 300, 4000, 300, 400);
    }


    private void doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(final BufferedImage src, final int maxWidth, final int maxHeight, final int expectedWith, final int expectedHeight) throws Exception {
        final BoundedResize op = new BoundedResize();
        op.setMaxWidth(maxWidth);
        op.setMaxHeight(maxHeight);

        final BufferedImage res = op.apply(src, null);
        write(res);

        assertEquals(expectedWith, res.getWidth()); // source is w:1600*h:1200
        assertEquals(expectedHeight, res.getHeight());
    }


    // TODO - test for square source
}
