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
package info.magnolia.imaging.operations;

import com.jhlabs.image.PointFilter;
import com.jhlabs.image.WoodFilter;
import info.magnolia.imaging.operations.cropresize.AutoCropAndResize;
import info.magnolia.imaging.operations.load.ClasspathImageLoader;
import info.magnolia.imaging.operations.load.Blank;
import info.magnolia.imaging.operations.text.FixedText;
import info.magnolia.imaging.operations.text.TextStyle;
import info.magnolia.imaging.StringParameterProvider;
import info.magnolia.imaging.OutputFormat;
import info.magnolia.imaging.CachingAndStoringImageGenerator;
import junit.framework.TestCase;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

/**
 * .
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageOperationChainTest extends TestCase {
    //TODO - test that nested chains on several level work too

    public void testBlankAndWood() throws Exception {
        final ImageOperationChain<StringParameterProvider> filterChain = new ImageOperationChain<StringParameterProvider>();

        final Blank blank = new Blank();
        blank.setWidth(600);
        blank.setHeight(400);
        blank.setColor(Color.red);
        filterChain.addOperation(blank);

        final WoodFilter wood = new WoodFilter();
//        wood.setAngle(1.3f);
        wood.setRings(0.3f);
        wood.setFibres(0.1f);
//        wood.setTurbulence(1f);
//        wood.setgain(0.8f);
//        wood.setScale(200f);
        wood.setStretch(4f);

        final BufferedImageOpDelegate woodW = new BufferedImageOpDelegate();
        woodW.setDelegate(wood);
        filterChain.addOperation(woodW);

        final StringParameterProvider p = new StringParameterProvider("hello");

        final BufferedImage result = filterChain.generate(p);

        final CachingAndStoringImageGenerator outputter = new CachingAndStoringImageGenerator(null);
        final OutputFormat of = new OutputFormat();
        of.setFormatName("jpg");
        outputter.write(result, new FileOutputStream("test-blank.jpg"), of);
        final OutputFormat of2 = new OutputFormat();
        of2.setFormatName("png");
        outputter.write(result, new FileOutputStream("test-blank.png"), of2);

        // Runtime.getRuntime().exec("open test-blank.jpg test-blank.png");
    }

    public void testSomeTransformations() throws Exception {

        final ImageOperationChain<StringParameterProvider> filterChain = new ImageOperationChain<StringParameterProvider>();
        final ClasspathImageLoader<StringParameterProvider> loader = new ClasspathImageLoader<StringParameterProvider>();
        loader.setSrc("/IMG_1937.JPG");
        filterChain.addOperation(loader);


        final AutoCropAndResize cropAndResize = new AutoCropAndResize();
        cropAndResize.setTargetHeight(200);
        cropAndResize.setTargetWidth(600);
        filterChain.addOperation(cropAndResize);

//        final RGBAdjustFilter rgb = new RGBAdjustFilter();
//        rgb.setBFactor(0.9f);
        //filterChain.addFilter(new BufferedImageOpDelegate(rgb));
        final FixedText textOverlay = new FixedText();
        final TextStyle txtStyle = new TextStyle();
        txtStyle.setFontName("Arial");
        txtStyle.setColor(Color.red);
        txtStyle.setFontSize(40);
        txtStyle.setFontStyle(1);
        textOverlay.setTextStyle(txtStyle);
        textOverlay.setText("heyyyyyy");
        filterChain.addOperation(textOverlay);

        final StringParameterProvider p = new StringParameterProvider("hello");

        final BufferedImage result = filterChain.generate( p);

        final CachingAndStoringImageGenerator outputter = new CachingAndStoringImageGenerator(null);
        final OutputFormat of = new OutputFormat();
        of.setFormatName("jpg");
        outputter.write(result, new FileOutputStream("test-result.jpg"), of);

//        Runtime.getRuntime().exec("open test-result.jpg");

/*
        final ClasspathImageLoader overlayLoad = new ClasspathImageLoader();
        overlayLoad.setSrc("/IMG_2463.JPG");
        final Opacity opacity = new Opacity();
        opacity.setOpacity(40);

        final ImageOperationChain overlay = new ImageOperationChain();
        overlay.addFilter(overlayLoad);

        overlay.addFilter(opacity);
        overlay.addFilter(new ImageOperation() {
            public BufferedImage apply(BufferedImage src, Object filterParams, Content dialogControlConfigNode) {
                final ColorModel cm = src.getColorModel();
                final WritableRaster raster = cm.createCompatibleWritableRaster(src.getWidth(), src.getHeight());
                final BufferedImage dest = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
                final PremultiplyFilter filter = new PremultiplyFilter();
                filter.filter(src, dest);
                return dest;

            }
        });
        filterChain.addFilter(overlay);
*/
    }

    public class PremultiplyFilter extends PointFilter {

        public PremultiplyFilter() {
        }

        public int filterRGB(int x, int y, int rgb) {
            int a = (rgb >> 24) & 0xff;
            int r = (rgb >> 16) & 0xff;
            int g = (rgb >> 8) & 0xff;
            int b = rgb & 0xff;
            float f = a * (1.0f / 255.0f);
            r *= f;
            g *= f;
            b *= f;
            return (a << 24) | (r << 16) | (g << 8) | b;
        }

        public String toString() {
            return "Alpha/Premultiply";
        }
    }

    /*

    overlay.addFilter(new ImageOperation() {
            public BufferedImage apply(BufferedImage src, Object filterParams, Content dialogControlConfigNode) {
                final ColorModel cm = src.getColorModel();
                final WritableRaster raster = cm.createCompatibleWritableRaster(src.getWidth(), src.getHeight());
                final BufferedImage dest = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
                final ScaleFilter filter = new ScaleFilter(256, 256);
                filter.filter(src, dest);
                return dest;

            }
        });

        */
}
