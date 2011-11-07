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
        // source is w:1600*h:1200
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getHorizontalTestImage(), 2500, 300, 400, 300);
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getHorizontalTestImage(), 200, 3000, 200, 150);
    }

    public void testKeepsOriginalRatioAndCompliesToSmallestMaximumDimensionForVerticalSourceToo() throws Exception {
        // source is w:1200*h:1600
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getVerticalTestImage(), 2500, 400, 300, 400);
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getVerticalTestImage(), 300, 4000, 300, 400);
    }

    public void testKeepsOriginalRatioAndCompliesToSmallestMaximumDimensionForSquareSourceToo() throws Exception {
        // source is w:500*h:500
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getSquareTestImage(), 2500, 400, 400, 400);
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getSquareTestImage(), 300, 4000, 300, 300);
    }

    public void testAlsoWorksWithOblongImages() throws Exception {
        final BufferedImage img = generateArbitraryTestImage(228, 700);
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(img, 160, 260, 84, 260);
    }

    public void testAnotherOblongCase() throws Exception {
        final BufferedImage img = generateArbitraryTestImage(800, 200);
        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(img, 160, 160, 160, 40);
    }

    // TODO - we could implement this for consistency's sake
//    public void testWhatHappensIfWeOnlySpecifyMaxWidth() throws Exception {
//        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getHorizontalTestImage(), 400, 0, 400, 300);
//        doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(getHorizontalTestImage(), 0, 400, 400, 300);
//    }


    private void doTestKeepsOriginalRatioAndCompliesToSmallestMaximumDimension(final BufferedImage src, final int maxWidth, final int maxHeight, final int expectedWith, final int expectedHeight) throws Exception {
        final BoundedResize op = new BoundedResize();
        op.setMaxWidth(maxWidth);
        op.setMaxHeight(maxHeight);

        final BufferedImage res = op.apply(src, null);
        write(res);

        assertEquals(expectedWith, res.getWidth());
        assertEquals(expectedHeight, res.getHeight());
    }
}
