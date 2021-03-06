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
package info.magnolia.imaging.operations.load;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;

import javax.jcr.RepositoryException;


/**
 * ImageOperation loading from content.
 *
 * @deprecated since 5.0, use {@code FromBinaryNode} instead.
 */
@Deprecated
public class FromContent extends AbstractFromContent<Content> {

    private String propertyName = "binary";

    @Override
    protected NodeData getNodeData(ParameterProvider<Content> param) throws ImagingException {
        try {
            final Content node = param.getParameter();
            if (!node.hasNodeData(propertyName)) {
                throw new ImagingException("There is no '" + propertyName + "' property at " + node.getHandle());
            }
            return node.getNodeData(propertyName);
        } catch (RepositoryException e) {
            throw new RuntimeException(e); // TODO - MAGNOLIA-2746
        }
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

}
