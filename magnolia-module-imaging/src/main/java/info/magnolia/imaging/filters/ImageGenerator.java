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
package info.magnolia.imaging.filters;

import info.magnolia.cms.filters.AbstractMgnlFilter;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.imaging.Config;

import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Filter responsible for the actual generation of the images.
 * This is totally temporary code.
 *
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageGenerator extends AbstractMgnlFilter {
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String filterName = "chain";

        final Config config = (Config) ModuleRegistry.Factory.getInstance().getModuleInstance("imaging");
        final ImageFilter<?> filter = config.getFilters().get(filterName);
        final BufferedImage result = filter.apply(null, null);

        // TODO -- mimetype etc.
        ImageIO.write(result, "jpg", response.getOutputStream());
    }
}
