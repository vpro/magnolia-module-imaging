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
package info.magnolia.imaging;

import info.magnolia.imaging.operations.load.ClasspathImageLoader;
import junit.framework.TestCase;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides utility/convenience methods to load and write images.
 * These are using the imaging framework itself, so they might not be
 * appropriate for all testing situations !
 * The write methods also keep track of generated images, so that one
 * can open them easily after all tests have run, if human verification
 * is necessary.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public abstract class AbstractImagingTest extends TestCase {
    private static final boolean KEEP_GENERATED_FILES_FOR_INSPECTION = false;
    private static final boolean OPEN_GENERATED_FILES_FOR_INSPECTION = false;

    protected static final OutputFormat BASIC_JPEG = new OutputFormat("jpeg", false, 80, null);
    private static final Set<String> generatedFiles = new HashSet<String>();

    protected BufferedImage loadFromResource(String source) throws ImagingException {
        final ClasspathImageLoader loader = new ClasspathImageLoader(source);
        return loader.apply(null, null);
    }

    protected void write(BufferedImage res, OutputFormat outputFormat) throws IOException {
        write(null, res, outputFormat);
    }

    protected void write(String filenameSuffix, BufferedImage res, OutputFormat outputFormat) throws IOException {
        final SampleImageStreamer streamer = new SampleImageStreamer();
        final StringBuilder filename = new StringBuilder();
        // TODO -- output folder for sample output images
        filename.append(getClass().getSimpleName())
                .append("-")
                .append(getCurrentTestMethodName());
        if (filenameSuffix != null) {
            filename.append("-");
            filename.append(filenameSuffix);
        }
        filename.append(".")
                .append(outputFormat.getFormatName());

        final File f = new File(filename.toString());
        streamer.write(res, new FileOutputStream(f), outputFormat);
        generatedFiles.add(f.getAbsolutePath());

        if (!KEEP_GENERATED_FILES_FOR_INSPECTION) {
            f.deleteOnExit();
        }
    }

    private String getCurrentTestMethodName() {
        final StackTraceElement[] stackTrace = new Exception().getStackTrace();
        for (StackTraceElement ste : stackTrace) {
            if (ste.getMethodName().startsWith("test")) {
                return ste.getMethodName();
            }
        }
        throw new IllegalStateException("Either you're not in a test at all, or you're calling this from a non-jUnit3 test.");
    }

    /**
     * This inner class exists for the sole purpose of being able to call
     * info.magnolia.imaging.DefaultImageStreamer#write
     */
    private static class SampleImageStreamer extends DefaultImageStreamer {
        protected void write(BufferedImage img, OutputStream out, OutputFormat outputFormat) throws IOException {
            super.write(img, out, outputFormat);
        }
    }


    static {
        if (KEEP_GENERATED_FILES_FOR_INSPECTION&&OPEN_GENERATED_FILES_FOR_INSPECTION) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    final StringBuilder command = new StringBuilder("open");
                    for (String file : generatedFiles) {
                        command.append(" ").append(file);
                    }
                    try {
                        Runtime.getRuntime().exec(command.toString());
                    } catch (IOException e) {
                        throw new IllegalStateException("Could run open command with generated files... ", e);
                    }
                }
            });
        }
    }
}
