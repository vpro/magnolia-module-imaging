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

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import info.magnolia.cms.exchange.ActivationManager;
import info.magnolia.cms.security.MgnlRoleManager;
import info.magnolia.cms.security.MgnlUserManager;
import info.magnolia.cms.security.Permission;
import info.magnolia.cms.security.Role;
import info.magnolia.cms.security.SecuritySupport;
import info.magnolia.cms.security.SecuritySupportImpl;
import info.magnolia.cms.security.UserManager;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.module.InstallContext;
import info.magnolia.module.ModuleVersionHandler;
import info.magnolia.module.ModuleVersionHandlerTestCase;
import info.magnolia.module.model.Version;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.test.ComponentsTestUtil;

import java.util.Arrays;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link ImagingModuleVersionHandler}.
 */
public class ImagingModuleVersionHandlerTest extends ModuleVersionHandlerTestCase {

    private Session userRoles;
    private MgnlRoleManager roleManager;
    private SecuritySupportImpl securitySupport;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        userRoles = MgnlContext.getJCRSession(RepositoryConstants.USER_ROLES);
        securitySupport = new SecuritySupportImpl();
        roleManager = new MgnlRoleManager();
        securitySupport.setRoleManager(roleManager);
        ComponentsTestUtil.setInstance(SecuritySupport.class, securitySupport);
        ComponentsTestUtil.setInstance(ActivationManager.class, mock(ActivationManager.class));

    }

    @Override
    protected String getModuleDescriptorPath() {
        return "/META-INF/magnolia/imaging.xml";
    }

    @Override
    protected List<String> getModuleDescriptorPathsForTests() {
        return Arrays.asList(
                "/META-INF/magnolia/core.xml"
                );
    }

    @Override
    protected ModuleVersionHandler newModuleVersionHandlerForTests() {
        return new ImagingModuleVersionHandler();
    }

    @Test
    public void testCleanInstall() throws Exception {
        // GIVEN
        this.setupNode(RepositoryConstants.USERS, "system");
        roleManager.createRole(UserManager.SYSTEM_USER);
        MgnlUserManager userManager = new MgnlUserManager();
        userManager.setRealmName("system");
        userManager.createUser(UserManager.ANONYMOUS_USER, "");
        securitySupport.addUserManager("system", userManager);

        this.setupConfigNode("/server/filters/servlets");

        // WHEN
        InstallContext ctx = executeUpdatesAsIfTheCurrentlyInstalledVersionWas(null);

        // THEN
        this.assertNoMessages(ctx);
    }

    @Test
    public void testUpdateFrom222() throws Exception {
        // GIVEN
        Role role = roleManager.createRole(ImagingModuleVersionHandler.ROLE_IMAGING_BASE);
        roleManager.addPermission(role, ImagingModuleVersionHandler.IMAGING_WORKSPACE, "/*", Permission.READ);
        assertTrue(userRoles.nodeExists("/" + ImagingModuleVersionHandler.ROLE_IMAGING_BASE + "/acl_imaging/0"));

        roleManager.addPermission(role, ImagingModuleVersionHandler.IMAGING_WORKSPACE, "/", Permission.ALL);
        assertTrue(userRoles.nodeExists("/" + ImagingModuleVersionHandler.ROLE_IMAGING_BASE + "/acl_imaging/00"));

        roleManager.addPermission(role, ImagingModuleVersionHandler.IMAGING_WORKSPACE, "/", Permission.READ);
        assertTrue(userRoles.nodeExists("/" + ImagingModuleVersionHandler.ROLE_IMAGING_BASE + "/acl_imaging/01"));

        // WHEN
        InstallContext ctx = executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("2.2.2"));

        // THEN
        Node acl_imaging = userRoles.getNode("/" + ImagingModuleVersionHandler.ROLE_IMAGING_BASE + "/acl_imaging");
        Node permissionNode = acl_imaging.getNode("0");
        assertEquals("/*", permissionNode.getProperty("path").getString());
        assertEquals(Permission.READ, permissionNode.getProperty("permissions").getLong());
        assertEquals(1, NodeUtil.asList(NodeUtil.getNodes(acl_imaging)).size());
        this.assertNoMessages(ctx);
    }

}
