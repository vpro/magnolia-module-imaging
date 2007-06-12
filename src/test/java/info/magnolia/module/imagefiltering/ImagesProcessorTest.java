/**
 *
 * Magnolia and its source-code is licensed under the LGPL.
 * You may copy, adapt, and redistribute this file for commercial or non-commercial use.
 * When copying, adapting, or redistributing this document in keeping with the guidelines above,
 * you are required to provide proper attribution to obinary.
 * If you reproduce or distribute the document without making any substantive modifications to its content,
 * please use the following attribution line:
 *
 * Copyright 1993-2006 obinary Ltd. (http://www.obinary.com) All rights reserved.
 *
 */
package info.magnolia.module.imagefiltering;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.FactoryUtil;
import info.magnolia.content2bean.Content2BeanException;
import info.magnolia.content2bean.Content2BeanProcessor;
import info.magnolia.content2bean.Content2BeanTransformer;
import info.magnolia.content2bean.impl.Content2BeanTransformerImpl;
import info.magnolia.module.imagefiltering.cropresize.Coords;
import info.magnolia.module.imagefiltering.cropresize.CropAndResizeFilter;
import info.magnolia.module.imagefiltering.cropresize.CropperInfo;
import info.magnolia.test.mock.MockNodeData;
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
    private final static String SAMPLE_PARAMS_WITH_IMAGERESIZER = "{\"CropperInfo\":{configName:\"foo\",coords:{\"x1\":1,\"y1\":30,\"x2\":20,\"y2\":50}}}";

    public void testPostSaveCallsImageResizerForBinaryNodesThatHaveCorrespondingCropperInfo() throws IOException, RepositoryException, Content2BeanException {
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
        expect(bin2Crop.getString()).andReturn(SAMPLE_PARAMS_WITH_IMAGERESIZER);
        expect(storageNode.hasNodeData("bin2_resized")).andReturn(false); // TODO we could have a test that asserts the node is overwritten
        expect(storageNode.createNodeData("bin2_resized", PropertyType.BINARY)).andReturn(createMock(NodeData.class));
        final InputStream imgStream = getClass().getResourceAsStream("/cookies.gif");
        expect(bin2.getStream()).andReturn(imgStream);

        final ImageFormat expectedFormat = new ImageFormat();
        expectedFormat.setFormatName("jpeg");
        expectedFormat.setProgressive(true);
        expectedFormat.setQuality(50);

        Content2BeanProcessor content2bean = createStrictMock(Content2BeanProcessor.class);
        expect(content2bean.setProperties(isA(ImageFormat.class), same(configNode), eq(false), (Content2BeanTransformer) anyObject())).andReturn(expectedFormat);

        FactoryUtil.setImplementation(Content2BeanTransformer.class, Content2BeanTransformerImpl.class);
        FactoryUtil.setInstance(Content2BeanProcessor.class, content2bean);

        final CropperInfo expectedCropperInfo = new CropperInfo("foo", new Coords(1, 30, 20, 50));
        final ImageFilter imageFilter = createStrictMock(ImageFilter.class);
        final BufferedImage dummyResultImg = ImageIO.read(getClass().getResourceAsStream("/funnel.gif"));
        expect(imageFilter.getParameterType()).andReturn(CropperInfo.class).times(3);
        expect(imageFilter.apply(isA(BufferedImage.class), eq(expectedCropperInfo), same(configNode))).andReturn(dummyResultImg);

        replay(content2bean, imageFilter, configNode, storageNode, bin1, bin2, bin2Crop);

        final ImagesProcessor imagesProcessor = new ImagesProcessor(imageFilter);
        imagesProcessor.processImages(storageNode, configNode);

        verify(content2bean, imageFilter, configNode, storageNode, bin1, bin2, bin2Crop);
    }

    public void testParamsAreProperlyDecoded() {
        final ImagesProcessor processor = new ImagesProcessor(new CropAndResizeFilter());
        final MockNodeData mockData = new MockNodeData("pouet", SAMPLE_PARAMS_WITH_IMAGERESIZER);
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