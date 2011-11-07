/**
 * This file Copyright (c) 2007-2011 Magnolia International
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
package info.magnolia.module.imaging;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.FactoryUtil;
import info.magnolia.content2bean.Content2BeanException;
import info.magnolia.content2bean.Content2BeanProcessor;
import info.magnolia.content2bean.Content2BeanTransformer;
import info.magnolia.content2bean.impl.Content2BeanTransformerImpl;
import info.magnolia.imaging.cropresize.Coords;
import info.magnolia.imaging.cropresize.CropAndResizeFilter;
import info.magnolia.imaging.cropresize.CropperInfo;
import info.magnolia.test.mock.MockNodeData;
import info.magnolia.imaging.OuputFormat;
import info.magnolia.imaging.operations.ImageFilter;
import junit.framework.TestCase;
import static org.easymock.classextension.EasyMock.*;

import javax.imageio.ImageIO;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImagesProcessorTest extends TestCase {
    private final static String SAMPLE_PARAMS_WITH_IMAGECROPPER = "{\"CropperInfo\":{configName:\"foo\",coords:{\"x1\":1,\"y1\":30,\"x2\":20,\"y2\":50}}}";

    public void testPostSaveCallsImageCropperForBinaryNodesThatHaveCorrespondingCropperInfo() throws IOException, RepositoryException, Content2BeanException {
        final Content configNode = createMock(Content.class);

        final Content storageNode = createMock(Content.class);
        // let's create dummy binary properties. the second one has a corresponding cropperInfo property and should thus be processed
        final NodeData bin1 = createMock(NodeData.class);
        final NodeData bin2 = createMock(NodeData.class);
        final NodeData bin2Crop = createMock(NodeData.class);
        expect(storageNode.getNodeDataCollection()).andReturn(Arrays.asList(bin2, bin1, bin2Crop));
        expect(bin2.getType()).andReturn(PropertyType.BINARY);
        expect(bin2.getName()).andReturn("bin2");
        expect(bin1.getType()).andReturn(PropertyType.BINARY);
        expect(bin1.getName()).andReturn("bin1");
        expect(bin2Crop.getType()).andReturn(PropertyType.STRING);
        expect(storageNode.hasNodeData("bin1_cropperInfo")).andReturn(false);
        expect(storageNode.hasNodeData("bin2_cropperInfo")).andReturn(true);
        expect(storageNode.getNodeData("bin2_cropperInfo")).andReturn(bin2Crop);
        expect(bin2Crop.getString()).andReturn(SAMPLE_PARAMS_WITH_IMAGECROPPER);
        expect(storageNode.hasNodeData("bin2_resized")).andReturn(false); // TODO we could have a test that asserts the node is overwritten
        expect(storageNode.createNodeData("bin2_resized", PropertyType.BINARY)).andReturn(createMock(NodeData.class));
        final InputStream imgStream = getClass().getResourceAsStream("/cookies.gif");
        expect(bin2.getStream()).andReturn(imgStream);

        final OuputFormat expectedFormat = new OuputFormat();
        expectedFormat.setFormatName("jpeg");
        expectedFormat.setProgressive(true);
        expectedFormat.setQuality(50);

        Content2BeanProcessor content2bean = createStrictMock(Content2BeanProcessor.class);
        expect(content2bean.setProperties(isA(OuputFormat.class), same(configNode), eq(false), (Content2BeanTransformer) anyObject())).andReturn(expectedFormat);

        FactoryUtil.setImplementation(Content2BeanTransformer.class, Content2BeanTransformerImpl.class);
        FactoryUtil.setInstance(Content2BeanProcessor.class, content2bean);

        final CropperInfo expectedCropperInfo = new CropperInfo("foo", new Coords(1, 30, 20, 50));
        final ImageFilter imageFilter = createStrictMock(ImageFilter.class);
        final BufferedImage dummyResultImg = ImageIO.read(getClass().getResourceAsStream("/funnel.gif"));
//        expect(imageFilter.getParameterType()).andReturn(CropperInfo.class).times(3);
        expect(imageFilter.apply(isA(BufferedImage.class), eq(expectedCropperInfo), same(configNode))).andReturn(dummyResultImg);

        replay(content2bean, imageFilter, configNode, storageNode, bin1, bin2, bin2Crop);

        final ImagesProcessor imagesProcessor = new ImagesProcessor(imageFilter);
        imagesProcessor.processImages(storageNode, configNode);

        verify(content2bean, imageFilter, configNode, storageNode, bin1, bin2, bin2Crop);
    }

    public void testParamsAreProperlyDecoded() {
        final ImagesProcessor processor = new ImagesProcessor(new CropAndResizeFilter());
        final MockNodeData mockData = new MockNodeData("pouet", SAMPLE_PARAMS_WITH_IMAGECROPPER);
        final Map params = processor.getUserParams(mockData);
        assertEquals(1, params.size());
        final CropperInfo cropperInfo = (CropperInfo) params.get("CropperInfo");
        assertNotNull(cropperInfo);
        assertEquals("foo", cropperInfo.getConfigName());
        final Coords coords = cropperInfo.getCoords();
        assertNotNull(coords);
        assertEquals(1, coords.getX1());
        assertEquals(30, coords.getY1());
        assertEquals(20, coords.getX2());
        assertEquals(50, coords.getY2());
    }

    public void testParamsMapIsEmptyPropIsEmpty() {
        final ImagesProcessor processor = new ImagesProcessor(new CropAndResizeFilter());
        final MockNodeData mockData = new MockNodeData("pouet", "");
        final Map params = processor.getUserParams(mockData);
        assertEquals(0, params.size());
    }
}
