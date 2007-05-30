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
package info.magnolia.module.imageresizer;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropperInfo {
    private int x1, y1, x2, y2;

    public CropperInfo() {
    }

    public CropperInfo(int x1, int y1, int x2, int y2) {
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

        CropperInfo that = (CropperInfo) o;

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
        return "CropperInfo{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                '}';
    }
}
