/**
 * This file Copyright (c) 2009 Magnolia International
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
package info.magnolia.imaging.util;

import info.magnolia.imaging.OutputFormat;
import junit.framework.TestCase;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageUtilTest extends TestCase {
    /**
     * A test case that shows something odd with the jpeg encoder when saving a transparent image.
     */
    public void testJpegOddity() throws IOException {
        final OutputFormat jpg = new OutputFormat();
        jpg.setFormatName("jpg");

        // create a transparent image of 300x300 pixels
        final BufferedImage img = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);

        // fill in a small red rectangle in the top right corner
        final Graphics2D g = img.createGraphics();
        g.setColor(Color.red);
        g.fill(new Rectangle(10, 10, 30, 30));

        // save it - that the background is pinkish, well, why not, but our small square is .. black !?
        final String filename = getClass().getSimpleName() + "-testJpegOddity.jpg";
        ImageIO.write(img, "jpg", new FileOutputStream(filename));

        // now use our workaround and compare results !
        final BufferedImage flattened = ImageUtil.flattenTransparentImageForOpaqueFormat(img, jpg);
        final String filename2 = getClass().getSimpleName() + "-testJpegOddity-flattenTransparentImageForOpaqueFormat.jpg";
        ImageIO.write(flattened, "jpg", new FileOutputStream(filename2));

        // use other workaround to see
        final BufferedImage filled = ImageUtil.fillTransparentPixels(img, Color.green);
        final String filename3 = getClass().getSimpleName() + "-testJpegOddity-fillTransparentPixels.jpg";
        ImageIO.write(filled, "jpg", new FileOutputStream(filename3));

        Runtime.getRuntime().exec("open " + filename + " " + filename2 + " " + filename3);

    }
}
