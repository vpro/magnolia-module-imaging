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

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.context.Context;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.caching.CachingStrategy;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImagingServletTest extends TestCase {

    protected void tearDown() throws Exception {
        MgnlContext.setInstance(null);
        super.tearDown();
    }

    public void testRequestToFactoryToGeneratorToImage() throws Exception {
        final ByteArrayOutputStream fakedOut = new ByteArrayOutputStream();
        final ServletOutputStream servletOut = new ServletOutputStream() {
            public void write(int b) throws IOException {
                fakedOut.write(b);
            }
        };
        final Context ctx = createStrictMock(Context.class);
        final HierarchyManager hm = createStrictMock(HierarchyManager.class);
        final Content node = createStrictMock(Content.class);
        final NodeData prop = createStrictMock(NodeData.class);
        final HttpServletRequest req = createStrictMock(HttpServletRequest.class);
        final HttpServletResponse res = createStrictMock(HttpServletResponse.class);
        expect(ctx.getHierarchyManager("imaging")).andReturn(hm);
        // TODO -- test should substitute another implementation of CachingImageStreamer
        // TODO -- either extract an interface, or make it an ImageGenerator
        expect(hm.isExist("/myGenerator/path-to/dummyUri/generated")).andReturn(true);
        expect(hm.getContent("/myGenerator/path-to/dummyUri/generated")).andReturn(node);

        expect(node.getNodeData("generated-image")).andReturn(prop);
        expect(prop.isExist()).andReturn(true);
        expect(prop.getStream()).andReturn(new ByteArrayInputStream(new byte[]{1,2,3}));
//        expect(node.hasNodeData("generated-image")).andReturn(false);
        expect(req.getPathInfo()).andReturn("/myGenerator/someWorkspace/some/path/to/a/node");
        expect(req.getRequestURI()).andReturn("dummyUri");
        expect(res.getOutputStream()).andReturn(servletOut);
        res.flushBuffer();

        final ParameterProviderFactory<HttpServletRequest, String> ppFactory = new ParameterProviderFactory<HttpServletRequest, String>() {
            public ParameterProvider<String> newParameterProviderFor(HttpServletRequest context) {
                return new StringParameterProvider(context.getRequestURI());
            }

            public CachingStrategy<String> getCachingStrategy() {
                return new StringBasedCachingStrategy();
            }
        };

        final OutputFormat outputFormat = new OutputFormat();
        outputFormat.setFormatName("png");
        final TestImageGenerator<StringParameterProvider> generator = new TestImageGenerator<StringParameterProvider>(ppFactory, outputFormat);

        final ImagingServlet imagingServlet = new ImagingServlet() {
            @Override
            protected ImagingModuleConfig getImagingConfiguration() {
                final ImagingModuleConfig cfg = new ImagingModuleConfig();
                cfg.addGenerator(generator.getName(), generator);
                return cfg;
            }
        };

        replay(ctx, hm, node, prop, req, res);
        MgnlContext.setInstance(ctx);
        imagingServlet.doGet(req, res);
        verify(ctx, hm, node, prop, req, res);

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

    private static class TestImageGenerator<P extends ParameterProvider<?>> implements ImageGenerator<P> {
        private final ParameterProviderFactory ppFactory;
        private final OutputFormat outputFormat;
        boolean imageGeneratorWasCalled;

        public TestImageGenerator(ParameterProviderFactory ppFactory, OutputFormat outputFormat) {
            this.ppFactory = ppFactory;
            this.outputFormat = outputFormat;
            imageGeneratorWasCalled = false;
        }

        public BufferedImage generate(P params) {
            assertEquals("dummyUri", params.getParameter());
            imageGeneratorWasCalled = true;
            return new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        }

        public ParameterProviderFactory getParameterProviderFactory() {
            return ppFactory;
        }

        public OutputFormat getOutputFormat() {
            return outputFormat;
        }

        public String getName() {
            return "myGenerator";
        }
    }
}
