/**
 * This file Copyright (c) 2009-2013 Magnolia International
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.context.Context;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.caching.CachingStrategy;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ImagingServlet}.
 */
public class ImagingServletTest {

    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private ImagingServlet imagingServlet;
    private final ByteArrayOutputStream fakedOut = new ByteArrayOutputStream();
    private final OutputFormat outputFormat = new OutputFormat();
    private final ImagingModuleConfig cfg = new ImagingModuleConfig();;

    @Before
    public void setUp() throws Exception {
        // GIVEN
        final ServletOutputStream servletOut = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                fakedOut.write(b);
            }
        };
        final Context ctx = mock(Context.class);
        final HierarchyManager hm = mock(HierarchyManager.class);
        final Content node = mock(Content.class);
        final NodeData prop = mock(NodeData.class);

        when(ctx.getHierarchyManager("imaging")).thenReturn(hm);
        // TODO -- test should substitute another implementation of CachingImageStreamer
        // TODO -- either extract an interface, or make it an ImageGenerator
        when(hm.isExist("/myGenerator/path-to/dummyUri/generated")).thenReturn(true);
        when(hm.getContent("/myGenerator/path-to/dummyUri/generated")).thenReturn(node);

        when(node.getNodeData("generated-image")).thenReturn(prop);
        when(prop.isExist()).thenReturn(true);
        when(prop.getStream()).thenReturn(new ByteArrayInputStream(new byte[]{1,2,3}));
        when(req.getRequestURI()).thenReturn("dummyUri");
        when(res.getOutputStream()).thenReturn(servletOut);
        res.flushBuffer();

        final ParameterProviderFactory<HttpServletRequest, String> ppFactory = new ParameterProviderFactory<HttpServletRequest, String>() {
            @Override
            public ParameterProvider<String> newParameterProviderFor(HttpServletRequest context) {
                return new StringParameterProvider(context.getRequestURI());
            }

            @Override
            public CachingStrategy<String> getCachingStrategy() {
                return new StringBasedCachingStrategy();
            }
        };

        final TestImageGenerator<StringParameterProvider> generator = new TestImageGenerator<StringParameterProvider>(ppFactory, outputFormat);

        imagingServlet = new ImagingServlet() {
            @Override
            protected ImagingModuleConfig getImagingConfiguration() {
                cfg.addGenerator(generator.getName(), generator);
                return cfg;
            }
        };

        MgnlContext.setInstance(ctx);
    }

    @After
    public void tearDown() throws Exception {
        MgnlContext.setInstance(null);
    }

    @Test
    public void testRequestToFactoryToGeneratorToImage() throws Exception {
        // GIVEN
        when(req.getPathInfo()).thenReturn("/myGenerator/someWorkspace/some/path/to/a/node.png");
        outputFormat.setFormatName("png");

        // WHEN
        imagingServlet.doGet(req, res);

        // THEN
        verify(res, atMost(2)).flushBuffer();

        // TODO - disabled for now
        // assertTrue("ImageGenerator.generate() wasn't called !", generator.imageGeneratorWasCalled);

        // TODO - instead we're checking we've output what was given by the cache
        assertEquals(3, fakedOut.toByteArray().length);
        assertEquals(1, fakedOut.toByteArray()[0]);
        assertEquals(2, fakedOut.toByteArray()[1]);
        assertEquals(3, fakedOut.toByteArray()[2]);

        // TODO - disabled for now
        // yes, we could instead feed the test with a FileOutputStream instead of doing the shenanigans of reading/saving, but this piece below shouldn't stay here
        // it just generates a small black jpeg...
        //        final BufferedImage img = ImageIO.read(new ByteArrayInputStream(fakedOut.toByteArray()));
        //        final String filename = getClass().getSimpleName() + ".jpg";
        //        ImageIO.write(img, "jpg", new File(filename));
        // Runtime.getRuntime().exec("open " + filename);
    }

    @Test
    public void testDoGetWrongExtension() throws Exception {
        // GIVEN
        when(req.getPathInfo()).thenReturn("/myGenerator/someWorkspace/some/path/to/a/node.wrongExtension");
        outputFormat.setFormatName("png");
        cfg.setServeOnlyForCorrectExtension(true);

        // WHEN
        imagingServlet.doGet(req, res);

        // THEN
        verify(res).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private static class TestImageGenerator<P extends ParameterProvider<?>> implements ImageGenerator<P> {
        private final ParameterProviderFactory ppFactory;
        private final OutputFormat outputFormat;
        boolean imageGeneratorWasCalled;

        public TestImageGenerator(ParameterProviderFactory ppFactory, OutputFormat outputFormat) {
            this.ppFactory = ppFactory;
            this.outputFormat = outputFormat;
            imageGeneratorWasCalled = false;
        }

        @Override
        public BufferedImage generate(P params) {
            assertEquals("dummyUri", params.getParameter());
            imageGeneratorWasCalled = true;
            return new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public ParameterProviderFactory getParameterProviderFactory() {
            return ppFactory;
        }

        @Override
        public OutputFormat getOutputFormat(P params) {
            return outputFormat;
        }

        @Override
        public String getName() {
            return "myGenerator";
        }
    }
}
