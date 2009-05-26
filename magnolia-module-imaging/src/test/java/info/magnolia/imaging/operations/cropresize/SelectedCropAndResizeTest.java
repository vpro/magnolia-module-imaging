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

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public abstract class SelectedCropAndResizeTest extends TestCase {


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
