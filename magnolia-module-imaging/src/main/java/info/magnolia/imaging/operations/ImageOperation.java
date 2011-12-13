/**
 * This file Copyright (c) 2007-2010 Magnolia International
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
package info.magnolia.imaging.operations;

import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;

import java.awt.image.BufferedImage;

/**
 * Applies any kind of filtering to an Image.
 *
 * TODO - the javadoc below is completely outdated.
 * TODO - Image generation/filtering parameters can come from various places. We'll need to define how these are aggregated, if needed, etc.
 *
 *
 *
 * The filtering can be configured either by the end user or within the dialog definition.
 * <ul>
 *  <li>filterParams, an arbitrary object converted to and from javascript by ImagesProcessor using JSON,
 * represents the settings the user has chosen.</li>
 *  <li>dialogControlConfigNode is the node configuring the control in the dialog, which can have
 * properties set to specific values by the dialog designer (i.e if we had a ColouringFilter, it could
 * set some values to make sure that all images are turned into black/white)</li>
 * </ul>
 *
 * @see info.magnolia.module.imaging.ImagesProcessor
 * @see java.awt.image.BufferedImageOp
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface ImageOperation<P extends ParameterProvider<?>> {

    BufferedImage apply(BufferedImage source, P params) throws ImagingException;

}
