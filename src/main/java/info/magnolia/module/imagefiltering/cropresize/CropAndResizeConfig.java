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
package info.magnolia.module.imagefiltering.cropresize;

/**
 * Holds a cropping configuration. (fixed ratio, minimum and maximum sizes
 * of the selection zone, and apply target size)
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropAndResizeConfig {
    private String name, label;
    private long ratioX, ratioY;
    private long minHeight, minWidth;
    private long maxHeight, maxWidth;
    private long targetHeight, targetWidth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getRatioX() {
        return ratioX;
    }

    public void setRatioX(long ratioX) {
        this.ratioX = ratioX;
    }

    public long getRatioY() {
        return ratioY;
    }

    public void setRatioY(long ratioY) {
        this.ratioY = ratioY;
    }

    public long getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(long minHeight) {
        this.minHeight = minHeight;
    }

    public long getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(long minWidth) {
        this.minWidth = minWidth;
    }

    public long getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(long maxHeight) {
        this.maxHeight = maxHeight;
    }

    public long getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(long maxWidth) {
        this.maxWidth = maxWidth;
    }

    public long getTargetHeight() {
        return targetHeight;
    }

    public void setTargetHeight(long targetHeight) {
        this.targetHeight = targetHeight;
    }

    public long getTargetWidth() {
        return targetWidth;
    }

    public void setTargetWidth(long targetWidth) {
        this.targetWidth = targetWidth;
    }
}
