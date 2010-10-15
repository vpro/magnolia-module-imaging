/**
 * This file Copyright (c) 2009-2010 Magnolia International
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

import info.magnolia.imaging.AbstractImagingTest;
import info.magnolia.imaging.DefaultImageStreamer;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.OutputFormat;
import info.magnolia.imaging.operations.ImageOperationChain;
import info.magnolia.imaging.operations.cropresize.resizers.MultiStepResizer;
import info.magnolia.imaging.operations.load.ClasspathImageLoader;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Test for resizing using multi step in all directions.
 */
public class MultiStepResizerTest extends AbstractImagingTest {

    public void testJpgNeedsAnEyeToCheck() throws IOException, ImagingException {

        final String originalRes = "/quality1/original.jpg";

        final OutputFormat outputFormat = new OutputFormat();
        outputFormat.setFormatName("jpg");
        outputFormat.setQuality(100);
        outputFormat.setProgressive(true);

        final ClasspathImageLoader loader = new ClasspathImageLoader();
        loader.setSrc(originalRes);

        final AutoCropAndResize cropAndResize = new AutoCropAndResize();

        final ImageOperationChain generator = new ImageOperationChain();
        generator.addOperation(loader);
        generator.addOperation(cropAndResize);
        generator.setOutputFormat(outputFormat);
        generator.setName("test");

        final DefaultImageStreamer imageStreamer = new DefaultImageStreamer();

        final String resFile2 = getClass().getSimpleName() + "test-result-multistep.jpg";
        cropAndResize.setResizer(new MultiStepResizer());

        // downsize both
        cropAndResize.setTargetHeight(169);
        cropAndResize.setTargetWidth(298);
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile2));

        // upsize both
        cropAndResize.setTargetHeight(1000);
        cropAndResize.setTargetWidth(1000);
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile2));

        // upsize h
        cropAndResize.setTargetHeight(1000);
        cropAndResize.setTargetWidth(298);
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile2));

        // upsize w
        cropAndResize.setTargetHeight(169);
        cropAndResize.setTargetWidth(2000);
        imageStreamer.serveImage(generator, null, new FileOutputStream(resFile2));
    }
}
