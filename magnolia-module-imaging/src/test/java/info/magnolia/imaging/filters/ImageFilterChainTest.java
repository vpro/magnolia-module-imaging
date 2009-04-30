/**
 * This file Copyright (c) 2009 Magnolia International
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
package info.magnolia.imaging.filters;

import com.jhlabs.image.PointFilter;
import com.jhlabs.image.RGBAdjustFilter;
import info.magnolia.imaging.filters.text.TextOverlayImageFilter;
import info.magnolia.imaging.filters.load.ResourceImage;
import junit.framework.TestCase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageFilterChainTest extends TestCase {
    public void testSomeTransformations() throws IOException {

        final ImageFilterChain filterChain = new ImageFilterChain();
        final ResourceImage loader = new ResourceImage();
        loader.setSrc("/IMG_1937.JPG");
        filterChain.addFilter(loader);


        final RGBAdjustFilter rgb = new RGBAdjustFilter();
        rgb.setBFactor(0.9f);
        filterChain.addFilter(new BufferedImageOpDelegate(rgb));
        filterChain.addFilter(new TextOverlayImageFilter());


        final BufferedImage result = filterChain.apply(null, null, null);
        ImageIO.write(result, "jpg", new File("test-result.jpg"));
        Runtime.getRuntime().exec("open test-result.jpg");

/*
        final ResourceImage overlayLoad = new ResourceImage();
        overlayLoad.setSrc("/IMG_2463.JPG");
        final Opacity opacity = new Opacity();
        opacity.setOpacity(40);

        final ImageFilterChain overlay = new ImageFilterChain();
        overlay.addFilter(overlayLoad);

        overlay.addFilter(opacity);
        overlay.addFilter(new ImageFilter() {
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

    overlay.addFilter(new ImageFilter() {
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
