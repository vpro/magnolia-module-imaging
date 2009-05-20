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

import com.jhlabs.image.FlipFilter;
import junit.framework.TestCase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class AutoCropAndResizeTest extends TestCase {
    // having these images as static constant is a mediocre attempt at making this test run faster (it was using around 7 seconds...)
    private static final BufferedImage horizontalImage;
    private static final BufferedImage verticalImage;

    // preload images for this test
    static {
        try {
            final FlipFilter f = new FlipFilter(FlipFilter.FLIP_90CW);
            horizontalImage = ImageIO.read(AutoCropAndResizeTest.class.getResource("/IMG_2463.JPG"));
            verticalImage = f.filter(horizontalImage, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void testJustResizeIfRatioIsEquivalent() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(400);
        op.setTargetHeight(300);

        final Coords coords = op.getCroopCoords(getTestImage(), op.getTargetWidth(), op.getTargetHeight());
        assertEquals(1600, coords.getWidth());
        assertEquals(1200, coords.getHeight());
        assertEquals(0, coords.getX1());
        assertEquals(1600, coords.getX2());
        assertEquals(0, coords.getY1());
        assertEquals(1200, coords.getY2());

        final BufferedImage res = op.apply(getTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(300, res.getHeight());
    }

    public void testRespectsRatioOfTargetSizeIfCroppingIsNeededAndCropFromTheCenter() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(400);
        op.setTargetHeight(225);

        final Coords coords = op.getCroopCoords(getTestImage(), op.getTargetWidth(), op.getTargetHeight());// 16:9 ratio
        assertEquals(1600, coords.getWidth());
        assertEquals(900, coords.getHeight());
        assertEquals(0, coords.getX1());
        assertEquals(1600, coords.getX2());
        assertEquals(150, coords.getY1()); // 1200 cropped to 900
        assertEquals(1050, coords.getY2());

        final BufferedImage res = op.apply(getTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(225, res.getHeight());
    }

    public void testVerticalRatiosAlsoWork() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(200);
        op.setTargetHeight(600);

        final Coords coords = op.getCroopCoords(getTestImage(), op.getTargetWidth(), op.getTargetHeight());
        assertEquals(400, coords.getWidth());
        assertEquals(1200, coords.getHeight());
        assertEquals(600, coords.getX1());
        assertEquals(1000, coords.getX2());
        assertEquals(0, coords.getY1());
        assertEquals(1200, coords.getY2());

        final BufferedImage res = op.apply(getTestImage(), null);
        assertEquals(200, res.getWidth());
        assertEquals(600, res.getHeight());
    }

    public void testDoesNotCropIfOnlyTargetHeightIsSpecifiedAndRespectsOriginalRatio() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(0);
        op.setTargetHeight(300);

        final Coords coords = op.getCroopCoords(getTestImage(), op.getTargetWidth(), op.getTargetHeight());
        assertEquals(1600, coords.getWidth());
        assertEquals(1200, coords.getHeight());
        assertEquals(0, coords.getX1());
        assertEquals(1600, coords.getX2());
        assertEquals(0, coords.getY1());
        assertEquals(1200, coords.getY2());

        final BufferedImage res = op.apply(getTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(300, res.getHeight());

    }

    public void testDoesNotCropIfOnlyTargetWidthIsSpecifiedAndRespectsOriginalRatio() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(400);
        op.setTargetHeight(0);

        final Coords coords = op.getCroopCoords(getTestImage(), op.getTargetWidth(), op.getTargetHeight());
        assertEquals(1600, coords.getWidth());
        assertEquals(1200, coords.getHeight());
        assertEquals(0, coords.getX1());
        assertEquals(1600, coords.getX2());
        assertEquals(0, coords.getY1());
        assertEquals(1200, coords.getY2());

        final BufferedImage res = op.apply(getTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(300, res.getHeight());
    }

    // tests with 1:1 ratio
    public void testSquareTargetUsesLargestPossibleZoneForHorizontalSource() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(400);
        op.setTargetHeight(400);

        final Coords coords = op.getCroopCoords(getTestImage(), op.getTargetWidth(), op.getTargetHeight());
        assertEquals(1200, coords.getWidth());
        assertEquals(1200, coords.getHeight());
        assertEquals(200, coords.getX1());
        assertEquals(1400, coords.getX2());
        assertEquals(0, coords.getY1());
        assertEquals(1200, coords.getY2());

        final BufferedImage res = op.apply(getTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(400, res.getHeight());
    }

    public void testSquareTargetUsesLargestPossibleZoneForVerticalSourceToo() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(400);
        op.setTargetHeight(400);

        final Coords coords = op.getCroopCoords(getVerticalTestImage(), op.getTargetWidth(), op.getTargetHeight());
        assertEquals(1200, coords.getWidth());
        assertEquals(1200, coords.getHeight());
        assertEquals(0, coords.getX1());
        assertEquals(1200, coords.getX2());
        assertEquals(200, coords.getY1());
        assertEquals(1400, coords.getY2());

        final BufferedImage res = op.apply(getVerticalTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(400, res.getHeight());
    }

    // tests with vertical source
    public void testVerticalSourceIsHandledJustAsWell() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(400);
        op.setTargetHeight(300);

        final Coords coords = op.getCroopCoords(getVerticalTestImage(), op.getTargetWidth(), op.getTargetHeight());
        assertEquals(1200, coords.getWidth());
        assertEquals(900, coords.getHeight());
        assertEquals(0, coords.getX1());
        assertEquals(1200, coords.getX2());
        assertEquals(350, coords.getY1());
        assertEquals(1250, coords.getY2());

        final BufferedImage res = op.apply(getVerticalTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(300, res.getHeight());
    }

    public void testVerticalSourceWithVerticalRatioIsAlsoSwell() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(150);
        op.setTargetHeight(600);

        final Coords coords = op.getCroopCoords(getVerticalTestImage(), op.getTargetWidth(), op.getTargetHeight());
        assertEquals(400, coords.getWidth());
        assertEquals(1600, coords.getHeight());
        assertEquals(400, coords.getX1());
        assertEquals(800, coords.getX2());
        assertEquals(0, coords.getY1());
        assertEquals(1600, coords.getY2());

        final BufferedImage res = op.apply(getVerticalTestImage(), null);
        assertEquals(150, res.getWidth());
        assertEquals(600, res.getHeight());
    }

    // TODO tests where source is smaller than target

    /**
     * Gets a well-known image, ensuring its dimensions haven't changed since the test was written and started.
     */
    private BufferedImage getTestImage() throws Exception {
        assertEquals(1600, horizontalImage.getWidth());
        assertEquals(1200, horizontalImage.getHeight());
        return horizontalImage;
    }

    private BufferedImage getVerticalTestImage() throws Exception {
        assertEquals(1200, verticalImage.getWidth());
        assertEquals(1600, verticalImage.getHeight());
        return verticalImage;
    }
}
