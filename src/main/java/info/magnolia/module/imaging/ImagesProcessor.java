/**
 * This file Copyright (c) 2007-2011 Magnolia International
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
package info.magnolia.module.imaging;

import info.magnolia.cms.beans.runtime.FileProperties;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.DateUtil;
import info.magnolia.cms.util.NodeDataUtil;
import info.magnolia.module.cropui.CropAndResizeControl;
import info.magnolia.content2bean.Content2BeanUtil;
import info.magnolia.content2bean.Content2BeanException;
import info.magnolia.imaging.operations.ImageFilter;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
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
 * -- this class is a remnant of the previous incarnation of m-m-imagefiltering - only kept around for reference until m-m-cropui is back into a useable state.
 * 
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
//        decodeParamsClasses.put(imageFilter.getParameterType().getSimpleName(), imageFilter.getParameterType());
    }

    /**
     * @param storageNode where the resized image should be stored
     * @param dialogControlConfigNode where the control is configured (for targetWidth and targetHeight)
     */
    public void processImages(Content storageNode, Content dialogControlConfigNode) throws RepositoryException, IOException, Content2BeanException {
        // let's loop through all properties, see which ones are binaries and see if they have a corresponding cropperInfo
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

    protected void processImage(NodeData binary, NodeData filteringParamsProp, NodeData target, Content dialogControlConfigNode) throws IOException, RepositoryException, Content2BeanException {
        final BufferedImage img = getImage(binary);
        final Map filteringParams = getUserParams(filteringParamsProp);

        // TODO : loop over filters and apply each with their own config ?
//        final String filterParamName = ClassUtils.getShortClassName(imageFilter.getParameterType());
        final Object filterParams = filteringParams.get("FOOBAR");
        final BufferedImage filtered = imageFilter.apply(img, filterParams, dialogControlConfigNode);

        final OuputFormat imageFormat = getImageFormat(dialogControlConfigNode);
        final String formatName = imageFormat.getFormatName();
        final File tempImageFile = File.createTempFile("magnolia-module-imagefiltering-", "." + formatName);
        try {
            final OutputStream tempOut = new FileOutputStream(tempImageFile);

            // this is factored out of ImageIO.write() to allow customization of the ImageWriteParam
            final ImageOutputStream imgOut = ImageIO.createImageOutputStream(tempOut);
            final ImageTypeSpecifier imgType = ImageTypeSpecifier.createFromRenderedImage(filtered);
            final Iterator iter = ImageIO.getImageWriters(imgType, formatName);
            if (!iter.hasNext()) {
                throw new IllegalStateException("Can't find ImageWriter for " + formatName);
            }
            final ImageWriter imageWriter = (ImageWriter) iter.next();
            final ImageWriteParam params = imageWriter.getDefaultWriteParam();
            imageFormat.applyTo(params);
            imageWriter.setOutput(imgOut);
            imageWriter.write(null, new IIOImage(filtered, null, null), params);
            imgOut.flush();
            imageWriter.dispose();
            imgOut.close();

            tempOut.flush();
            IOUtils.closeQuietly(tempOut);

            // TODO this needs to be factored out of SaveHandlerImpl.saveDocument() and ScaleImageTag.createImageNode() !
            final InputStream tempIn = new FileInputStream(tempImageFile);
            target.setValue(tempIn);
            target.setAttribute(FileProperties.PROPERTY_CONTENTTYPE, "image/" + formatName);
            target.setAttribute(FileProperties.PROPERTY_LASTMODIFIED, DateUtil.getCurrentUTCCalendar());
            target.setAttribute(FileProperties.PROPERTY_FILENAME, "filtered"); // TODO : change this hardcoded name ?
            target.setAttribute(FileProperties.PROPERTY_EXTENSION, formatName);
            target.setAttribute(FileProperties.PROPERTY_SIZE, Long.toString(tempImageFile.length()));
            target.setAttribute(FileProperties.PROPERTY_WIDTH, Integer.toString(filtered.getWidth()));
            target.setAttribute(FileProperties.PROPERTY_HEIGHT, Integer.toString(filtered.getHeight()));

            IOUtils.closeQuietly(tempIn);
        } finally {
            tempImageFile.delete();
        }

    }

    protected OuputFormat getImageFormat(Content dialogControlConfigNode) throws Content2BeanException {
        // TODO : use content2bean
        return (OuputFormat) Content2BeanUtil.setProperties(new OuputFormat(), dialogControlConfigNode);
    }

    protected Map getUserParams(NodeData paramsProp) {
        final String jsonString = paramsProp.getString();
        if (StringUtils.isEmpty(jsonString)) {
            return Collections.EMPTY_MAP;
        }
        final JSONObject json = new JSONObject(jsonString);
        return (Map) JSONObject.toBean(json, Map.class, decodeParamsClasses);
    }

    protected BufferedImage getImage(NodeData binary) throws IOException {
        final InputStream stream = binary.getStream();
        return ImageIO.read(stream);
    }
}
