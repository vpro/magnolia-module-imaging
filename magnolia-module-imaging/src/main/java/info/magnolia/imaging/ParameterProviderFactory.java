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
 * A ParameterProviderFactory is responsible for instanciating a ParameterProviderFactory
 * for a given context. Implementations know what concrete context they expect and what
 * concrete type of ParameterProvider they generate.
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
public interface ParameterProviderFactory<C, PT> {

    /**
     * Instanciates a new ParameterProvider for the given context.
     */
    ParameterProvider<PT> newParameterProviderFor(C context);

}
