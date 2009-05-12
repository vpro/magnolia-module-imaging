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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Iterator;

/**
 *
 * TODO : cleanup API, this is proof-of-concept / temp
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CachingAndStoringImageGenerator<P> {
    private static final String GENERATED_IMAGE_PROPERTY = "generated-image";

    private final HierarchyManager hm;

    public CachingAndStoringImageGenerator(HierarchyManager hm) {
        this.hm = hm;
    }

    public void serveImage(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params, OutputStream out) throws IOException, ImagingException {
        // TODO -- this is assuming that a generated node's path is retrievable from the params.
        // TODO -- since there might be querying involved, it might be more efficient and clear if this method would instead
        // TODO -- return the node instance directly
        final String nodePath = getGeneratedImageNodePath(generator, params);
        InputStream imgStream = fromCache(nodePath, params);
        if (imgStream == null) {
            final BufferedImage img = generator.generate(params);

            imgStream = store(img, nodePath, generator.getOutputFormat());
        }
        IOUtils.copy(imgStream, out);
    }

    protected String getGeneratedImageNodePath(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params) {
        final ParameterProviderFactory<?, P> factory = generator.getParameterProviderFactory();
        final String nodePathSuffix = factory.getGeneratedImageNodePath(params.getParameter());
        return "/" + generator.getName() + nodePathSuffix;
    }

    /**
     * Gets an InputStream with data for the appropriate image, or null if the image should be regenerated.
     */
    private InputStream fromCache(String nodePath, ParameterProvider<P> p) {
        try {
            if (!hm.isExist(nodePath)) {
                return null;
            }
            final Content imageNode = hm.getContent(nodePath);
            final NodeData nodeData = imageNode.getNodeData(GENERATED_IMAGE_PROPERTY);
            if (!nodeData.isExist()) {
                return null;
            }
            if (shouldRegenerate(nodeData, p)) {
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

    private boolean shouldRegenerate(NodeData nodeData, ParameterProvider p) {
        // TODO
        return false;
    }

    /**
     * Store the given image under the given path, and returns an InputStream to read the image back.
     */
    private InputStream store(BufferedImage img, String nodePath, OutputFormat outputFormat) throws IOException {
        try {
            final Content imageNode = ContentUtil.createPath(hm, nodePath, false);
            final NodeData imageData = NodeDataUtil.getOrCreate(imageNode, GENERATED_IMAGE_PROPERTY, PropertyType.BINARY);

            final ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
            write(img, tempOut, outputFormat);

            final ByteArrayInputStream tempIn = new ByteArrayInputStream(tempOut.toByteArray());
            imageData.setValue(tempIn);
            // TODO
            imageData.setAttribute(FileProperties.PROPERTY_CONTENTTYPE, "image/" + outputFormat.getFormatName());
            imageData.setAttribute(FileProperties.PROPERTY_LASTMODIFIED, Calendar.getInstance());

            hm.save();

            return imageData.getStream();
        } catch (RepositoryException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    // todo : moved this method out of ImagingServlet - used by tests but should not be public
    // TODO : move this to yet another more appropriate place ?
    public void write(BufferedImage img, final OutputStream out, final OutputFormat outputFormat) throws IOException {
        // if source is transparent and format doesn't support transparency, we have to do some handy work
        img = flattenTransparentImageForOpaqueFormat(img, outputFormat);

        // this is factored out of ImageIO.write() to allow customization of the ImageWriteParam
        final ImageOutputStream imgOut = ImageIO.createImageOutputStream(out);
        final ImageTypeSpecifier imgType = ImageTypeSpecifier.createFromRenderedImage(img);
        final Iterator<ImageWriter> iter = ImageIO.getImageWriters(imgType, outputFormat.getFormatName());
        if (!iter.hasNext()) {
            throw new IllegalStateException("Can't find ImageWriter for " + outputFormat.getFormatName());
        }
        final ImageWriter imageWriter = iter.next();
        final ImageWriteParam params = imageWriter.getDefaultWriteParam();

        outputFormat.applyTo(params);
        imageWriter.setOutput(imgOut);

        imageWriter.write(null, new IIOImage(img, null, null), params);
        imageWriter.dispose();
    }

    /**
     * @see info.magnolia.imaging.SelfTest#testJpegOddity()
     */
    protected BufferedImage flattenTransparentImageForOpaqueFormat(BufferedImage img, OutputFormat outputFormat) {
        if (img.getTransparency() != Transparency.OPAQUE && !outputFormat.supportsTransparency()) {
            final WritableRaster raster = img.getRaster();
            final WritableRaster newRaster = raster.createWritableChild(0, 0, img.getWidth(), img.getHeight(), 0, 0, new int[]{0, 1, 2});

            // create a ColorModel that represents the one of the ARGB except the alpha channel
            final DirectColorModel cm = (DirectColorModel) img.getColorModel();
            final DirectColorModel newCM = new DirectColorModel(cm.getPixelSize(), cm.getRedMask(), cm.getGreenMask(), cm.getBlueMask());

            // now create the new buffer that we'll use to write the image
            img = new BufferedImage(newCM, newRaster, false, null);
        }
        return img;
    }

}
