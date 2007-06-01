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

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImagesProcessorTest extends TestCase {
    public void testPostSaveCallsImageResizerForBinaryNodesThatHaveCorrespondingCropperInfo() throws IOException, RepositoryException {
        final Content configNode = createMock(Content.class);
        final NodeData targetWidth = createMock(NodeData.class);
        final NodeData targetHeight = createMock(NodeData.class);
        expect(configNode.getNodeData("targetWidth")).andReturn(targetWidth);
        expect(configNode.getNodeData("targetHeight")).andReturn(targetHeight);
        expect(targetWidth.getLong()).andReturn(234l);
        expect(targetHeight.getLong()).andReturn(567l);
        
        final Content storageNode = createMock(Content.class);
        // let's create dummy binary properties. the second one has a corresponding cropperInfo property and should thus be processed
        final NodeData bin1 = createMock(NodeData.class);
        final NodeData bin2 = createMock(NodeData.class);
        final NodeData bin2Crop = createMock(NodeData.class);
        expect(storageNode.getNodeDataCollection()).andReturn(Arrays.asList(bin2,bin1,bin2Crop));
        expect(bin2.getType()).andReturn(PropertyType.BINARY);
        expect(bin2.getName()).andReturn("bin2");
        expect(bin1.getType()).andReturn(PropertyType.BINARY);
        expect(bin1.getName()).andReturn("bin1");
        expect(bin2Crop.getType()).andReturn(PropertyType.STRING);
        expect(storageNode.hasNodeData("bin1_cropperInfo")).andReturn(false);
        expect(storageNode.hasNodeData("bin2_cropperInfo")).andReturn(true);
        expect(storageNode.getNodeData("bin2_cropperInfo")).andReturn(bin2Crop);
        expect(bin2Crop.getString()).andReturn("{\"x1\":1,\"y1\":30,\"x2\":20,\"y2\":50}");
        expect(storageNode.hasNodeData("bin2_resized")).andReturn(false); // TODO we could have a test that asserts the node is overwritten
        expect(storageNode.createNodeData("bin2_resized", PropertyType.BINARY)).andReturn(createMock(NodeData.class));
        final InputStream imgStream = getClass().getResourceAsStream("/cookies.gif");
        expect(bin2.getStream()).andReturn(imgStream);

        final CropperInfo expectedCropperInfo = new CropperInfo(1, 30, 20, 50);
        final ImageResizer imageResizer = createStrictMock(ImageResizer.class);
        final BufferedImage dummyResultImg = ImageIO.read(getClass().getResourceAsStream("/funnel.gif"));
        expect(imageResizer.resize(isA(Image.class), eq(expectedCropperInfo), eq(234), eq(567))).andReturn(dummyResultImg);

        replay(imageResizer, configNode, targetHeight, targetWidth, storageNode, bin1, bin2, bin2Crop);

        final ImagesProcessor imagesProcessor = new ImagesProcessor(imageResizer);
        imagesProcessor.processImages(storageNode, configNode);

        verify(imageResizer, configNode, targetHeight, targetWidth, storageNode, bin1, bin2, bin2Crop);
    }

    public void testCropperInfoAreProperlyDecoded() {
        final ImagesProcessor processor = new ImagesProcessor(new ImageResizerImpl());
        final MockNodeData mockData = new MockNodeData("pouet", "{\"x1\":1,\"y1\":30,\"x2\":20,\"y2\":50}");
        final CropperInfo cropperInfo = processor.getCropperInfo(mockData);
        assertNotNull(cropperInfo);
        assertEquals(1, cropperInfo.getX1());
        assertEquals(30, cropperInfo.getY1());
        assertEquals(20, cropperInfo.getX2());
        assertEquals(50, cropperInfo.getY2());
    }

    public void testCropperInfoIsNullIfPropIsEmpty() {
        final ImagesProcessor processor = new ImagesProcessor(new ImageResizerImpl());
        final MockNodeData mockData = new MockNodeData("pouet", "");
        final CropperInfo cropperInfo = processor.getCropperInfo(mockData);
        assertEquals(null, cropperInfo);
    }
}
