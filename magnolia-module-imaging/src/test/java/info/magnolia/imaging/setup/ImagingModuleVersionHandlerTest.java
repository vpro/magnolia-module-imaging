/**
 * This file Copyright (c) 2014 Magnolia International
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
package info.magnolia.imaging.setup;

import static info.magnolia.test.hamcrest.NodeMatchers.hasProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import info.magnolia.cms.security.MgnlRoleManager;
import info.magnolia.cms.security.Permission;
import info.magnolia.cms.security.SecuritySupport;
import info.magnolia.cms.security.SecuritySupportImpl;
import info.magnolia.context.MgnlContext;
import info.magnolia.module.ModuleVersionHandler;
import info.magnolia.module.ModuleVersionHandlerTestCase;
import info.magnolia.module.model.Version;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.test.ComponentsTestUtil;

import java.util.Arrays;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ImagingModuleVersionHandler}.
 */
public class ImagingModuleVersionHandlerTest extends ModuleVersionHandlerTestCase {

    private Session userRolesSession;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        SecuritySupportImpl securitySupport = new SecuritySupportImpl();
        ComponentsTestUtil.setInstance(SecuritySupport.class, securitySupport);
        MgnlRoleManager roleManager = new MgnlRoleManager();
        securitySupport.setRoleManager(roleManager);
        roleManager.createRole("imaging-base");

        userRolesSession = MgnlContext.getJCRSession(RepositoryConstants.USER_ROLES);

        addSupportForSetupModuleRepositoriesTask("imaging");

        // Setup the filters node to prevent error msg
        setupConfigNode("/server/filters/servlets");
    }

    @Override
    protected String getModuleDescriptorPath() {
        return "/META-INF/magnolia/imaging.xml";
    }

    @Override
    protected ModuleVersionHandler newModuleVersionHandlerForTests() {
        return new ImagingModuleVersionHandler();
    }

    @Override
    protected List<String> getModuleDescriptorPathsForTests() {
        return Arrays.asList(
                "/META-INF/magnolia/core.xml",
                "/META-INF/magnolia/activation.xml"
        );
    }

    @Test
    public void testUpdateFrom31AddsPermissionsForImagingBaseRole() throws Exception {
        // GIVEN

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("3.1"));

        // THEN
        assertThatRoleHasReadPermissionToItself();
    }

    @Test
    public void testCleanInstallAddsPermissionsForImagingBaseRole() throws Exception {
        // GIVEN

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(null);

        // THEN
        assertThatRoleHasReadPermissionToItself();
    }

    private void assertThatRoleHasReadPermissionToItself() throws RepositoryException {
        assertThat(userRolesSession.itemExists("/imaging-base/acl_userroles/0"), is(true));
        Node role = userRolesSession.getNode("/imaging-base/acl_userroles/0");
        assertThat(role, hasProperty("path", "/imaging-base"));
        assertThat(role, hasProperty("permissions", Permission.READ));
    }

}
