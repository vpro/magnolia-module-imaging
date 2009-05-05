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
import info.magnolia.imaging.StringParameterStrategy;
import info.magnolia.imaging.operations.load.ClasspathImageLoader;
import info.magnolia.imaging.operations.text.FixedText;
import info.magnolia.imaging.operations.text.TextStyle;
import junit.framework.TestCase;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * .
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageOperationChainTest extends TestCase {
    public void testSomeTransformations() throws IOException {

        final ImageOperationChain<StringParameterStrategy> filterChain = new ImageOperationChain<StringParameterStrategy>();
        final ClasspathImageLoader<StringParameterStrategy> loader = new ClasspathImageLoader<StringParameterStrategy>();
        loader.setSrc("/IMG_1937.JPG");
        filterChain.addOperation(loader);


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


        final StringParameterStrategy p = new StringParameterStrategy();

        final BufferedImage result = filterChain.apply(null, p);
        ImageIO.write(result, "jpg", new File("test-result.jpg"));
        Runtime.getRuntime().exec("open test-result.jpg");

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
