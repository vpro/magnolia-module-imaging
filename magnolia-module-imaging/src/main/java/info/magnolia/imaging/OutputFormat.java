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
package info.magnolia.imaging;

import javax.imageio.ImageWriteParam;

/**
 * A simple bean holding image formatName output configuration. This is used to get the appropriate
 * ImageWriter and applied to its  underlying <code>javax.imageio.ImageWriteParam</code>.
 * It's also exposing a simpler API, making it easier to configure through a tree/gui.
 *
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class OutputFormat implements Cloneable {
    private String formatName;
    private boolean progressive;
    private int quality;
    private String compressionType;

    /**
     * @deprecated since 2.1, implement {@link ImageGenerator#getOutputFormat(ParameterProvider)} instead
     */
    private boolean dynamicFormatType;

    public OutputFormat() {
    }

    public OutputFormat(String formatName, boolean progressive, int quality, String compressionType) {
        this.formatName = formatName;
        this.progressive = progressive;
        this.quality = quality;
        this.compressionType = compressionType;
    }

    /**
     * @deprecated since 2.1, implement {@link ImageGenerator#getOutputFormat(ParameterProvider)} instead
     */
    public OutputFormat(String formatName, boolean progressive, int quality, String compressionType, boolean dynamicFormatType) {
        this(formatName, progressive, quality, compressionType);
        this.dynamicFormatType = dynamicFormatType;
    }

    public void applyTo(ImageWriteParam param) {
        if (param.canWriteProgressive()) {
            param.setProgressiveMode(progressive ? ImageWriteParam.MODE_DEFAULT : ImageWriteParam.MODE_DISABLED);
        }

        if (param.canWriteCompressed()) {
            if (compressionType != null) {
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

    // there's probably a better way ...

    public boolean supportsTransparency() {
        return !"jpg".equalsIgnoreCase(formatName) && !"jpeg".equalsIgnoreCase(formatName) && !"bmp".equalsIgnoreCase(formatName);
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

    /**
     * @deprecated since 2.1, implement {@link ImageGenerator#getOutputFormat(ParameterProvider)} instead
     */
    public boolean getDynamicFormatType() {
        return dynamicFormatType;
    }

    /**
     * @deprecated since 2.1, implement {@link ImageGenerator#getOutputFormat(ParameterProvider)} instead
     */
    public void setDynamicFormatType(boolean dynamicFormatType) {
        this.dynamicFormatType = dynamicFormatType;
    }

    @Override
    public OutputFormat clone() throws CloneNotSupportedException {
        return (OutputFormat) super.clone();
    }
}
