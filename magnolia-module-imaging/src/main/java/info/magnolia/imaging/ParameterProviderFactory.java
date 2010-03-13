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
package info.magnolia.imaging;

import info.magnolia.imaging.caching.CachingStrategy;

/**
 * A ParameterProviderFactory is responsible for instanciating a ParameterProvider
 * for a given environment. Implementations know what concrete environment they expect and what
 * concrete type of ParameterProvider they generate. The "environment" is likely to be
 * an HttpServletRequest, but could also be a WorkItemContext, for instance.
 *
 * The ParameterProviderFactory must also ensure the parameter has appropriate
 * equals() and hashCode() methods: the parameter is used as part of a key of a
 * blocking map to avoid generating the same image multiple times.
 *
 * TODO -- verify, validate, fix the following :
 * TODO -- The type of context basically depends on the caller. In most cases, the context (C)
 * TODO -- will be an HttpServletRequest or a Magnolia Context instance.
 * TODO -- Since the ParameterProviderFactory is provided by
 * TODO -- the ImageGenerator, this might means that a particular ImageGenerator is tied
 * TODO -- to a specific context.
 *
 * @see info.magnolia.imaging.caching.ImageGenerationJob
 *
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface ParameterProviderFactory<E, PT> {

    /**
     * Instanciates a new ParameterProvider for the given environment.
     */
    ParameterProvider<PT> newParameterProviderFor(E environment);

    CachingStrategy<PT> getCachingStrategy();

}
