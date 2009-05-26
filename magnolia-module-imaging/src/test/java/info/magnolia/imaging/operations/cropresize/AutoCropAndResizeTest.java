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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class AutoCropAndResizeTest extends AbstractImagingTest {

    public void testJustResizeIfTargetRatioIsEquivalentToSourceRatio() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(400);
        op.setTargetHeight(300);

        final Coords coords = op.getCroopCoords(getHorizontalTestImage(), null);
        assertEquals(1600, coords.getWidth());
        assertEquals(1200, coords.getHeight());
        assertEquals(0, coords.getX1());
        assertEquals(1600, coords.getX2());
        assertEquals(0, coords.getY1());
        assertEquals(1200, coords.getY2());

        final BufferedImage res = op.apply(getHorizontalTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(300, res.getHeight());
    }

    public void testRespectsRatioOfTargetSizeIfCroppingIsNeededAndCropFromTheCenter() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(400);
        op.setTargetHeight(225);

        final Coords coords = op.getCroopCoords(getHorizontalTestImage(), null);// 16:9 ratio
        assertEquals(1600, coords.getWidth());
        assertEquals(900, coords.getHeight());
        assertEquals(0, coords.getX1());
        assertEquals(1600, coords.getX2());
        assertEquals(150, coords.getY1()); // 1200 cropped to 900
        assertEquals(1050, coords.getY2());

        final BufferedImage res = op.apply(getHorizontalTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(225, res.getHeight());
    }

    public void testVerticalRatiosAlsoWork() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(200);
        op.setTargetHeight(600);

        final Coords coords = op.getCroopCoords(getHorizontalTestImage(), null);
        assertEquals(400, coords.getWidth());
        assertEquals(1200, coords.getHeight());
        assertEquals(600, coords.getX1());
        assertEquals(1000, coords.getX2());
        assertEquals(0, coords.getY1());
        assertEquals(1200, coords.getY2());

        final BufferedImage res = op.apply(getHorizontalTestImage(), null);
        assertEquals(200, res.getWidth());
        assertEquals(600, res.getHeight());
    }

    public void testDoesNotCropIfOnlyTargetHeightIsSpecifiedAndRespectsOriginalRatio() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(0);
        op.setTargetHeight(300);

        final Coords coords = op.getCroopCoords(getHorizontalTestImage(), null);
        assertEquals(1600, coords.getWidth());
        assertEquals(1200, coords.getHeight());
        assertEquals(0, coords.getX1());
        assertEquals(1600, coords.getX2());
        assertEquals(0, coords.getY1());
        assertEquals(1200, coords.getY2());

        final BufferedImage res = op.apply(getHorizontalTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(300, res.getHeight());

    }

    public void testDoesNotCropIfOnlyTargetWidthIsSpecifiedAndRespectsOriginalRatio() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(400);
        op.setTargetHeight(0);

        final Coords coords = op.getCroopCoords(getHorizontalTestImage(), null);
        assertEquals(1600, coords.getWidth());
        assertEquals(1200, coords.getHeight());
        assertEquals(0, coords.getX1());
        assertEquals(1600, coords.getX2());
        assertEquals(0, coords.getY1());
        assertEquals(1200, coords.getY2());

        final BufferedImage res = op.apply(getHorizontalTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(300, res.getHeight());
    }

    // tests with 1:1 ratio
    public void testSquareTargetUsesLargestPossibleZoneForHorizontalSource() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(400);
        op.setTargetHeight(400);

        final Coords coords = op.getCroopCoords(getHorizontalTestImage(), null);
        assertEquals(1200, coords.getWidth());
        assertEquals(1200, coords.getHeight());
        assertEquals(200, coords.getX1());
        assertEquals(1400, coords.getX2());
        assertEquals(0, coords.getY1());
        assertEquals(1200, coords.getY2());

        final BufferedImage res = op.apply(getHorizontalTestImage(), null);
        assertEquals(400, res.getWidth());
        assertEquals(400, res.getHeight());
    }

    public void testSquareTargetUsesLargestPossibleZoneForVerticalSourceToo() throws Exception {
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(400);
        op.setTargetHeight(400);

        final Coords coords = op.getCroopCoords(getVerticalTestImage(), null);
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

        final Coords coords = op.getCroopCoords(getVerticalTestImage(), null);
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

        final Coords coords = op.getCroopCoords(getVerticalTestImage(), null);
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

    public void testNoResizeIfWidthAndHeightAreNotSpecified() throws IOException {
        doResizeTest(-1, -1, 16, 8);
        doResizeTest(0, 0, 16, 8);
        doResizeTest(0, 0, new Coords(5, 6, 10, 18), 5, 12);
    }

    public void testResizesProportionallyIfOnlyWidthIsSpecified() throws IOException {
        doResizeTest(50, -1, 50, 25);
        doResizeTest(8, -1, 8, 4);
    }

    public void testResizesProportionallyIfOnlyHeightIsSpecified() throws IOException {
        doResizeTest(-1, 50, 100, 50);
        doResizeTest(-1, 6, 12, 6);
    }

    public void testResizesUsingBothWidthAndHeightIfSpecified() throws IOException {
        doResizeTest(30, 30, 30, 30);
        doResizeTest(20, 50, 20, 50);
    }

    private void doResizeTest(int targetWidth, int targetHeight, int expectedWidth, int expectedHeight) throws IOException {
        doResizeTest(targetWidth, targetHeight, new Coords(0, 0, 16, 8), expectedWidth, expectedHeight);
    }

    private void doResizeTest(int targetWidth, int targetHeight, Coords cropCoords, int expectedWidth, int expectedHeight) throws IOException {
        final BufferedImage dummyImg = ImageIO.read(getClass().getResourceAsStream("/funnel.gif"));
        final AutoCropAndResize op = new AutoCropAndResize();
        op.setTargetWidth(targetWidth);
        op.setTargetHeight(targetHeight);
        final Size effectiveTargetSize = op.getEffectiveTargetSize(dummyImg, cropCoords, null);
        final BufferedImage result = op.resize(dummyImg, cropCoords, effectiveTargetSize);
        assertEquals(expectedWidth, result.getWidth());
        assertEquals(expectedHeight, result.getHeight());
    }
    // TODO tests where source is smaller than target

}
