/**
 * This file Copyright (c) 2009-2010 Magnolia International
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
import java.util.concurrent.locks.ReentrantLock;

/**
 * An ImageStreamer which stores and serves generated images to/from a specific workspace.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CachingImageStreamer<P> implements ImageStreamer<P> {
    private static final String GENERATED_IMAGE_PROPERTY = "generated-image";

    private final HierarchyManager hm;
    private final CachingStrategy<P> cachingStrategy;
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

    /**
     * Despite the currentJobs doing quite a good job at avoiding multiple requests
     * for the same job, we still need to lock around JCR operations, otherwise multiple
     * requests end up creating the same cachePath (or parts of it), thus yielding
     * InvalidItemStateException: "Item cannot be saved because it has been modified externally".
     * TODO - this is currently static because we *know* ImagingServlet uses a different instance
     * of CachingImageStreamer for every request. This is not exactly the most elegant.
     * TODO - see related TODO in currentJobs and info.magnolia.imaging.ImagingServlet#getStreamer
     */
    private static final ReentrantLock lock = new ReentrantLock();

    public CachingImageStreamer(HierarchyManager hm, CachingStrategy<P> cachingStrategy, ImageStreamer<P> delegate) {
        this.hm = hm;
        this.cachingStrategy = cachingStrategy;
        this.delegate = delegate;

        this.currentJobs = new MapMaker()
        //                    .concurrencyLevel(32)
        //                    .softKeys() weakKeys()
        //                    .softValues() weakValues()
        // entries from the map will be removed 500ms after their creation,
        // thus unblocking further requests for an equivalent job.
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
                    throw new RuntimeException(e);
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
                unwrapRuntimeException(e);
            }
        }
        serve(imgProp, out);
    }

    /**
     * Gets the binary property (NodeData) for the appropriate image, ready to be served,
     * or null if the image should be regenerated.
     */
    protected NodeData fetchFromCache(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> parameterProvider) {
        final String cachePath = cachingStrategy.getCachePath(generator, parameterProvider);
        if (cachePath == null) {
            // the CachingStrategy decided it doesn't want us to cache :(
            return null;
        }
        try {
            if (!hm.isExist(cachePath)) {
                return null;
            }
            final Content imageNode = hm.getContent(cachePath);
            final NodeData nodeData = imageNode.getNodeData(GENERATED_IMAGE_PROPERTY);
            if (!nodeData.isExist()) {
                return null;
            }
            InputStream in = nodeData.getStream();
            if (in == null) {
                // binary data were not stored yet
                return null;
            } else {
                IOUtils.closeQuietly(in);
            }

            if (cachingStrategy.shouldRegenerate(nodeData, parameterProvider)) {
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
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(out);
    }

    protected NodeData generateAndStore(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> parameterProvider) throws IOException, ImagingException {
        // generate
        final ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
        delegate.serveImage(generator, parameterProvider, tempOut);

        // it's time to lock now, we can only save one node at a time, since we'll be working on the same nodes as other threads
        lock.lock();
        try {
            // create cachePath if needed
            final String cachePath = cachingStrategy.getCachePath(generator, parameterProvider);
            final Content cacheNode = ContentUtil.createPath(hm, cachePath, false);
            final NodeData imageData = NodeDataUtil.getOrCreate(cacheNode, GENERATED_IMAGE_PROPERTY, PropertyType.BINARY);

            // store generated image
            final ByteArrayInputStream tempIn = new ByteArrayInputStream(tempOut.toByteArray());
            imageData.setValue(tempIn);
            // TODO mimetype, lastmod, and other attributes ?
            imageData.setAttribute(FileProperties.PROPERTY_CONTENTTYPE, "image/" + generator.getOutputFormat().getFormatName());
            imageData.setAttribute(FileProperties.PROPERTY_LASTMODIFIED, Calendar.getInstance());

            // Update metadata of the cache *after* a succesfull image generation (creationDate has been set when creating
            // Since this might be called from a different thread than the actual request, we can't call cacheNode.updateMetaData(), which by default tries to set the authorId by using the current context
            cacheNode.getMetaData().setModificationDate();

            // finally save it all
            hm.save();
            return imageData;
        } catch (RepositoryException e) {
            throw new ImagingException("Can't store rendered image: " + e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Unwrap ComputationExceptions wrapping a RuntimeException wrapping an ImagingException or IOException,
     * as thrown by the Function of the computing map.
     * @see #currentJobs
     */
    private void unwrapRuntimeException(RuntimeException e) throws ImagingException, IOException {
        final Throwable cause = e.getCause();
        if (cause instanceof ImagingException) {
            throw (ImagingException) cause;
        } else if (cause instanceof IOException) {
            throw (IOException) cause;
        } else if (cause instanceof RuntimeException) {
            unwrapRuntimeException((RuntimeException) cause);
        } else if (cause == null) {
            // This really, really, should not happen... but we'll let this exception bubble up
            throw new IllegalStateException("Unexpected and unhandled exception: " + (e.getMessage() != null ? e.getMessage() : ""), e);
        } else {
            // this shouldn't happen either, actually.
            throw new ImagingException(e.getMessage(), cause);
        }
    }
}
