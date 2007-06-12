/**
 *
 * Magnolia and its source-code is licensed under the LGPL.
 * You may copy, adapt, and redistribute this file for commercial or non-commercial use.
 * When copying, adapting, or redistributing this document in keeping with the guidelines above,
 * you are required to provide proper attribution to obinary.
 * If you reproduce or distribute the document without making any substantive modifications to its content,
 * please use the following attribution line:
 *
 * Copyright 1993-2006 obinary Ltd. (http://www.obinary.com) All rights reserved.
 *
 */
package info.magnolia.module.imagefiltering;

import info.magnolia.cms.core.Content;

import java.awt.image.BufferedImage;

/**
 * Applies any kind of filtering to an Image.
 *
 * The filtering can be configured either by the end user or within the dialog definition.
 * <ul>
 *  <li>filterParams, an arbitrary object converted to and from javascript by ImagesProcessor using JSON,
 * represents the settings the user has chosen.</li>
 *  <li>dialogControlConfigNode is the node configuring the control in the dialog, which can have
 * properties set to specific values by the dialog designer (i.e if we had a ColouringFilter, it could
 * set some values to make sure that all images are turned into black/white)</li>
 *
 * @see ImagesProcessor
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface ImageFilter {
    Class getParameterType();

    BufferedImage apply(BufferedImage source, Object filterParams, Content dialogControlConfigNode);
}
