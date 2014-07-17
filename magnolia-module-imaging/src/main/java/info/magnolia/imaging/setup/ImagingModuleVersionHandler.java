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
package info.magnolia.imaging.setup;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.security.Permission;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.AbstractRepositoryTask;
import info.magnolia.module.delta.AddPermissionTask;
import info.magnolia.module.delta.AddRoleToUserTask;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.module.delta.RemovePermissionTask;
import info.magnolia.module.delta.TaskExecutionException;

import java.util.Collections;
import java.util.List;

import javax.jcr.RepositoryException;


/**
 * VersionHandler for the imaging module.
 *
 * @version $Id$
 */
public class ImagingModuleVersionHandler extends DefaultModuleVersionHandler {
    public ImagingModuleVersionHandler() {
        super();

        register(DeltaBuilder.checkPrecondition("2.1.1", "2.2"));

        register(DeltaBuilder.update("2.2.3", "")
                .addTask(new ArrayDelegateTask("Update simaging-base role", "Change permission for imaging repo from Write-Read into Read only",
                        new RemovePermissionTask("", "", "imaging-base", "imaging", "/", Permission.WRITE),
                        new AddPermissionTask("", "", "imaging-base", "imaging", "/", Permission.READ, true))
                ));

        register(DeltaBuilder.update("3.0.5", "")
                .addTask(new AddPermissionTask("Update imaging-base role", "Add read permissions for imaging-base role", "imaging-base", "userroles", "/imaging-base", Permission.READ, false))
        );

    }

    @Override
    protected List getExtraInstallTasks(InstallContext installContext) {
        // TODO - see MGNLIMG-36
        return Collections.singletonList(
                new AddRoleToUserTask("Add the base role to the anonymous user", "anonymous", "imaging-base")
        );
    }

    private static class CreateConfigNodeTask extends AbstractRepositoryTask {
        public CreateConfigNodeTask() {
            super("Configuration", "Creates an empty configuration node for generators if it does not exist.");
        }

        @Override
        protected void doExecute(InstallContext ctx) throws RepositoryException, TaskExecutionException {
            final Content config = ctx.getOrCreateCurrentModuleConfigNode();
            if (!config.hasContent("generators")) {
                config.createContent("generators", "mgnl:content");
            }
        }
    }
}
