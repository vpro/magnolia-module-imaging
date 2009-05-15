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

import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.util.FactoryUtil;
import info.magnolia.cms.util.HierarchyManagerWrapper;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.DefaultImageStreamer;
import info.magnolia.imaging.ImageGenerator;
import info.magnolia.imaging.ImageStreamer;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.OutputFormat;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.ParameterProviderFactory;
import info.magnolia.imaging.StringParameterProvider;
import info.magnolia.imaging.operations.ImageOperationChain;
import info.magnolia.logging.AuditLoggingManager;
import info.magnolia.module.ModuleManagementException;
import info.magnolia.module.ModuleManager;
import info.magnolia.module.ModuleManagerImpl;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.module.model.ModuleDefinition;
import info.magnolia.module.model.reader.ModuleDefinitionReader;
import info.magnolia.test.RepositoryTestCase;
import static org.easymock.EasyMock.createNiceMock;

import javax.imageio.ImageIO;
import javax.jcr.RepositoryException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * TODO : this is sort of a load test and uses a real (in memory) repository.
 * TODO : cleanup.
 * TODO : ensure generation is trigger only once properly, not by looking at the console output.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CachingImageStreamerRepositoryTest extends RepositoryTestCase {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CachingImageStreamerRepositoryTest.class);

    @Override
    protected void setUp() throws Exception {
        setAutoStart(false);
        super.setUp();
        cleanUp();
        startRepository();
        // autostart also triggers the repo shutdown in teardown... but we should wait.. TODO -- check how best to do this
        FactoryUtil.setInstance(AuditLoggingManager.class, new AuditLoggingManager());
    }

    public void testRequestForSimilarUncachedImageOnlyGeneratesItOnce() throws Exception {
        final ParameterProviderFactory<Object, String> ppf = new ParameterProviderFactory<Object, String>() {
            public ParameterProvider newParameterProviderFor(Object environment) {
                return new StringParameterProvider("MY PARAM YO");
            }

            public String getGeneratedImageNodePath(String p) {
                return p.toLowerCase().replace(' ', '/');
            }
        };

        final OutputFormat png = new OutputFormat();
        png.setFormatName("png");

        final ImageOperationChain generator = new ImageOperationChain() {
            public BufferedImage generate(ParameterProvider params) throws ImagingException {
                try {
                    final BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/funnel.gif"));
                    return img;
                } catch (IOException e) {
                    throw new ImagingException(e.getMessage(), e);
                }
            }
        };
        generator.setName("test");
        generator.setOutputFormat(png);
        generator.setParameterProviderFactory(ppf);


        final HierarchyManager hm = new HierarchyManagerWrapper(MgnlContext.getHierarchyManager("website")) {
            boolean saved = false;

            public void save() throws RepositoryException {
                if (saved) {
                    fail("save() was called more than once");
                } else {
                    saved = true;
                }
                super.save();
            }
        };
        final ImageStreamer streamer = new CachingImageStreamer(hm, new DefaultImageStreamer());

        // Generator instances will always be the same (including paramProvFac)
        // since they are instanciated with the module config and c2b.
        // ParamProv is a new instance every time.
        // streamer can (must) be the same - once single HM, one cache.

        // thread pool of 10, launching 8 requests, can we hit some concurrency please ?
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        final ByteArrayOutputStream[] outs = new ByteArrayOutputStream[8];
        final Future[] futures = new Future[8];
        for (int i = 0; i < outs.length; i++) {
            outs[i] = new ByteArrayOutputStream();
            futures[i] = executor.submit(new TestJob(generator, streamer, outs[i]));
        }
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        for (Future<?> future : futures) {
            assertTrue(future.isDone());
            assertFalse(future.isCancelled());
            // ignore the results of TestJob - all we care about is if an exception was thrown
            // and if there was any, it is kept in Future until we call Future.get()
            future.get();
        }

        // assert all outs are the same
        for (int i = 1; i < outs.length; i++) {
            // TODO assert they're all equals byte to byte to the source? or in size? can't as-is since we convert...
            final byte[] a = outs[i - 1].toByteArray();
            final byte[] b = outs[i].toByteArray();
            assertTrue(a.length > 0);
            assertEquals("Different sizes (" + Math.abs(a.length - b.length) + " bytes diff.) with i=" + i, a.length, b.length);
            assertTrue("not equals for outs/" + i, Arrays.equals(a, b));
            outs[i - 1] = null; // cleanup all those byte[], or we'll soon run out of memory
        }
        outs[outs.length - 1] = null;

        // now start again another bunch of requests... they should ALL get their results from the cache
        final ExecutorService executor2 = Executors.newFixedThreadPool(10);
        final ByteArrayOutputStream[] outs2 = new ByteArrayOutputStream[8];
        final Future[] futures2 = new Future[8];
        for (int i = 0; i < outs2.length; i++) {
            outs2[i] = new ByteArrayOutputStream();
            futures2[i] = executor2.submit(new TestJob(generator, streamer, outs2[i]));
        }
        executor2.shutdown();
        executor2.awaitTermination(30, TimeUnit.SECONDS);

        for (Future<?> future : futures2) {
            assertTrue(future.isDone());
            assertFalse(future.isCancelled());
            // ignore the results of TestJob - all we care about is if an exception was thrown
            // and if there was any, it is kept in Future until we call Future.get()
            future.get();
        }

        // assert all outs are the same
        for (int i = 1; i < outs2.length; i++) {
            // TODO assert they're all equals byte to byte to the source? or in size? can't as-is since we re-save..
            final byte[] a = outs2[i - 1].toByteArray();
            final byte[] b = outs2[i].toByteArray();
            assertTrue(a.length > 0);
            assertEquals("Different sizes (" + Math.abs(a.length - b.length) + " bytes diff.) with i=" + i, a.length, b.length);
            assertTrue("not equals for outs2/" + i, Arrays.equals(a, b));
            outs2[i - 1] = null;
        }
        outs2[outs2.length - 1] = null;
    }

    // just a generation job for tests
    private class TestJob implements Callable<Object> {
        private final ImageGenerator generator;
        private final ImageStreamer streamer;
        private final OutputStream out;

        public TestJob(ImageGenerator generator, ImageStreamer streamer, final OutputStream out) {
            this.generator = generator;
            this.streamer = streamer;
            this.out = out;
        }

        public Object call() throws Exception {
            final ParameterProvider p = generator.getParameterProviderFactory().newParameterProviderFor(null);
            streamer.serveImage(generator, p, out);
            return null;
        }
    }

    // TODO - this is an ugly hack to workaround MAGNOLIA-2593 - we should review RepositoryTestCase
    protected void initDefaultImplementations() throws IOException {
        //MgnlTestCase clears factory before running this method, so we have to instrument factory here rather then in setUp() before calling super.setUp()
        ModuleRegistry registry = createNiceMock(ModuleRegistry.class);
        FactoryUtil.setInstance(ModuleRegistry.class, registry);

        final ModuleDefinitionReader fakeReader = new ModuleDefinitionReader() {
            public ModuleDefinition read(Reader in) throws ModuleManagementException {
                return null;
            }

            public Map readAll() throws ModuleManagementException {
                Map m = new HashMap();
                m.put("moduleDef", "dummy");
                return m;
            }

            public ModuleDefinition readFromResource(String resourcePath) throws ModuleManagementException {
                return null;
            }
        };
        final ModuleManagerImpl fakeModuleManager = new ModuleManagerImpl(null, fakeReader) {
            public List loadDefinitions() throws ModuleManagementException {
                // TODO Auto-generated method stub
                return new ArrayList();
            }
        };
        FactoryUtil.setInstance(ModuleManager.class, fakeModuleManager);
        super.initDefaultImplementations();
    }

}
