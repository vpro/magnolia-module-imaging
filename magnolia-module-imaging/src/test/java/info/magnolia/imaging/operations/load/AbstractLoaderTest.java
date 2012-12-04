/**
 * This file Copyright (c) 2012 Magnolia International
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
package info.magnolia.imaging.operations.load;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import info.magnolia.imaging.AbstractRepositoryTestCase;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AbstractLoaderTest extends AbstractRepositoryTestCase {

    private static Logger log = LoggerFactory.getLogger(AbstractLoaderTest.class);

    private Loader loader;
    private final ParameterProvider provider = mock(ParameterProvider.class);

    private BufferedImage originalJpgImage;
    private BufferedImage originalPngImage;
    private BufferedImage originalGifImage;
    private BufferedImage originalBmpImage;
    private BufferedImage originalPngWithAlphaImage;

    private final String path = "src/test/resources/AbstractLoaderTest/";
    private final String pathWithOriginals = path + "originals/";
    private final String pathWithOutputs = "";

    private final File originalJpg = new File(pathWithOriginals + "originalJpg.jpg");
    private final File originalGif = new File(pathWithOriginals + "originalGif.gif");
    private final File originalPng = new File(pathWithOriginals + "originalPng.png");
    private final File originalBmp = new File(pathWithOriginals + "originalBmp.bmp");
    private final File originalPngWithAlpha = new File(pathWithOriginals + "originalPngWithAlpha.png");

    private final File jpg2jpg = new File(pathWithOutputs + "jpg2jpg");
    private final File jpg2png = new File(pathWithOutputs + "jpg2png");
    private final File jpg2gif = new File(pathWithOutputs + "jpg2gif");

    private final File png2jpg = new File(pathWithOutputs + "png2jpg");
    private final File png2png = new File(pathWithOutputs + "png2png");
    private final File png2gif = new File(pathWithOutputs + "png2gif");

    private final File gif2jpg = new File(pathWithOutputs + "gif2jpg");
    private final File gif2png = new File(pathWithOutputs + "gif2png");
    private final File gif2gif = new File(pathWithOutputs + "gif2gif");

    private final File pngWithAlpha2Png = new File(pathWithOutputs + "pngWithAlpha2Png");

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        loader = new Loader();

        originalJpgImage = ImageIO.read(originalJpg);
        originalPngImage = ImageIO.read(originalPng);
        originalGifImage = ImageIO.read(originalGif);
        originalBmpImage = ImageIO.read(originalBmp);
        originalPngWithAlphaImage = ImageIO.read(originalPngWithAlpha);
    }

    //PNG
    @Test
    public void testBufferedImageFromPng() throws Exception {
        //GIVEN
        loader.setSource(originalPngImage);
        //WHEN
        BufferedImage bufferedImage = loader.apply(null,provider);
        log.info("\nTest BufferedImage from png image: source image type: {}, target image type: {}",
                imageTypeToString(originalPngImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertEquals(originalPngImage.getType(),bufferedImage.getType());
        assertTrue(equalsPictures(originalPngImage,bufferedImage, 0, 0));
        assertTrue(equalsAlpha(originalPngImage,bufferedImage));

        ImageIO.write(bufferedImage, "gif", png2gif);
        ImageIO.write(bufferedImage, "png", png2png);
        ImageIO.write(bufferedImage, "jpg", png2jpg);

        png2PngImage();
        png2JpgImage();
        png2GifImage();
    }

    private void png2PngImage() throws IOException {
        //WHEN
        BufferedImage bufferedImage = ImageIO.read(png2png);
        log.info("\n Test png to png image: source image type: {}, target image type: {}",
                imageTypeToString(originalPngImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertTrue(equalsPictures(originalPngImage,bufferedImage, 0, 0)); //lossless png compression
        assertTrue(equalsAlpha(originalPngImage,bufferedImage));
    }

    private void png2JpgImage() throws IOException {
        //WHEN
        BufferedImage bufferedImage = ImageIO.read(png2jpg);
        log.info("\n Test png to jpg image: source image type: {}, target image type: {}",
                imageTypeToString(originalPngImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertTrue(equalsPictures(originalPngImage,bufferedImage, 20, 10));
        assertTrue(equalsAlpha(originalPngImage,bufferedImage));
    }

    private void png2GifImage() throws IOException {
        //WHEN
        BufferedImage bufferedImage = ImageIO.read(png2gif);
        log.info("\n Test png to gif image: source image type: {}, target image type: {}",
                imageTypeToString(originalPngImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertTrue(equalsPictures(originalPngImage,bufferedImage, 10, 5));
        assertTrue(equalsAlpha(originalPngImage,bufferedImage));
    }

    //JPEG
    @Test
    public void testBufferedImageFromJpg() throws Exception {
        //GIVEN
        loader.setSource(originalJpgImage);
        //WHEN
        BufferedImage bufferedImage = loader.apply(null,provider);
        log.info("\nTest BufferedImage from jpg image: source image type: {}, target image type: {}",
                imageTypeToString(originalJpgImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertEquals(originalJpgImage.getType(),bufferedImage.getType());
        assertTrue(equalsPictures(originalJpgImage,bufferedImage, 0, 0));
        assertTrue(equalsAlpha(originalJpgImage,bufferedImage));

        ImageIO.write(bufferedImage, "gif", jpg2gif);
        ImageIO.write(bufferedImage, "png", jpg2png);
        ImageIO.write(bufferedImage, "jpg", jpg2jpg);

        jpg2GifImage();
        jpg2PngImage();
        jpg2JpgImage();
    }

    private void jpg2GifImage() throws IOException {
        //WHEN
        BufferedImage bufferedImage = ImageIO.read(jpg2gif);
        log.info("\n Test jpg to gif image: source image type: {}, target image type: {}",
                imageTypeToString(originalJpgImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertTrue(equalsPictures(originalJpgImage,bufferedImage, 10, 5));
        assertTrue(equalsAlpha(originalJpgImage,bufferedImage));
    }

    private void jpg2JpgImage() throws IOException {
        //WHEN
        BufferedImage bufferedImage = ImageIO.read(jpg2jpg);
        log.info("\n Test jpg to jpg image: source image type: {}, target image type: {}",
                imageTypeToString(originalJpgImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertTrue(equalsPictures(originalJpgImage,bufferedImage, 20, 10));
        assertTrue(equalsAlpha(originalJpgImage,bufferedImage));
    }

    private void jpg2PngImage() throws IOException {
        //WHEN
        BufferedImage bufferedImage = ImageIO.read(jpg2png);
        log.info("\n Test jpg to png image: source image type: {}, target image type: {}",
                imageTypeToString(originalJpgImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertTrue(equalsPictures(originalJpgImage,bufferedImage, 0, 0));  //lossless png compression
        assertTrue(equalsAlpha(originalJpgImage,bufferedImage));
    }

    @Test
    public void testBufferedImageFromGif() throws Exception {
        //GIVEN
        loader.setSource(originalGifImage);
        //WHEN
        BufferedImage bufferedImage = loader.apply(null,provider);
        log.info("\nTest BufferedImage from gif image: source image type: {}, target image type: {}",
                imageTypeToString(originalGifImage.getType()), imageTypeToString(bufferedImage.getType()));
        ImageIO.write(bufferedImage, "gif", pngWithAlpha2Png);

        //THEN
        assertEquals(originalGifImage.getType(),bufferedImage.getType());
        assertTrue(equalsPictures(originalGifImage,bufferedImage, 35, 10)); //lossy gif compression
        assertTrue(equalsAlpha(originalGifImage,bufferedImage));

        ImageIO.write(bufferedImage, "gif", gif2gif);
        ImageIO.write(bufferedImage, "png", gif2png);
        ImageIO.write(bufferedImage, "jpg", gif2jpg);

        gif2GifImage();
        gif2PngImage();
        gif2JpgImage();
    }

    private void gif2PngImage() throws IOException {
        //WHEN
        BufferedImage bufferedImage = ImageIO.read(gif2png);
        log.info("\n Test gif to png image: source image type: {}, target image type: {}",
                imageTypeToString(originalGifImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertTrue(equalsPictures(originalGifImage,bufferedImage, 35, 10)); //lossy gif compression
        assertTrue(equalsAlpha(originalGifImage,bufferedImage));
    }

    private void gif2JpgImage() throws IOException {
        //WHEN
        BufferedImage bufferedImage = ImageIO.read(gif2jpg);
        log.info("\n Test gif to jpg image: source image type: {}, target image type: {}",
                imageTypeToString(originalGifImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertTrue(equalsPictures(originalGifImage,bufferedImage, 35, 10)); //lossy gif compression
        assertTrue(equalsAlpha(originalGifImage,bufferedImage));
    }

    private void gif2GifImage() throws IOException {
        //WHEN
        BufferedImage bufferedImage = ImageIO.read(gif2gif);
        log.info("\n Test gif to gif image: source image type: {}, target image type: {}",
                imageTypeToString(originalGifImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertTrue(equalsPictures(originalGifImage,bufferedImage, 35, 10)); //lossy gif compression
        assertTrue(equalsAlpha(originalGifImage,bufferedImage));
    }

    @Test
    public void testBufferedImageFromBmp() throws Exception {
        //GIVEN
        loader.setSource(originalBmpImage);
        //WHEN
        BufferedImage bufferedImage = loader.apply(null,provider);
        log.info("\nTest BufferedImage from bmp image: source image type: {}, target image type: {}",
                imageTypeToString(originalBmpImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertEquals(originalBmpImage.getType(),bufferedImage.getType());
        assertTrue(equalsPictures(originalBmpImage,bufferedImage, 0, 0));
        assertTrue(equalsAlpha(originalBmpImage,bufferedImage));
    }

    @Test
    public void testBufferedImageFromPngWithAlpha() throws Exception {
        //GIVEN
        loader.setSource(originalPngWithAlphaImage);
        //WHEN
        BufferedImage bufferedImage = loader.apply(null,provider);
        log.info("\nTest BufferedImage from png with alpha image: source image type: {}, target image type: {}",
                imageTypeToString(originalPngWithAlphaImage.getType()), imageTypeToString(bufferedImage.getType()));
        //THEN
        assertEquals(originalPngWithAlphaImage.getType(),bufferedImage.getType());
        assertTrue(equalsPictures(originalPngWithAlphaImage,bufferedImage,5,5));
        assertTrue(equalsAlpha(originalPngWithAlphaImage,bufferedImage));

        //for visual checkout
        ImageIO.write(bufferedImage, "png", pngWithAlpha2Png);
    }


    final class Loader extends AbstractLoader {

        private BufferedImage source;

        public void setSource(BufferedImage originalImage) {
            source = originalImage;
        }

        @Override
        protected BufferedImage loadSource(ParameterProvider filterParams) throws ImagingException {
            return source;
        }
    }

    private void showSupportedWriterFormats() {
              String writerNames[] = ImageIO.getWriterFormatNames();
              String msg = "\nSupported writer formats: ";
                for(String name: writerNames) {
                    msg += name + ",";
                }
                log.info(msg);
    }


    private String imageTypeToString(int imageType) {
        switch (imageType) {
        case BufferedImage.TYPE_INT_RGB:
            return "TYPE_INT_RGB";

        case BufferedImage.TYPE_INT_ARGB:
            return "TYPE_INT_ARGB";

        case BufferedImage.TYPE_INT_ARGB_PRE:
            return "TYPE_INT_ARGB_PRE";

        case BufferedImage.TYPE_INT_BGR:
            return "TYPE_INT_BGR";

        case BufferedImage.TYPE_3BYTE_BGR:
            return "TYPE_3BYTE_BGR";

        case BufferedImage.TYPE_4BYTE_ABGR:
            return "TYPE_4BYTE_ABGR";

        case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            return "TYPE_4BYTE_ABGR_PRE";

        case BufferedImage.TYPE_BYTE_GRAY:
            return "TYPE_BYTE_GRAY";

        case BufferedImage.TYPE_USHORT_GRAY:
            return "TYPE_USHORT_GRAY";

        case BufferedImage.TYPE_BYTE_BINARY:
            return "TYPE_BYTE_BINARY";

        case BufferedImage.TYPE_BYTE_INDEXED:
            return "TYPE_BYTE_INDEXED";

        case BufferedImage.TYPE_USHORT_565_RGB:
            return "TYPE_USHORT_565_RGB";
        case BufferedImage.TYPE_USHORT_555_RGB:
            return "TYPE_USHORT_555_RGB";
        default:
            throw new IllegalArgumentException ("Unknown image type " +
                                                imageType);
        }
    }

    private boolean equalsAlpha(BufferedImage bi1, BufferedImage bi2) {
        if (bi1.getWidth() != bi2.getWidth() || bi1.getHeight() != bi2.getHeight()) {
            return false;
        }
        WritableRaster wr1 = bi1.getAlphaRaster();
        WritableRaster wr2 = bi2.getAlphaRaster();

        if (wr1 == null && wr2 == null) {
            return true;
        } else if (wr1 == null && wr2 != null || wr1 != null && wr2 == null) {
            return false;
        }

        int size = bi1.getWidth() * bi1.getHeight();
        int dArray1[] = wr1.getPixel(0, 0, new int[size]);
        int dArray2[] = wr2.getPixel(0, 0, new int[size]);

        for (int x = 0; x < size; x++) {
            if (dArray1[x] != dArray2[x]) {
                return false;
            }
        }
        return true;
    }


    private boolean equalsPictures(BufferedImage bi1, BufferedImage bi2, int colorTolerance, int wrongPixelsTolerancePercent) {

        if ( bi1.getWidth() != bi2.getWidth() || bi1.getHeight() != bi2.getHeight() ) {
            return false;
        }

        int differentPixels = 0;
        int averageError = 0;
        int size = bi1.getWidth() * bi1.getHeight();

        for (int x = 0; x < bi1.getWidth(); x++) {
            for (int y = 0; y < bi1.getHeight(); y++) {
              int pixel1 = bi1.getRGB(x,y);
              int pixel2 = bi2.getRGB(x,y);

              int red1 = (pixel1 & 0x00ff0000) >> 16;
              int red2 = (pixel1 & 0x00ff0000) >> 16;

              int green1 = (pixel1 & 0x0000ff00) >> 8;
              int green2 = (pixel2 & 0x0000ff00) >> 8;

              int blue1 = pixel1 & 0x000000ff;
              int blue2 = pixel2 & 0x000000ff;

              int redError = Math.abs(red1 - red2);
              int greenError = Math.abs(green1 - green2);
              int blueError = Math.abs(blue1 - blue2);

              if (  redError > colorTolerance || greenError > colorTolerance || blueError > colorTolerance ) {
                  differentPixels++;
                  averageError += (redError + greenError + blueError) / 3;
              }
            }
        }

        return differentPixels > size*wrongPixelsTolerancePercent/100 ? false : true;
    }
}
