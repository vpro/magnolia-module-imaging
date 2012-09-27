/**
 * This file Copyright (c) 2010-2012 Magnolia International
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

import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;

import javax.jcr.Binary;
import javax.jcr.RepositoryException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * AbstractFromNode.
 * @param <PT> type of ParameterProvider's parameter
 */
public abstract class AbstractFromNode<PT> extends AbstractLoader<ParameterProvider<PT>> {

    @Override
    protected BufferedImage loadSource(ParameterProvider<PT> param) throws ImagingException {
        InputStream is = null;

        try {
            Binary binary = getBinaryFromNode(param);
            is = binary.getStream();
        } catch (RepositoryException e) {
            throw new ImagingException("Can't load binary from node.");
        }

        try {
            return doReadAndClose(is);
        }
        catch (IOException e) {
            throw new ImagingException("Can't read image stream from node.");
        }
    }

    protected abstract Binary getBinaryFromNode(ParameterProvider<PT> param) throws RepositoryException;
}
