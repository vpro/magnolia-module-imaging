/**
 * This file Copyright (c) 2007-2012 Magnolia International
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
 * @param <P> type of ParameterProvider
 *
 * @version $Id$
 */
public interface ImageOperation<P extends ParameterProvider<?>> {

    BufferedImage apply(BufferedImage source, P params) throws ImagingException;

}
