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
    public void testJustResizeIfRatioIsEquivalent() throws IOException {
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

    public void testRespectsRatioOfTargetSizeIfCroppingIsNeededAndCropFromTheCenter() throws IOException {
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

    public void testVerticalRatiosAlsoWork() throws IOException {
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

    public void testDoesNotCropIfOnlyTargetHeightIsSpecifiedAndRespectsOriginalRatio() throws IOException {
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

    public void testDoesNotCropIfOnlyTargetWidthIsSpecifiedAndRespectsOriginalRatio() throws IOException {
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

    // TODO more tests..
    // tests with 1:1 ratio
    public void testSquareTargetUsesLargestPossibleZoneForHorizontalSource() throws IOException {
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

    public void testSquareTargetUsesLargestPossibleZoneForVerticalSourceToo() throws IOException {
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
    public void testVerticalSourceIsHandledJustAsWell() throws IOException {
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

    public void testVerticalSourceWithVerticalRatioIsAlsoSwell() throws IOException {
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
     * Loads a well-known image, ensuring its dimensions haven't changed since the test was written.
     */
    private BufferedImage getTestImage() throws IOException {
        final BufferedImage img = ImageIO.read(getClass().getResource("/IMG_2463.JPG"));
        assertEquals(1600, img.getWidth());
        assertEquals(1200, img.getHeight());
        return img;
    }

    private BufferedImage getVerticalTestImage() throws IOException {
        final BufferedImage img = getTestImage();
        final FlipFilter f = new FlipFilter(FlipFilter.FLIP_90CW);
        final BufferedImage v = f.filter(img, null);
        assertEquals(1200, v.getWidth());
        assertEquals(1600, v.getHeight());
        return v;
    }
}
