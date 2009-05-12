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

/**
 * A ParameterProviderFactory is responsible for instanciating a ParameterProvider
 * for a given environment. Implementations know what concrete environment they expect and what
 * concrete type of ParameterProvider they generate. The "environment" is likely to be
 * an HttpServletRequest, but could also be a WorkItemContext, for instance.
 * 
 * TODO -- verify, validate, fix the following :
 * TODO -- The type of context basically depends on the caller. In most cases, the context (C)
 * TODO -- will be an HttpServletRequest or a Magnolia Context instance.
 * TODO -- Since the ParameterProviderFactory is provided by
 * TODO -- the ImageGenerator, this might means that a particular ImageGenerator is tied
 * TODO -- to a specific context. 
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface ParameterProviderFactory<E, PT> {

    /**
     * Instanciates a new ParameterProvider for the given environment.
     */
    ParameterProvider<PT> newParameterProviderFor(E environment);

    /**
     * Returns a unique path pertinent to the given parameter. Note that this is *not* the final
     * path to the generated image's node, as the CachingAndStoringImageGenerator will augment
     * this path, for instance by prefixing it with the generator name.
     */
    String getGeneratedImageNodePath(PT p);

}
