/**
 * This file Copyright (c) 2009-2011 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.imaging;

import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.util.BooleanUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.caching.CachingImageStreamer;
import info.magnolia.imaging.caching.CachingStrategy;
import info.magnolia.imaging.util.PathSplitter;
import info.magnolia.module.ModuleRegistry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet responsible for the actual generation of the images.
 * TODO This servlet might need some investigation - improvements; particularly how the parameterProvider, and various factories are bound together.
 *
 * During development / tests of generators, set the storeGeneratedImages parameter to "false".
 *
 * @version $Id$
 */
public class ImagingServlet extends HttpServlet {
    private boolean storeGeneratedImages = true;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storeGeneratedImages = BooleanUtil.toBoolean(config.getInitParameter("storeGeneratedImages"), true);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String generatorName = getImageGeneratorName(request);
        final ImageGenerator generator = getGenerator(generatorName);
        if (generator == null) {
            throw new IllegalArgumentException("No ImageGenerator with name " + generatorName);
        }

        final ParameterProviderFactory parameterProviderFactory = generator.getParameterProviderFactory();
        final ParameterProvider p = parameterProviderFactory.newParameterProviderFor(request);

        try {
            // TODO -- mimetype etc.
            final ImageStreamer streamer = getStreamer(parameterProviderFactory);
            streamer.serveImage(generator, p, response.getOutputStream());

            response.flushBuffer();
            // IOUtils.closeQuietly(response.getOutputStream());
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

    protected ImageStreamer getStreamer(ParameterProviderFactory parameterProviderFactory) {
        // TODO -- CachingImageStreamer currently has a non-static "currentJobs" map.
        // TODO -- to investigate, but I highly suspect that it's not really useful if
        // we use a different instance of CachingImageStreamer for every request.

        final DefaultImageStreamer newDefaultImageStreamer = new DefaultImageStreamer();
        if (!storeGeneratedImages) {
            return newDefaultImageStreamer;
        } else {
            final HierarchyManager hm = MgnlContext.getHierarchyManager("imaging");
            final CachingStrategy cachingStrategy = parameterProviderFactory.getCachingStrategy();
            return new CachingImageStreamer(hm, cachingStrategy, newDefaultImageStreamer);
        }
    }

    protected ImagingModuleConfig getImagingConfiguration() {
        return (ImagingModuleConfig) ModuleRegistry.Factory.getInstance().getModuleInstance("imaging");
    }

}
