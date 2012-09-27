/**
 * This file Copyright (c) 2009-2011 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
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
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.imaging;

import static org.junit.Assert.assertEquals;
import info.magnolia.imaging.operations.load.ClasspathImageLoader;
import info.magnolia.imaging.operations.load.DefaultImageIOImageDecoder;
import info.magnolia.imaging.operations.load.ImageDecoder;
import info.magnolia.imaging.util.ImageUtil;
import info.magnolia.test.ComponentsTestUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;

import com.jhlabs.image.FlipFilter;

/**
 * Provides utility/convenience methods to load and write images.
 * Also provides well known source images for tests.
 *
 * Some of these methods are using the imaging framework itself,
 * so they might not be appropriate for all testing situations !
 *
 * The write methods also keep track of generated images, so that one
 * can open them easily after all tests have run, if human verification
 * is necessary.
 *
 * @version $Id$
 */
public abstract class AbstractImagingTest {
    private static final boolean KEEP_GENERATED_FILES_FOR_INSPECTION = false;
    private static final boolean OPEN_GENERATED_FILES_FOR_INSPECTION = false;

    protected static final OutputFormat BASIC_JPEG = new OutputFormat("jpeg", false, 80, null, false);
    private static final Set<String> generatedFiles = new HashSet<String>();

    @Before
    public void setUp() throws Exception {
        // this is set via the module descriptor (imaging.xml)
        ComponentsTestUtil.setImplementation(ImageDecoder.class, DefaultImageIOImageDecoder.class);
    }

    @After
    public void tearDown() throws Exception {
        ComponentsTestUtil.clear();
    }

    protected BufferedImage loadFromResource(String source) throws ImagingException {
        final ClasspathImageLoader loader = new ClasspathImageLoader(source);
        return loader.apply(null, null);
    }

    protected void write(BufferedImage res) throws IOException {
        write(res, BASIC_JPEG);
    }

    protected void write(BufferedImage res, OutputFormat outputFormat) throws IOException {
        write(null, res, outputFormat);
    }

    protected void write(String filenameSuffix, BufferedImage res, OutputFormat outputFormat) throws IOException {
        final SampleImageStreamer streamer = new SampleImageStreamer();
        final StringBuilder filename = new StringBuilder();
        // TODO -- output folder for sample output images
        filename.append(getClass().getSimpleName())
                .append("-")
                .append(getCurrentTestMethodName());
        if (filenameSuffix != null) {
            filename.append("-");
            filename.append(filenameSuffix);
        }
        filename.append(".")
                .append(outputFormat.getFormatName());

        final File f = new File(filename.toString());
        streamer.write(res, new FileOutputStream(f), outputFormat);
        generatedFiles.add(f.getAbsolutePath());

        if (!KEEP_GENERATED_FILES_FOR_INSPECTION) {
            f.deleteOnExit();
        }
    }

    // having these images as static constants is a mediocre attempt at making the tests run faster (AutoCropAndResizeTest was using around 7 seconds...)
    private static final BufferedImage horizontalImage;
    private static final BufferedImage verticalImage;
    private static final BufferedImage squareImage;

    // preload images for this test

    static {
        try {
            final FlipFilter f = new FlipFilter(FlipFilter.FLIP_90CW);
            squareImage = ImageIO.read(AbstractImagingTest.class.getResource("/IMG_4995.JPG"));
            horizontalImage = ImageIO.read(AbstractImagingTest.class.getResource("/IMG_2463.JPG"));
            verticalImage = f.filter(horizontalImage, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a well-known image, ensuring its dimensions haven't changed since the test was written and started.
     */
    protected BufferedImage getHorizontalTestImage() throws Exception {
        assertEquals(1600, horizontalImage.getWidth());
        assertEquals(1200, horizontalImage.getHeight());
        return horizontalImage;
    }

    /**
     * Gets a well-known image, ensuring its dimensions haven't changed since the test was written and started.
     */
    protected BufferedImage getVerticalTestImage() throws Exception {
        assertEquals(1200, verticalImage.getWidth());
        assertEquals(1600, verticalImage.getHeight());
        return verticalImage;
    }

    /**
     * Gets a well-known image, ensuring its dimensions haven't changed since the test was written and started.
     */
    protected BufferedImage getSquareTestImage() throws Exception {
        assertEquals(500, squareImage.getWidth());
        assertEquals(500, squareImage.getHeight());
        return squareImage;
    }

    /**
     * Generates an image of arbitrary width and height. Re-generated for every call.
     */
    protected BufferedImage generateArbitraryTestImage(final int width, final int height) {
        final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = img.createGraphics();
        // background
        g.setColor(Color.green);
        g.fill(new Rectangle(0, 0, width, height));
        // dashed diagonals
        g.setColor(Color.darkGray);
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f, new float[]{10, 10}, 0));
        g.drawLine(0, 0, width, height);
        g.drawLine(0, height, width, 0);
        // small square in each corner
        g.setColor(Color.red);
        g.fill(new Rectangle(10, 10, 30, 30));
        g.setColor(Color.blue);
        g.fill(new Rectangle(width - 40, height - 40, 30, 30));
        g.setColor(Color.yellow);
        g.fill(new Rectangle(width - 40, 10, 30, 30));
        g.setColor(Color.white);
        g.fill(new Rectangle(10, height - 40, 30, 30));
        return ImageUtil.flattenTransparentImageForOpaqueFormat(img, BASIC_JPEG);
    }

    /**
     * @deprecated This has been moved to info.magnolia.test.TestUtil#getCurrentTestMethodName() in Magnolia 4.3
     */
    private String getCurrentTestMethodName() {
        final StackTraceElement[] stackTrace = new Exception().getStackTrace();
        for (StackTraceElement ste : stackTrace) {
            if (ste.getMethodName().startsWith("test")) {
                return ste.getMethodName();
            }
        }
        throw new IllegalStateException("Either you're not in a test at all, or you're calling this from a non-jUnit3 test.");
    }

    /**
     * This inner class exists for the sole purpose of being able to call
     * info.magnolia.imaging.DefaultImageStreamer#write
     */
    private static class SampleImageStreamer extends DefaultImageStreamer {
        @Override
        protected void write(BufferedImage img, OutputStream out, OutputFormat outputFormat) throws IOException {
            super.write(img, out, outputFormat);
        }
    }

    static {
        if (KEEP_GENERATED_FILES_FOR_INSPECTION && OPEN_GENERATED_FILES_FOR_INSPECTION) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    final StringBuilder command = new StringBuilder("open");
                    for (String file : generatedFiles) {
                        command.append(" ").append(file);
                    }
                    try {
                        Runtime.getRuntime().exec(command.toString());
                    } catch (IOException e) {
                        throw new IllegalStateException("Could run open command with generated files... ", e);
                    }
                }
            });
        }
    }
}
