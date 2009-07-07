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
package info.magnolia.imaging.fix;

import info.magnolia.module.ModuleLifecycle;
import info.magnolia.module.ModuleLifecycleContext;

import java.awt.Toolkit;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class PreloadAWT implements ModuleLifecycle {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PreloadAWT.class);

    public void start(ModuleLifecycleContext moduleLifecycleContext) {
        log.info("Starting up" + this);
        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        log.info("Lib loaded, defaultToolkit is " + defaultToolkit);
    }

    public void stop(ModuleLifecycleContext moduleLifecycleContext) {
    }
}
