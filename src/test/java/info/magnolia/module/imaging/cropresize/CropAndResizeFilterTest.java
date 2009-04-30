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
package info.magnolia.module.imaging.cropresize;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import junit.framework.TestCase;
import static org.easymock.classextension.EasyMock.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropAndResizeFilterTest extends TestCase {
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
        final BufferedImage result = new CropAndResizeFilter().resize(dummyImg, cropCoords, targetWidth, targetHeight);
        assertEquals(expectedWidth, result.getWidth());
        assertEquals(expectedHeight, result.getHeight());
    }

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
        final BufferedImage result = new CropAndResizeFilter().apply(dummyImg, cropInfo, configNode);
        assertEquals(234, result.getWidth());
        assertEquals(567, result.getHeight());

        verify(configNode, configSubNode, targetWidth, targetHeight);
    }

}
