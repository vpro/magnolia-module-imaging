/**
 * This file Copyright (c) 2003-2009 Magnolia International
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
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropperInfo {
    private String configName;
    private Coords coords;

    public CropperInfo(String configName, Coords coords) {
        this.configName = configName;
        this.coords = coords;
    }

    public CropperInfo() {
    }

    // regular (generated) getters and setters :
    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    // generated equals / hashcode
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CropperInfo that = (CropperInfo) o;

        if (configName != null ? !configName.equals(that.configName) : that.configName != null) {
            return false;
        }
        if (coords != null ? !coords.equals(that.coords) : that.coords != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (configName != null ? configName.hashCode() : 0);
        result = 31 * result + (coords != null ? coords.hashCode() : 0);
        return result;
    }

}
