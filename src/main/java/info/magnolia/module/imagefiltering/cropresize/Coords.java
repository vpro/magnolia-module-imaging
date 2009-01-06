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
public class Coords {
    private int x1, y1, x2, y2;

    public Coords() {
    }

    public Coords(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getWidth() {
        final int width = x2 - x1;
        if (width <= 0) {
            throw new IllegalStateException("Invalid coordinates, negative width: x1 = " + x1 + ", x2 = " + x2);
        } else {
            return width;
        }
    }

    public int getHeight() {
        final int height = y2 - y1;
        if (height <= 0) {
            throw new IllegalStateException("Invalid coordinates, negative height: y1 = " + y1 + ", y2 = " + y2);
        } else {
            return height;
        }
    }

    // regular (generated) getters and setters :
    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    // generated methods:
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coords that = (Coords) o;

        if (x1 != that.x1) {
            return false;
        }
        if (x2 != that.x2) {
            return false;
        }
        if (y1 != that.y1) {
            return false;
        }
        if (y2 != that.y2) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = x1;
        result = 31 * result + y1;
        result = 31 * result + x2;
        result = 31 * result + y2;
        return result;
    }

    public String toString() {
        return "Coords{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                '}';
    }
}
