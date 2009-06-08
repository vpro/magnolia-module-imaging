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
package info.magnolia.imaging.setup;

import info.magnolia.cms.security.Permission;
import info.magnolia.cms.security.Role;
import info.magnolia.cms.security.Security;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.AbstractTask;
import info.magnolia.module.delta.IsAuthorInstanceDelegateTask;
import info.magnolia.module.delta.TaskExecutionException;

import java.util.Collections;
import java.util.List;


/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImagingModuleVersionHandler extends DefaultModuleVersionHandler {

    protected List getExtraInstallTasks(InstallContext installContext) {
        // TODO - see MGNLIMG-36
        return Collections.singletonList(new IsAuthorInstanceDelegateTask("Anonymous permissions for generated images", "The anonymous user needs write permission so that images can be cached on public instances.",
                null,
                new AbstractTask("", "") {
                    public void execute(InstallContext installContext) throws TaskExecutionException {
                        Role anonymous = Security.getRoleManager().getRole("anonymous");
                        anonymous.addPermission("imaging", "/*", Permission.ALL);
                    }
                }));
    }
}
