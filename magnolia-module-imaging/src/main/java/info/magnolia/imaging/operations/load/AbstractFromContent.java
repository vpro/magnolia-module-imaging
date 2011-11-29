/**
 * This file Copyright (c) 2009-2011 Magnolia International
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
package info.magnolia.imaging.operations.load;

import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;

import javax.jcr.PropertyType;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * @param <PT> type of ParameterProvider's parameter
 *
 * @version $Id$
 */
public abstract class AbstractFromContent<PT> extends AbstractLoader<ParameterProvider<PT>> {

    @Override
    protected BufferedImage loadSource(ParameterProvider<PT> param) throws ImagingException {
        final NodeData data = getNodeData(param);
        if (!data.isExist() || data.getType() != PropertyType.BINARY) {
            throw new ImagingException("Nodedata " + data.getHandle() + " doesn't exist or is not of type binary.");
        }
        final InputStream in = data.getStream();
        if (in == null) {
            throw new ImagingException("Can't get InputStream from " + data.getHandle());
        }
        try {
            return doReadAndClose(in);
        } catch (IOException e) {
            throw new ImagingException("Can't load image from " + data.getHandle());
        }
    }

    /**
     * Gets the appropriate NodeData instance based on the given ParameterProvider.
     * If possible, the implementation should throw exceptions early, for instance if
     * the NodeData can't be loaded.
     */
    protected abstract NodeData getNodeData(ParameterProvider<PT> param) throws ImagingException;

}
