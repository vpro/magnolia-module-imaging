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
package info.magnolia.imaging.operations.cropresize;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import javax.jcr.RepositoryException;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

/**
 * A CropAndResize implementation which uses coordinates as selected in a UI componenent. This currently
 * expects a JSon string representation of CroppingInfo (wrapped in map, which is something that might change...)
 *
 * Given the goo that's Magnolia's current "hiding" of binary nodes, disguising them as properties,
 * it is not possible (?) for a dialog to be configured to store more properties *inside* an image's
 * node; so we resort to 'cropInfoSiblingPropertySuffix': we will look for a property that has the
 * same nade as the ParameterProvider's node, suffixed, and which is a *sibling* of the image's node,
 * not a children.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class SelectedCropAndResize extends AbstractCropAndResize<ParameterProvider<Content>> {
    private int targetWidth;
    private int targetHeight;
    private String cropInfoSiblingPropertySuffix;

    @Override
    protected Coords getCroopCoords(BufferedImage source, ParameterProvider<Content> params) throws ImagingException {
        try {
            final Content imageNode = params.getParameter();
            final String cropInfoPropertyName = imageNode.getName() + cropInfoSiblingPropertySuffix;
            final Content parent = imageNode.getParent();
            if (!parent.hasNodeData(cropInfoPropertyName)) {
                throw new ImagingException("There is no '" + cropInfoPropertyName + "' property at " + parent.getHandle());
            }
            final NodeData cropInfoND = parent.getNodeData(cropInfoPropertyName);
            final CroppingInfo cropInfo = decode(cropInfoND.getString());
            return cropInfo.getCoords();
        } catch (RepositoryException e) {
            throw new RuntimeException(e); // TODO - MAGNOLIA-2746
        }
    }

    @Override
    protected Size getEffectiveTargetSize(BufferedImage source, Coords cropCoords, ParameterProvider<Content> params) {
        return Size.conformToCropRatio(cropCoords, targetWidth, targetHeight);
    }

    public void setCropInfoSiblingPropertySuffix(String cropInfoSiblingPropertySuffix) {
        this.cropInfoSiblingPropertySuffix = cropInfoSiblingPropertySuffix;
    }

    public void setTargetWidth(int targetWidth) {
        this.targetWidth = targetWidth;
    }

    public void setTargetHeight(int targetHeight) {
        this.targetHeight = targetHeight;
    }

    protected CroppingInfo decode(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        final JSONObject json = JSONObject.fromObject(jsonString);
        final Map map = (Map) JSONObject.toBean(json, Map.class, Collections.singletonMap("CropperInfo", CroppingInfo.class));
        return (CroppingInfo) map.get("CropperInfo");
    }
}
