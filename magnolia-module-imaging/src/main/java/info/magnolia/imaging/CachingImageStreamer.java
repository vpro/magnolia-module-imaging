/**
 * This file Copyright (c) 2009 Magnolia International
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
package info.magnolia.imaging;

import info.magnolia.cms.beans.runtime.FileProperties;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.cms.util.NodeDataUtil;
import org.apache.commons.io.IOUtils;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * An ImageStreamer which stores and serves generated images to/from a specific workspace.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CachingImageStreamer<P> implements ImageStreamer<P> {
    private static final String GENERATED_IMAGE_PROPERTY = "generated-image";

    private final HierarchyManager hm;
    private final ImageStreamer<P> delegate;

    public CachingImageStreamer(HierarchyManager hm, ImageStreamer<P> delegate) {
        this.hm = hm;
        this.delegate = delegate;
    }

    public void serveImage(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params, OutputStream out) throws IOException, ImagingException {
        InputStream imgStream = fetchFromCache(generator, params);
        if (imgStream == null) {
            // image is not in cache or should be regenerated
            // TODO - lock/wait on requests for the same image
           imgStream = generateAndStore(generator, params);
        }
        IOUtils.copy(imgStream, out);
    }

    /**
     * Gets an InputStream with data for the appropriate image, or null if the image should be regenerated.
     */
    protected InputStream fetchFromCache(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> parameterProvider) {
        final String nodePath = getGeneratedImageNodePath(generator, parameterProvider);
        try {
            if (!hm.isExist(nodePath)) {
                return null;
            }
            final Content imageNode = hm.getContent(nodePath);
            final NodeData nodeData = imageNode.getNodeData(GENERATED_IMAGE_PROPERTY);
            if (!nodeData.isExist()) {
                return null;
            }
            if (shouldRegenerate(nodeData, parameterProvider)) {
                return null;
            }
            final InputStream in = nodeData.getStream();
            if (in == null) {
                throw new IllegalStateException("Can't get InputStream from " + nodeData.getHandle());
            }

            return in;
        } catch (RepositoryException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    // TODO -- this is assuming that a generated node's path is retrievable from the params.
    // TODO -- since there might be querying involved, it might be more efficient and clear if this method would instead
    // TODO -- return the node instance directly
    protected String getGeneratedImageNodePath(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params) {
        final ParameterProviderFactory<?, P> factory = generator.getParameterProviderFactory();
        final String nodePathSuffix = factory.getGeneratedImageNodePath(params.getParameter());
        return "/" + generator.getName() + nodePathSuffix;
    }

    protected boolean shouldRegenerate(NodeData cachedBinary, ParameterProvider parameterProvider) {
        // TODO
        return false;
    }


    /**
     * Store the given image under the given path, and returns an InputStream to read the image back.
     */
    protected InputStream generateAndStore(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> parameterProvider) throws IOException, ImagingException {
        final String nodePath = getGeneratedImageNodePath(generator, parameterProvider);
        try {
            final Content imageNode = ContentUtil.createPath(hm, nodePath, false);
            final NodeData imageData = NodeDataUtil.getOrCreate(imageNode, GENERATED_IMAGE_PROPERTY, PropertyType.BINARY);

            final ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
            delegate.serveImage(generator, parameterProvider, tempOut);

            final ByteArrayInputStream tempIn = new ByteArrayInputStream(tempOut.toByteArray());
            imageData.setValue(tempIn);
            // TODO mimetype, lastmod, and other attributes ?
            imageData.setAttribute(FileProperties.PROPERTY_CONTENTTYPE, "image/" + generator.getOutputFormat().getFormatName());
            imageData.setAttribute(FileProperties.PROPERTY_LASTMODIFIED, Calendar.getInstance());

            hm.save();

            return imageData.getStream();
        } catch (RepositoryException e) {
            throw new ImagingException("Can't store rendered image: " + e.getMessage(), e);
        }
    }

}
