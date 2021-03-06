/**
 * This file Copyright (c) 2009-2012 Magnolia International
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
package info.magnolia.imaging.caching;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.context.JCRSessionStrategy;
import info.magnolia.context.MgnlContext;
import info.magnolia.imaging.AbstractRepositoryTestCase;
import info.magnolia.imaging.DefaultImageStreamer;
import info.magnolia.imaging.ImageGenerator;
import info.magnolia.imaging.ImageStreamer;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.OutputFormat;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.ParameterProviderFactory;
import info.magnolia.imaging.operations.ImageOperationChain;
import info.magnolia.imaging.operations.load.URLImageLoader;
import info.magnolia.imaging.parameters.ContentParameterProvider;
import info.magnolia.imaging.parameters.SimpleEqualityContentWrapper;
import info.magnolia.jcr.wrapper.DelegateSessionWrapper;
import info.magnolia.module.ModuleManagementException;
import info.magnolia.module.ModuleManager;
import info.magnolia.module.ModuleManagerImpl;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.module.model.ModuleDefinition;
import info.magnolia.module.model.reader.ModuleDefinitionReader;
import info.magnolia.test.ComponentsTestUtil;
import info.magnolia.test.mock.MockContext;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import javax.imageio.ImageIO;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * TODO : this is sort of a load test and uses a real (in memory) repository.
 * TODO : cleanup.
 *
 * @version $Id$
 */
