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
import com.google.common.collect.MapMaker;
import info.magnolia.cms.beans.runtime.FileProperties;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.cms.util.NodeDataUtil;
import info.magnolia.imaging.ImageStreamer;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ImageGenerator;
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
import java.util.Date;
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

    public CachingImageStreamer(HierarchyManager hm, ImageStreamer<P> delegate) {
        this.hm = hm;
        this.delegate = delegate;
    }

    /** TODO: make static if we don't use the exact same instance for all threads ? */
    /**
     * This Map is the key to understanding how this class works.
     * By using a computing map, we are essentially locking all requests
     * coming in for the same image (ImageGenerationJob) except one.
     * This Map implementation is written as such that the first call to get(K) will
     * generate the value (by calling <V> Function.apply(<K>). Further calls are
     * blocked until the value is generated, and they all retrieve the same value.
     *
     * TODO : exception handling. see ComputationException
     */
    final ConcurrentMap<ImageGenerationJob<P>, NodeData> currentJobs = new MapMaker()
//                    .concurrencyLevel(32)
//                    .softKeys()
//                    .weakValues()
            // entries from the map will be removed 500ms after their creation
            // TODO - is this correct ? or will it fail if the image generation takes longer ? (seemed to work with 3ms but then again the generation wasn't heavy)
            .expiration(500, TimeUnit.MILLISECONDS)

            .makeComputingMap(new Function<ImageGenerationJob<P>, NodeData>() {
                public NodeData apply(ImageGenerationJob<P> job) {
                    try {
                        System.out.println(new Date() + " Generating and storing for = " + job.params + " with " + job.generator);
                        return generateAndStore(job.generator, job.params);
                    } catch (IOException e) {
                        throw new RuntimeException(e); // TODO
                    } catch (ImagingException e) {
                        throw new RuntimeException(e); // TODO
                    }
                }
            });

    public void serveImage(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params, OutputStream out) throws IOException, ImagingException {
        NodeData imgProp = fetchFromCache(generator, params);
        if (imgProp == null) {
            // image is not in cache or should be regenerated
            System.out.println(new Date() + " not found in cache for = " + params + " with " + generator);
            imgProp = currentJobs.get(new ImageGenerationJob(generator, params));
            System.out.println(new Date() + " served from current jobs - currentJobs.size() = " + currentJobs.size());
        } else {
            System.out.println(new Date() + " found in cache - currentJobs.size() = " + currentJobs.size());
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

    protected boolean shouldRegenerate(NodeData cachedBinary, ParameterProvider parameterProvider) {
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

    // the IMPORTANT bit here is that we compare params.getParameter() and not params !
    // same thing for calculating the hash
    // we're assuming that whatever parameter is used provides a valid equals/hashCode() pair
    // TODO - and ... DefaultContent does not.
    // TODO - and since there isn't one single/simple way of implementing equals() for a node
    // (using just the uuid wouldn't be enough, it might have been modified, and here we might have the "same" node coming from 2 different sessions... etc etc)
    // TODO - then we might want to have an equals/hash pair at ParameterProvider(Factory) level

    protected static final class ImageGenerationJob<P> {
        private final ImageGenerator generator;
        private final ParameterProvider<P> params;

        private ImageGenerationJob(ImageGenerator generator, ParameterProvider<P> params) {
            this.generator = generator;
            this.params = params;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ImageGenerationJob that = (ImageGenerationJob) o;

            if (!generator.equals(that.generator)) return false;
            if (!params.getParameter().equals(that.params.getParameter())) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = generator.hashCode();
            result = 31 * result + params.getParameter().hashCode();
            return result;
        }

    }
}
