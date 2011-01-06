/**
 * This file Copyright (c) 2009-2011 Magnolia International
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

import info.magnolia.cms.core.Content;
import info.magnolia.cms.security.Security;
import info.magnolia.cms.security.User;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.AbstractRepositoryTask;
import info.magnolia.module.delta.AbstractTask;
import info.magnolia.module.delta.BootstrapSingleResource;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.module.delta.TaskExecutionException;

import javax.jcr.RepositoryException;
import java.util.Collections;
import java.util.List;


/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImagingModuleVersionHandler extends DefaultModuleVersionHandler {
    public ImagingModuleVersionHandler() {
        super();
        register(DeltaBuilder.update("2.0.4", "")
                .addTask(new CreateConfigNodeTask())
                .addTask(new BootstrapSingleResource("Tree", "Bootstraps a tree configuration.", "/mgnl-bootstrap/imaging/config.modules.imaging.trees.imaging.xml"))
        );

    }

    @Override
    protected List getExtraInstallTasks(InstallContext installContext) {
        // TODO - see MGNLIMG-36
        return Collections.singletonList(new AbstractTask("Add the base role to the anonymous user", "The anonymous user needs write permission so that images can be cached on public instances.") {

            public void execute(InstallContext installContext) throws TaskExecutionException {
                User anonymous = Security.getUserManager().getUser("anonymous");
                anonymous.addRole("imaging-base");
            }
        });
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