public class CachingImageStreamerRepositoryTest extends AbstractRepositoryTestCase {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CachingImageStreamerRepositoryTest.class);

    @Override
    @Before
    public void setUp() throws Exception {
        // this used to set autostart to false, but I'm not sure why.
        // It seems fine as is.

        super.setUp();

        // Now replace JcrSessionStrategy instances (current ctx and system ctx) with a wrapper than ensures we only save once, for the purpose of these tests.
        final MockContext systemContext = (MockContext) MgnlContext.getSystemContext();
        systemContext.setRepositoryStrategy(new DelegatingSessionStrategy(systemContext.getRepositoryStrategy()));

        final MockContext regularContext = (MockContext) MgnlContext.getInstance();
        regularContext.setRepositoryStrategy(new DelegatingSessionStrategy(regularContext.getRepositoryStrategy()));

        final GraphicsEnvironment ge =  GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (!ge.isHeadless()) {
            log.warn("This test should run in headless mode as the server will likely run headless too!!!!!");
        }
    }

    @Test
    public void testRequestForSimilarUncachedImageOnlyGeneratesItOnce() throws Exception {
        final HierarchyManager srcHM = MgnlContext.getHierarchyManager("website");
        final String srcPath = "/foo/bar";
        ContentUtil.createPath(srcHM, srcPath);

        // ParameterProvider for tests - return a new instance of the same node everytime
        // if we'd return the same src instance everytime, the purpose of this test would be null
        final ParameterProviderFactory<Object, Content> ppf = new TestParameterProviderFactory(srcHM, srcPath);

        final OutputFormat png = new OutputFormat();
        png.setFormatName("png");

        final BufferedImage dummyImg = ImageIO.read(getClass().getResourceAsStream("/funnel.gif"));
        assertNotNull("Couldn't load dummy test image", dummyImg);

        final ImageGenerator<ParameterProvider<Content>> generator = mock(ImageGenerator.class);
        when(generator.getParameterProviderFactory()).thenReturn(ppf);
        when(generator.getName()).thenReturn("test");
        when(generator.getOutputFormat(isA(ParameterProvider.class))).thenReturn(png);

        // aaaaand finally, here's the real reason for this test !
        when(generator.generate(isA(ParameterProvider.class))).thenReturn(dummyImg);

        // yeah, we're using a "wrong" workspace for the image cache, to avoid having to setup a custom one in this test
        final HierarchyManager hm = MgnlContext.getHierarchyManager("config");
        final ImageStreamer streamer = new CachingImageStreamer(hm, ppf.getCachingStrategy(), new DefaultImageStreamer());

        // Generator instances will always be the same (including paramProvFac)
        // since they are instantiated with the module config and c2b.
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

        final NodeData cachedNodeData = hm.getNodeData("/test/website/foo/bar/generated-image");
        // update node meta data
        Content cachedNode = hm.getContent("/test/website/foo/bar");
        cachedNode.getMetaData().setModificationDate();
        cachedNode.save();
        final InputStream res = cachedNodeData.getStream();
        final ByteArrayOutputStream cachedOut = new ByteArrayOutputStream();
        IOUtils.copy(res, cachedOut);

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
        assertTrue("failed comparing last thread's result with what we got from hierarchyManager",
                Arrays.equals(outs[outs.length - 1].toByteArray(), cachedOut.toByteArray()));
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

        final NodeData cachedNodeData2 = hm.getNodeData("/test/website/foo/bar/generated-image");
        final InputStream res2 = cachedNodeData2.getStream();
        final ByteArrayOutputStream cachedOut2 = new ByteArrayOutputStream();
        IOUtils.copy(res2, cachedOut2);

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
        assertTrue("failed comparing last thread's result with what we got from hierarchyManager",
                Arrays.equals(outs2[outs2.length - 1].toByteArray(), cachedOut2.toByteArray()));

        outs2[outs2.length - 1] = null;
    }

    @Test
    public void testGenerateAndStoreIsDoneUnderSystemContext() throws RepositoryException, IOException, ImagingException {
        // GIVEN
        final HierarchyManager srcHM = MgnlContext.getHierarchyManager("website");
        final String srcPath = "/foo/bar";
        ContentUtil.createPath(srcHM, srcPath);

        // ParameterProvider for tests - return a new instance of the same node everytime
        // if we'd return the same src instance everytime, the purpose of this test would be null
        final ParameterProviderFactory<Object, Content> ppf = new TestParameterProviderFactory(srcHM, srcPath);

        final OutputFormat png = new OutputFormat();
        png.setFormatName("png");

        final BufferedImage dummyImg = ImageIO.read(getClass().getResourceAsStream("/funnel.gif"));
        assertNotNull("Couldn't load dummy test image", dummyImg);

        final ImageGenerator<ParameterProvider<Content>> generator = mock(ImageGenerator.class);
        when(generator.getParameterProviderFactory()).thenReturn(ppf);
        when(generator.getName()).thenReturn("test");
        when(generator.getOutputFormat(isA(ParameterProvider.class))).thenReturn(png);

        when(generator.generate(isA(ParameterProvider.class))).thenReturn(dummyImg);

        // yeah, we're using a "wrong" workspace for the image cache, to avoid having to setup a custom one in this test
        final HierarchyManager hm = MgnlContext.getHierarchyManager("config");

        final CachingImageStreamer streamer = new CachingImageStreamer(hm, ppf.getCachingStrategy(), new DefaultImageStreamer());

        // WHEN
        streamer.generateAndStore(generator, generator.getParameterProviderFactory().newParameterProviderFor(null));

        // THEN
        final Session systemSession = MgnlContext.getSystemContext().getJCRSession("config");
        final Session session = MgnlContext.getJCRSession("config");
        assertTrue(systemSession instanceof SingleSaveSessionWrapper);
        assertTrue(session instanceof SingleSaveSessionWrapper);
        assertTrue("should have saved in system session", ((SingleSaveSessionWrapper)systemSession).saved);
        assertFalse("should not have saved in regular session", ((SingleSaveSessionWrapper)session).saved);
    }

    /**
     * This test is not executed by default - too long !
     * Used to reproduce the "session already closed issue", see MGNLIMG-59.
     * Set the "expiration" property of the jobs map in CachingImageStreamer to a longer value
     * to have more chances of reproducing the problem.
     */
    @Ignore
    @Test
    public void testConcurrencyAndJCRSessions() throws Exception {
        final HierarchyManager srcHM = MgnlContext.getHierarchyManager("website");
        final String srcPath = "/foo/bar";
        ContentUtil.createPath(srcHM, srcPath);

        // ParameterProvider for tests - return a new instance of the same node everytime
        // if we'd return the same src instance everytime, the purpose of this test would be null
        final ParameterProviderFactory<Object, Content> ppf = new TestParameterProviderFactory(srcHM, srcPath);

        final OutputFormat png = new OutputFormat();
        png.setFormatName("png");

        final ImageOperationChain<ParameterProvider<Content>> generator = new ImageOperationChain<ParameterProvider<Content>>();
        final URLImageLoader<ParameterProvider<Content>> load = new URLImageLoader<ParameterProvider<Content>>();
        load.setUrl(getClass().getResource("/funnel.gif").toExternalForm());
        generator.addOperation(load);
        generator.setOutputFormat(png);
        generator.setName("foo blob bar");
        generator.setParameterProviderFactory(ppf);

        // yeah, we're using a "wrong" workspace for the image cache, to avoid having to setup a custom one in this test
        final HierarchyManager hm = MgnlContext.getHierarchyManager("config");

        final ImageStreamer streamer = new CachingImageStreamer(hm, ppf.getCachingStrategy(), new DefaultImageStreamer());

        // thread pool of 10, launching 8 requests, can we hit some concurrency please ?
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        final ByteArrayOutputStream[] outs = new ByteArrayOutputStream[8];
        final Future[] futures = new Future[8];
        for (int i = 0; i < outs.length; i++) {
            final int ii = i;
            outs[i] = new ByteArrayOutputStream();
            futures[i] = executor.submit(new Runnable() {
                @Override
                public void run() {
                    final ParameterProvider p = generator.getParameterProviderFactory().newParameterProviderFor(null);
                    try {
                        streamer.serveImage(generator, p, outs[ii]);
                    } catch (Exception e) {
                        throw new RuntimeException(e); // TODO
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        for (Future<?> future : futures) {
            assertTrue(future.isDone());
            assertFalse(future.isCancelled());
            // ignore the results of TestJob - but if there was an exception thrown by TestJob.call(),
            // it is only thrown back at us when we call get() below. (so the test will fail badly if the job threw an exception)
            Object ignored = future.get();
        }

        shutdownRepository(true);

        // sleep for a while so that the jobs map's expiration thread can kick in !
        Thread.sleep(10000);
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

        @Override
        public Object call() throws Exception {
            final ParameterProvider p = generator.getParameterProviderFactory().newParameterProviderFor(null);
            streamer.serveImage(generator, p, out);
            return null;
        }
    }

    // TODO - this is an ugly hack to workaround MAGNOLIA-2593 - we should review RepositoryTestCase
    @Override
    protected void initDefaultImplementations() throws IOException, ModuleManagementException {
        //MgnlTestCase clears factory before running this method, so we have to instrument factory here rather then in setUp() before calling super.setUp()
        ModuleRegistry registry = mock(ModuleRegistry.class);
        ComponentsTestUtil.setInstance(ModuleRegistry.class, registry);

        final ModuleDefinitionReader fakeReader = new ModuleDefinitionReader() {
            @Override
            public ModuleDefinition read(Reader in) throws ModuleManagementException {
                return null;
            }

            @Override
            public Map readAll() throws ModuleManagementException {
                Map m = new HashMap();
                m.put("moduleDef", "dummy");
                return m;
            }

            @Override
            public ModuleDefinition readFromResource(String resourcePath) throws ModuleManagementException {
                return null;
            }
        };
        final ModuleManagerImpl fakeModuleManager = new ModuleManagerImpl(null, fakeReader) {
            @Override
            public List loadDefinitions() throws ModuleManagementException {
                // TODO Auto-generated method stub
                return new ArrayList();
            }
        };
        ComponentsTestUtil.setInstance(ModuleManager.class, fakeModuleManager);
        super.initDefaultImplementations();
    }

    /*
    final IMocksControl iMocksControl = createStrictControl();
    // can't be strict on method call order on hm, because we can't guarantee which of the 8 threads will create the node
    iMocksControl.checkOrder(false);
    // not exactly sure what this entails; hopefully it doesn't render the test useless...
    iMocksControl.makeThreadSafe(true);
    final HierarchyManager hm = iMocksControl.createMock(HierarchyManager.class);
    final Content root = createStrictMock(Content.class);
    final Content t = createStrictMock(Content.class);
    final Content m = createStrictMock(Content.class);
    final Content p = createStrictMock(Content.class);
    final Content y = createStrictMock(Content.class);

    // first 8 request: node doesn't exist.
    for (int i = 0; i < 8; i++) {
        // generator name + path provided by ParameterProviderFactory
        expect(hm.isExist("/test/my/param/yo")).andReturn(false);
    }

    // one of these 8 threads creates the node
    expect(hm.getRoot()).andReturn(root);
    expect(root.hasContent("test")).andReturn(false);
    expect(root.createContent("test", ItemType.CONTENT)).andReturn(t);
    expect(t.hasContent("my")).andReturn(false);
    expect(t.createContent("my", ItemType.CONTENT)).andReturn(m);
    expect(m.hasContent("param")).andReturn(false);
    expect(m.createContent("param", ItemType.CONTENT)).andReturn(p);
    expect(p.hasContent("yo")).andReturn(false);
    expect(p.createContent("yo", ItemType.CONTENT)).andReturn(y);
    expect(y.hasNodeData("generated-image")).andReturn(false);

    // 8 more requests, the node exists in the hm
    for (int i = 0; i < 8; i++) {
        expect(hm.isExist("/test/my/param/yo")).andReturn(true);
    }

    replay(hm, root, t, m, p, y);
     */

    /**
     * A JCRSessionStrategy which allows returning a given session instead of the regular one.
     */
    private static class DelegatingSessionStrategy implements JCRSessionStrategy {
        private final Map<String, Session> jcrSessions = new HashMap<String, Session>();
        private final JCRSessionStrategy delegate;

        DelegatingSessionStrategy(JCRSessionStrategy delegate) {
            this.delegate = delegate;
        }

        @Override
        public Session getSession(String workspaceName) throws RepositoryException {
            if (jcrSessions.containsKey(workspaceName)) {
                final Session session = jcrSessions.get(workspaceName);
                assertThat(session, is(instanceOf(SingleSaveSessionWrapper.class)));
                return session;
            } else {
                final Session session = delegate.getSession(workspaceName);
                assertThat(session, is(not(instanceOf(SingleSaveSessionWrapper.class))));
                final SingleSaveSessionWrapper sessionWrapper = new SingleSaveSessionWrapper(session);
                jcrSessions.put(workspaceName,sessionWrapper);
                return sessionWrapper;
            }
        }

        @Override
        public void release() {
            delegate.release();
        }
    }

    private static class TestParameterProviderFactory implements ParameterProviderFactory {
        private final HierarchyManager srcHM;
        private final String srcPath;

        public TestParameterProviderFactory(HierarchyManager srcHM, String srcPath) {
            this.srcHM = srcHM;
            this.srcPath = srcPath;
        }

        @Override
        public ParameterProvider<Content> newParameterProviderFor(Object environment) {
            try {
                final Content src = srcHM.getContent(srcPath);
                // copied from ContentParameterProviderFactory
                return new ContentParameterProvider(new SimpleEqualityContentWrapper(src));
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public CachingStrategy getCachingStrategy() {
            return new ContentBasedCachingStrategy();
        }
    }

    private static class SingleSaveSessionWrapper extends DelegateSessionWrapper {
        boolean saved = false;

        public SingleSaveSessionWrapper(Session session) throws RepositoryException {
            super(session);
        }

        @Override
        public synchronized void save() throws RepositoryException {
            if (saved) {
                fail("save() was called more than once");
            } else {
                saved = true;
            }
            super.save();
        }
    }
}
