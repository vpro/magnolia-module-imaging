/**
 * This file Copyright (c) 2007-2010 Magnolia International
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
package info.magnolia.module.cropui;

/**
 * Holds a cropping configuration. (fixed ratio, minimum and maximum sizes
 * of the selection zone)
 * This is used to configure and setup the cropping UI widget.
 * TODO : should it be used to check values before applying a SelectedCropAndResize too ?
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropAndResizeConfig {
    private String name, label;
    private long ratioX, ratioY;
    private long minHeight, minWidth;
    private long maxHeight, maxWidth;
  //  private long targetHeight, targetWidth;

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

//    public long getTargetHeight() {
//        return targetHeight;
//    }
//
//    public void setTargetHeight(long targetHeight) {
//        this.targetHeight = targetHeight;
//    }
//
//    public long getTargetWidth() {
//        return targetWidth;
//    }
//
//    public void setTargetWidth(long targetWidth) {
//        this.targetWidth = targetWidth;
//    }
}
