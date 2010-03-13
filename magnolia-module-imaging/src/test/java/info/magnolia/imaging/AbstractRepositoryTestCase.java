/**
 * This file Copyright (c) 2009-2010 Magnolia International
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

import info.magnolia.cms.util.FactoryUtil;
import info.magnolia.imaging.operations.load.DefaultImageIOImageDecoder;
import info.magnolia.imaging.operations.load.ImageDecoder;
import info.magnolia.logging.AuditLoggingManager;
import info.magnolia.module.ModuleManagementException;
import info.magnolia.module.ModuleManager;
import info.magnolia.module.ModuleManagerImpl;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.module.model.ModuleDefinition;
import info.magnolia.module.model.reader.ModuleDefinitionReader;
import info.magnolia.test.RepositoryTestCase;

import static org.easymock.EasyMock.createNiceMock;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public abstract class AbstractRepositoryTestCase extends RepositoryTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FactoryUtil.setInstance(AuditLoggingManager.class, new AuditLoggingManager());
        // this is set via the module descriptor (imaging.xml)
        FactoryUtil.setDefaultImplementation(ImageDecoder.class, DefaultImageIOImageDecoder.class);
    }

    protected void tearDown() throws Exception {
        FactoryUtil.clear();
        super.tearDown();
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
