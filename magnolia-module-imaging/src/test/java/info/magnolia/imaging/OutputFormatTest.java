/**
 * This file Copyright (c) 2007-2009 Magnolia International
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

import javax.imageio.ImageWriteParam;
import javax.imageio.plugins.bmp.BMPImageWriteParam;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.util.Locale;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class OutputFormatTest extends TestCase {

    public void testQualityIsProperlyConvertedToOneZeroRangeAndSetsCompressionModeToExplicit() {
        final ImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
        final OutputFormat f = new OutputFormat();
        f.setQuality(63);
        f.applyTo(param);

        assertEquals(0.63f, param.getCompressionQuality(), 0f);
        assertEquals(ImageWriteParam.MODE_EXPLICIT, param.getCompressionMode());
    }

    public void testCompressionTypeIsProperlyConvertedToOneZeroRangeAndSetsCompressionModeToExplicit() {
        final ImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
        final OutputFormat f = new OutputFormat();
        f.setCompressionType("JPEG");
        f.applyTo(param);

        assertEquals("JPEG", param.getCompressionType());
        assertEquals(ImageWriteParam.MODE_EXPLICIT, param.getCompressionMode());
    }

    public void testProgressiveFlagIsSetProperlyWhenTrue() {
        final ImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
        final OutputFormat f = new OutputFormat();
        f.setProgressive(true);
        f.applyTo(param);

        assertEquals(ImageWriteParam.MODE_DEFAULT, param.getProgressiveMode());
    }

    public void testProgressiveFlagIsSetProperlyWhenFalse() {
        final ImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
        final OutputFormat f = new OutputFormat();
        f.setProgressive(false);
        f.applyTo(param);

        assertEquals(ImageWriteParam.MODE_DISABLED, param.getProgressiveMode());
    }

    public void testCompressionSettingsAreNotResetWhenBothTypeAndQualityAreApplied() {
        final ImageWriteParam param = new BMPImageWriteParam(Locale.getDefault());
        final OutputFormat f = new OutputFormat();
        f.setCompressionType("BI_JPEG");
        f.setQuality(42);
        f.applyTo(param);

        assertEquals("BI_JPEG", param.getCompressionType());
        assertEquals(0.42f, param.getCompressionQuality(), 0f);
        assertEquals(ImageWriteParam.MODE_EXPLICIT, param.getCompressionMode());
    }

}
