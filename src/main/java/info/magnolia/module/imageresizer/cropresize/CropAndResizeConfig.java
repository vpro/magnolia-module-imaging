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
package info.magnolia.module.imageresizer.cropresize;

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
