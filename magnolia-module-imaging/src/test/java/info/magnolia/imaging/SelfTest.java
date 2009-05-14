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
import info.magnolia.cms.util.FactoryUtil;
import info.magnolia.cms.util.NodeDataUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.logging.AuditLoggingManager;
import info.magnolia.module.ModuleManagementException;
import info.magnolia.module.ModuleManager;
import info.magnolia.module.ModuleManagerImpl;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.module.model.ModuleDefinition;
import info.magnolia.module.model.reader.ModuleDefinitionReader;
import info.magnolia.test.RepositoryTestCase;
import org.apache.commons.io.IOUtils;
import static org.easymock.EasyMock.createNiceMock;

import javax.jcr.PropertyType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class SelfTest extends RepositoryTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FactoryUtil.setInstance(AuditLoggingManager.class, new AuditLoggingManager());
    }

    public void testCanGetBinaryStreamOutOfTheSamePropertyInstanceTwice() throws Exception {
        final HierarchyManager hm = MgnlContext.getHierarchyManager("website");
        final Content content = ContentUtil.createPath(hm, "/foo/bar");
        final NodeData prop = NodeDataUtil.getOrCreate(content, "test", PropertyType.BINARY);

        final ByteArrayInputStream tempIn = new ByteArrayInputStream("HELLO".getBytes());
        prop.setValue(tempIn);
        prop.setAttribute(FileProperties.PROPERTY_CONTENTTYPE, "text/plain");
        prop.setAttribute(FileProperties.PROPERTY_LASTMODIFIED, Calendar.getInstance());
        hm.save();

        // -------
        final NodeData p = hm.getNodeData("/foo/bar/test");
        final InputStream in1 = p.getStream();
        final InputStream in2 = p.getStream();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in1, out);
        IOUtils.copy(in2, out); // outputs "HELLO" a 2nd time
        IOUtils.copy(in1, out); // outputs nothing, the whole stream has already been read
        assertEquals("HELLOHELLO", out.toString());

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
