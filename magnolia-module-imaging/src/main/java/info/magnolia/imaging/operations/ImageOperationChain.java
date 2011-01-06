/**
 * This file Copyright (c) 2009-2011 Magnolia International
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

import info.magnolia.imaging.ImageGenerator;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.OutputFormat;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.ParameterProviderFactory;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of ImageOperation and ImageGenerator which delegates
 * to a list of other ImageOperation instances.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageOperationChain<P extends ParameterProvider<?>> implements ImageOperation<P>, ImageGenerator<P> {
    private final List<ImageOperation<P>> operations;
    private ParameterProviderFactory parameterProviderFactory;

    /**
     * The output format as configured by c2b.
     */
    private OutputFormat outputFormat;
    private String name;

    public ImageOperationChain() {
        this.operations = new ArrayList<ImageOperation<P>>();
    }

    public BufferedImage generate(P params) throws ImagingException {
        // We don't create an empty Image instance here, we're relying on operations
        // from the info.magnolia.imaging.operations.load package to do so
        // This might change in the future, especially if we want to overlay
        // images, and even more so if those overlays need alpha blending.

        return apply(null, params);
    }

    public BufferedImage apply(BufferedImage source, P params) throws ImagingException {
        BufferedImage result = source;
        for (ImageOperation<P> op : operations) {
            result = op.apply(result, params);
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParameterProviderFactory getParameterProviderFactory() {
        return parameterProviderFactory;
    }

    public void setParameterProviderFactory(ParameterProviderFactory parameterProviderFactory) {
        this.parameterProviderFactory = parameterProviderFactory;
    }

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    /**
     * Returns the static output format as configured with c2b by delegating to {@link #getOutputFormat()}.
     */
    public OutputFormat getOutputFormat(P params) {
        return getOutputFormat();
    }

    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public List<ImageOperation<P>> getOperations() {
        return operations;
    }

    public void addOperation(ImageOperation<P> operation) {
        operations.add(operation);
    }
}
