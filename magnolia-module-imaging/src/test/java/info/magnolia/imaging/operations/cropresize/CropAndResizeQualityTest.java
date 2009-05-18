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
package info.magnolia.imaging.operations.cropresize;

import info.magnolia.imaging.DefaultImageStreamer;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.OutputFormat;
import info.magnolia.imaging.operations.ImageOperationChain;
import info.magnolia.imaging.operations.load.ClasspathImageLoader;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropAndResizeQualityTest extends TestCase {
    public void testNeedsAnEyeToCheck() throws IOException, ImagingException {

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
        cropAndResize.setResizeTechnique(new BasicResizeTechnique());
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile));

        final String resFile2 = getClass().getSimpleName() + "test-quality1-result-multistep.jpg";
        cropAndResize.setResizeTechnique(new MultiStepResizeTechnique());
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile2));

        final String resFile3 = getClass().getSimpleName() + "test-quality1-result-scalearea-averaging.jpg";
        cropAndResize.setResizeTechnique(new ScaleAreaAveragingResizeTechnique());
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile3));

//        final String resFile4 = "test-quality1-result-trilinear.jpg";
//        cropAndResize.setResizeTechnique(new TriLinearResizeTechnique());
//        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile4));

        // originalCopy + " "
        Runtime.getRuntime().exec("open " + expected
                + " " + resFile
                + " " + resFile2
                + " " + resFile3
//                + " " + resFile4
        );

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
