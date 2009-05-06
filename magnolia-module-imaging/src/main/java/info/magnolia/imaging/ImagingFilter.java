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

import info.magnolia.cms.filters.AbstractMgnlFilter;
import info.magnolia.cms.core.Content;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.context.MgnlContext;

import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
public class ImagingFilter extends AbstractMgnlFilter {
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String filterName = "chain";

        final ImagingModuleConfig<NodeParameterProvider> config = (ImagingModuleConfig<NodeParameterProvider>) ModuleRegistry.Factory.getInstance().getModuleInstance("imaging");
        final ImageGenerator<NodeParameterProvider> generator = config.getGenerators().get(filterName);

        final Content content = MgnlContext.getAggregationState().getCurrentContent();
        
        final NodeParameterProvider p = new NodeParameterProvider(content);
        final BufferedImage result = generator.generate(p);

        // TODO -- mimetype etc.
        ImageIO.write(result, "jpg", response.getOutputStream());

        // TODO use OutputFormat
    }
}
