/**
 * This file Copyright (c) 2007-2011 Magnolia International
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
package info.magnolia.imaging.operations.cropresize;

/**
 * Holds the result of a human selection in a cropping UI component.
 *
 * The cropui module, for instance, stores instances of this (in the form of a JSon string) into the repository.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CroppingInfo {
    private String configName;
    private Coords coords;

    public CroppingInfo(String configName, Coords coords) {
        this.configName = configName;
        this.coords = coords;
    }

    public CroppingInfo() {
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

        CroppingInfo that = (CroppingInfo) o;

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
