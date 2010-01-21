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

import info.magnolia.imaging.AbstractImagingTest;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.operations.ImageOperationChain;
import info.magnolia.imaging.operations.load.Blank;
import info.magnolia.imaging.operations.load.ClasspathImageLoader;
import info.magnolia.imaging.operations.load.ImageDecoder;
import info.magnolia.imaging.operations.load.SunJPEGCodecImageDecoder;
import info.magnolia.imaging.operations.load.SunJPEGCodecImageDecoderAlt;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageUtilTest extends AbstractImagingTest {

    // not exactly sure what differentiates this jpeg yet - but com.sun.image.codec.jpeg.JPEGImageDecoder handles it correctly for us
    // while the imageio's built-in jpeg support will only "work" if we pass the image's metadata back to the write (which is not
    // practical since we might want to overlay another image on top for instance)

    public void testDecodingHuffmanImage() throws Exception {
        doTestDecodingHuffmanImage(new SunJPEGCodecImageDecoder());
    }

    public void testDecodingHuffmanImageAlt() throws Exception {
        doTestDecodingHuffmanImage(new SunJPEGCodecImageDecoderAlt());
    }

    private void doTestDecodingHuffmanImage(ImageDecoder imageDecoder) throws ImagingException, IOException {
        final ClasspathImageLoader loader = new ClasspathImageLoader("/huffman.jpg");
        loader.setImageDecoder(imageDecoder);

        final ImageOperationChain chain = new ImageOperationChain();
        chain.addOperation(loader);
        // chain.addOperation(new ClasspathImageLoader("/cookies.gif")); // overlay some cookies
        // yeah so the overlay isn't supported at the moment.

        final BufferedImage res = chain.generate(null);
        final BufferedImage flat = ImageUtil.flattenTransparentImageForOpaqueFormat(res, BASIC_JPEG);
        // write("loaded", res, BASIC_JPEG);
        write("flat", flat, BASIC_JPEG);
    }

    /* stuff
//        loader.setSrc("/huffmann-saved-by-imageeditor.jpg");
//        final BufferedImage test2 = ImageUtil.flattenTransparentImageForOpaqueFormat(loader.apply(null, null), BASIC_JPEG);
//        write("test2", test2, BASIC_JPEG);
//
//        final URLImageLoader ul = new URLImageLoader();
//        ul.setUrl("file:///Users/gjoseph/tmp/resaved.jpg");
//        final BufferedImage test3 = ImageUtil.flattenTransparentImageForOpaqueFormat(ul.apply(null, null), BASIC_JPEG);
//        write("test3", test3, BASIC_JPEG);

        // ------------------------------------------------------------------------
//        ImageDecoder ir = ImageIO.getImageReadersByFormatName("jpeg").next();
//        final InputStream in = getClass().getResourceAsStream("/huffman.jpg");
//        ImageInputStream iis = ImageIO.createImageInputStream(in);
//        ir.setInput(iis);
//        final BufferedImage bufferedImage = ir.read(0);
//        IIOImage iioImage = new IIOImage(bufferedImage, null, ir.getImageMetadata(0));
//        JPEGImageWriteParam writerParam = new JPEGImageWriteParam(null);
//        writerParam.setOptimizeHuffmanTables(true);
//        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(new File("/Users/gjoseph/tmp/resaved.jpg"));
//        ImageWriter iw = ImageIO.getImageWritersByFormatName("jpeg").next();
//        iw.setOutput(imageOutputStream);
//        iw.write(iioImage.getMetadata(), iioImage, writerParam);
//        imageOutputStream.close();

        
    }
//
//        ImageIO.write(iioImage.getRenderedImage(), "jpeg", new File("/Users/gjoseph/Downloads/34348371-broken.jpg"));
//
//        ir.setInput(ImageIO.createImageInputStream(new FileInputStream("/Users/gjoseph/Downloads/34348371-test.jpg")));
//        final IIOMetadata mdOK = ir.getImageMetadata(0);
//
//        ir.setInput(ImageIO.createImageInputStream(new FileInputStream("/Users/gjoseph/Downloads/34348371-broken.jpg")));
//        final IIOMetadata mdKO = ir.getImageMetadata(0);
//
//        System.out.println("mdOK = " + mdOK);
//        System.out.println("mdKO = " + mdKO);

    private void printImageTypes(String path) throws IOException {
        System.out.println(path + ":");
        final JPEGImageReader re = (JPEGImageReader) ImageIO.getImageReadersByFormatName("jpg").next();
        final InputStream in = getClass().getResourceAsStream(path);
        re.setInput(ImageIO.createImageInputStream(in));
        final int num = re.getNumImages(true);
        for (int i = 0; i < num; i++) {
            System.out.println("i = " + i);
            final Iterator types = re.getImageTypes(i);
            while (types.hasNext()) {
                ImageTypeSpecifier type = (ImageTypeSpecifier) types.next();
                System.out.println("  type: " + type);
                System.out.println("    cm: " + type.getColorModel());
                System.out.println("    sm: " + type.getSampleModel());
                System.out.println("    nb: " + type.getNumBands());
            }
            System.out.println();
        }
    }


        ImageDecoder ir = ImageIO.getImageReadersByFormatName("jpeg").next();
        FileInputStream fis = new FileInputStream(new File(path));
        ImageInputStream iis = ImageIO.createImageInputStream(fis);
        ir.setInput(iis);

        final BufferedImage bufferedImage = ir.read(0);
        IIOImage iioImage = new IIOImage(bufferedImage, null, ir.getImageMetadata(0));

        JPEGImageWriteParam writerParam = new JPEGImageWriteParam(null);
        writerParam.setOptimizeHuffmanTables(true);

        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(new File("/Users/gjoseph/Downloads/34348371-test.jpg"));
        ImageWriter iw = ImageIO.getImageWritersByFormatName("jpeg").next();
        iw.setOutput(imageOutputStream);
        iw.write(iioImage.getMetadata(), iioImage, writerParam);
        imageOutputStream.close();

        ImageIO.write(iioImage.getRenderedImage(), "jpeg", new File("/Users/gjoseph/Downloads/34348371-broken.jpg"));
        
        ir.setInput(ImageIO.createImageInputStream(new FileInputStream("/Users/gjoseph/Downloads/34348371-test.jpg")));
        final IIOMetadata mdOK = ir.getImageMetadata(0);

        ir.setInput(ImageIO.createImageInputStream(new FileInputStream("/Users/gjoseph/Downloads/34348371-broken.jpg")));
        final IIOMetadata mdKO = ir.getImageMetadata(0);

        System.out.println("mdOK = " + mdOK);
        System.out.println("mdKO = " + mdKO);

    }*/

    public void testCanHandleImageCreatedByBlankOperationWithoutBackgroundColor() throws Exception {
        final Blank blank = new Blank(200, 200);
        final BufferedImage img = blank.apply(null, null);
        final BufferedImage res = ImageUtil.flattenTransparentImageForOpaqueFormat(img, BASIC_JPEG);
        // turns out black, this is ok
        write(res, BASIC_JPEG);
    }

    public void testCanHandleImageCreatedByBlankOperationWithBackgroundColor() throws Exception {
        final Blank blank = new Blank(Color.orange, 200, 200);
        final BufferedImage img = blank.apply(null, null);
        final BufferedImage res = ImageUtil.flattenTransparentImageForOpaqueFormat(img, BASIC_JPEG);
        // should turn out green
        write(res, BASIC_JPEG);
    }

    public void testCanHandleOpaqueGIFSourceWhenFlatteningForJPEG() throws Exception {
        doTestFlattenTransparentImageForOpaqueFormat("/some_opaque.gif");
    }

    public void testCanHandleTransparentGIFSourceWhenFlatteningForJPEG() throws Exception {
        doTestFlattenTransparentImageForOpaqueFormat("/cookies.gif");
    }

    public void testCanHandleOpaquePNGSourceWhenFlatteningForJPEG() throws Exception {
        doTestFlattenTransparentImageForOpaqueFormat("/random_screenshot.png");
    }

    public void testCanHandleTransparentPNGSourceWhenFlatteningForJPEG() throws Exception {
        doTestFlattenTransparentImageForOpaqueFormat("/some_transparent.png");
    }

    public void testCanHandleTranslucentPNGSourceWhenFlatteningForJPEG() throws Exception {
        doTestFlattenTransparentImageForOpaqueFormat("/pngtrans/rgba16.png");
    }

    private void doTestFlattenTransparentImageForOpaqueFormat(final String source) throws Exception {
        /* debug print outs
        final BufferedImage src = ImageIO.read(getClass().getResourceAsStream(source));
        final boolean isOpaque = src.getTransparency() == Transparency.OPAQUE;
        final int numBands = src.getRaster().getNumBands();
        System.out.println(source + " isOpaque: " + isOpaque);
        System.out.println(StringUtils.repeat(" ", source.length()) + " numBands: " + numBands);
        */

        final BufferedImage img = loadFromResource(source);
        final BufferedImage res = ImageUtil.flattenTransparentImageForOpaqueFormat(img, BASIC_JPEG);
        write(res, BASIC_JPEG);
    }

    /**
     * A test case that shows something odd with the jpeg encoder when saving a transparent image.
     */
    public void testJpegOddity() throws IOException {
        // create a transparent image of 300x300 pixels
        final BufferedImage img = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);

        // fill in a small red rectangle in the top right corner
        final Graphics2D g = img.createGraphics();
        g.setColor(Color.red);
        g.fill(new Rectangle(10, 10, 30, 30));

        // save it - that the background is pinkish, well, why not, but our small square is .. black !?
        write("withoutHack", img, BASIC_JPEG);

        // now use our workaround and compare results !
        final BufferedImage flattened = ImageUtil.flattenTransparentImageForOpaqueFormat(img, BASIC_JPEG);
        write("flattenTransparentImageForOpaqueFormat", flattened, BASIC_JPEG);

        // use other workaround to see
        final BufferedImage filled = ImageUtil.fillTransparentPixels(img, Color.green);
        write("fillTransparentPixels", filled, BASIC_JPEG);
    }

    public void testPerformance() throws Exception {
        final Blank blank = new Blank(200, 200);
        final BufferedImage src = blank.apply(null, null);
        final int iterations = 1;
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                ImageUtil.flattenTransparentImageForOpaqueFormat(src, BASIC_JPEG);
            }
            System.out.println("flattenTransparentImageForOpaqueFormat: " + (System.currentTimeMillis() - start) + "ms.");
        }
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                ImageUtil.fillTransparentPixels(src, Color.black);
            }
            System.out.println("fillTransparentPixels                 : " + (System.currentTimeMillis() - start) + "ms.");
        }
    }

    public void testLoadingPerformance() throws Exception {
        final URL smallPng = getClass().getResource("/pngtrans/rgba16.png");
        final URL largeJpeg = getClass().getResource("/IMG_1937.JPG");
        final int iterations = 1;
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                ImageIO.read(smallPng);
            }
            System.out.println("warm up                               : " + (System.currentTimeMillis() - start) + "ms.");
        }
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                ImageIO.read(largeJpeg);
            }
            System.out.println("warm up 2                             : " + (System.currentTimeMillis() - start) + "ms.");
        }
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                ImageIO.read(smallPng);
            }
            System.out.println("just reading small png                : " + (System.currentTimeMillis() - start) + "ms.");
        }
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                ImageIO.read(largeJpeg);
            }
            System.out.println("just reading large jpeg               : " + (System.currentTimeMillis() - start) + "ms.");
        }
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                final BufferedImage src = ImageIO.read(smallPng);

                final BufferedImage img = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
                final Graphics2D g = img.createGraphics();
                final boolean b = g.drawImage(src, null, null);
                if (!b) {
                    throw new IllegalStateException("wtf?");
                }
            }
            System.out.println("with drawing on new image - small png : " + (System.currentTimeMillis() - start) + "ms.");
        }
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                final BufferedImage src = ImageIO.read(largeJpeg);

                final BufferedImage img = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
                final Graphics2D g = img.createGraphics();
                final boolean b = g.drawImage(src, null, null);
                if (!b) {
                    throw new IllegalStateException("wtf?");
                }
            }
            System.out.println("with drawing on new image - large jpeg: " + (System.currentTimeMillis() - start) + "ms.");
        }
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                final ClasspathImageLoader loader = new ClasspathImageLoader("/IMG_1937.JPG");
                loader.apply(null, null);
            }
            System.out.println("with ClasspathImageLoader - large jpeg: " + (System.currentTimeMillis() - start) + "ms.");
        }
    }


}
