/**
 * This file Copyright (c) 2010 Magnolia International
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
import info.magnolia.cms.core.ItemType;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.AbstractRepositoryTask;
import info.magnolia.module.delta.TaskExecutionException;

import java.util.Collection;

import javax.jcr.RepositoryException;

/**
 * Visits all imaging generators.
 * 
 * @author ochytil
 * @version $Revision: $ ($Author: $)
 */
public abstract class UpdateAllImagingGenerators extends AbstractRepositoryTask {

    public UpdateAllImagingGenerators(String name, String description) {
            super(name, description);
        }

        @Override
        protected void doExecute(InstallContext ctx) throws RepositoryException, TaskExecutionException {
            Content modulesNode = ctx.getModulesNode();

            if (modulesNode.hasContent("imaging")) {
                Content generators = modulesNode.getContent("imaging").getContent("config").getContent("generators");
                Collection<Content> generatorsDefinitions = generators.getChildren(ItemType.CONTENTNODE);

                for (Content imagingDefinition : generatorsDefinitions) {
                    updateImagingDefinition(ctx, imagingDefinition);
                }
            }
        }

        protected abstract void updateImagingDefinition(InstallContext ctx, Content imagingDefinition) throws RepositoryException, TaskExecutionException;

}