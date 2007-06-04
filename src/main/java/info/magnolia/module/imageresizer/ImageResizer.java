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

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface ImageResizer {
    /**
     * If targetWidth and targetHeight are <=0, no resizing will happen. (ie the CropperInfo will be used to determine targetWidth and targetHeight)
     * If targetWidth or targetHeight is <=0, the other side will be sized proportionally, using the given CropperInfo.
     * If both targetWidth and targetHeight are >0, we will use them even if they don't match the original ratio.
     */
    BufferedImage resize(Image source, CropperInfo cropperInfo, int targetWidth, int targetHeight);
}
