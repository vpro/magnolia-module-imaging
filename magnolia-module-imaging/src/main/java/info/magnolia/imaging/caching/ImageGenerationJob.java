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
package info.magnolia.imaging.caching;

import info.magnolia.imaging.ImageGenerator;
import info.magnolia.imaging.ParameterProvider;

/**
 * Wrapper around an ImageGenerator and a ParameterProvider used as a key for current running jobs in
 * CachingImageStreamer.
 *
 * The IMPORTANT bit here is that we implemented equals() and hashCode() to compare
 * the actual parameter and not the ParameterProvider instance.
 *
 * We're assuming that whatever parameter is used provides a equals/hashCode() implementations.
 *
 * TODO - and ... DefaultContent does not.
 * TODO - and since there isn't one single/simple way of implementing equals() for a node
 * (using just the uuid wouldn't be enough, it might have been modified, and here we might have the "same" node coming from 2 different sessions... etc etc)
 * TODO - then we might want to have an equals/hash pair at ParameterProvider(Factory) level
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
class ImageGenerationJob<P> {
    private final ImageGenerator<ParameterProvider<P>> generator;
    private final ParameterProvider<P> params;

    ImageGenerationJob(ImageGenerator<ParameterProvider<P>> generator, ParameterProvider<P> params) {
        this.generator = generator;
        this.params = params;
    }

    public ImageGenerator<ParameterProvider<P>> getGenerator() {
        return generator;
    }

    public ParameterProvider<P> getParams() {
        return params;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ImageGenerationJob<ParameterProvider<P>> that = (ImageGenerationJob<ParameterProvider<P>>) o;

        if (!generator.equals(that.generator)) return false;
        if (!params.getParameter().equals(that.params.getParameter())) return false;

        return true;
    }

    public int hashCode() {
        int result = generator.hashCode();
        result = 31 * result + params.getParameter().hashCode();
        return result;
    }

}
