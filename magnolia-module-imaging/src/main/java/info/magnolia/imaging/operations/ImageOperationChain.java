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

import info.magnolia.imaging.ImageGenerator;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;
import info.magnolia.imaging.ParameterProviderFactory;
import info.magnolia.imaging.OutputFormat;

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
    private OutputFormat outputFormat;

    public ImageOperationChain() {
        this.operations = new ArrayList<ImageOperation<P>>();
    }

    public BufferedImage generate(P params) throws ImagingException {
        // TODO : create base empty Image instance with appropriate settings (raster, colormodel, ...) ?

        return apply(null, params);
    }

    public BufferedImage apply(BufferedImage source, P params) throws ImagingException {
        BufferedImage result = source;
        for (ImageOperation<P> op : operations) {
            result = op.apply(result, params);
        }
        return result;
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
