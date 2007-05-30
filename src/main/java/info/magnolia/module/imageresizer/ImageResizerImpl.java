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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageResizerImpl implements ImageResizer {

    public BufferedImage resize(Image src, CropperInfo cropperInfo, int targetWidth, int targetHeight) {
        // this is pretty unsatisfying from a quality standpoint - colors are ugly - but is pretty fast
        final BufferedImage dst = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dst.createGraphics();
        g.drawImage(src, 0, 0, targetWidth, targetHeight, cropperInfo.getX1(), cropperInfo.getY1(), cropperInfo.getX2(), cropperInfo.getY2(), null);
        g.dispose();

        return dst;
    }
}
