/**
 * This file Copyright (c) 2003-2008 Magnolia International
 * Ltd.  (http://www.magnolia.info). All rights reserved.
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
 * is available at http://www.magnolia.info/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.module.imagefiltering;

import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageWriteParam;

/**
 * A simple bean holding image formatName output configuration. This is used to get the appropriate
 * ImageWriter and applied to its  underlying <code>javax.imageio.ImageWriteParam</code>.
 * It's also exposing a simpler API, making it easier to configure through a tree/gui.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageFormat {
    private String formatName;
    private boolean progressive;
    private int quality;
    private String compressionType;

    public void applyTo(ImageWriteParam param) {
        if (param.canWriteProgressive()) {
            param.setProgressiveMode(progressive ? ImageWriteParam.MODE_DEFAULT : ImageWriteParam.MODE_DISABLED);
        }

        if (param.canWriteCompressed()) {
            if (StringUtils.isNotEmpty(compressionType)) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionType(compressionType);
            }
            if (quality > 0) {
                if (param.getCompressionMode() != ImageWriteParam.MODE_EXPLICIT) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                }
                final float compressionQuality = ((float) quality) / 100;
                param.setCompressionQuality(compressionQuality);
            }
        }
    }

    // -- generated getters and setters
    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public boolean isProgressive() {
        return progressive;
    }

    public void setProgressive(boolean progressive) {
        this.progressive = progressive;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }
}
