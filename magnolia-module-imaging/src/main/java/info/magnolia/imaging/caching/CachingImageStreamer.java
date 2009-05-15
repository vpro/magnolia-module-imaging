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
package info.magnolia.imaging.caching;

import com.google.common.base.Function;
import com.google.common.collect.ComputationException;
import com.google.common.collect.MapMaker;
import info.magnolia.cms.beans.runtime.FileProperties;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.cms.util.NodeDataUtil;
import info.magnolia.imaging.ImageGenerator;
import info.magnolia.imaging.ImageStreamer;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.ParameterProviderFactory;
import org.apache.commons.io.IOUtils;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

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

    /** TODO: make static if we don't use the exact same instance for all threads ? */
    /**
     * This Map is the key to understanding how this class works.
     * By using a ConcurrentMap, we are essentially locking all requests
     * coming in for the same image (ImageGenerationJob) except the first one.
     *
     * MapMaker.makeComputingMap() returns a Map implemented as such that the
     * first call to get(K) will generate the value (by calling <V> Function.apply(<K>).
     * Further calls are blocked until the value is generated, and they all retrieve the same value.
     */
    private final ConcurrentMap<ImageGenerationJob<P>, NodeData> currentJobs;

    public CachingImageStreamer(HierarchyManager hm, ImageStreamer<P> delegate) {
        this.hm = hm;
        this.delegate = delegate;

        this.currentJobs = new MapMaker()
//                    .concurrencyLevel(32)
//                    .softKeys() weakKeys()
//                    .softValues() weakValues()
                // entries from the map will be removed 500ms after their creation
                // TODO - is this correct ? or will it fail if the image generation takes longer ? (seemed to work with 3ms but then again the generation wasn't heavy)
                .expiration(500, TimeUnit.MILLISECONDS)

                .makeComputingMap(new Function<ImageGenerationJob<P>, NodeData>() {
                    public NodeData apply(ImageGenerationJob<P> job) {
                        try {
                            return generateAndStore(job.getGenerator(), job.getParams());
                        } catch (IOException e) {
                            // the map will further wrap these in ComputationExceptions, and we will, in turn, unwrap them ...
                            throw new RuntimeException(e);
                        } catch (ImagingException e) {
                            // the map will further wrap these in ComputationExceptions, and we will, in turn, unwrap them ...
                            throw new ComputationException(e);
                        }
                    }
                });
    }

    public void serveImage(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params, OutputStream out) throws IOException, ImagingException {
        NodeData imgProp = fetchFromCache(generator, params);
        if (imgProp == null) {
            // image is not in cache or should be regenerated
            try {
                imgProp = currentJobs.get(new ImageGenerationJob<P>(generator, params));
            } catch (ComputationException e) {
                // thrown if the ComputingMap's Function failed
                throw new ImagingException(e.getMessage(), e.getCause());
            }
        }
        serve(imgProp, out);
    }

    /**
     * Gets the binary property (NodeData) for the appropriate image, ready to be served,
     * or null if the image should be regenerated.
     */
    protected NodeData fetchFromCache(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> parameterProvider) {
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
            return nodeData;
        } catch (RepositoryException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    protected void serve(NodeData binary, OutputStream out) throws IOException {
        final InputStream in = binary.getStream();
        if (in == null) {
            throw new IllegalStateException("Can't get InputStream from " + binary.getHandle());
        }
        IOUtils.copy(in, out);
    }

    // TODO -- this is assuming that a generated node's path is retrievable from the params.
    // TODO -- since there might be querying involved, it might be more efficient and clear if this method would instead
    // TODO -- return the node instance directly
    protected String getGeneratedImageNodePath(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params) {
        final ParameterProviderFactory<?, P> factory = generator.getParameterProviderFactory();
        final String nodePathSuffix = factory.getGeneratedImageNodePath(params.getParameter());
        return "/" + generator.getName() + nodePathSuffix;
    }

    protected boolean shouldRegenerate(NodeData cachedBinary, ParameterProvider<P> parameterProvider) {
        // TODO
        return false;
    }

    protected NodeData generateAndStore(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> parameterProvider) throws IOException, ImagingException {
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

            return imageData;
        } catch (RepositoryException e) {
            throw new ImagingException("Can't store rendered image: " + e.getMessage(), e);
        }
    }
}
