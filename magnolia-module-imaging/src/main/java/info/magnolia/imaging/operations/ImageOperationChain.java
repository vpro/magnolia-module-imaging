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
 * @param <P> type of ParameterProvider
 *
 * @version $Id$
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

    @Override
    public BufferedImage generate(P params) throws ImagingException {
        // We don't create an empty Image instance here, we're relying on operations
        // from the info.magnolia.imaging.operations.load package to do so
        // This might change in the future, especially if we want to overlay
        // images, and even more so if those overlays need alpha blending.

        return apply(null, params);
    }

    @Override
    public BufferedImage apply(BufferedImage source, P params) throws ImagingException {
        BufferedImage result = source;
        for (ImageOperation<P> op : operations) {
            result = op.apply(result, params);
        }
        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
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
    @Override
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
