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
