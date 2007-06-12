/**
 *
 * Magnolia and its source-code is licensed under the LGPL.
 * You may copy, adapt, and redistribute this file for commercial or non-commercial use.
 * When copying, adapting, or redistributing this document in keeping with the guidelines above,
 * you are required to provide proper attribution to obinary.
 * If you reproduce or distribute the document without making any substantive modifications to its content,
 * please use the following attribution line:
 *
 * Copyright 1993-2006 obinary Ltd. (http://www.obinary.com) All rights reserved.
 *
 */
package info.magnolia.module.imagefiltering;

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
public class ImageFormatTest extends TestCase {

    public void testQualityIsProperlyConvertedToOneZeroRangeAndSetsCompressionModeToExplicit() {
        final ImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
        final ImageFormat f = new ImageFormat();
        f.setQuality(63);
        f.applyTo(param);

        assertEquals(0.63f, param.getCompressionQuality(), 0f);
        assertEquals(ImageWriteParam.MODE_EXPLICIT, param.getCompressionMode());
    }

    public void testCompressionTypeIsProperlyConvertedToOneZeroRangeAndSetsCompressionModeToExplicit() {
        final ImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
        final ImageFormat f = new ImageFormat();
        f.setCompressionType("JPEG");
        f.applyTo(param);

        assertEquals("JPEG", param.getCompressionType());
        assertEquals(ImageWriteParam.MODE_EXPLICIT, param.getCompressionMode());
    }

    public void testProgressiveFlagIsSetProperlyWhenTrue() {
        final ImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
        final ImageFormat f = new ImageFormat();
        f.setProgressive(true);
        f.applyTo(param);

        assertEquals(ImageWriteParam.MODE_DEFAULT, param.getProgressiveMode());
    }

    public void testProgressiveFlagIsSetProperlyWhenFalse() {
        final ImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
        final ImageFormat f = new ImageFormat();
        f.setProgressive(false);
        f.applyTo(param);

        assertEquals(ImageWriteParam.MODE_DISABLED, param.getProgressiveMode());
    }

    public void testCompressionSettingsAreNotResetWhenBothTypeAndQualityAreApplied() {
        final ImageWriteParam param = new BMPImageWriteParam(Locale.getDefault());
        final ImageFormat f = new ImageFormat();
        f.setCompressionType("BI_JPEG");
        f.setQuality(42);
        f.applyTo(param);

        assertEquals("BI_JPEG", param.getCompressionType());
        assertEquals(0.42f, param.getCompressionQuality(), 0f);
        assertEquals(ImageWriteParam.MODE_EXPLICIT, param.getCompressionMode());
    }

}
