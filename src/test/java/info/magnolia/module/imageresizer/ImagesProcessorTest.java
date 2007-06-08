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
package info.magnolia.module.imageresizer;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.test.mock.MockNodeData;
import info.magnolia.module.imageresizer.cropresize.CropAndResizeFilter;
import info.magnolia.module.imageresizer.cropresize.CropperInfo;
import info.magnolia.module.imageresizer.cropresize.Coords;
import junit.framework.TestCase;
import static org.easymock.classextension.EasyMock.*;

import javax.imageio.ImageIO;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import java.awt.Image;
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

    public void testPostSaveCallsImageResizerForBinaryNodesThatHaveCorrespondingCropperInfo() throws IOException, RepositoryException {
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

        final CropperInfo expectedCropperInfo = new CropperInfo("foo", new Coords(1, 30, 20, 50));
        final ImageFilter imageFilter = createStrictMock(ImageFilter.class);
        final BufferedImage dummyResultImg = ImageIO.read(getClass().getResourceAsStream("/funnel.gif"));
        expect(imageFilter.getParameterType()).andReturn(CropperInfo.class).times(3);
        expect(imageFilter.apply(isA(Image.class), eq(expectedCropperInfo), same(configNode))).andReturn(dummyResultImg);

        replay(imageFilter, configNode, storageNode, bin1, bin2, bin2Crop);

        final ImagesProcessor imagesProcessor = new ImagesProcessor(imageFilter);
        imagesProcessor.processImages(storageNode, configNode);

        verify(imageFilter, configNode, storageNode, bin1, bin2, bin2Crop);
    }

    public void testParamsAreProperlyDecoded() {
        final ImagesProcessor processor = new ImagesProcessor(new CropAndResizeFilter());
        final MockNodeData mockData = new MockNodeData("pouet", SAMPLE_PARAMS_WITH_IMAGERESIZER);
        final Map params = processor.getParams(mockData);
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
        final Map params = processor.getParams(mockData);
        assertEquals(0, params.size());
    }
}
