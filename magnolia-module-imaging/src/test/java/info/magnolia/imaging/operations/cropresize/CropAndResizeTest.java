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
package info.magnolia.imaging.operations.cropresize;

import junit.framework.TestCase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropAndResizeTest extends TestCase {
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
        final BufferedImage result = op.resize(dummyImg, cropCoords);
        assertEquals(expectedWidth, result.getWidth());
        assertEquals(expectedHeight, result.getHeight());
    }

    /**
     * This test is now irrelevant since this is configured at operation level using content2bean
     * Keeping the test around because we'll need something similar for ui - when we figure out
     * how to connect the two together.
     *
     *
     public void testGetsTargetWidthAndHeightFromSpecifiedConfigSubNode() throws IOException {
     final Content configNode = createStrictMock(Content.class);
     final Content configSubNode = createStrictMock(Content.class);
     final NodeData targetWidth = createStrictMock(NodeData.class);
     final NodeData targetHeight = createStrictMock(NodeData.class);
     expect(configNode.getChildByName("foo")).andReturn(configSubNode);
     expect(configSubNode.getNodeData("targetWidth")).andReturn(targetWidth);
     expect(configSubNode.getNodeData("targetHeight")).andReturn(targetHeight);
     expect(targetWidth.getLong()).andReturn(234l);
     expect(targetHeight.getLong()).andReturn(567l);

     replay(configNode, configSubNode, targetWidth, targetHeight);

     final BufferedImage dummyImg = ImageIO.read(getClass().getResourceAsStream("/funnel.gif"));
     final Coords cropCoords = new Coords(0, 0, 16, 8);
     final CropperInfo cropInfo = new CropperInfo("foo", cropCoords);
     final CropAndResize op = new CropAndResize();
     op.setTargetWidth(234);
     op.setTargetHeight(567);
     final BufferedImage result = op.apply(dummyImg, cropInfo, configNode);
     assertEquals(234, result.getWidth());
     assertEquals(567, result.getHeight());

     verify(configNode, configSubNode, targetWidth, targetHeight);
     }
     */

}
