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
package info.magnolia.imaging;

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
public class SelfTest extends TestCase {
    /**
     * A test case that shows something odd with the jpeg encoder when saving a transparent image.
     * @see info.magnolia.imaging.CachingAndStoringImageGenerator#flattenTransparentImageForOpaqueFormat(java.awt.image.BufferedImage, OutputFormat) 
     */
    public void testJpegOddity() throws IOException {
        // create a transparent image of 300x300 pixels
        final BufferedImage img = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);

        // fill in a small red rectangle in the top right corner
        final Graphics2D g = img.createGraphics();
        g.setColor(Color.red);
        g.fill(new Rectangle(10, 10, 30, 30));

        // save it - that the background is pinkish, well, why not, but our small square is .. black !?
        final String filename = getClass().getSimpleName() + "-testJpegOddity.jpg";
        ImageIO.write(img, "jpg", new FileOutputStream(filename));
        Runtime.getRuntime().exec("open " + filename);
    }
}
