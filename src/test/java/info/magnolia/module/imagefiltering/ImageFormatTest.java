/**
 * This file Copyright (c) 2003-2009 Magnolia International
 * Ltd.  (http://www.magnolia.info). All rights reserved.
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
 * is available at http://www.magnolia.info/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
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
