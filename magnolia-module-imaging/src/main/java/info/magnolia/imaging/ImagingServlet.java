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

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
            // TODO use OutputFormat
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (ImagingException e) {
            throw new ServletException(e); // TODO
        }
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
