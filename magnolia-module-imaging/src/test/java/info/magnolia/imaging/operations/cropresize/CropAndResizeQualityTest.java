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
package info.magnolia.imaging.operations.cropresize;

import static org.junit.Assert.*;
import info.magnolia.imaging.AbstractImagingTest;
import info.magnolia.imaging.DefaultImageStreamer;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.OutputFormat;
import info.magnolia.imaging.operations.ImageOperationChain;
import info.magnolia.imaging.operations.cropresize.resizers.BasicResizer;
import info.magnolia.imaging.operations.cropresize.resizers.MultiStepResizer;
import info.magnolia.imaging.operations.cropresize.resizers.ScaleAreaAveragingResizer;
import info.magnolia.imaging.operations.load.ClasspathImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * @version $Id$
 */
public class CropAndResizeQualityTest extends AbstractImagingTest {

    @Test
    public void testJpgNeedsAnEyeToCheck() throws IOException, ImagingException {

        final String originalRes = "/quality1/original.jpg";
        final String originalCopy = copyTempResource(originalRes);
        final String expected = copyTempResource("/quality1/expected.jpg");

        final OutputFormat outputFormat = new OutputFormat();
        outputFormat.setFormatName("jpg");
        outputFormat.setQuality(100);
        outputFormat.setProgressive(true);

        // resize to 298x169

        final ClasspathImageLoader loader = new ClasspathImageLoader();
        loader.setSrc(originalRes);

        final AutoCropAndResize cropAndResize = new AutoCropAndResize();
        cropAndResize.setTargetHeight(169);
        cropAndResize.setTargetWidth(298);

        final ImageOperationChain generator = new ImageOperationChain();
        generator.addOperation(loader);
        generator.addOperation(cropAndResize);
        generator.setOutputFormat(outputFormat);
        generator.setName("test");

        final DefaultImageStreamer imageStreamer = new DefaultImageStreamer();

        final String resFile = getClass().getSimpleName() + "test-quality1-result-basic.jpg";
        cropAndResize.setResizer(new BasicResizer());
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile));

        final String resFile2 = getClass().getSimpleName() + "test-quality1-result-multistep.jpg";
        cropAndResize.setResizer(new MultiStepResizer());
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile2));

        final String resFile3 = getClass().getSimpleName() + "test-quality1-result-scalearea-averaging.jpg";
        cropAndResize.setResizer(new ScaleAreaAveragingResizer());
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile3));

//        final String resFile4 = "test-quality1-result-trilinear.jpg";
//        cropAndResize.setResizer(new TriLinearResizer());
//        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile4));

//        Runtime.getRuntime().exec("open " + expected
//                + " " + resFile
//                + " " + resFile2
//                + " " + resFile3
//                + " " + resFile4
//        );

    }

    @Test
    public void testPngNeedsAnEyeToCheck() throws IOException, ImagingException {

        final String originalRes = "/quality1/original.png";
        final String originalCopy = copyTempResource(originalRes);
        final String expected = copyTempResource("/quality1/expected.png");

        final OutputFormat outputFormat = new OutputFormat();
        outputFormat.setFormatName("png");
        outputFormat.setQuality(100);
        outputFormat.setProgressive(true);

        // resize to 298x169

        final ClasspathImageLoader loader = new ClasspathImageLoader();
        loader.setSrc(originalRes);

        final AutoCropAndResize cropAndResize = new AutoCropAndResize();
        cropAndResize.setTargetHeight(169);
        cropAndResize.setTargetWidth(298);

        final ImageOperationChain generator = new ImageOperationChain();
        generator.addOperation(loader);
        generator.addOperation(cropAndResize);
        generator.setOutputFormat(outputFormat);
        generator.setName("test");

        final DefaultImageStreamer imageStreamer = new DefaultImageStreamer();

        final String resFile = getClass().getSimpleName() + "test-quality1-result-basic.png";
        cropAndResize.setResizer(new BasicResizer());
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile));

        final String resFile2 = getClass().getSimpleName() + "test-quality1-result-multistep.png";
        cropAndResize.setResizer(new MultiStepResizer());
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile2));

        final String resFile3 = getClass().getSimpleName() + "test-quality1-result-scalearea-averaging.png";
        cropAndResize.setResizer(new ScaleAreaAveragingResizer());
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile3));

//        final String resFile4 = "test-quality1-result-trilinear.jpg";
//        cropAndResize.setResizer(new TriLinearResizer());
//        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile4));

//        Runtime.getRuntime().exec("open " + expected
//                + " " + resFile
//                + " " + resFile2
//                + " " + resFile3
//                + " " + resFile4
//        );

    }

    private String copyTempResource(final String path) throws IOException {
        final String out = getClass().getSimpleName() + path.replace('/', '-');
        final InputStream o = getClass().getResourceAsStream(path);
        assertNotNull(o);
        final File f = new File(out);
        IOUtils.copy(o, new FileOutputStream(f));
        assertTrue(f.exists());
        //   f.deleteOnExit();
        return out;
    }
}
