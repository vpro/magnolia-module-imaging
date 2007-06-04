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

import info.magnolia.cms.beans.runtime.FileProperties;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.DateUtil;
import info.magnolia.cms.util.NodeDataUtil;
import info.magnolia.module.imageresizer.control.ImageResizeControl;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * In a given node, searches for binary properties with associated cropper properties,
 * and generates appropriate resized/cropped images.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImagesProcessor {
    private final ImageResizer imageResizer;

    public ImagesProcessor(ImageResizer imageResizer) {
        this.imageResizer = imageResizer;
    }

    public void processImages(Content storageNode, Content configNode) throws RepositoryException, IOException {
        // TODO : using the DialogHandler, this is currently configured at dialog level. Ideally it should be configured at control level, especially once the file and crop controls are decoupled. 
        final int targetWidth = (int) configNode.getNodeData("targetWidth").getLong();
        final int targetHeight = (int) configNode.getNodeData("targetHeight").getLong();

        // let's loop through all properties, see which ones are binaries and see if they have a corresponding cropperInfo
        final Collection props = storageNode.getNodeDataCollection();
        Iterator it = props.iterator();
        while (it.hasNext()) {
            NodeData nd = (NodeData) it.next();
            if (nd.getType() == PropertyType.BINARY) {
                final String binaryName = nd.getName();
                final String potentialCropperInfoProperty = ImageResizeControl.getCropperInfoPropertyName(binaryName);
                if (storageNode.hasNodeData(potentialCropperInfoProperty)) {
                    final NodeData cropperInfo = storageNode.getNodeData(potentialCropperInfoProperty);
                    final String targetBinaryProperty = ImageResizeControl.getTargetBinaryPropertyName(binaryName);
                    final NodeData target = NodeDataUtil.getOrCreate(storageNode, targetBinaryProperty, PropertyType.BINARY);
                    processImage(nd, cropperInfo, target, targetWidth, targetHeight);
                }
            }
        }
    }

    protected void processImage(NodeData binary, NodeData cropperInfoProp, NodeData target, int targetWidth, int targetHeight) throws IOException, RepositoryException {
        final Image img = getImage(binary);
        final CropperInfo cropperInfo = getCropperInfo(cropperInfoProp);

        final BufferedImage resized = imageResizer.resize(img, cropperInfo, targetWidth, targetHeight);

        final File tempImageFile = File.createTempFile("tmp-imageprocessor-", ".jpg");
        try {
            final OutputStream tempOut = new FileOutputStream(tempImageFile);
            ImageIO.write(resized, "jpg", tempOut); // TODO : file format (jpg, png, ...)
            tempOut.flush();
            IOUtils.closeQuietly(tempOut);

            // TODO this needs to be factored out of SaveHandlerImpl.saveDocument() and ScaleImageTag.createImageNode() !
            final InputStream tempIn = new FileInputStream(tempImageFile);
            target.setValue(tempIn);
            target.setAttribute(FileProperties.PROPERTY_CONTENTTYPE, "image/jpg"); // TODO file format
            target.setAttribute(FileProperties.PROPERTY_LASTMODIFIED, DateUtil.getCurrentUTCCalendar());

            IOUtils.closeQuietly(tempIn);
        } finally {
            tempImageFile.delete();
        }

    }

    protected CropperInfo getCropperInfo(NodeData cropperInfoProp) {
        final String jsonString = cropperInfoProp.getString();
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        final JSONObject json = new JSONObject(jsonString);
        return (CropperInfo) JSONObject.toBean(json, CropperInfo.class);
    }

    protected Image getImage(NodeData binary) throws IOException {
        final InputStream stream = binary.getStream();
        return ImageIO.read(stream);
    }

}
