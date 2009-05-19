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

import com.jhlabs.image.WoodFilter;
import info.magnolia.imaging.StringParameterProvider;
import info.magnolia.imaging.operations.cropresize.AutoCropAndResize;
import info.magnolia.imaging.operations.load.Blank;
import info.magnolia.imaging.operations.load.ClasspathImageLoader;
import info.magnolia.imaging.operations.text.FixedText;
import info.magnolia.imaging.operations.text.HorizontalAlignment;
import info.magnolia.imaging.operations.text.TextStyle;
import info.magnolia.imaging.operations.text.VerticalAlignment;
import junit.framework.TestCase;

import javax.imageio.ImageIO;
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

        ImageIO.write(result, "jpg", new FileOutputStream("test-blank.jpg"));
        ImageIO.write(result, "png", new FileOutputStream("test-blank.png"));

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
        final TextStyle txtStyle = new TextStyle();
        txtStyle.setFontName("Arial");
        txtStyle.setColor(Color.red);
        txtStyle.setFontSize(40);
        txtStyle.setFontStyle(1);

        final FixedText topLeft = new FixedText();
        topLeft.setX(10);
        topLeft.setY(10);
        topLeft.setHorizontalAlign(HorizontalAlignment.left);
        topLeft.setVerticalAlign(VerticalAlignment.top);
        topLeft.setTextStyle(txtStyle);
        topLeft.setText("top left");
        filterChain.addOperation(topLeft);

        final FixedText center = new FixedText();
        center.setX(10);
        center.setY(10);
        center.setHorizontalAlign(HorizontalAlignment.center);
        center.setVerticalAlign(VerticalAlignment.middle);
        center.setTextStyle(txtStyle);
        center.setText("ceeeenter");
        filterChain.addOperation(center);

        final FixedText rightBottom = new FixedText();
        rightBottom.setX(10);
        rightBottom.setY(10);
        rightBottom.setHorizontalAlign(HorizontalAlignment.right);
        rightBottom.setVerticalAlign(VerticalAlignment.bottom);
        rightBottom.setTextStyle(txtStyle);
        rightBottom.setText("right bottom");
        filterChain.addOperation(rightBottom);

        final StringParameterProvider p = new StringParameterProvider("hello");

        final BufferedImage result = filterChain.generate(p);

        ImageIO.write(result, "png", new FileOutputStream("test-result.png"));

        Runtime.getRuntime().exec("open test-result.png");

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

}
