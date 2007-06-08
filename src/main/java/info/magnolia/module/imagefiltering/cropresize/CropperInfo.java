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
