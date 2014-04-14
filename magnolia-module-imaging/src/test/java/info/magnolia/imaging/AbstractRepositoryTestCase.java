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
package info.magnolia.imaging;

import static org.mockito.Mockito.*;
import info.magnolia.audit.AuditLoggingManager;
import info.magnolia.imaging.operations.load.DefaultImageIOImageDecoder;
import info.magnolia.imaging.operations.load.ImageDecoder;
import info.magnolia.module.ModuleManagementException;
import info.magnolia.module.ModuleManager;
import info.magnolia.module.ModuleManagerImpl;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.module.model.ModuleDefinition;
import info.magnolia.module.model.reader.ModuleDefinitionReader;
import info.magnolia.test.ComponentsTestUtil;
import info.magnolia.test.RepositoryTestCase;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

/**
 * @version $Id$
 */
public abstract class AbstractRepositoryTestCase extends RepositoryTestCase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        ComponentsTestUtil.setInstance(AuditLoggingManager.class, new AuditLoggingManager());
        // this is set via the module descriptor (imaging.xml)
        ComponentsTestUtil.setImplementation(ImageDecoder.class, DefaultImageIOImageDecoder.class);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        // super tearDown needs a Ctx so we have to call it prior to resetting MgnlContext instance
        super.tearDown();
        ComponentsTestUtil.clear();
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
}
