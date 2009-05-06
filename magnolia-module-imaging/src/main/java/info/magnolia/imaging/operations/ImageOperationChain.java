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
import info.magnolia.imaging.operations.ImageOperation;
import info.magnolia.imaging.ParameterProvider;

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

    public ImageOperationChain() {
        this.operations = new ArrayList<ImageOperation<P>>();
    }

    public List<ImageOperation<P>> getOperations() {
        return operations;
    }

    public void addOperation(ImageOperation<P> operation) {
        operations.add(operation);
    }

    public BufferedImage generate(P params) {
        // TODO : create base empty Image instance with appropriate settings (raster, colormodel, ...) ?

        return apply(null, params);
    }

    public BufferedImage apply(BufferedImage source, P params) {
        BufferedImage result = source;
        for (ImageOperation<P> op : operations) {
            result = op.apply(result, params);
        }
        return result;
    }

}
