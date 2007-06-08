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
import info.magnolia.module.imageresizer.cropresize.CropAndResizeControl;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ClassUtils;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * In a given node, searches for binary properties with associated cropper properties,
 * and generates appropriate resized/cropped images.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImagesProcessor {
    private final Map decodeParamsClasses;
    private final ImageFilter imageFilter;

    public ImagesProcessor(ImageFilter imageFilter) {
        this.imageFilter = imageFilter;
        decodeParamsClasses = new HashMap();
        // TODO loop over filters
        decodeParamsClasses.put(imageFilter.getParameterType().getSimpleName(), imageFilter.getParameterType());
    }

    /**
     * @param storageNode where the resized image should be stored
     * @param dialogControlConfigNode where the control is configured (for targetWidth and targetHeight)
     */
    public void processImages(Content storageNode, Content dialogControlConfigNode) throws RepositoryException, IOException {
        // let'name loop through all properties, see which ones are binaries and see if they have a corresponding cropperInfo
        final Collection props = storageNode.getNodeDataCollection();
        final Iterator it = props.iterator();
        while (it.hasNext()) {
            NodeData nd = (NodeData) it.next();
            if (nd.getType() == PropertyType.BINARY) {
                final String binaryName = nd.getName();
                final String potentialCropperInfoProperty = CropAndResizeControl.getCropperInfoPropertyName(binaryName);
                if (storageNode.hasNodeData(potentialCropperInfoProperty)) {
                    final NodeData filteringParams = storageNode.getNodeData(potentialCropperInfoProperty);
                    final String targetBinaryProperty = CropAndResizeControl.getTargetBinaryPropertyName(binaryName);
                    final NodeData target = NodeDataUtil.getOrCreate(storageNode, targetBinaryProperty, PropertyType.BINARY);
                    processImage(nd, filteringParams, target, dialogControlConfigNode);
                }
            }
        }
    }

    protected void processImage(NodeData binary, NodeData filteringParamsProp, NodeData target, Content dialogControlConfigNode) throws IOException, RepositoryException {
        final Image img = getImage(binary);
        final Map filteringParams = getParams(filteringParamsProp);

        // TODO : loop over filters and apply each with their own config ?
        final String filterParamName = ClassUtils.getShortClassName(imageFilter.getParameterType());
        final Object filterParams = filteringParams.get(filterParamName);
        final BufferedImage filtered = imageFilter.apply(img, filterParams, dialogControlConfigNode);

        final File tempImageFile = File.createTempFile("tmp-imageprocessor-", ".jpg");
        try {
            final OutputStream tempOut = new FileOutputStream(tempImageFile);
            ImageIO.write(filtered, "jpg", tempOut); // TODO : MGNLIMG-11 file format (jpg, png, ...)
            tempOut.flush();
            IOUtils.closeQuietly(tempOut);

            // TODO this needs to be factored out of SaveHandlerImpl.saveDocument() and ScaleImageTag.createImageNode() !
            final InputStream tempIn = new FileInputStream(tempImageFile);
            target.setValue(tempIn);
            target.setAttribute(FileProperties.PROPERTY_CONTENTTYPE, "image/jpg"); // TODO MGNLIMG-11 file format
            target.setAttribute(FileProperties.PROPERTY_LASTMODIFIED, DateUtil.getCurrentUTCCalendar());

            IOUtils.closeQuietly(tempIn);
        } finally {
            tempImageFile.delete();
        }

    }

    protected Map getParams(NodeData paramsProp) {
        final String jsonString = paramsProp.getString();
        if (StringUtils.isEmpty(jsonString)) {
            return Collections.EMPTY_MAP;
        }
        final JSONObject json = new JSONObject(jsonString);
        return (Map) JSONObject.toBean(json, Map.class, decodeParamsClasses);
    }

    protected Image getImage(NodeData binary) throws IOException {
        final InputStream stream = binary.getStream();
        return ImageIO.read(stream);
    }
}
