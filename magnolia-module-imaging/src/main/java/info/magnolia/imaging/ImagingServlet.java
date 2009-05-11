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

import info.magnolia.imaging.util.PathSplitter;
import info.magnolia.module.ModuleRegistry;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * Filter responsible for the actual generation of the images.
 * TODO This is totally temporary code.
 * TODO And please find a decent name for this.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImagingServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String generatorName = getImageGeneratorName(request);
        final ImageGenerator generator = getGenerator(generatorName);
        if (generator == null) {
            throw new IllegalArgumentException("No ImageGenerator with name " + generatorName);
        }

        final ParameterProviderFactory parameterProviderFactory = generator.getParameterProviderFactory();
        final ParameterProvider p = parameterProviderFactory.newParameterProviderFor(request);

        try {
            final BufferedImage result = generator.generate(p);
            // TODO -- mimetype etc.
            write(result, response.getOutputStream(), generator.getOutputFormat());

            response.flushBuffer();
            // IOUtils.closeQuietly(response.getOutputStream());
        } catch (ImagingException e) {
            throw new ServletException(e); // TODO
        }
    }

    // todo : move this method out of ImagingServlet
    public void write(BufferedImage img, final OutputStream out, final OutputFormat outputFormat) throws IOException {
        // if source is transparent and format doesn't support transparency, we have to do some handy work
        if (img.getTransparency() != Transparency.OPAQUE && !outputFormat.supportsTransparency()) {
            final WritableRaster raster = img.getRaster();
            final WritableRaster newRaster = raster.createWritableChild(0, 0, img.getWidth(), img.getHeight(), 0, 0, new int[]{0, 1, 2});

            // create a ColorModel that represents the one of the ARGB except the alpha channel
            final DirectColorModel cm = (DirectColorModel) img.getColorModel();
            final DirectColorModel newCM = new DirectColorModel(cm.getPixelSize(), cm.getRedMask(), cm.getGreenMask(), cm.getBlueMask());

            // now create the new buffer that we'll use to write the image
            img = new BufferedImage(newCM, newRaster, false, null);
        }

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
     * Determines the ImageGenerator to use, using the first path element of the pathInfo.
     */
    protected String getImageGeneratorName(HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();
        return new PathSplitter(pathInfo).skipTo(0);
    }

    protected ImageGenerator getGenerator(String generatorName) {
        final ImagingModuleConfig config = getImagingConfiguration();
        return config.getGenerators().get(generatorName);
    }

    protected ImagingModuleConfig getImagingConfiguration() {
        return (ImagingModuleConfig) ModuleRegistry.Factory.getInstance().getModuleInstance("imaging");
    }

}
